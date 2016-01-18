[getList]
SELECT vr.*, la.borrower_objid, la.borrower_name, lr.area AS route_area, lr.code AS route_code,
	lr.description AS route_description, CONCAT(lr.description, " - ", lr.area) AS route_name
FROM voidrequest vr
INNER JOIN loanapp la ON vr.loanapp_objid=la.objid
INNER JOIN loan_route lr on vr.routecode=lr.code
ORDER BY vr.dtfiled DESC

[xgetList]
SELECT vr.objid, vr.state, vr.loanapp_appno, vr.collector_objid,
	vr.collector_name, vr.reason, la.borrower_objid, la.borrower_name, 
	lr.area AS route_area, vr.txncode, lr.code AS route_code,
	lr.description AS route_description, CONCAT(lr.description, " - ", lr.area) AS route_name
FROM voidrequest vr
INNER JOIN loanapp la ON vr.loanapp_objid=la.objid
INNER JOIN loan_route lr on vr.routecode=lr.code
ORDER BY vr.dtfiled DESC

[getListByState]
SELECT vr.*, la.borrower_objid, la.borrower_name, lr.area AS route_area, lr.code AS route_code,
	lr.description AS route_description, CONCAT(lr.description, " - ", lr.area) AS route_name
FROM voidrequest vr
INNER JOIN loanapp la ON vr.loanapp_objid=la.objid
INNER JOIN loan_route lr on vr.routecode=lr.code
WHERE vr.state = $P{state}
ORDER BY vr.dtfiled DESC

[xgetListByState]
SELECT vr.objid, vr.state, vr.loanapp_appno, vr.collector_objid,
	vr.collector_name, vr.reason, la.borrower_objid, 
	la.borrower_name, lr.area AS route_area, vr.txncode, lr.code AS route_code,
	lr.description AS route_description, CONCAT(lr.description, " - ", lr.area) AS route_name
FROM voidrequest vr
INNER JOIN loanapp la ON vr.loanapp_objid=la.objid
INNER JOIN loan_route lr on vr.routecode=lr.code
WHERE vr.state = $P{state}
ORDER BY vr.dtfiled DESC

[findFieldVoidRequest]
SELECT v.*, r.code AS route_code, r.description AS route_description, r.area AS route_area,
	c.address_text AS borrower_address, f.amount AS payamount
FROM voidrequest v
INNER JOIN customer c ON v.borrower_objid = c.objid
INNER JOIN loan_route r ON v.routecode = r.code
INNER JOIN fieldcollection_payment f ON f.objid = v.paymentid
WHERE v.objid = $P{objid}

[findOnlineVoidRequest]
SELECT v.*, r.code AS route_code, r.description AS route_description, r.area AS route_area,
	c.address_text AS borrower_address, o.amount AS payamount
FROM voidrequest v
INNER JOIN customer c ON v.borrower_objid = c.objid
INNER JOIN loan_route r ON v.routecode = r.code
INNER JOIN onlinecollection_detail o ON o.objid = v.paymentid
WHERE v.objid = $P{objid}

[findVoidRequestByPaymentid]
SELECT * FROM voidrequest 
WHERE paymentid = $P{paymentid}
	AND state <> "DISAPPROVED"

[changeState]
UPDATE voidrequest SET state = $P{state}
WHERE objid = $P{objid}