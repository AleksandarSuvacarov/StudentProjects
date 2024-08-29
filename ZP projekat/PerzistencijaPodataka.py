import os
import json
from GenerateRSA import *

class PerzistencijaPodataka:

    def __init__(self, glavnaApp, imeKorisnika):
        self.glavnaApp = glavnaApp
        self.imeKorisnika = imeKorisnika
        self.base_path = os.path.join(self.glavnaApp.data_directory, self.imeKorisnika)
        if not os.path.exists(self.base_path):
            os.makedirs(self.base_path)

    def save_privateRing_record_to_file(self, record):
        record_copy = record.copy()
        record_copy["PublicKey"] = record_copy["PublicKey"].decode('utf-8')
        record_copy["EncPrivateKey"] = b64encode(record_copy["EncPrivateKey"]).decode('utf-8')

        user_id = record["UserId"]
        file_name = f"privateRing_{user_id}.json"
        file_path = os.path.join(self.base_path, file_name)
        with open(file_path, "w") as file:
            json.dump(record_copy, file, indent=4)

    def load_privateRing_records_from_file(self):
        records = []

        for file_name in os.listdir(self.base_path):
            if file_name.startswith("privateRing_") and file_name.endswith(".json"):
                file_path = os.path.join(self.base_path, file_name)
                with open(file_path, "r") as file:
                    record = json.load(file)

                    record["PublicKey"] = RSA.import_key(record["PublicKey"]).export_key()
                    record["EncPrivateKey"] = b64decode(record["EncPrivateKey"])
                    records.append(record)

        return records

    def delete_privateRing_record_from_file(self, record):
        user_id = record["UserId"]
        file_name = f"privateRing_{user_id}.json"
        file_path = os.path.join(self.base_path, file_name)
        if os.path.exists(file_path):
            os.remove(file_path)


    def save_publicRing_record_to_file(self, record):
        record_copy = record.copy()
        record_copy["PublicKey"] = record_copy["PublicKey"].decode('utf-8')

        user_id = record["UserId"]
        file_name = f"publicRing_{user_id}.json"
        file_path = os.path.join(self.base_path, file_name)
        with open(file_path, "w") as file:
            json.dump(record_copy, file, indent=4)

    def load_publicRing_records_from_file(self):
        records = []

        for file_name in os.listdir(self.base_path):
            if file_name.startswith("publicRing_") and file_name.endswith(".json"):
                file_path = os.path.join(self.base_path, file_name)
                with open(file_path, "r") as file:
                    record = json.load(file)

                    record["PublicKey"] = RSA.import_key(record["PublicKey"]).export_key()
                    records.append(record)

        return records

