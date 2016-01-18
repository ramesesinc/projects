[getList]
SELECT p.* 
FROM loan_payment_schedule p
WHERE p.name LIKE $P{searchtext}