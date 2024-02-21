import os
from dotenv import load_dotenv

from shoe_catalogue_scraper.catalogue_scraper import scrape_catalogue
from shoe_discount_scraper.discount_scraper import scrape_discount

load_dotenv()

url = os.getenv('SCRAPE_URL')
catalogue_url = os.getenv('CATALOGUE_SCRAPE_URL')
uri = os.getenv('MONGO_URI')
database_name = os.getenv('DATABASE_NAME')
collection_name = os.getenv('COLLECTION_NAME')
connection_string = os.getenv('CONNECTION_STRING')
container_name = os.getenv('CONTAINER_NAME')

# scrape_catalogue(catalogue_url, uri, database_name, collection_name, connection_string, container_name)
# for test purposes only scrape 1 page
scrape_discount(url, uri, database_name, collection_name, connection_string, container_name, final_page=1)
