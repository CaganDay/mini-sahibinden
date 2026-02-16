import pandas as pd
import os

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
INPUT_FILE = os.path.join(SCRIPT_DIR, '..', 'resources', 'processed_turkish_house_sales.csv')
OUTPUT_FILE = os.path.join(SCRIPT_DIR, '..', 'resources', 'house_data.sql')

def parse_date(date_str):
    """Parse Turkish date format to SQL date format"""
    months = {
        'Ocak': '01', 'Şubat': '02', 'Mart': '03', 'Nisan': '04', 
        'Mayıs': '05', 'Haziran': '06', 'Temmuz': '07', 'Ağustos': '08', 
        'Eylül': '09', 'Ekim': '10', 'Kasım': '11', 'Aralık': '12'
    }
    try:
        parts = date_str.strip().split()
        day = parts[0].zfill(2)
        month = months.get(parts[1], '01')
        year = parts[2]
        return f'{year}-{month}-{day}'
    except:
        return '2025-01-01'

def generate_house_sql():
    print(f"Reading from: {INPUT_FILE}")
    df = pd.read_csv(INPUT_FILE)
    print(f"Loaded {len(df)} rows")

    with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
        f.write('-- House data INSERT statements\n')
        f.write('USE MiniSahibinden;\n\n')
        f.write('INSERT INTO house (seller_type, square_meters, room_count, city, district, neighborhood, date_posted, price) VALUES\n')
        
        values = []
        for _, row in df.iterrows():
            seller = str(row['satici_tip']).replace("'", "''")
            sqm = float(row['Metrekare']) if pd.notna(row['Metrekare']) else 0
            rooms = str(row['Oda_Sayisi']).replace("'", "''")
            city = str(row['il']).replace("'", "''")
            district = str(row['Ilce']).replace("'", "''")
            neighborhood = str(row['Mahalle']).replace("'", "''")
            date = parse_date(str(row['Tarih']))
            price = int(row['fiyat']) if pd.notna(row['fiyat']) else 0
            
            values.append(f"('{seller}', {sqm}, '{rooms}', '{city}', '{district}', '{neighborhood}', '{date}', {price})")
        
        f.write(',\n'.join(values) + ';\n')

    print(f"Generated SQL for {len(values)} houses at: {OUTPUT_FILE}")

if __name__ == "__main__":
    generate_house_sql()
