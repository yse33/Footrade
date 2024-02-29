import logging
import requests
from requests.exceptions import RequestException
from urllib.parse import urlparse
from bs4 import BeautifulSoup
from azure.storage.blob import BlobServiceClient
from pymongo import MongoClient

logging.basicConfig(level=logging.WARNING)


def check_url(url):
    parsed_url = urlparse(url)
    if not parsed_url.scheme or not parsed_url.netloc:
        logging.error("Error: Invalid URL: %s", url)
        return False
    return True


def get_final_url(url, driver):
    try:
        driver.get(url)
        return driver.current_url
    except requests.exceptions.RequestException as e:
        logging.exception("Exception: Failed to get final URL: %s", repr(e))
        return None


def get_soup(url, timeout=10):
    try:
        headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) '
                                 'Chrome/58.0.3029.110 Safari/537.3'}
        page = requests.get(url, headers=headers, timeout=timeout, verify=True)
        page.raise_for_status()
        soup = BeautifulSoup(page.text, 'html.parser')
        return soup
    except RequestException as e:
        logging.exception("Exception: Failed to make request to %s : %s", url, repr(e))
        return None


def get_brand_from_model(model):
    model = model.upper()
    if 'ADIDAS' in model:
        return 'ADIDAS'
    elif 'NIKE' in model or 'JORDAN' in model:
        return 'NIKE'
    elif 'PUMA' in model:
        return 'PUMA'
    elif 'VANS' in model:
        return 'VANS'
    else:
        return None


def download_image(url, driver):
    if not check_url(url):
        return None

    try:
        driver.get(url)
        image = driver.get_screenshot_as_png()
        return image
    except requests.exceptions.RequestException as e:
        print(f"Error downloading image from {url}: {e}")
        return None


def upload_image(image, container_name, connection_string, blob_name):
    try:
        blob_service_client = BlobServiceClient.from_connection_string(connection_string)
        container_client = blob_service_client.get_container_client(container_name)

        blob_client = container_client.get_blob_client(blob_name)

        if not blob_client.exists():
            blob_client.upload_blob(image)
        else:
            logging.warning("Warning: Blob already exists: %s. Skipping upload.", blob_name)

        blob_url = f"https://{blob_service_client.account_name}.blob.core.windows.net/{container_name}/{blob_name}"
        return blob_url
    except Exception as e:
        logging.exception("Error: Failed uploading image: %s", str(e))
        return None


def store_shoes(shoes, uri, database_name, collection_name):
    try:
        with (MongoClient(uri) as client):
            db = client[database_name]
            collection = db[collection_name]

            for shoe in shoes:
                print(shoe.to_dict())
                shoe_dict = shoe.to_dict()

                print("Storing shoe: ", shoe_dict.get('model'))

                existing_shoe = collection.find_one({"initial_image": shoe_dict.get('initial_image')})

                if (
                        existing_shoe
                        and existing_shoe.get('on_sale')
                        and existing_shoe.get('new_price') <= shoe_dict.get('new_price')
                ):
                    shoe_dict['last_updated'] = existing_shoe.get('last_updated')

                collection.update_one(
                    {"initial_image": shoe_dict.get('initial_image')},
                    {
                        "$set": {
                            'brand': shoe_dict.get('brand'),
                            'model': shoe_dict.get('model'),
                            'provider': shoe_dict.get('provider'),
                            'gender': shoe_dict.get('gender'),
                            'new_price': shoe_dict.get('new_price'),
                            'old_price': shoe_dict.get('old_price'),
                            'sizes': shoe_dict.get('sizes'),
                            'available_sizes': shoe_dict.get('available_sizes'),
                            'images': shoe_dict.get('images'),
                            'initial_image': shoe_dict.get('initial_image'),
                            'url': shoe_dict.get('url'),
                            'last_updated': shoe_dict.get('last_updated'),
                            'on_sale': shoe_dict.get('on_sale'),
                        }
                    },
                    upsert=True
                )

            collection.update_many(
                {
                    "on_sale": True,
                    "initial_image": {"$nin": [shoe.to_dict().get('initial_image') for shoe in shoes]}
                },
                {
                    "$set": {
                        "on_sale": False
                    }
                }
            )

    except Exception as e:
        logging.error("Error during storing products: %s", str(e))
