import base64
import struct
from datetime import datetime
from Crypto.Util.Padding import pad
import zipfile
import io


def serijalizacijaSamoPoruke(subject, timestamp, message):
    #timestamp uvek u formatu "YYYY-MM-DD HH:MM:SSZ" (19 karaktera)
    assert len(timestamp) == 19, "Timestamp mora biti u formatu 'YYYY-MM-DD HH:MM:SS'"

    # Pretvorimo subject i message u bajtove
    subject_bytes = subject.encode('utf-8')
    timestamp_bytes = timestamp.encode('utf-8')
    message_bytes = message.encode('utf-8')

    # Dobijamo dužinu subject-a i pretvaramo je u bajt (ili više bajtova ako je potrebno)
    subject_length = len(subject_bytes)

    # Pretpostavimo da je dužina subject-a uvek manja od 256, tako da možemo koristiti jedan bajt za dužinu
    if subject_length >= 256:
        raise ValueError("Subject je predugačak. Maksimalna dužina je 255 bajtova.")

    subject_length_byte = struct.pack('B', subject_length)  # 'B' format znači jedan bajt

    # Konkateniramo sve bajtove zajedno: subject_length_byte + subject_bytes + timestamp_bytes + message_bytes
    serialized_data = subject_length_byte + subject_bytes + timestamp_bytes + message_bytes

    return serialized_data

def serijalizujSaPotpisom(velikiPrivatni, timestamp, keyid, okteti, potpis, serijalizovana_poruka):
    assert len(timestamp) == 19, "Timestamp mora biti u formatu 'YYYY-MM-DD HH:MM:SS'"

    if velikiPrivatni == 1:
        velikiPrivatni_byte = b'\x01'
    else:
        velikiPrivatni_byte = b'\x00'

        # Redom 19, 16, 2, 128 bajtova
    timestamp_bytes = timestamp.encode('utf-8')
    keyid_bytes = keyid.encode('utf-8')  # u bajtovima
    okteti_bytes = okteti
    potpis_bytes = potpis
    serijalizovana_poruka_bytes = serijalizovana_poruka

    # Konkatenacija svih delova
    serialized_data = (
            velikiPrivatni_byte +
            timestamp_bytes +
            keyid_bytes +
            okteti_bytes +
            potpis_bytes +
            serijalizovana_poruka_bytes
    )

    return serialized_data

    # #redom 19, 16, 2, 128 bajtova
    # timestamp_bytes = timestamp.encode('utf-8')
    # keyid_bytes = keyid.encode('utf-8') #u bajtovima
    # okteti_bytes = okteti
    # potpis_bytes = potpis
    # serijalizovana_poruka_bytes = serijalizovana_poruka
    #
    # # Konkatenacija svih delova
    # serialized_data = (
    #     timestamp_bytes +
    #     keyid_bytes +
    #     okteti_bytes +
    #     potpis_bytes +
    #     serijalizovana_poruka_bytes
    # )
    #
    # return serialized_data

def deserijalizujSaPotpisom(serialized_data):
    # Prvi bajt je velikiPrivatni bit
    velikiPrivatni_byte = serialized_data[0:1]
    #print(velikiPrivatni_byte)

    # Sledećih 19 bajtova je timestamp
    timestamp_bytes = serialized_data[1:20]
    timestamp = timestamp_bytes.decode('utf-8')

    # Sledećih 16 bajtova je keyid
    keyid_bytes = serialized_data[20:36]
    keyid = keyid_bytes.decode('utf-8')

    # Sledeća 2 bajta su okteti
    okteti_bytes = serialized_data[36:38]

    # Dužina potpisa zavisi od vrednosti velikogPrivatnog
    if velikiPrivatni_byte == b'\x01':
        potpis_length = 256
    else:
        potpis_length = 128

    #print(potpis_length)
    # Sledećih 128 ili 256 bajtova je potpis
    potpis_bytes = serialized_data[38:38 + potpis_length]

    # Ostatak su bajtovi poruke
    serijalizovana_poruka_bytes = serialized_data[38 + potpis_length:]

    return timestamp, keyid, okteti_bytes, potpis_bytes, serijalizovana_poruka_bytes


def dodajBiteZaglavlja(serijalizovanaPoruka, bit1, bit2, bit3):
    bits = (bit1 << 2) | (bit2 << 1) | bit3
    bits_byte = struct.pack('B', bits)

    serialized_data = bits_byte + serijalizovanaPoruka

    return serialized_data


