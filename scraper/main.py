import logging
import os
from dotenv import load_dotenv
from selenium import webdriver

from scraper_sizeer import scrape_all_shoes
from shared_functions import store_shoes

logging.basicConfig(level=logging.INFO)


def main():
    dotenv_path = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), '.env')
    load_dotenv(dotenv_path)
    url_sizeer = os.getenv('URL_SIZEER')
    container_name = os.getenv('CONTAINER_NAME')
    connection_string = os.getenv('CONNECTION_STRING')
    database_name = os.getenv('DATABASE_NAME')
    collection_name = os.getenv('COLLECTION_NAME')
    uri = os.getenv('MONGO_URI')

    driver = webdriver.Chrome()

    shoes = scrape_all_shoes(url_sizeer, container_name, connection_string, driver, final_page=2)

    if not shoes:
        print("No shoes found")
        return

    store_shoes(shoes, uri, database_name, collection_name)


if __name__ == '__main__':
    main()
