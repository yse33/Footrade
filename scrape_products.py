import logging
import requests
from bs4 import BeautifulSoup
from requests import RequestException

logging.basicConfig(level=logging.ERROR)


def get_soup(url, timeout=10):
    try:
        page = requests.get(url, timeout=timeout)
        page.raise_for_status()
        soup = BeautifulSoup(page.text, 'html.parser')
        return soup
    except RequestException as e:
        logging.error("Error during requests to %s : %s", url, str(e))
        return None


def scrape_products(url, timeout=10):
    products = []

    soup = get_soup(url, timeout)
    if soup is None:
        return products

    product_divs = soup.find_all('div', class_='tracker-item')

    for product_div in product_divs:
        product_url = product_div.find('a', class_='j-track-ec')['href']
        product = scrape_product(product_url, timeout)
        if product:
            products.append(product)

    return products


def scrape_product(product_url, timeout=10):
    soup = get_soup(product_url, timeout)
    if soup is None:
        return None

    product = soup.find('div', class_='product-detail__product-content')
    return product
