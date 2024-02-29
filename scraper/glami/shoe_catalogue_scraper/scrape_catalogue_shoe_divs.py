from urllib.parse import urlparse
import logging
import requests
from bs4 import BeautifulSoup
from requests.exceptions import RequestException

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


def scrape_divs(url, timeout=10):
    divs = []

    parsed_url = urlparse(url)
    if not parsed_url.scheme or not parsed_url.netloc:
        logging.error("Error: Invalid URL: %s", url)
        return divs

    try:
        soup = get_soup(url, timeout)
        if soup is None:
            return divs

        divs = soup.find_all('div', class_='tracker-item')

        return divs
    except requests.exceptions.Timeout:
        logging.exception("Exception: Failed due to Timeout during requests to %s", url)
        return divs
    except Exception as e:
        logging.exception("Exception: Failed during parsing HTML: %s", repr(e))
