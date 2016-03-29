INSERT INTO sys_ruleset 
(name, title, packagename, domain, role, permission)
VALUES 
('paymentorder', 'Payment Order', 'paymentorder', 
	'TREASURY', 'RULE_AUTHOR', NULL);


INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('initial', 'paymentorder', 'Initial', 0 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('preinfo', 'paymentorder', 'Pre Info', 1 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('info', 'paymentorder', 'Info', 2 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('postinfo', 'paymentorder', 'Post Info', 3 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('precomputefee', 'paymentorder', 'Pre Compute Fee', 4 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('computefee', 'paymentorder', 'Compute Fee', 5 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('postcomputefee', 'paymentorder', 'Post Compute Fee', 6 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('summary', 'paymentorder', 'Summary', 7 );






