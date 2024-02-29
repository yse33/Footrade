import logging
import os
from dotenv import load_dotenv
from selenium import webdriver

from scraper_sizeer import scrape_all_shoes as scrape_all_shoes_sizeer
from scraper_district import scrape_all_shoes as scrape_all_shoes_district
from shared_functions import store_shoes

logging.basicConfig(level=logging.INFO)


def main():
    dotenv_path = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), '.env')
    load_dotenv(dotenv_path)
    container_name = os.getenv('CONTAINER_NAME')
    connection_string = os.getenv('CONNECTION_STRING')
    database_name = os.getenv('DATABASE_NAME')
    collection_name = os.getenv('COLLECTION_NAME')
    uri = os.getenv('MONGO_URI')

    url_sizeer = "https://sizeer.bg/mazhki/obuvki/maratonki/converse,jordan,puma,vans,adidas,nike?page="
    url_district = "https://www.districtshoes.bg/muje-obuvki/ejednevni_obuvki-2_77_13/adidas,adidas_sportswear,adidas_originals,converse,vans,nike,puma?brandId=7.2734.8.41.396.136.149&showBy=80&orderBy=discount&page="

    driver = webdriver.Chrome()

    shoes_district = scrape_all_shoes_district(url_district, container_name, connection_string, driver, final_page=2)

    if not shoes_district:
        print("No shoes found from DISTRICT")
        return

    shoes_sizeer = scrape_all_shoes_sizeer(url_sizeer, container_name, connection_string, driver, final_page=2)

    if not shoes_sizeer:
        print("No shoes found from SIZEER")
        return

    shoes = shoes_district + shoes_sizeer

    store_shoes(shoes, uri, database_name, collection_name)


if __name__ == '__main__':
    main()
