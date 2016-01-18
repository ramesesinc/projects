INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BEFORE-ADJUSTMENT', 'landassessment', 'Before Adjustment Computation', '13')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ADJUSTMENT', 'landassessment', 'Adjustment Computation', '14')
go 
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ADJUSTMENT', 'landassessment', 'After Adjustment Computation', '15')
go 


alter table planttreedetail alter column areacovered decimal(16,4)
go 