[getList]
SELECT * FROM loan_ledger_segregationtype
ORDER BY dtcreated DESC

[getListByState]
SELECT * FROM loan_ledger_segregationtype
WHERE state = $P{state}
ORDER BY dtcreated DESC

[getLookupList]
SELECT * FROM loan_ledger_segregationtype

[getLookupListByState]
SELECT * FROM loan_ledger_segregationtype
WHERE state = $P{state}

[getListByStateAndCategory]
SELECT s.* FROM loan_ledger_segregationtype s
WHERE s.state = $P{state}
	AND s.category = $P{category}

[getRootNodes]
SELECT s.name AS caption, 'loanstate' AS filestate, s.objid AS rootid
FROM loan_ledger_segregationtype s
WHERE s.state = 'ACTIVATED'
	AND s.category = 'LOANSTATE'

[getChildNodes]
SELECT s.name AS caption, 'paymentstate' AS filestate, s.objid AS typeid
FROM loan_ledger_segregationtype s
WHERE s.state = 'ACTIVATED'
	AND s.category = 'PAYMENTSTATE'