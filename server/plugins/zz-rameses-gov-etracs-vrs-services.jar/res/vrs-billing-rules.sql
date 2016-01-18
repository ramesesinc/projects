
INSERT INTO sys_ruleset 
(NAME, title, packagename, domain, role, permission)
VALUES 
('vrsbilling', 'Vehicle Reg. Billing', 'vrs', 'VRS', 'RULE_AUTHOR', NULL);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('initial', 'vrsbilling', 'Initial', 0);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('computefee', 'vrsbilling', 'Compute Fee', 1);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('discount', 'vrsbilling', 'Compute Discount', 2);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('surcharge', 'vrsbilling', 'Compute Surcharge', 3);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('interest', 'vrsbilling', 'Compute Interest', 4);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('summary', 'vrsbilling', 'Summary', 5);