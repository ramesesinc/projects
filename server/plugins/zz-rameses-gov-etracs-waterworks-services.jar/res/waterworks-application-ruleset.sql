INSERT INTO sys_ruleset 
(NAME, title, packagename, domain, role, permission)
VALUES 
('waterworksapplication', 'Water Works Application', 'waterworksapplication', 'WATERWORKS', 'RULE_AUTHOR', NULL);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('initial', 'waterworksapplication', 'Initial', 0);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('computefee', 'waterworksapplication', 'Compute Fee', 1);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('requirement', 'waterworksapplication', 'Requirements', 5);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('summary', 'waterworksapplication', 'Summary', 5);