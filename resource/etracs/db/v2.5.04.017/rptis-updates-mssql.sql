INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) 
VALUES ('machassessment', 'Machinery Assessment Rules', 'machassessment', 'RPT', 'RULE_AUTHOR', NULL)
go 

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('INITIAL', 'machassessment', 'Initial Computation', '0')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BASEMARKETVALUE', 'machassessment', 'Base Market Value Computation', '5')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('DEPRECIATION', 'machassessment', 'Depreciation Computation', '11')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-DEPRECIATION', 'machassessment', 'After Depreciation Computation', '12')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-BASEMARKETVALUE', 'machassessment', 'After Base Market Value Computation', '10')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MARKETVALUE', 'machassessment', 'Market Value Computation', '25')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-MARKETVALUE', 'machassessment', 'After Market Value Computation', '30')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSLEVEL', 'machassessment', 'Assess Level Computation', '35')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSLEVEL', 'machassessment', 'After Assess Level Computation', '36')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSEDVALUE', 'machassessment', 'Assessed Value Computation', '40')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSEDVALUE', 'machassessment', 'After Assessed Value Computation', '45')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('SUMMARY', 'machassessment', 'Summary Computation', '100')
go 




-- MISC ASSESSMENT RULE SUPPORT ---
INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) VALUES ('miscassessment', 'Miscellaneous Assessment Rules', 'miscassessment', 'RPT', 'RULE_AUTHOR', NULL)
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('INITIAL', 'miscassessment', 'Initial Computation', '0')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BASEMARKETVALUE', 'miscassessment', 'Base Market Value Computation', '5')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-BASEMARKETVALUE', 'miscassessment', 'After Base Market Value Computation', '10')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('DEPRECIATION', 'miscassessment', 'Depreciation Computation', '15')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFER-DEPRECIATION', 'miscassessment', 'After Depreciation Computation', '16')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MARKETVALUE', 'miscassessment', 'Market Value Computation', '25')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-MARKETVALUE', 'miscassessment', 'After Market Value Computation', '30')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSLEVEL', 'miscassessment', 'Assess Level Computation', '35')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSLEVEL', 'miscassessment', 'After Assess Level Computation', '36')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSEDVALUE', 'miscassessment', 'Assessed Value Computation', '40')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSEDVALUE', 'miscassessment', 'After Assessed Value Computation', '45')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('SUMMARY', 'miscassessment', 'Summary Computation', '100')
go 




alter table faas_txntype add checkbalance int
go 

UPDATE faas_txntype SET [objid]='CC', [checkbalance]=1 WHERE ([objid]='CC') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='CD', [checkbalance]=1 WHERE ([objid]='CD') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='CE', [checkbalance]=1 WHERE ([objid]='CE') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='CS', [checkbalance]=1 WHERE ([objid]='CS') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='CS/SD', [checkbalance]=1 WHERE ([objid]='CS/SD') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='CT', [checkbalance]=1 WHERE ([objid]='CT') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='CTD', [checkbalance]=1 WHERE ([objid]='CTD') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='DC', [checkbalance]=0 WHERE ([objid]='DC') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='GR', [checkbalance]=0 WHERE ([objid]='GR') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='MC', [checkbalance]=0 WHERE ([objid]='MC') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='MCS', [checkbalance]=0 WHERE ([objid]='MCS') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='ND', [checkbalance]=0 WHERE ([objid]='ND') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='RE', [checkbalance]=1 WHERE ([objid]='RE') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='RS', [checkbalance]=0 WHERE ([objid]='RS') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='RV', [checkbalance]=1 WHERE ([objid]='RV') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='SD', [checkbalance]=1 WHERE ([objid]='SD') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='TR', [checkbalance]=1 WHERE ([objid]='TR') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='TRC', [checkbalance]=1 WHERE ([objid]='TRC') AND ([checkbalance] IS NULL)
go 
UPDATE faas_txntype SET [objid]='TRE', [checkbalance]=1 WHERE ([objid]='TRE') AND ([checkbalance] IS NULL)
go 
