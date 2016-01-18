[getList]
SELECT c.objid AS borrower_objid, c.name AS borrower_name, c.address_text AS borrower_address,
	la.appno AS loanapp_appno, la.objid AS loanapp_objid, ll.*, la.loanamount AS loanapp_loanamount,
	DATE_ADD(ll.dtstarted, INTERVAL -1 DAY) AS dtreleased
FROM loan_ledger ll
INNER JOIN customer c ON ll.acctid=c.objid
INNER JOIN loanapp la ON ll.appid=la.objid
WHERE ll.state = 'OPEN'
	AND CURDATE() > ll.dtmatured
	AND c.name LIKE $P{searchtext}