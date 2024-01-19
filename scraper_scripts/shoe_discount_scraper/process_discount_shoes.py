import logging
import requests
from requests import RequestException

from shoe import Shoe

from azure.storage.blob import BlobServiceClient

logging.basicConfig(level=logging.INFO)


def download_image(url, timeout=5):
    try:
        response = requests.head(url, timeout=timeout)
        if response.status_code == 200:
            response = requests.get(url)
            return response.content
        else:
            logging.warning("Warning: Failed to download image from %s. Status code: %s", url, response.status_code)
    except RequestException as e:
        logging.exception("Exception: Failed to download image from %s. Error: %s", url, str(e))

    return None


def upload_image(image, container_name, connection_string, blob_id, blob_name):
    try:
        if blob_id is not None and blob_name is not None:
            blob = blob_id + '-' + blob_name
        else:
            logging.error("Error: Blob ID or Blob Name is empty or None")
            return None

        blob_service_client = BlobServiceClient.from_connection_string(connection_string)
        container_client = blob_service_client.get_container_client(container_name)

        blob_client = container_client.get_blob_client(blob)

        if not blob_client.exists():
            blob_client.upload_blob(image)
        else:
            logging.warning("Warning: Blob already exists: %s. Skipping upload.", blob)

        blob_url = f"https://{blob_service_client.account_name}.blob.core.windows.net/{container_name}/{blob}"
        return blob_url
    except Exception as e:
        logging.exception("Error: Failed uploading image: %s", str(e))
        return None


def process_shoe(shoe_div, container_name, connection_string):
    model = None
    try:
        model = shoe_div.select_one('h1.heading-2').text.strip()

        brand = shoe_div.select_one('div.heading-eyebrow a').text.strip().upper()

        new_price = float(
            shoe_div.select_one('div.price').text.strip()
            .replace('\xa0лв.', '').replace(',', '.')
        )
        old_price = float(
            shoe_div.select_one('div.price-old').text.strip()
            .replace('\xa0лв.', '').replace(',', '.')
        )

        size_list = shoe_div.select_one('div.component-sizes__size-list')
        sizes = [
            size.text.strip()
            for size in size_list.select('div.component-sizes__size')
        ]
        available_sizes = [
            a.select_one('div').text.strip()
            for a in size_list.select('a.component-sizes__size-text')
        ]

        provider = shoe_div.select_one('a.provider-link').text.strip()

        image_links = [
            a['data-src-orig']
            for a in shoe_div.select('a.detail-change-photo')
        ]

        images_binary = [download_image(image) for image in image_links]

        images = []

        blob_id = image_links[0].split('/')[-1].split('.')[0]

        for i in range(1, len(images_binary)):
            blob_name = image_links[i].split('/')[-1].split('.')[0]
            image_url = upload_image(images_binary[i], container_name, connection_string, blob_id, blob_name)
            images.append(image_url)

        shoe = Shoe(brand, model, new_price, old_price, 'MALE', sizes, available_sizes,
                    provider, None, images, images[0], blob_id, True)

        logging.info("Processed shoe: %s", shoe.to_dict())

        return shoe
    except AttributeError as e:
        logging.exception("Exception: Fail during processing of %s, Error: %s", model, str(e))
        return None


def process_shoes(shoe_divs, container_name, connection_string):
    if (shoe_divs is None or container_name is None or connection_string is None or
            shoe_divs == '' or container_name == '' or connection_string == ''):
        logging.error("Error: Invalid input parameters")
        return None

    shoes = [process_shoe(shoe_div, container_name, connection_string) for shoe_div in shoe_divs]
    shoes = [shoe for shoe in shoes if shoe is not None]
    logging.warning("Warning: Failed to process %s shoes", len(shoe_divs) - len(shoes))
    return shoes
