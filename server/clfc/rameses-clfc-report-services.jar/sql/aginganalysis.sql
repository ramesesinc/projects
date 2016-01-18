[getList]
SELECT l.balance, r.description AS route_description, r.code AS route_code,
	DATEDIFF(CURDATE(), DATE_SUB(dtstarted, INTERVAL 1 DAY)) AS days
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loan_route r ON a.route_code = r.code
WHERE l.state = 'OPEN'
ORDER BY days