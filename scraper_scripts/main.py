import os
from dotenv import load_dotenv

from scrape_products import scrape_products
from process_products import process_product
from store_products import store_products

load_dotenv()

url = os.getenv('SCRAPE_URL')
uri = os.getenv('MONGODB_URI')

products = scrape_products(url)
processed_products = [
    process_product(product)
    for product in products
    if process_product(product) is not None
]
store_products(processed_products, uri)
