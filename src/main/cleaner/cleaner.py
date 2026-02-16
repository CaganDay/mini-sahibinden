import csv
import re
import os
import pandas as pd

# Configuration
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
INPUT_FILE = os.path.join(SCRIPT_DIR, 'allcar.csv')
OUTPUT_FILE = os.path.join(SCRIPT_DIR, 'allcar_2025.csv')
PRICE_MULTIPLIER = 5.5  # Adjusts 2021 prices to 2025 levels

def clean_price(price_str):
    if pd.isna(price_str): return 0
    # Extract the last number found in the string (handling '109.000 TL108.000 TL')
    matches = re.findall(r'(\d{1,3}(?:\.\d{3})*)', str(price_str))
    if not matches: return 0
    return int(matches[-1].replace('.', ''))

def clean_km(km_str):
    if pd.isna(km_str): return 0
    # Remove non-numeric characters
    clean_val = re.sub(r'[^\d]', '', str(km_str))
    return int(clean_val) if clean_val else 0

def process_data():
    # 1. Read with Pandas (Handles newlines inside quotes automatically)
    # Try different encodings - Turkish CSV files often use ISO-8859-9 (Latin-5) or Windows-1254
    encodings_to_try = ['utf-8', 'latin-1', 'iso-8859-9', 'cp1254', 'utf-16']
    
    df = None
    for enc in encodings_to_try:
        try:
            df = pd.read_csv(INPUT_FILE, encoding=enc, on_bad_lines='skip')
            print(f"Successfully read file with encoding: {enc}")
            break
        except (UnicodeDecodeError, Exception) as e:
            print(f"Failed with encoding {enc}: {e}")
            continue
    
    if df is None:
        print("ERROR: Could not read the CSV file with any encoding!")
        return

    # 2. Rename columns to standard names for easier access
    # Assuming columns are: ["Model Year", "Model", "Price", "Kilometers"]
    df.columns = ['year', 'model_full', 'price_raw', 'km_raw']
    
    # Limit to 1000 rows
    df = df.head(1000)

    print("Generating SQL statements...")
    
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as f:
        f.write("INSERT INTO car (model_year, model, price, kilometers) VALUES\n")
        
        values_list = []
        for index, row in df.iterrows():
            # Clean Data
            year = int(row['year']) if str(row['year']).isdigit() else 2000
            
            # Full model includes make (e.g., "Hyundai Accent 1.5 GLS")
            full_model = str(row['model_full']).strip()

            # Price Calculation
            price = int(clean_price(row['price_raw']) * PRICE_MULTIPLIER)

            # KM Cleaning
            km = clean_km(row['km_raw'])

            # Create SQL Value String
            # Escape single quotes in names (e.g. "D'acia")
            model_sql = full_model.replace("'", "''")
            
            values_list.append(f"({year}, '{model_sql}', {price}, {km})")

        # Join all values with commas and a semicolon at the end
        f.write(",\n".join(values_list) + ";\n")

    print(f"Done! Processed {len(values_list)} rows into {OUTPUT_FILE}")

if __name__ == "__main__":
    process_data()