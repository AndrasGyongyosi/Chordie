# ChordCalculator

Konfiguráció:
  - "//localhost:3306/" url-en MySQL szerver futtatása
  - "chord_cal_db" néven adatbázis létrehozása
  - User létrehozása az application.properties-ben található paraméterekkel
  - Az első futtatás után a táblákhoz jogok adása:
  GRANT ALL PRIVILEGES ON autsoft_test_db.* TO 'springuser'@'localhost';
