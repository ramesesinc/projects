[getList]
SELECT c.* FROM branch_report_criteria c
WHERE c.name LIKE $P{searchtext}