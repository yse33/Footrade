import logging

from pymongo import MongoClient

logging.basicConfig(level=logging.INFO)


def store_products(products, uri):
    client = None
    try:
        client = MongoClient(uri)
        db = client['diploma']
        collection = db['shoes']

        collection.delete_many({})

        product_dicts = [product.to_dict() for product in products]
        collection.insert_many(product_dicts)
    except Exception as e:
        logging.error("Error during storing products: %s", str(e))

    finally:
        if client:
            try:
                client.close()
            except Exception as e:
                logging.error("Error while closing MongoDB connection: %s", str(e))
