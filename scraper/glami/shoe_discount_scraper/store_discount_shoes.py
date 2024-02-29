import logging

from pymongo import MongoClient

logging.basicConfig(level=logging.INFO)


def set_on_sale_false(uri, database_name, collection_name):
    client = None
    try:
        with MongoClient(uri) as client:
            db = client[database_name]
            collection = db[collection_name]

            update_result = collection.update_many(
                {"on_sale": True},
                {
                    "$set": {
                        'on_sale': False
                    }
                }
            )

            logging.info("Updated %s documents", update_result.modified_count)
    except Exception as e:
        logging.error("Error during setting on_sale to False: %s", str(e))


def store_shoes(shoes, uri, database_name, collection_name):
    client = None
    try:
        with MongoClient(uri) as client:
            db = client[database_name]
            collection = db[collection_name]

            for shoe in shoes:
                shoe_dict = shoe.to_dict()
                update_result = collection.update_one(
                    {"blob_id": shoe_dict.get('blob_id')},
                    {
                        "$set": {
                            'new_price': shoe_dict.get('new_price'),
                            'old_price': shoe_dict.get('old_price'),
                            'sizes': shoe_dict.get('sizes'),
                            'available_sizes': shoe_dict.get('available_sizes'),
                            'images': shoe_dict.get('images'),
                            'on_sale': True,
                            'last_updated': shoe_dict.get('last_updated')
                        }
                    },
                    upsert=True
                )

                if update_result.upserted_id:
                    logging.info("Inserted new shoe with blob_id: %s", shoe_dict.get('blob_id'))
                else:
                    logging.info("Updated old shoe with blob_id: %s", shoe_dict.get('blob_id'))
    except Exception as e:
        logging.error("Error during storing products: %s", str(e))
