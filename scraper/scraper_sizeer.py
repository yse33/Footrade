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

    shoe_container = page_source.find('div', class_='product-cta-container')
    if shoe_container is None:
        print("No shoe container found in: ", shoe_page_url)
        return None

    model = shoe_container.find('h1', class_='product-name').text.strip()
    if not model:
        print("No model found in: ", shoe_page_url)
        return None

    brand = get_brand_from_model(model)
    if not brand:
        print("No brand found in: ", shoe_page_url)
        return None

    new_price_string = (shoe_container.find('div', class_='price-new')
                        .find('span', class_='price-value').text.strip()
                        .replace(',', '.'))
    if not new_price_string:
        print("No price found in: ", shoe_page_url)
        return None
    new_price = float(new_price_string)

    on_sale = False

    old_price_div = shoe_container.find('div', class_='price-old', attrs={'data-price-type': 'catalog'})
    if old_price_div:
        old_price_string = (old_price_div.find('span', class_='price-value')
                            .text.strip()
                            .replace(',', '.'))
        if not old_price_string:
            print("No old price found in: ", shoe_page_url)
            return None
        old_price = float(old_price_string)
        on_sale = True
    else:
        old_price = new_price

    size_list = shoe_container.find('div', class_='is-euSizes')
    size_items = size_list.find_all('div', class_='size-item')
    available_sizes = []
    sizes = []

    for size_item in size_items:
        size = size_item.find('span', class_='size-original').text.strip()
        if size:
            size = "EU " + size
            sizes.append(size)
            if 'size-unavailable' not in size_item['class']:
                available_sizes.append(size)

    provider = 'SIZEER'

    image_links = [img['data-src']
                   for img_container in shoe_container.find_all('a', class_='color-item')
                   for img in img_container.find_all('img')]
    image_links = [link.replace('_280_280', '') for link in image_links]
    image_links = ['https://sizeer.bg' + link for link in image_links]
    images_binary = [download_image(image, driver) for image in image_links]
    blob_names = [link.split('/')[8] for link in image_links]
    print(blob_names)
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

    divs = soup.find_all('div', class_='list-item')

    shoe_page_urls = []
    for div in divs:
        shoe_page_url = 'https://sizeer.bg' + div.find('a', class_='item-name')['href']
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

    for shoe in shoes:
        print(shoe.to_dict())

    return shoes
