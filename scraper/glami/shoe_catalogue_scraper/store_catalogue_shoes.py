import logging
from pymongo import MongoClient

logging.basicConfig(level=logging.INFO)


def delete_shoes(uri, database_name, collection_name):
    try:
        with MongoClient(uri) as client:
            db = client[database_name]
            collection = db[collection_name]

            collection.delete_many({})

    except Exception as e:
        logging.exception("Exception: Failed during deleting shoes: %s", str(e))


def store_shoes(shoes, uri, database_name, collection_name):
    try:
        with MongoClient(uri) as client:
            db = client[database_name]
            collection = db[collection_name]

            for shoe in shoes:
                shoe_dict = shoe.to_dict()
                update_result = collection.update_one(
                    {"blob_id": shoe_dict.get('blob_id')},
                    {"$set": shoe_dict},
                    upsert=True
                )

                # if update_result.upserted_id:
                #     logging.info("Inserted new shoe with blob_id: %s", shoe_dict.get('blob_id'))
                # else:
                #     logging.info("Updated old shoe with blob_id: %s", shoe_dict.get('blob_id'))

            logging.info("Stored %s shoes", len(shoes))

    except Exception as e:
        logging.exception("Exception: Failed during storing shoes: %s", str(e))
