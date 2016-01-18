[getDomainRulesets]
SELECT * FROM sys_ruleset WHERE domain=$P{domain}

[getRuleFacts]
SELECT * FROM sys_rule_fact WHERE domain=$P{domain}

[getRuleFactFields]
SELECT * FROM sys_rule_fact_field WHERE parentid = $P{objid}

[getRuleActionDefs]
SELECT * FROM sys_rule_actiondef WHERE domain=$P{domain}

[getRuleActionDefParams]
SELECT * FROM sys_rule_actiondef_param WHERE parentid = $P{objid}

[getRulegroups]
SELECT * FROM sys_rulegroup WHERE ruleset = $P{ruleset}

[getFactRulesets]
SELECT * FROM sys_ruleset_fact WHERE ruleset=$P{ruleset}

[getActionDefRulesets]
SELECT * FROM sys_ruleset_actiondef WHERE ruleset=$P{ruleset}


