import logging
from shoe import Shoe

logging.basicConfig(level=logging.INFO)


def process_product(product):
    title = None
    try:
        brand = product.select_one('div.heading-eyebrow a').text.strip().upper()
        model = product.select_one('h1.heading-2').text.strip()

        new_price = float(
            product.select_one('div.price').text.strip()
            .replace('\xa0лв.', '').replace(',', '.')
        )
        old_price = float(
            product.select_one('div.price-old').text.strip()
            .replace('\xa0лв.', '').replace(',', '.')
        )

        size_list = product.select_one('div.component-sizes__size-list')
        all_sizes = [
            size.text.strip()
            for size in size_list.select('div.component-sizes__size')
        ]
        available_sizes = [
            a.select_one('div').text.strip()
            for a in size_list.select('a.component-sizes__size-text')
        ]

        provider = product.select_one('a.provider-link').text.strip()
        url = product.select_one('a.j-track-ec')['href']

        images = [
            a['data-src-orig']
            for a in product.select('a.detail-change-photo')
        ]

        shoe = Shoe(brand, model, new_price, old_price, all_sizes, available_sizes,
                    provider, url, images)

        return shoe
    except AttributeError as e:
        logging.error("Error during processing %s, Error: %s", title, str(e))
        return None
