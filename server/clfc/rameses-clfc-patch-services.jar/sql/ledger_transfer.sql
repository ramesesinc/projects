[getList]
SELECT * FROM loan_route
WHERE code <> $P{routecode}
ORDER BY description