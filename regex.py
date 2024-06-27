import re
import os

file_path = os.path.join("Secuencia", "Secuencia.txt")

with open(file_path, 'r', encoding='utf-8') as file:
    data = file.read()

pattern = (r'T0(.*?)((T1)(.*?)(T3)(.*?)((T5)(.*?)(T7)(.*?)(T9)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14))|(T6)(.*?)(T8)(.*?)(T10)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14)))|(T2)(.*?)(T4)(.*?)((T5)(.*?)(T7)(.*?)(T9)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14))|(T6)(.*?)(T8)(.*?)(T10)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14))))(.*?)(T15)(.*?)(T16)(.*?)')
replacement = (r'\g<1>\g<4>\g<6>\g<9>\g<11>\g<13>\g<16>\g<19>\g<22>\g<24>\g<26>\g<29>\g<32>\g<35>\g<37>\g<40>\g<42>\g<44>\g<47>\g<50>\g<53>\g<55>\g<57>\g<60>\g<63>\g<65>\g<67>\g<69>')

total_count = 0

while True:
    modified_data, count = re.subn(pattern, replacement, data)
    ##print("[" + modified_data + ", " + str(count) + "]")
    total_count += count
    if count == 0:
        break
    data = modified_data

t11_count = len(re.findall(r'T11', data))
t12_count = len(re.findall(r'T12', data))


print(str(total_count) + " invariantes encontradas.")
print("Data sobrante: " + data)
print(f"Las transiciones T11 y T12 se han disparado un extra de: {t11_count} , {t12_count} veces")

