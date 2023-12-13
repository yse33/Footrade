import logging
import requests
from bs4 import BeautifulSoup
from requests import RequestException

logging.basicConfig(level=logging.ERROR)


def get_soup(url, timeout=10):
    try:
        page = requests.get(url, timeout=timeout)
        if page.status_code == 200:
            soup = BeautifulSoup(page.text, 'html.parser')
            return soup
        else:
            logging.error("Error during requests to %s : %s", url, str(page.status_code))
            return None
    except RequestException as e:
        logging.error("Error during requests to %s : %s", url, repr(e))
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
        if len(products) == 10:
            break

    return products


def scrape_product(product_url, timeout=10):
    soup = get_soup(product_url, timeout)
    if soup is None:
        return None

    product = soup.find('div', class_='product-detail__product-content')
    return product
