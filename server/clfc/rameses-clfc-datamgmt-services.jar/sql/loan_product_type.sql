[getList]
SELECT p.* 
FROM loan_product_type p
WHERE p.name LIKE $P{searchtext}

[findByName]
SELECT * FROM loan_product_type WHERE name=$P{name} 
