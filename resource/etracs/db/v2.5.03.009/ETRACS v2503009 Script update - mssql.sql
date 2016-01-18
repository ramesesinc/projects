

CREATE TABLE rpu_assessment 
(
  objid nvarchar(50) NOT NULL,
  rpuid nvarchar(50) NOT NULL,
  classification_objid nvarchar(50) NOT NULL,
  actualuse_objid nvarchar(50) NOT NULL,
  areasqm decimal(16,2) NOT NULL,
  areaha decimal(16,6) NOT NULL,
  marketvalue decimal(16,2) NOT NULL,
  assesslevel decimal(16,2) NOT NULL,
  assessedvalue decimal(16,2) NOT NULL,
  PRIMARY KEY  (objid),
  CONSTRAINT FK_rpuassessmetn_rpu FOREIGN KEY (rpuid) REFERENCES rpu (objid)
)
GO

CREATE INDEX  FK_rpuassessmetn_rpu ON rpu_assessment(rpuid)
GO 

DELETE FROM sys_ruleset WHERE name = 'planttreeassessment'
go

INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) VALUES ('planttreeassessment', 'Plant/Tree Assessment Rules', 'planttreeassessment', 'RPT', 'RULE_AUTHOR', NULL);
go

DELETE FROM sys_rulegroup WHERE ruleset = 'planttreeassessment'
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSEDVALUE', 'planttreeassessment', 'After Assessed Value Computation', '45')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSLEVEL', 'planttreeassessment', 'After Assess Level Computation', '36')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-BASEMARKETVALUE', 'planttreeassessment', 'After Base Market Value Computation', '10')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-MARKETVALUE', 'planttreeassessment', 'After Market Value Computation', '30')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSEDVALUE', 'planttreeassessment', 'Assessed Value Computation', '40')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSLEVEL', 'planttreeassessment', 'Assess Level Computation', '35')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BASEMARKETVALUE', 'planttreeassessment', 'Base Market Value Computation', '5')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('INITIAL', 'planttreeassessment', 'Initial Computation', '0')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MARKETVALUE', 'planttreeassessment', 'Market Value Computation', '25')
go

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('SUMMARY', 'planttreeassessment', 'Summary Computation', '100')
go

