INSERT INTO sys_usergroup  (objid, title, domain, userclass, orgclass, role) VALUES ('MARKET.ADMIN','MARKET ADMIN','MARKET',NULL,NULL,'ADMIN');
INSERT INTO sys_usergroup  (objid, title, domain, userclass, orgclass, role) VALUES ('MARKET.MASTER','MARKET MASTER','MARKET',NULL,NULL,'MASTER');
INSERT INTO sys_usergroup  (objid, title, domain, userclass, orgclass, role) VALUES ('MARKET.RULE_AUTHOR','MARKET RULE AUTHOR','MARKET',NULL,NULL,'RULE_AUTHOR');


INSERT INTO collectiontype (objid,state,`name`,title,formno,`handler`,allowbatch,barcodekey,allowonline,allowoffline,sortorder)
VALUES ('COLLTYPE-3804f0a4:156da401dbb:-7dd1','DRAFT','MARKET_RENTAL','MARKET RENTAL','51','marketrental',NULL,'51031',1,0,0);