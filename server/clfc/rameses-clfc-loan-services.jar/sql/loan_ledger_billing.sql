[getList]
SELECT llb.*, lls.parentid AS parentid, lls.subcollector_objid, lls.subcollector_name
FROM loan_ledger_billing llb
LEFT JOIN loan_ledger_subbilling lls ON llb.objid=lls.objid
LEFT JOIN loan_ledger_specialcollection llsc ON llb.objid=llsc.billingid
WHERE (llb.createdby LIKE $P{searchtext} OR llb.collector_name LIKE $P{searchtext})
	AND llb.state=$P{state}
	AND llsc.objid IS NULL
ORDER BY llb.billdate

[getDefaultList]
SELECT * FROM loan_ledger_billing
WHERE createdby LIKE $P{searchtext}
ORDER BY billdate

[findBillingPayment]
SELECT objid FROM field_collection_payment
WHERE fieldcollectionid = $P{objid}
LIMIT 1

[findBillingByCollectoridAndBilldate]
SELECT * FROM loan_ledger_billing
WHERE collector_objid=$P{collectorid}
	AND billdate=$P{billdate}

[findUnuploadRouteByBillingid]
SELECT * FROM loan_ledger_billing_route
WHERE billingid=$P{billingid}
	AND downloaded=1
	AND uploaded=0

[getRoutesByBillingid]
SELECT lr.*, llbr.downloaded 
FROM loan_ledger_billing_route llbr
INNER JOIN loan_route lr ON llbr.routecode=lr.code
WHERE llbr.billingid=$P{billingid}

[getUnuploadedRoutesByBillingid]
SELECT lr.* FROM loan_ledger_billing_route llbr
INNER JOIN loan_route lr ON llbr.routecode=lr.code
WHERE llbr.billingid=$P{billingid}
	AND llbr.uploaded=0

[removeRouteByBillingid]
DELETE FROM loan_ledger_billing_route WHERE billingid=$P{billingid}

[removeBillingDetailByParentid]
DELETE FROM loan_ledger_billing_detail WHERE parentid=$P{parentid}

[getBillingDetailByParentid]
SELECT * FROM loan_ledger_billing_detail WHERE parentid=$P{parentid}

[getBillingByCollectorid]
SELECT llb.* 
FROM loan_ledger_billing llb
LEFT JOIN loan_ledger_subbilling lls ON llb.objid=lls.objid
LEFT JOIN loan_ledger_specialcollection llsc ON llb.objid=llsc.billingid
WHERE llb.collector_objid=$P{collectorid}
	AND lls.subcollector_objid IS NULL
	AND llb.billdate=$P{billdate}
	AND llsc.objid IS NULL
	AND llb.state='FOR_DOWNLOAD'

[getBillingBySubCollectorid]
SELECT llb.* 
FROM loan_ledger_billing llb
LEFT JOIN loan_ledger_subbilling lls ON llb.objid=lls.objid
LEFT JOIN loan_ledger_specialcollection llsc ON llb.objid=llsc.billingid
WHERE lls.subcollector_objid=$P{subcollectorid}
	AND llb.billdate=$P{billdate}
	AND llsc.objid IS NULL
	AND llb.state='FOR_DOWNLOAD'

[getRoutesByCollectorid]
SELECT lr.* 
FROM loan_ledger_billing lb
 INNER JOIN loan_ledger_billing_route lbr ON lb.objid=lbr.billingid
 INNER JOIN loan_route lr ON lbr.routecode=lr.code
WHERE lb.collector_objid=$P{collectorid}

[findBillingByBilldate]
SELECT * FROM loan_ledger_billing 
WHERE billdate=$P{billdate}
	AND collector_objid=$P{collectorid}

[getBillingDetailByRoutecode]
SELECT llbd.*, lbn.nexttoid AS nextto
FROM loan_ledger_billing_detail llbd
LEFT JOIN loanapp_borrower_nextto lbn ON llbd.acctid=lbn.borrowerid
WHERE parentid=$P{billingid} 
	AND route_code=$P{route_code}

[findBillingLock]
SELECT * FROM loan_ledger_billing_lock WHERE billingid=$P{billingid} AND routecode=$P{routecode}

[removeBillingLockByBillingid]
DELETE FROM loan_ledger_billing_lock WHERE billingid=$P{billingid}

[findAvgOverpaymentAmount]
SELECT MAX(sq.count) AS max, sq.groupbaseamount FROM (
	SELECT COUNT(ll.groupbaseamount) AS count, ll.groupbaseamount 
	FROM loan_ledger_detail ll
	WHERE ll.parentid=$P{parentid}
	GROUP BY ll.groupbaseamount
) sq
GROUP BY sq.groupbaseamount
LIMIT 1

[changeState]
UPDATE loan_ledger_billing SET state=$P{state}
WHERE objid=$P{objid}

[changeStateCompleted]
UPDATE loan_ledger_billing SET state="COMPLETED"
WHERE objid=$P{objid}

[changeStateUploaded]
UPDATE loan_ledger_billing SET state="UPLOADED"
WHERE objid=$P{objid}

[changeStateVoided]
UPDATE loan_ledger_billing SET state="VOIDED"
WHERE objid=$P{objid}

[changeStateDraft]
UPDATE loan_ledger_billing SET state="DRAFT"
WHERE objid=$P{objid}

[getPastBillingsNotCollected]
SELECT llb.*
FROM loan_ledger_billing llb
LEFT JOIN field_collection fc ON llb.objid=fc.objid
WHERE llb.state IN ('DRAFT', 'FOR_DOWNLOAD')
	AND llb.billdate < $P{date}
	AND fc.objid IS NULL

[getPastBillingsCollected]
SELECT * FROM loan_ledger_billing
WHERE totalunposted > 0
	AND state IN ('DRAFT', 'FOR_DOWNLOAD')
	AND billdate < $P{date}

[getPastBillingsNotRemitted]
SELECT * FROM loan_ledger_billing
WHERE state IN ('DRAFT', 'FOR_DOWNLOAD')
	AND billdate < $P{date}
	AND objid IN (SELECT DISTINCT(fieldcollectionid) FROM field_collection_route
			WHERE fieldcollectionid=loan_ledger_billing.objid AND totalcount=0)

[getBillingForSubCollection]
SELECT * FROM loan_ledger_billing
WHERE collector_name LIKE $P{searchtext}
	AND state='FOR_DOWNLOAD'
ORDER BY billdate

[findRouteByBillingidAndRoutecode]
SELECT * FROM loan_ledger_billing_route
WHERE billingid=$P{billingid}
	AND routecode=$P{routecode}

[routeDownloaded]
UPDATE loan_ledger_billing_route SET downloaded=1
WHERE billingid=$P{billingid}
	AND routecode=$P{routecode}

[routeUploaded]
UPDATE loan_ledger_billing_route SET uploaded=1
WHERE billingid=$P{billingid}
	AND routecode=$P{routecode}