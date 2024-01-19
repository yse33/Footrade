import logging

from .scrape_discount_shoes import scrape_divs
from .process_discount_shoes import process_shoes
from .store_discount_shoes import store_shoes


def scrape_discount(url, uri, database_name, collection_name, connection_string, container_name):
    page = 0

    while True:
        page += 1

        page_url = url + str(page)

        print(f"(DISCOUNT) Scraping page {page_url}")

        shoe_divs = scrape_divs(page_url)

        processed_shoes = process_shoes(shoe_divs, container_name, connection_string)

        if not processed_shoes:
            break

        store_shoes(processed_shoes, uri, database_name, collection_name)

    logging.info("Finished scraping discount")
