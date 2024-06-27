import re

data = 'T0T1T0T2T3T5T4T6T0T1T7T8T0T2T3T9T5T11T0T4T10T1T6T7T13T15T11T0T2T3T8T9T5T16T13T15T11T0T4T1T10T6T7T16T13T15T12T0T2T3T8T9T5T14T16T11T15T0T4T1T10T6T7T16T13T15T11T0T2T3T8T9T5T16T13T15T11T4T0T1T10T6T7T16T13T15T11T0T2T3T8T9T5T16T13T11T15T0T4T1T10T6T7T16T13T15T12T0T2T3T8T9T5T14T16T15T12T0T4T1T10T6T7T14T16T12T15T0T2T3T8T9T5T14T16T15T11T0T4T1T10T6T7T16T13T12T15T0T2T3T8T9T5T16T14T12T15T4T0T1T10T6T7T14T16T12T15T0T2T3T8T9T5T16T14T12T15T0T4T1T10T6T7T16T14T15T11T0T2T3T8T9T5T13T16T15T12T0T4T1T10T6T7T14T16T12T15T0T2T3T8T9T5T14T16T12T15T4T0T1T10T6T7T16T14T11T0T2T3T8T9T13T4T10'
pattern = ('T0(.*?)((T1)(.*?)(T3)(.*?)((T5)(.*?)(T7)(.*?)(T9)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14))|(T6)(.*?)(T8)(.*?)(T10)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14)))|(T2)(.*?)(T4)(.*?)((T5)(.*?)(T7)(.*?)(T9)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14))|(T6)(.*?)(T8)(.*?)(T10)(.*?)((T11)(.*?)(T13)|(T12)(.*?)(T14))))(.*?)(T15)(.*?)(T16)(.*?)')
replacement = ('\g<1>\g<4>\g<6>\g<9>\g<11>\g<13>\g<16>\g<19>\g<22>\g<24>\g<26>\g<29>\g<32>\g<35>\g<37>\g<40>\g<42>\g<44>\g<47>\g<50>\g<53>\g<55>\g<57>\g<60>\g<63>\g<65>\g<67>\g<69>')

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

