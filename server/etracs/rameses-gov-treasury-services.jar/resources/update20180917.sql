ALTER TABLE checkpayment CHANGE collectorid collector_objid varchar(50);
ALTER TABLE checkpayment ADD COLUMN collector_name VARCHAR(255);
ALTER TABLE checkpayment ADD COLUMN subcollector_objid VARCHAR(50);
ALTER TABLE checkpayment ADD COLUMN subcollector_name VARCHAR(255);
UPDATE checkpayment cp, sys_user u
SET cp.collector_name = u.name 
WHERE cp.collector_objid = u.objid;

ALTER TABLE checkpayment_deadchecks CHANGE collectorid collector_objid varchar(50);
ALTER TABLE checkpayment_deadchecks ADD COLUMN collector_name VARCHAR(255);
ALTER TABLE checkpayment_deadchecks ADD COLUMN subcollector_objid VARCHAR(50);
ALTER TABLE checkpayment_deadchecks ADD COLUMN subcollector_name VARCHAR(255);
