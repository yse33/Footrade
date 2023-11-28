from pymongo import MongoClient


def store_products(products, uri):
    client = MongoClient(uri)
    db = client['diploma']
    collection = db['shoes']

    for product in products:
        collection.insert_one(product.to_dict())

    client.close()
