class Shoe:
    def __init__(self, brand, title, old_price, new_price, sizes, provider, image):
        self.brand = brand
        self.title = title
        self.old_price = old_price
        self.new_price = new_price
        self.sizes = sizes
        self.provider = provider
        self.image = image

    def __str__(self):
        return (
            f'Brand: {self.brand}\n'
            f'Title: {self.title}\n'
            f'Old price: {self.old_price}\n'
            f'New price: {self.new_price}\n'
            f'Sizes: {self.sizes}\n'
            f'Provider: {self.provider}\n'
            f'Image: {self.image}\n'
        )

    def to_dict(self):
        return {
            'brand': self.brand,
            'title': self.title,
            'old_price': self.old_price,
            'new_price': self.new_price,
            'sizes': self.sizes,
            'provider': self.provider,
            'image': self.image
        }
