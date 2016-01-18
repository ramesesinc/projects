[getList]
SELECT b.*
FROM (
	SELECT l.objid
	FROM loan_ledger_proceeds_delete l
	WHERE l.borrower_name LIKE $P{searchtext}
	UNION
	SELECT l.objid
	FROM loan_ledger_proceeds_delete l
	WHERE l.loanapp_appno LIKE $P{searchtext}
) a INNER JOIN loan_ledger_proceeds_delete b ON a.objid = b.objid
ORDER BY b.dtfiled DESC

[getListByState]
SELECT b.*
FROM (
	SELECT l.objid
	FROM loan_ledger_proceeds_delete l
	WHERE l.borrower_name LIKE $P{searchtext}
		AND l.state = $P{state}
	UNION
	SELECT l.objid
	FROM loan_ledger_proceeds_delete l
	WHERE l.loanapp_appno LIKE $P{searchtext}
		AND l.state = $P{state}
) a INNER JOIN loan_ledger_proceeds_delete b ON a.objid = b.objid
ORDER BY b.dtfiled DESC

[changeState] 
UPDATE loan_ledger_proceeds_delete SET state = $P{state}
WHERE objid = $P{objid}