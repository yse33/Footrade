class Shoe:
    def __init__(self, brand, model, new_price, old_price, gender, sizes, available_sizes,
                 provider, url, images, initial_image, on_sale, last_updated):
        self.brand = brand
        self.model = model
        self.new_price = new_price
        self.old_price = old_price
        self.gender = gender
        self.sizes = sizes
        self.available_sizes = available_sizes
        self.provider = provider
        self.url = url
        self.images = images
        self.initial_image = initial_image
        self.on_sale = on_sale
        self.last_updated = last_updated

    def __str__(self):
        return (
            f'Brand: {self.brand}\n'
            f'Model: {self.model}\n'
            f'New price: {self.new_price}\n'
            f'Old price: {self.old_price}\n'
            f'Gender: {self.gender}\n'
            f'Sizes: {self.sizes}\n'
            f'Available sizes: {self.available_sizes}\n'
            f'Provider: {self.provider}\n'
            f'Url: {self.url}\n'
            f'Images: {self.images}\n'
            f'Initial image: {self.initial_image}\n'
            f'On sale: {self.on_sale}\n'
            f'Last updated: {self.last_updated}\n'
        )

    def to_dict(self):
        return {
            'brand': self.brand,
            'model': self.model,
            'new_price': self.new_price,
            'old_price': self.old_price,
            'gender': self.gender,
            'sizes': self.sizes,
            'available_sizes': self.available_sizes,
            'provider': self.provider,
            'url': self.url,
            'images': self.images,
            'initial_image': self.initial_image,
            'on_sale': self.on_sale,
            'last_updated': self.last_updated
        }
