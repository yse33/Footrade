import logging
from shoe import Shoe

logging.basicConfig(level=logging.INFO)


def process_product(product):
    title = None
    try:
        brand = product.find('div', class_='heading-eyebrow').find('a').text.strip().lower()
        title = product.find('h1', class_='heading-2').text.strip()

        new_price = product.find('div', class_='price').text.strip()
        old_price = product.find('div', class_='price-old').text.strip()

        size_list = product.find('div', class_='component-sizes__size-list')
        all_sizes = [
            size.text.strip()
            for size in size_list.find_all('div', class_='component-sizes__size')
        ]
        available_sizes = [
            a.find('div').text.strip()
            for a in size_list.find_all('a', class_='component-sizes__size-text')
        ]

        provider = product.find('a', class_='provider-link').text.strip()
        provider_url = product.find('a', class_='j-track-ec')['href']

        images = [
            a['data-src-orig']
            for a in product.find_all('a', class_='detail-change-photo')
        ]

        shoe = Shoe(brand, title, new_price, old_price, all_sizes, available_sizes,
                    provider, provider_url, images)

        return shoe
    except AttributeError as e:
        logging.error("Error during processing %s, Error: %s", title, str(e))
        return None
