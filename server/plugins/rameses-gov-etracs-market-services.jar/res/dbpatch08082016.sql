INSERT INTO sys_ruleset 
(NAME,title,packagename,domain,role)
VALUES
('marketbilling', 'Market Billing', 'marketbilling', 'MARKET', 'RULE_AUTHOR');

INSERT INTO sys_rulegroup 
(NAME, ruleset, title, sortorder)
VALUES 
('initial', 'marketbilling', 'Initial', 0),
('surcharge', 'marketbilling', 'Surcharge', 1),
('interest', 'marketbilling', 'Interest', 2),
('summary', 'marketbilling', 'Summary', 3);