[getList]
SELECT c.* FROM branch_report_type c
WHERE c.name LIKE $P{searchtext}