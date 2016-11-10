

ALTER TABLE collectiongroup ADD afno varchar(50) null;
ALTER TABLE collectiongroup ADD org_objid varchar(50) null;
ALTER TABLE collectiongroup ADD org_name varchar(150) null;
CREATE INDEX ix_afno ON collectiongroup (afno);
CREATE INDEX ix_org_objid ON collectiongroup (org_objid);

ALTER TABLE collectiongroup_revenueitem ADD valuetype varchar(50) null;
ALTER TABLE collectiongroup_revenueitem ADD defaultvalue decimal(16,2) null;

