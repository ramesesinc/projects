 
ALTER TABLE collectiontype ADD allowpos INT DEFAULT 1; 
ALTER TABLE collectiontype ADD sortorder INT DEFAULT 0; 
 
UPDATE collectiontype SET sortorder=100 WHERE name LIKE 'large_cattle%'; 
UPDATE collectiontype SET sortorder=2 WHERE name LIKE 'REAL_PROPERTY_TAX%'; 
UPDATE collectiontype SET sortorder=1 WHERE name LIKE 'BUSINESS_COLLECTION%'; 
UPDATE collectiontype SET sortorder=0 WHERE name LIKE 'GENERAL_COLLECTION%'; 
