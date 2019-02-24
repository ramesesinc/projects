IRIGA OVS MIGRATIONS

ALTER TABLE ovs_violation_ticket_entry DROP COLUMN account_objid;
ALTER TABLE ovs_violation_ticket_entry DROP COLUMN account_title;
ALTER TABLE ovs_payment ADD COLUMN entityid VARCHAR(50);
DELETE FROM ovs_payment_item WHERE refid NOT IN (SELECT objid FROM ovs_violation_ticket_entry);
ALTER TABLE `ovs_payment_item` ADD CONSTRAINT `fk_ovs_payment_item_ticketentry` FOREIGN KEY (`refid`) REFERENCES `ovs_violation_ticket_entry` (`objid`);

ALTER TABLE ovs_payment_item ADD COLUMN reftype VARCHAR(50);
UPDATE ovs_payment_item SET reftype = 'ovs_violation_ticket_entry';

INSERT IGNORE INTO sys_usergroup (objid,title,domain,userclass,orgclass,role)
VALUES ('OVS.INFO', 'OVS INFO', 'OVS', 'usergroup', NULL, 'INFO'),
('OVS.MASTER', 'OVS MASTER', 'OVS', 'usergroup', NULL, 'MASTER'),
('OVS.REPORT', 'OVS REPORT', 'OVS', 'usergroup', NULL, 'REPORT');