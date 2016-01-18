[findOnlineCollectionRemittance]
SELECT cr.*
FROM collection_remittance cr
INNER JOIN collection_remittance_pending crp ON cr.objid = crp.objid
WHERE group_objid = $P{groupid}
	AND group_type = 'online'

[findCollectionRemittance]
SELECT r.*, IFNULL((SELECT SUM(amount) FROM collection_remittance_detail WHERE parentid = r.objid), 0) AS totalcollection,
	IFNULL((SELECT SUM(amount) FROM collection_remittance_other WHERE parentid = r.objid), 0) AS others
FROM collection_remittance r
WHERE r.collection_objid = $P{collectionid}
	AND r.group_objid = $P{groupid}
	AND r.group_type = $P{grouptype}

[findRemittanceDetailByRefid]
SELECT * 
FROM collection_remittance_detail
WHERE refid = $P{refid}

[findUnpostedRemittance]
SELECT r.* 
FROM collection_remittance r
WHERE r.state IN ('DRAFT', 'FOR_POSTING')

[findUnpostedRemittanceByDate]
SELECT r.* 
FROM collection_remittance r
WHERE r.txndate = $P{date}
	AND r.state IN ('DRAFT', 'FOR_POSTING')

[findRemittanceOtherByRefid]
SELECT o.* FROM collection_remittance_other o
WHERE o.refid = $P{refid}

[findRemittanceByCollectionid]
SELECT r.* 
FROM collection_remittance r
WHERE r.collection_objid = $P{collectionid}

[getOnlineCollectionPayments]
SELECT ocd.*
FROM collection_remittance_detail crd
INNER JOIN online_collection_detail ocd ON crd.refid = ocd.objid
WHERE crd.parentid = $P{objid}

[getOthers]
SELECT o.* FROM collection_remittance_other o
WHERE o.parentid = $P{objid}

[getRemittancesWithInfoByCollectorAndDate]
SELECT cr.*, r.*
FROM collection_remittance cr
LEFT JOIN loan_route r ON cr.group_objid = r.code
LEFT JOIN onlinecollection o ON cr.group_objid = o.objid
LEFT JOIN specialcollection s ON cr.group_objid = s.objid 
WHERE cr.txndate = $P{billdate}
	AND cr.collector_objid = $P{collectorid}

[getPostedCollectionsByTxndate]
SELECT d.borrower_name AS borrower, d.loanapp_appno AS appno, d.refno, d.dtpaid,
	d.amount, d.payoption, c.collector_name AS collector, d.paidby, d.bank_objid AS bank
FROM collection_remittance c
	INNER JOIN collection_remittance_detail d ON c.objid = d.parentid
WHERE c.txndate = $P{txndate}
	AND c.state = 'POSTED'
ORDER BY d.borrower_name

[getPostedOnlineCollectionsByTxndate]
SELECT d.borrower_name AS borrower, d.loanapp_appno AS appno, d.refno, d.dtpaid,
	d.amount, d.payoption, c.collector_name AS collector, d.paidby, d.bank_objid AS bank
FROM collection_remittance c
	INNER JOIN collection_remittance_detail d ON c.objid = d.parentid
WHERE c.txndate = $P{txndate}
	AND c.state = 'POSTED'
	AND c.collection_type = 'ONLINE'
ORDER BY d.borrower_name

[getPostedCollectionsByStartdateAndEnddate]
SELECT c.collector_name AS collector, c.txndate, c.cbsno, c.totalamount AS amount,
	(SELECT MIN(control_seriesno) FROM loanpayment_detail WHERE parentid = p.objid) AS startseries,
	(SELECT MAX(control_seriesno) FROM loanpayment_detail WHERE parentid = p.objid) AS endseries,
	CASE 
		WHEN c.group_type = 'route' THEN r.description
		WHEN c.group_type = 'online' THEN "DIRECT"
		ELSE UCASE(c.group_type)
	END AS route, 
	(SELECT COUNT(DISTINCT loanapp_objid) FROM collection_remittance_detail WHERE parentid = c.objid) AS noofaccts
FROM collection_remittance c
INNER JOIN loanpayment p ON c.objid = p.objid
LEFT JOIN loan_route r ON c.group_objid = r.code
WHERE c.state = 'POSTED'
	AND c.txndate BETWEEN $P{startdate} AND $P{enddate}
ORDER BY route, c.txndate

[getListByStartdateAndEnddate]
SELECT r.*, CONCAT(lr.description, " - ", lr.area) AS description
FROM collection_remittance r
LEFT JOIN loan_route lr ON r.group_objid = lr.code
WHERE r.txndate BETWEEN $P{startdate} AND $P{enddate}

[getReportData]
SELECT r.*, 
	CASE 
		WHEN r.group_type = 'route' THEN CONCAT(lr.description, " - ", lr.area)
		WHEN r.group_type = 'online' THEN 'DIRECT'
		ELSE r.group_type
 	END AS description
FROM collection_remittance r
INNER JOIN collection_remittance_detail d ON r.objid = d.parentid
INNER JOIN loan_ledger l ON d.loanapp_objid = l.appid
LEFT JOIN loan_route lr ON r.group_objid = lr.code
${filter}

[getReportDataDetail]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower
FROM (
	SELECT d.borrower_name AS bname, d.loanapp_appno AS appno, d.refno, d.dtpaid,
		d.amount, d.payoption, c.collector_name AS collector, d.paidby, d.bank_objid AS bank,
		(SELECT objid FROM loanapp_borrower WHERE parentid = l.appid AND `type` = 'JOINT' LIMIT 1) AS jointid
	FROM collection_remittance c
	INNER JOIN collection_remittance_detail d ON c.objid = d.parentid
	INNER JOIN loan_ledger l ON d.loanapp_objid = l.appid
	${filter}
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xgetReportDetail]
SELECT d.borrower_name AS borrower, d.loanapp_appno AS appno, d.refno, d.dtpaid,
	d.amount, d.payoption, c.collector_name AS collector, d.paidby, d.bank_objid AS bank
FROM collection_remittance c
INNER JOIN collection_remittance_detail d ON c.objid = d.parentid
INNER JOIN loan_ledger l ON d.loanapp_objid = l.appid
${filter}

[changeState]
UPDATE collection_remittance SET state = $P{state}
WHERE objid = $P{objid}

[getDetails]
SELECT c.* FROM collection_remittance_detail c
WHERE c.parentid = $P{objid}

[removeDetails]
DELETE FROM collection_remittance_detail
WHERE parentid = $P{objid}