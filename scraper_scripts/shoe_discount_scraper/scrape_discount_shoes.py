from urllib.parse import urlparse
import logging
import requests
from bs4 import BeautifulSoup
from requests import RequestException

logging.basicConfig(level=logging.INFO)


def get_soup(url, timeout=10):
    try:
        page = requests.get(url, timeout=timeout, verify=True)
        page.raise_for_status()
        soup = BeautifulSoup(page.text, 'html.parser')
        return soup
    except RequestException as e:
        logging.exception("Exception: Failed to make request to %s : %s", url, repr(e))
        return None


def check_url(url):
    parsed_url = urlparse(url)
    if not parsed_url.scheme or not parsed_url.netloc:
        logging.error("Error: Invalid URL: %s", url)
        return False
    return True


def scrape_divs(url, timeout=10):
    shoes = []

    if not check_url(url):
        return shoes

    try:
        soup = get_soup(url, timeout)
        if soup is None:
            return shoes

        divs = soup.find_all('div', class_='tracker-item')

        for div in divs:
            product_url = div.find('a', class_='j-track-ec')['href']
            product = scrape_div_details(product_url, timeout)
            if product:
                shoes.append(product)

        return shoes
    except requests.exceptions.Timeout:
        logging.exception("Exception: Failed due to Timeout during requests to %s", url)
        return shoes
    except Exception as e:
        logging.exception("Exception: Failed during parsing HTML: %s", repr(e))


def scrape_div_details(product_url, timeout=10):
    if not check_url(product_url):
        return None

    soup = get_soup(product_url, timeout)
    if soup is None:
        return None

    try:
        product = soup.find('div', class_='product-detail__product-content')
    except Exception as e:
        logging.exception("Exception: Failed during parsing HTML: %s", repr(e))
        return None

    return product
