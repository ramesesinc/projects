
CREATE TABLE report_itemaccount_mapping ( 
	objid varchar(50) not null, 
	name varchar(255) not null, 
	accountid varchar(50) not null 
) 
; 
ALTER TABLE report_itemaccount_mapping ADD PRIMARY KEY (objid) 
;
CREATE INDEX ix_name ON report_itemaccount_mapping (name)
;
CREATE INDEX ix_accountid ON report_itemaccount_mapping (accountid)
;

INSERT INTO report_itemaccount_mapping (objid, name) VALUES ('GF', 'GARBAGE FEE');
INSERT INTO report_itemaccount_mapping (objid, name) VALUES ('MP', 'MAYOR''S PERMIT');
INSERT INTO report_itemaccount_mapping (objid, name) VALUES ('OCF', 'OCCUPATIONAL FEE');
INSERT INTO report_itemaccount_mapping (objid, name) VALUES ('PLATE', 'BUSINESS PLATE');
INSERT INTO report_itemaccount_mapping (objid, name) VALUES ('STICKER', 'BUSINESS STICKER');
INSERT INTO report_itemaccount_mapping (objid, name) VALUES ('WM', 'WEIGHTS AND MEASURES');

