import requests
from bs4 import BeautifulSoup
from requests import RequestException


def scrape_products(url, timeout=10):
    try:
        page = requests.get(url, timeout=timeout)
        page.raise_for_status()
        soup = BeautifulSoup(page.text, 'html.parser')
        products = soup.find_all('div', class_='tracker-item')
        return products
    except RequestException as e:
        print(f"'Error during requests to {url} : {str(e)}')")
        return []
