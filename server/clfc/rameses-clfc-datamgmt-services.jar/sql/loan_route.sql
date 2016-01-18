[getList]
SELECT * FROM loan_route

[getLookupRoutes]
SELECT r.*
FROM (
	SELECT r.code FROM loan_route r WHERE r.code LIKE $P{searchtext}
	UNION
	SELECT r.code FROM loan_route r WHERE r.description LIKE $P{searchtext}
	UNION
	SELECT r.code FROM loan_route r WHERE r.area LIKE $P{searchtext}
) sq
	INNER JOIN loan_route r ON sq.code = r.code
ORDER BY r.description


[findByCode]
SELECT * FROM loan_route 
WHERE code=$P{code} 
