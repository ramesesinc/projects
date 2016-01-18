RENAME TABLE revenueitem TO itemaccount;
DROP TABLE revenueitem_attribute; 
DROP TABLE revenueitem_attribute_type;
ALTER TABLE itemaccount CHANGE parentrevenueitemid parentid VARCHAR(50); 
UPDATE itemaccount SET TYPE='INCOME';