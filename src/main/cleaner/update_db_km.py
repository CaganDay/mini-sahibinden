import csv
import re
import os
import mysql.connector

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
CSV_FILE = os.path.join(SCRIPT_DIR, 'allcar.csv')

def clean_km(km_str):
    """Convert '173.000 ' to 173000"""
    if not km_str:
        return 0
    clean_val = re.sub(r'[^\d]', '', str(km_str))
    return int(clean_val) if clean_val else 0

def read_csv_data():
    """Read CSV and return list of kilometers"""
    km_data = []
    
    with open(CSV_FILE, 'r', encoding='latin-1') as f:
        reader = csv.DictReader(f)
        for row in reader:
            km = clean_km(row.get('Kilometers', '0'))
            km_data.append(km)
    
    print(f"Read {len(km_data)} records from CSV")
    return km_data

def update_database(km_data):
    """Update MySQL database with correct kilometers"""
    conn = mysql.connector.connect(
        host='localhost',
        user='root',
        password='Merrow4863',
        database='MiniSahibinden'
    )
    cursor = conn.cursor()
    
    # Get all vehicles ordered by listing_id
    cursor.execute("SELECT listing_id FROM Vehicles ORDER BY listing_id")
    vehicles = cursor.fetchall()
    
    updated = 0
    for i, (listing_id,) in enumerate(vehicles):
        csv_index = (listing_id - 1) % len(km_data)
        new_km = km_data[csv_index]
        
        cursor.execute(
            "UPDATE Vehicles SET kilometers = %s WHERE listing_id = %s",
            (new_km, listing_id)
        )
        updated += 1
        
        if updated <= 5:
            print(f"  Updated listing_id={listing_id}: km={new_km}")
    
    conn.commit()
    cursor.close()
    conn.close()
    
    print(f"\n✅ Updated {updated} vehicles in the database!")

if __name__ == '__main__':
    km_data = read_csv_data()
    if km_data:
        update_database(km_data)
