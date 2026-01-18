import csv
import re
import os

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
CSV_FILE = os.path.join(SCRIPT_DIR, 'allcar.csv')
DATA_SQL_FILE = os.path.join(SCRIPT_DIR, '..', 'resources', 'data.sql')
OUTPUT_SQL_FILE = os.path.join(SCRIPT_DIR, '..', 'resources', 'data.sql')

def clean_km(km_str):
    """Convert '173.000 ' to 173000"""
    if not km_str:
        return 0
    clean_val = re.sub(r'[^\d]', '', str(km_str))
    return int(clean_val) if clean_val else 0

def read_csv_data():
    """Read CSV and create a mapping of (year, model) -> kilometers"""
    km_data = []
    
    encodings = ['utf-8', 'latin-1', 'iso-8859-9', 'cp1254']
    
    for enc in encodings:
        try:
            with open(CSV_FILE, 'r', encoding=enc) as f:
                reader = csv.DictReader(f)
                for row in reader:
                    year = row.get('Model Year', '')
                    model = row.get('Model', '')
                    km = clean_km(row.get('Kilometers', '0'))
                    if year and model:
                        km_data.append({
                            'year': int(year) if year.isdigit() else 0,
                            'model': model.strip(),
                            'km': km
                        })
            print(f"Successfully read CSV with encoding: {enc}")
            print(f"Total records: {len(km_data)}")
            break
        except (UnicodeDecodeError, Exception) as e:
            print(f"Failed with {enc}: {e}")
            continue
    
    return km_data

def update_data_sql(km_data):
    """Update data.sql with correct kilometer values from CSV"""
    
    with open(DATA_SQL_FILE, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Find the Vehicles INSERT section
    vehicle_pattern = r"INSERT INTO Vehicles \(listing_id, model_year, model_name, kilometers\) VALUES\n(.*?);"
    match = re.search(vehicle_pattern, content, re.DOTALL)
    
    if not match:
        print("ERROR: Could not find Vehicles INSERT statement!")
        return
    
    vehicles_section = match.group(1)
    
    # Parse each vehicle line
    # Format: (1, 1997, 'Hyundai Accent 1.5 GLS', 1730),
    vehicle_line_pattern = r"\((\d+),\s*(\d+),\s*'([^']+)',\s*(\d+)\)"
    
    lines = vehicles_section.split('\n')
    new_lines = []
    updated_count = 0
    
    for line in lines:
        match_line = re.search(vehicle_line_pattern, line)
        if match_line:
            listing_id = int(match_line.group(1))
            year = int(match_line.group(2))
            model = match_line.group(3)
            old_km = int(match_line.group(4))
            
            # Find matching km from CSV (use listing_id - 1 as index since CSV is 0-indexed)
            csv_index = (listing_id - 1) % len(km_data)  # Wrap around if needed
            
            if csv_index < len(km_data):
                csv_record = km_data[csv_index]
                new_km = csv_record['km']
                
                # Replace the old km with new km
                new_line = line.replace(f", {old_km})", f", {new_km})")
                new_lines.append(new_line)
                
                if old_km != new_km:
                    updated_count += 1
                    if updated_count <= 10:  # Show first 10 updates
                        print(f"  Updated: {model} ({year}) - {old_km} -> {new_km} km")
            else:
                new_lines.append(line)
        else:
            new_lines.append(line)
    
    # Rebuild the content
    new_vehicles_section = '\n'.join(new_lines)
    new_content = content.replace(vehicles_section, new_vehicles_section)
    
    # Write back
    with open(OUTPUT_SQL_FILE, 'w', encoding='utf-8') as f:
        f.write(new_content)
    
    print(f"\n✅ Updated {updated_count} vehicle records with correct kilometers!")
    print(f"File saved: {OUTPUT_SQL_FILE}")

if __name__ == '__main__':
    print("Reading CSV data...")
    km_data = read_csv_data()
    
    if km_data:
        print("\nUpdating data.sql...")
        update_data_sql(km_data)
    else:
        print("ERROR: No data read from CSV!")
