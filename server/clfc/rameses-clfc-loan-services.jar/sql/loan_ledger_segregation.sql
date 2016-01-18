[getSegregationsByRefid]
SELECT * FROM loan_ledger_segregation
WHERE refid = $P{refid}

[findSegregationByRefidAndTypeid]
SELECT s.* FROM loan_ledger_segregation s
WHERE s.refid = $P{refid}
	AND s.typeid = $P{typeid}