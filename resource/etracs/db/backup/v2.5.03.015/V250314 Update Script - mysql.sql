alter table rptbill_ledger_item add column basicidle decimal(16,2);
update rptbill_ledger_item set basicidle = 0.0;

alter table rptledgerfaas add column idleland int;
update rptledgerfaas set idleland = 0;

alter table cashreceiptitem_rpt_online add column basicidle decimal(16,2);
update cashreceiptitem_rpt_online  set basicidle = 0;



INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('IDLE_LAND', 'rptbilling', 'Idle Land Computation', '10');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('AFTER_IDLE_LAND', 'rptbilling', 'After Idle Land Computation', '11');


UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Tax Computation', `sortorder`='1' WHERE (`name`='TAX') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='After Tax Computation', `sortorder`='2' WHERE (`name`='AFTER_TAX') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Penalty Computation', `sortorder`='3' WHERE (`name`='PENALTY') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='After Penalty Computation', `sortorder`='4' WHERE (`name`='AFTER_PENALTY') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Discount Computation', `sortorder`='5' WHERE (`name`='DISCOUNT') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='After Discount Computation', `sortorder`='6' WHERE (`name`='AFTER_DISCOUNT') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Idle Land Computation', `sortorder`='10' WHERE (`name`='IDLE_LAND') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='After Idle Land Computation', `sortorder`='11' WHERE (`name`='AFTER_IDLE_LAND') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Before Miscellaneous Computation', `sortorder`='15' WHERE (`name`='BEFORE-MISC-COMP') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Miscellaneous Computation', `sortorder`='16' WHERE (`name`='MISC-COMP') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Summary', `sortorder`='20' WHERE (`name`='SUMMARY') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Barangay Share Computation', `sortorder`='25' WHERE (`name`='BRGY_SHARE') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='LGU Share Computation', `sortorder`='26' WHERE (`name`='LGU_SHARE') AND (`ruleset`='rptbilling');
UPDATE `sys_rulegroup` SET `ruleset`='rptbilling', `title`='Province Share Computation', `sortorder`='27' WHERE (`name`='PROV_SHARE') AND (`ruleset`='rptbilling');



-- IDLE LAND ACOUNTS
INSERT INTO `revenueitem` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`) VALUES ('REVITEM3570b102:14736a41941:-51fa', 'APPROVED', '591-50', 'KALIBO IDLE LAND PREVIOUS SHARE', '', '', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL);
INSERT INTO `revenueitem` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`) VALUES ('REVITEM3570b102:14736a41941:-51fb', 'APPROVED', '591-50', 'KALIBO IDLE LAND CURRENT SHARE', '', '', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL);
INSERT INTO `revenueitem` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`) VALUES ('REVITEM3570b102:14736a41941:-51fC', 'APPROVED', '591-50', 'KALIBO IDLE LAND ADVANCE SHARE', '', '', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL);
INSERT INTO `revenueitem` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`) VALUES ('REVITEM77954847:147600d44ac:-7d8eA', 'APPROVED', '591', 'PROVINCE IDLE LAND PREVIOUS SHARE', '', '', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL);
INSERT INTO `revenueitem` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`) VALUES ('REVITEM77954847:147600d44ac:-7d8eb', 'APPROVED', '591', 'PROVINCE IDLE LAND CURRENT SHARE', '', '', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL);
INSERT INTO `revenueitem` (`objid`, `state`, `code`, `title`, `description`, `type`, `fund_objid`, `fund_code`, `fund_title`, `defaultvalue`, `valuetype`, `org_objid`, `org_name`) VALUES ('REVITEM77954847:147600d44ac:-7d8eC', 'APPROVED', '591', 'PROVINCE IDLE LAND ADVANCE SHARE', '', '', 'GENERAL', '01', 'GENERAL', '0.00', 'ANY', NULL, NULL);


