[getPaymentsByLoanid]
SELECT p.*
FROM payment p
WHERE loanid = $P{loanid}

[getForMigrationList]
SELECT p.*
FROM payment p
WHERE p.loanid = $P{loanid}
ORDER BY p.ordate 

[removeByLoanidAndOrnoAndOrdate]
DELETE FROM payment 
WHERE loanid = $P{loanid}
	AND orno = $P{orno}
	AND ordate = $P{ordate}