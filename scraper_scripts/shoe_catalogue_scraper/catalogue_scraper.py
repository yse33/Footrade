import logging

from .scrape_catalogue_shoe_divs import scrape_divs
from .process_catalogue_shoes import process_shoes, get_final_url
from .store_catalogue_shoes import store_shoes, delete_shoes


def scrape_catalogue(url, uri, database_name, collection_name, connection_string, container_name, flag=False):
    page = 0

    while True:
        page += 1

        page_url = url + str(page)

        if page != 1 and page_url != get_final_url(page_url):
            print(f"Old page {page_url}, redirecting to {get_final_url(page_url)}")
            break

        print(f"(CATALOGUE) Scraping page {page_url}")

        shoe_divs = scrape_divs(page_url)

        processed_shoes = process_shoes(shoe_divs, container_name, connection_string, flag)

        if not processed_shoes:
            break

        store_shoes(processed_shoes, uri, database_name, collection_name)

    logging.info("Finished scraping catalogue")
