[getForResolve]
SELECT e.* 
FROM encashment e 
WHERE e.txndate BETWEEN $P{startdate} AND $P{enddate}
ORDER BY e.txndate DESC