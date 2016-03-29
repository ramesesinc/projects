INSERT INTO sys_ruleset 
(name, title, packagename, domain, role, permission)
VALUES 
('revenue', 'Revenue Rules', 'revenue', 
	'TREASURY', 'RULE_AUTHOR', NULL);


INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('initial', 'revenue', 'Initial', 0 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('preinfo', 'revenue', 'Pre Info', 1 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('info', 'revenue', 'Info', 2 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('postinfo', 'revenue', 'Post Info', 3 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('precomputefee', 'revenue', 'Pre Compute Fee', 4 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('computefee', 'revenue', 'Compute Fee', 5 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('postcomputefee', 'revenue', 'Post Compute Fee', 6 );

INSERT INTO sys_rulegroup 
(name, ruleset, title, sortorder)
VALUES
('summary', 'revenue', 'Summary', 7 );






