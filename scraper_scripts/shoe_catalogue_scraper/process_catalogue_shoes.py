from urllib.parse import urlparse
import logging
import requests
from requests.exceptions import RequestException
from azure.core.exceptions import ResourceNotFoundError, AzureError
from azure.storage.blob import BlobServiceClient

from shoe import Shoe

logging.basicConfig(level=logging.INFO)
logging.getLogger("azure.core.pipeline.policies.http_logging_policy").setLevel(logging.WARNING)


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


def delete_blobs(connection_string, container_name):
    try:
        blob_service_client = BlobServiceClient.from_connection_string(connection_string)
        container_client = blob_service_client.get_container_client(container_name)

        blobs = container_client.list_blobs()

        for blob in blobs:
            try:
                container_client.delete_blob(blob.name)
            except ResourceNotFoundError:
                logging.exception("Exception: Blob not found: %s", blob.name)
            except AzureError as e:
                logging.exception("Exception: Failed during deleting blob: %s", str(e))
    except AzureError as e:
        logging.exception("Exception: Failed during deleting blobs: %s", str(e))


def get_final_url(initial_url, timeout=30):
    parsed_url = urlparse(initial_url)
    if not parsed_url.scheme or not parsed_url.netloc:
        logging.error("Error: Invalid URL: %s", initial_url)
        return None

    try:
        response = requests.get(initial_url, allow_redirects=True, timeout=timeout)
        if response.status_code == 200 or response.status_code == 403:
            return response.url
        else:
            logging.warning("Warning: Failed to get final URL from %s. Status code: %s", initial_url,
                            response.status_code)
    except RequestException as e:
        logging.exception("Exception: Failed to get final URL from %s. Error: %s", initial_url, str(e))

    return None


def process_shoe(shoe_div, container_name, connection_string):
    model = None
    try:
        model_element = shoe_div.find('div', class_='item__title')
        model = model_element.text.strip()

        brand_element = shoe_div.find('div', class_='item__brand').span
        brand = brand_element.text.strip().upper()

        provider_element = shoe_div.find('span', class_='item__provider-name')
        provider = provider_element.get('title').strip()

        redirect_url = shoe_div.a.get('data-exli', None)
        url = get_final_url(redirect_url)
        if not url:
            url = redirect_url

        image_element = shoe_div.find('div', class_='item__photo__inner').img
        image_link = image_element['src'].replace('/228x298be/', '/')

        image_binary = download_image(image_link)
        if not image_binary:
            image_binary = download_image(image_link.replace('webp', 'jpg'))
        if not image_binary:
            logging.error("Error: Failed downloading image from %s", image_link)
            return None

        blob_id = image_link.split('/')[-1].split('.')[0]

        initial_image = upload_image(image_binary, container_name, connection_string, blob_id, blob_id)

        shoe = Shoe(brand, model, None, None, None, [], [],
                    provider, url, [initial_image], initial_image, blob_id, False)

        # logging.info("Processed shoe: %s", shoe.to_dict())

        return shoe
    except AttributeError as e:
        logging.exception("Exception: Fail during processing of %s, Error: %s", model, str(e))
        return None


def process_shoes(shoe_divs, container_name, connection_string, delete_blobs_flag=False):
    if (shoe_divs is None or container_name is None or connection_string is None or
            shoe_divs == '' or container_name == '' or connection_string == ''):
        logging.error("Error: Invalid input parameters")
        return None

    if delete_blobs_flag:
        delete_blobs(connection_string, container_name)
        logging.info("Deleted all blobs from container")

    shoes = [process_shoe(shoe_div, container_name, connection_string) for shoe_div in shoe_divs]
    shoes = [shoe for shoe in shoes if shoe is not None]
    logging.warning("Warning: Failed to process %s shoes", len(shoe_divs) - len(shoes))
    return shoes
