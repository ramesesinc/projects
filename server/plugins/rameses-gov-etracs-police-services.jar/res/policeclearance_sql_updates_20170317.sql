ALTER TABLE policeclearance_payment DROP COLUMN franchiseid;
DROP TABLE policeclearance_payment_item;
ALTER TABLE policeclearance_application ADD COLUMN payment_objid VARCHAR(50);
ALTER TABLE policeclearance_application ADD COLUMN payment_refno VARCHAR(50);
ALTER TABLE policeclearance_application ADD COLUMN payment_refdate DATE;