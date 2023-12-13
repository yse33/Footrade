class Shoe:
    def __init__(self, brand, model, new_price, old_price, all_sizes, available_sizes, provider, provider_url, images):
        self.brand = brand
        self.model = model
        self.new_price = new_price
        self.old_price = old_price
        self.all_sizes = all_sizes
        self.available_sizes = available_sizes
        self.provider = provider
        self.provider_url = provider_url
        self.images = images

    def __str__(self):
        return (
            f'Brand: {self.brand}\n'
            f'Model: {self.model}\n'
            f'New price: {self.new_price}\n'
            f'Old price: {self.old_price}\n'
            f'Sizes: {self.all_sizes}\n'
            f'Available sizes: {self.available_sizes}\n'
            f'Provider: {self.provider}\n'
            f'Provider url: {self.provider_url}\n'
            f'Images: {self.images}\n'
        )

    def to_dict(self):
        return {
            'brand': self.brand,
            'model': self.model,
            'new_price': self.new_price,
            'old_price': self.old_price,
            'sizes': self.all_sizes,
            'available_sizes': self.available_sizes,
            'provider': self.provider,
            'provider_url': self.provider_url,
            'images': self.images
        }
