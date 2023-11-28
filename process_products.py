from shoe import Shoe


def process_product(product):
    title = None
    try:
        brand = product.find('div', class_='item__brand').span.text.strip()
        title = product.find('div', class_='item__title').text.strip()
        provider = product.find('span', class_='item__provider-name').contents[-1].strip()

        size_list = product.find('span', class_='size-list')
        sizes = [size.text.strip('| ') for size in size_list.find_all('span') if size.text.strip()]

        new_price_tag = (product.find('span', class_='item-price__new')
                         or product.find('span', class_='item__price__voucher'))
        new_price = new_price_tag.text.strip() if new_price_tag else None

        old_price = product.find('strike', class_='item__price__old').text.strip()
        image = product.find('img')['src']

        shoe = Shoe(brand, title, old_price, new_price, sizes, provider, image)
        return shoe
    except AttributeError as e:
        print(f'Error during processing product with title {title}, Error: {str(e)}')
        return None
