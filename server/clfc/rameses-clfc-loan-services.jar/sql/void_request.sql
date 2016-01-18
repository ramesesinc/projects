[getList]
SELECT vr.objid, vr.state, vr.loanapp_appno, vr.collector_objid,
	vr.collector_name, vr.reason, la.borrower_objid, 
	la.borrower_name, lr.area AS route_area, vr.txncode
FROM void_request vr
INNER JOIN loanapp la ON vr.loanapp_objid=la.objid
INNER JOIN loan_route lr on vr.routecode=lr.code
WHERE vr.state LIKE $P{state}

[findVoidRequestByPaymentid]
SELECT * FROM void_request 
WHERE paymentid=$P{paymentid}
	AND state <> "DISAPPROVED"

[changeState]
UPDATE void_request SET state=$P{state} WHERE objid=$P{objid}

[changeStateVoided]
UPDATE void_request SET state="VOIDED" WHERE objid=$P{objid}

[changeStateApproved]
UPDATE void_request SET state="APPROVED" WHERE objid=$P{objid}

[findFieldVoidRequestById]
SELECT vr.objid, vr.state, vr.loanapp_appno, la.borrower_name, la.borrower_objid,
	c.address AS borrower_address, lr.description AS route_description, 
	lr.area AS route_area, lr.code AS route_code, vr.collector_objid, vr.collector_name, 
	fcp.payamount, vr.reason, vr.approvedremarks, vr.disapprovedremarks, vr.paymentid,
	vr.txncode
FROM void_request vr
INNER JOIN field_collection_payment fcp ON vr.paymentid=fcp.objid
INNER JOIN loanapp la ON vr.loanapp_objid=la.objid
INNER JOIN customer c ON la.borrower_objid=c.objid
INNER JOIN loan_route lr ON vr.routecode=lr.code
WHERE vr.objid=$P{objid}

[findOnlineVoidRequestById]
SELECT vr.objid, vr.state, vr.loanapp_appno, la.borrower_objid, la.borrower_name, 
	c.address AS borrower_address, lr.description AS route_description, lr.area AS route_area, 
	lr.code AS route_code, vr.collector_objid, vr.collector_name, ocd.amount AS payamount, 
	vr.reason, vr.approvedremarks, vr.disapprovedremarks, vr.paymentid, vr.txncode
FROM void_request vr
INNER JOIN online_collection_detail ocd ON vr.paymentid=ocd.objid
INNER JOIN loanapp la ON vr.loanapp_objid=la.objid
INNER JOIN customer c ON la.borrower_objid=c.objid
INNER JOIN loan_route lr ON vr.routecode=lr.code
WHERE vr.objid=$P{objid}