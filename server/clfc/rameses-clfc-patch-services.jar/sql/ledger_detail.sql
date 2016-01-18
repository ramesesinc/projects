[getList]
SELECT ll.objid, ll.paymentmethod, ll.overpaymentamount, l.*, lr.description AS route_description, lr.area AS route_area,
	DATE_SUB(ll.dtstarted, INTERVAL 1 DAY) AS dtreleased, ll.dtstarted
FROM loanapp l
INNER JOIN loan_ledger ll ON l.objid = ll.appid
INNER JOIN loan_route lr ON l.route_code = lr.code
WHERE l.borrower_name LIKE $P{searchtext}