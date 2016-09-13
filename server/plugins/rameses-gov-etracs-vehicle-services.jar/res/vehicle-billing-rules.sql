INSERT INTO sys_ruleset 
(NAME, title, packagename, domain, role, permission)
VALUES 
('vehicleassessment', 'Vehicle Assessment', 'vehicle', 'VEHICLE', 'RULE_AUTHOR', NULL);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('initial', 'vehicleassessment', 'Initial', 0);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('computefee', 'vehicleassessment', 'Compute Fee', 1);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('summary', 'vehicleassessment', 'Summary', 2);

#####################################################################################################
INSERT INTO sys_ruleset 
(NAME, title, packagename, domain, role, permission)
VALUES 
('vehiclebilling', 'Vehicle Reg. Billing', 'vehicle', 'VEHICLE', 'RULE_AUTHOR', NULL);


INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('discount', 'vehiclebilling', 'Compute Discount', 0);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('surcharge', 'vehiclebilling', 'Compute Surcharge', 1);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('interest', 'vehiclebilling', 'Compute Interest', 2);

INSERT INTO sys_rulegroup 
( NAME, ruleset, title, sortorder )
VALUES 
('summary', 'vehiclebilling', 'Summary', 3);