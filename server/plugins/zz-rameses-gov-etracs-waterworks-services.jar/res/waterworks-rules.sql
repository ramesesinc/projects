INSERT INTO sys_ruleset 
(NAME, title, packagename, domain, role, permission)
VALUES 
('waterworkscomputation', 'Water Works Computation', 'waterworkscomputation', 'WATERWORKS', 'RULE_AUTHOR', NULL);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('initial', 'waterworkscomputation', 'Initial', 0);


INSERT INTO sys_ruleset 
(NAME, title, packagename, domain, role, permission)
VALUES 
('waterworksbilling', 'Water Works Billing', 'waterworksbilling', 'WATERWORKS', 'RULE_AUTHOR', NULL);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('initial', 'waterworksbilling', 'Initial', 0);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('computefee', 'waterworksbilling', 'Compute Fee', 1);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('discount', 'waterworksbilling', 'Compute Discount', 2);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('surcharge', 'waterworksbilling', 'Compute Surcharge', 3);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('interest', 'waterworksbilling', 'Compute Interest', 4);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('summary', 'waterworksbilling', 'Summary', 5);