def skiniBiteZaglavlja(serijalizovanaPoruka):
    # Prvih osam bita su bitovi zaglavlja
    bits_byte = serijalizovanaPoruka[:1]
    bits = struct.unpack('B', bits_byte)[0]

    # Izdvajamo pojedine bitove
    bit1 = (bits & 0b100) >> 2
    bit2 = (bits & 0b010) >> 1
    bit3 = bits & 0b001

    # Ostatak serijalizovane poruke je bez bitova zaglavlja
    bezBitovaZaglavlja = serijalizovanaPoruka[1:]

    return bit1, bit2, bit3, bezBitovaZaglavlja





def deserijalizacijaSamoPoruke(serialized_data):
    #prvi bajt je dužina subjecta
    subject_length = struct.unpack('B', serialized_data[:1])[0]

    #subject_length bajtova za subject
    subject_bytes = serialized_data[1:1 + subject_length]
    subject = subject_bytes.decode('utf-8')

    #19 bajtova je timestamp
    timestamp_bytes = serialized_data[1 + subject_length:1 + subject_length + 19]
    timestamp = timestamp_bytes.decode('utf-8')

    #Ostatak su bajtovi poruke
    message_bytes = serialized_data[1 + subject_length + 19:]
    message = message_bytes.decode('utf-8')

    return subject, timestamp, message


def zipuj_poruku(serijalizovana_poruka):
    zipovan_arhiv = io.BytesIO()

    with zipfile.ZipFile(zipovan_arhiv, 'w', zipfile.ZIP_DEFLATED) as zipf:
        zipf.writestr('poruka.txt', serijalizovana_poruka)

    zipovani_bajtovi = zipovan_arhiv.getvalue()

    return zipovani_bajtovi

def dezipuj_poruku(zipovani_bajtovi):
    zipovan_arhiv = io.BytesIO(zipovani_bajtovi)

    with zipfile.ZipFile(zipovan_arhiv, 'r') as zipf:
        sadrzaj = zipf.read('poruka.txt')

    return sadrzaj


def serijalizacijaEnkriptovanePoruke(algoritam, velikiJavni, keyid, key, message):
    algoritam_byte = None
    if algoritam == "AES128" and velikiJavni == 0:
        algoritam_byte = b'\x00'
    elif algoritam == "AES128" and velikiJavni == 1:
        algoritam_byte = b'\x01'
    elif algoritam == "TripleDES" and velikiJavni == 0:
        algoritam_byte = b'\x10'
    elif algoritam == "TripleDES" and velikiJavni == 1:
        algoritam_byte = b'\x11'
    keyid_bytes = keyid.encode('utf-8')
    if len(key) < 256:
        key_bytes = pad(key, 256)
    else:
        key_bytes = key[:256]
    message_bytes = message
    #print(algoritam_byte)
    serialized_data = algoritam_byte + keyid_bytes + key_bytes + message_bytes

    return serialized_data


def deserijalizacijaEnkriptovanePoruke(serialized_data):
    algoritam_byte = serialized_data[0:1]
    algoritam = None
    velikiJavni = None
    if algoritam_byte == b'\x00':
        algoritam = "AES128"
        velikiJavni = 0
    elif algoritam_byte == b'\x01':
        algoritam = "AES128"
        velikiJavni = 1
    elif algoritam_byte == b'\x10':
        algoritam = "TripleDES"
        velikiJavni = 0
    elif algoritam_byte == b'\x11':
        algoritam = "TripleDES"
        velikiJavni = 1
    #print("Veliki javni: ", velikiJavni)
    #print(algoritam)
    keyid_bytes = serialized_data[1:17]
    #TODO uzeti samo 128 ako je kraci
    key_bytes = serialized_data[17:273]
    message_bytes = serialized_data[273:]

    keyid = keyid_bytes.decode('utf-8')

    return algoritam,velikiJavni, keyid, key_bytes, message_bytes




# Primer podataka
# subject = "Hellooooooooooooooooooo"
# timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
# message = "This is a test message."
#
# serialized_data = serijalizacijaSamoPoruke(subject, timestamp, message)
# serialized_data = dodajBiteZaglavlja(serialized_data, 1,0,0)
# with open("upis.bin", "wb") as file:
#     file.write(serialized_data)
# print(serialized_data)
# print("Serialized data as list of bytes:", repr(serialized_data))
# print()
#
# # Dekodovanje nazad u originalne podatke
#
# bit1, bit2,bit3, serialized_data = skiniBiteZaglavlja(serialized_data)
# deserialized_subject, deserialized_timestamp, deserialized_message = deserijalizacijaSamoPoruke(serialized_data)
# print(deserialized_subject)
# print(deserialized_timestamp)
# print(deserialized_message)
# print(bit1)
# print(bit2)
# print(bit3)