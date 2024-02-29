import logging
from bs4 import BeautifulSoup
import datetime

from shoe import Shoe
from shared_functions import (check_url, get_final_url, get_soup,
                              get_brand_from_model, download_image, upload_image)

logging.basicConfig(level=logging.INFO)


def get_details(shoe_page_url, container_name, connection_string, driver):
    if not check_url(shoe_page_url):
        return None

    print("Scraping: ", shoe_page_url)

    driver.get(shoe_page_url)
    page_source = BeautifulSoup(driver.page_source, 'html.parser')

    model = page_source.find('span', {'data-product-number': 'name-formated'}).text.strip()
    if not model:
        print("No model found in: ", shoe_page_url)
        return None

    brand = get_brand_from_model(model)
    if not brand:
        print("No brand found in: ", shoe_page_url)
        return None

    new_price_string = (page_source.find('span', class_='current')
                        .text
                        .strip()
                        .replace(' лв.', '')
                        .replace(',', '.')
                        )
    if not new_price_string:
        print("No price found in: ", shoe_page_url)
        return None
    new_price = float(new_price_string)

    on_sale = False

    old_price_span = page_source.find('span', class_='old')
    if old_price_span:
        old_price_string = (old_price_span
                            .text
                            .strip()
                            .replace(' лв.', '')
                            .replace(',', '.'))
        if not old_price_string:
            print("No old price found in: ", shoe_page_url)
            return None
        old_price = float(old_price_string)
        on_sale = True
    else:
        old_price = new_price

    size_container = page_source.find('div', class_='product-size-section')
    size_items = size_container.find_all('button', class_='btn-product-size')
    sizes = []
    available_sizes = []

    for size_item in size_items:
        size = size_item.text.strip()
        if size:
            size = "EU " + size
            sizes.append(size)
            if 'not-available' not in size_item['class']:
                available_sizes.append(size)

    provider = 'DISTRICT SHOES'

    image_container = page_source.find('div', class_='product-thumbs-container')
    image_links = [img['src']
                   for img in image_container.find_all('img', class_='lazyloaded')
                   ]
    image_links = [link.replace('thumb', 'detail') for link in image_links]
    images_binary = [download_image(image, driver) for image in image_links]
    blob_names = [link.split('/')[6].replace('.jpg', '') for link in image_links]
    images = []
    for i in range(len(images_binary)):
        image_url = upload_image(images_binary[i], container_name, connection_string, blob_names[i])
        images.append(image_url)

    current_date = datetime.datetime.utcnow()

    return Shoe(brand, model, new_price, old_price, 'MALE', sizes, available_sizes,
                provider, shoe_page_url, images, images[0], on_sale, current_date)


def scrape_shoe_page_urls(url, timeout=50):
    divs = []

    if not check_url(url):
        return divs

    soup = get_soup(url, timeout)
    if soup is None:
        return divs

    divs = soup.find_all('div', class_='item preview')

    shoe_page_urls = []
    for div in divs:
        shoe_page_url = 'https://districtshoes.bg' + div.find('a', class_='image')['href']
        shoe_page_urls.append(shoe_page_url)

    return shoe_page_urls


def scrape_all_shoes(base_url, container_name, connection_string, driver, final_page=-1):
    shoes = []
    page = 1

    while True:
        if page == final_page:
            logging.info("Reached final page")
            break

        url = base_url + str(page)

        if page != 1 and url != get_final_url(url, driver):
            logging.info("No more pages to scrape")
            break

        logging.info(f"Scraping page {page}")

        shoe_page_urls = scrape_shoe_page_urls(url)

        if not shoe_page_urls:
            break

        shoes += [get_details(shoe_page_url, container_name, connection_string, driver)
                  for shoe_page_url in shoe_page_urls]

        if not shoes:
            break

        page += 1

    shoes = [shoe for shoe in shoes if shoe is not None]

    return shoes
