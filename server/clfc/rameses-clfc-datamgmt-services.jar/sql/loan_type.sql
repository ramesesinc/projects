[getList]
SELECT t.* FROM loan_type t WHERE t.name LIKE $P{searchtext}