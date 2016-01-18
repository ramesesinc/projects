[getList]
SELECT * FROM loan_ledger_history
WHERE parentid=$P{parentid}
	AND dtcreated <= $P{searchtext}
	AND paytype IS NOT NULL
ORDER BY dtcreated, refno