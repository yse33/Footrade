# # 65a450b5bc3460728dcf0141
#
# from pymongo import MongoClient
# from datetime import datetime
#
# shoe_id = "65a450b5bc3460728dcf0141"
#
# def dummy_func():
#     try:
#         with MongoClient("mongodb+srv://zombiestriker:qY76SkHXktSxvGL0@cluster0.nnbrai4.mongodb.net/?retryWrites=true&w=majority") as client:
#             db = client["diploma"]
#             collection = db["shoes"]
#
#             collection.insert_one({
#                 "name": "Test Nike Air Max 91",
#                 "blob_id": "random_blob_id",
#                 "available_sizes": [],
#                 "brand": "Nike",
#                 "images": ["image1"],
#                 "initial_image": "image1",
#                 "model": "TEST PAIR OF SHOES",
#                 "new_price": 20,
#                 "old_price": 100,
#                 "on_sale": True,
#                 "provider": "NIKE PROVIDER",
#                 "sizes": [],
#                 "url": "https://www.nike.com/air-max-91",
#                 "last_updated": datetime.now(),
#             })
#     except Exception as e:
#             print("Exception: Failed during dummy_func: %s", str(e))
#
#     print("HELLO FROM FUNCTION")
#
# if __name__ == "__main__":
#     dummy_func()
