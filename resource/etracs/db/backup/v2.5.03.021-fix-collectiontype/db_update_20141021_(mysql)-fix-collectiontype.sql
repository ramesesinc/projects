
ALTER TABLE collectiontype ADD COLUMN category VARCHAR(100); 

CREATE INDEX ix_collectiontype_category ON collectiontype (category);

SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE cashreceipt 
	ADD CONSTRAINT fk_cashreceipt_collectiontype FOREIGN KEY (collectiontype_objid) REFERENCES collectiontype(`objid`); 
SET FOREIGN_KEY_CHECKS=1;	

