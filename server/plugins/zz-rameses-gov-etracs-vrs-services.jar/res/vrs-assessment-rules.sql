
INSERT INTO sys_ruleset 
(NAME, title, packagename, domain, role, permission)
VALUES 
('vrsassessment', 'Vehicle Reg. Assessment', 'vrsassessment', 'VRS', 'RULE_AUTHOR', NULL);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('computefee', 'vrsassessment', 'Compute Fee', 1);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('summary', 'vrsassessment', 'Summary', 5);