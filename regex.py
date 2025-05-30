import re
import os

file_path = os.path.join("Secuencia", "Secuencia.txt")

with open(file_path, 'r', encoding='utf-8') as file:
    data = file.read()

pattern = r'T00(.*?)(((T01)(.*?)(T03)(.*?))|((T02)(.*?)(T04)(.*?)))(((T05)(.*?)(T07)(.*?)(T09)(.*?))|((T06)(.*?)(T08)(.*?)(T10)(.*?)))(((T11)(.*?)(T13)(.*?))|((T12)(.*?)(T14)(.*?)))(T15)(.*?)(T16)'
replacement = (r'\g<1>\g<5>\g<7>\g<10>\g<12>\g<16>\g<18>\g<20>\g<23>\g<25>\g<27>\g<31>\g<33>\g<36>\g<38>\g<40>')

invariant_patterns = [
    (r'T00.*?T01.*?T03.*?T05.*?T07.*?T09.*?T12.*?T14.*?T15.*?T16', 'T0-T1-T3-T5-T7-T9-T12-T14-T15-T16'),
    (r'T00.*?T01.*?T03.*?T05.*?T07.*?T09.*?T11.*?T13.*?T15.*?T16', 'T0-T1-T3-T5-T7-T9-T11-T13-T15-T16'),
    (r'T00.*?T01.*?T03.*?T06.*?T08.*?T10.*?T12.*?T14.*?T15.*?T16', 'T0-T1-T3-T6-T8-T10-T12-T14-T15-T16'),
    (r'T00.*?T01.*?T03.*?T06.*?T08.*?T10.*?T11.*?T13.*?T15.*?T16', 'T0-T1-T3-T6-T8-T10-T11-T13-T15-T16'),
    (r'T00.*?T02.*?T04.*?T05.*?T07.*?T09.*?T11.*?T13.*?T15.*?T16', 'T0-T2-T4-T5-T7-T9-T11-T13-T15-T16'),
    (r'T00.*?T02.*?T04.*?T05.*?T07.*?T09.*?T12.*?T14.*?T15.*?T16', 'T0-T2-T4-T5-T7-T9-T12-T14-T15-T16'),
    (r'T00.*?T02.*?T04.*?T06.*?T08.*?T10.*?T12.*?T14.*?T15.*?T16', 'T0-T2-T4-T6-T8-T10-T12-T14-T15-T16'),
    (r'T00.*?T02.*?T04.*?T06.*?T08.*?T10.*?T11.*?T13.*?T15.*?T16', 'T0-T2-T4-T6-T8-T10-T11-T13-T15-T16')
]
invariant_counts = [0] * len(invariant_patterns)

total_count = 0

while True:
    match = re.search(pattern, data)
    if not match:
        break

    extracted_match = match[0]
    print("Patrón extraído:", extracted_match)

    for i, (invariant_pattern, name) in enumerate(invariant_patterns):
        if re.fullmatch(invariant_pattern, extracted_match):
            invariant_counts[i] += 1
            print(f"  - Coincide con: {name}")
            break

    data = re.sub(pattern, replacement, data, count=1)
    total_count += 1

t11_count = len(re.findall(r'T11', data))
t12_count = len(re.findall(r'T12', data))


print("Data sobrante: " + data)
# print(f"Las transiciones T11 y T12 se han disparado un extra de: {t11_count} , {t12_count} veces")
print("\n" + str(total_count) + " invariantes encontradas.")
for i, (count, (_, pattern)) in enumerate(zip(invariant_counts, invariant_patterns)):
    print(f"Invariante de transición {pattern}: ha sucedido {count} veces")
