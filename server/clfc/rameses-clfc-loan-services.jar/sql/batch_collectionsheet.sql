[findByRoutecode]
SELECT * FROM batch_collectionsheet
WHERE route_code=$P{route_code} AND state='DRAFT'

[getPaymentsByParentId]
SELECT bcd.objid, l.objid AS appid, l.borrower_name AS borrowername, 
	bcdp.refno, bcdp.paytype, bcdp.payamount, l.appno 
FROM loanapp l
INNER JOIN batch_collectionsheet_detail bcd ON l.objid=bcd.appid
INNER JOIN batch_collectionsheet_detail_payment bcdp ON bcd.objid=bcdp.parentid
WHERE bcd.parentid=$P{parentid}

[getUnpostedCollectionSheets]
SELECT bcd.objid, l.objid AS appid, l.borrower_name AS borrower_name, 
	l.borrower_objid AS borrower_objid, l.appno, bcd.remarks
FROM loanapp l
INNER JOIN batch_collectionsheet_detail bcd ON l.objid=bcd.appid
WHERE bcd.parentid=$P{parentid}

[findDetailByAppIdAndParentId]
SELECT * FROM batch_collectionsheet_detail
WHERE appid=$P{appid} AND parentid=$P{parentid}

[findTotalDetailByParentId]
SELECT COUNT(*) AS total FROM batch_collectionsheet_detail
WHERE parentid=$P{parentid}

[findUnpostedPayment]
SELECT * FROM batch_collectionsheet bc
INNER JOIN batch_collectionsheet_detail bcd ON bc.objid=bcd.parentid
INNER JOIN batch_collectionsheet_detail_payment bcdp ON bcd.objid=bcdp.parentid
WHERE bc.state='DRAFT' AND bc.route_code=$P{route_code}
	AND (SELECT objid FROM loan_ledger_subbilling WHERE objid=bc.objid) IS NULL
LIMIT 1

[changeStateApproved]
UPDATE batch_collectionsheet SET state='APPROVED'
WHERE objid=$P{objid}

[getCashBreakdown]
SELECT * FROM batch_collectionsheet_cashbreakdown
WHERE parentid=$P{parentid}
ORDER BY denomination DESC

[getPaymentsByDetailid]
SELECT * FROM batch_collectionsheet_detail_payment
WHERE parentid=$P{parentid}

[getPaymentsWithPostedByByDetailid]
SELECT bcdp.*, su.name AS postedby
FROM batch_collectionsheet_detail_payment bcdp
INNER JOIN sys_user su ON bcdp.collectorid=su.objid
WHERE bcdp.parentid=$P{parentid}

[getPaymentsAndVoidrequestByDetailid]
SELECT bcdp.*,
	CASE WHEN vr.objid IS NULL THEN FALSE ELSE TRUE END AS isvoided
FROM batch_collectionsheet_detail_payment bcdp
LEFT JOIN void_request vr ON bcdp.objid=vr.paymentid
WHERE bcdp.parentid=$P{parentid}

[getNotesByDetailid]
SELECT * FROM batch_collectionsheet_detail_note
WHERE parentid=$P{parentid}

[getNotesWithPostedByByDetailid]
SELECT bcdn.*, su.name AS postedby
FROM batch_collectionsheet_detail_note bcdn
INNER JOIN sys_user su ON bcdn.collectorid=su.objid
WHERE parentid=$P{parentid}

[findDetailById]
SELECT * FROM batch_collectionsheet_detail
where objid=$P{objid}

[getDetailsByParentid]
SELECT * FROM batch_collectionsheet_detail
WHERE parentid=$P{parentid}

[findDetailPayment]
SELECT dp.*, 
	l.objid AS loanapp_objid, l.appno AS loanapp_appno, 
	l.borrower_objid, l.borrower_name, b.address AS borrower_address, 
	r.code AS route_code, r.description AS route_description, r.area AS route_area  
FROM batch_collectionsheet_detail_payment dp 
	INNER JOIN batch_collectionsheet_detail d ON dp.parentid=d.objid 
	INNER JOIN loanapp l ON d.appid=l.objid 
	INNER JOIN loan_route r ON l.route_code=r.code 
	INNER JOIN borrower b ON l.borrower_objid=b.objid  
WHERE 
	dp.objid=$P{objid} 
 