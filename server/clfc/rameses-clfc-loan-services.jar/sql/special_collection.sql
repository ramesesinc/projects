[getList]
SELECT sc.* 
FROM special_collection sc
LEFT JOIN followup_collection fc ON sc.objid=fc.objid
WHERE fc.objid IS NULL
	AND sc.state = $P{state}
ORDER BY sc.dtrequested

[getFollowupCollectionList]
SELECT sc.* 
FROM special_collection sc
INNER JOIN followup_collection fc ON sc.objid=fc.objid
ORDER BY sc.dtrequested

[getFollowupCollectionsByBillingid]
SELECT sc.* 
FROM special_collection sc
INNER JOIN followup_collection fc ON sc.objid=fc.objid
WHERE billingid=$P{billingid}

[getLedgersByBillingid]
SELECT ll.*, lr.code AS route_code, lr.area AS route_area,
	lr.description AS route_description
FROM loan_ledger_billing_detail llbd
INNER JOIN loan_ledger ll ON llbd.ledgerid=ll.objid
INNER JOIN loan_route lr ON llbd.route_code=lr.code
INNER JOIN special_collection_loan scl ON llbd.objid=scl.billingdetailid
WHERE llbd.parentid=$P{billingid}
	AND scl.parentid=$P{parentid}

[getForSpecialCollectionList]
SELECT ll.*, lr.code AS route_code, lr.area AS route_area,
	lr.description AS route_description, la.objid AS loanappid,
	la.appno, c.address_text AS homeaddress, la.dtcreated AS loandate,
	la.loanamount
FROM loan_ledger ll
INNER JOIN loanapp la ON ll.appid=la.objid
INNER JOIN customer c ON ll.acctid=c.objid
INNER JOIN loan_route lr ON la.route_code=lr.code
WHERE ll.state = 'OPEN'
	AND ll.acctname LIKE $P{searchtext}
	AND ll.objid NOT IN (SELECT llbd.ledgerid 
				FROM loan_ledger_billing llb
				INNER JOIN loan_ledger_billing_detail llbd ON llb.objid=llbd.parentid
				WHERE llb.collector_objid=$P{collectorid}
					AND llb.billdate=$P{billdate})

[getBillingDetailsBySpecialcollectionid]
SELECT llbd.*
FROM special_collection_loan scl
INNER JOIN loan_ledger_billing_detail llbd ON scl.billingdetailid=llbd.objid
WHERE scl.parentid=$P{specialcollectionid}

[getRoutesByBillingid]
SELECT * FROM special_collection_route
WHERE billingid=$P{billingid}

[getRoutesBySpecialcollectionid]
SELECT scr.*, lr.code AS route_code, lr.area AS route_area,
		lr.description AS route_description
FROM special_collection_route scr
INNER JOIN loan_route lr ON scr.routecode=lr.code
WHERE scr.specialcollectionid=$P{specialcollectionid}

[getFieldCollectionsBySpecialcollectionid]
SELECT lr.code AS route_code, lr.description AS route_description,
	lr.area AS route_area, SUM(fcp.payamount) AS total, fcr.trackerid
FROM special_collection_loan scl
INNER JOIN special_collection sc ON scl.parentid=sc.objid
INNER JOIN field_collection_loan fcl ON scl.billingdetailid=fcl.objid
INNER JOIN field_collection_payment fcp ON fcl.objid=fcp.parentid
INNER JOIN loan_route lr ON scl.routecode=lr.code
INNER JOIN field_collection_route fcr ON fcr.routecode=lr.code
WHERE sc.objid = $P{objid}
	AND IFNULL(sc.posted, 0) = 0
	AND fcr.fieldcollectionid = fcl.parentid
GROUP BY lr.code

[getSpecialCollectionsBySpecialcollectionid]
SELECT lr.code AS route_code, lr.description AS route_description,
	lr.area AS route_area, SUM(fcp.payamount) AS total, fcr.trackerid
FROM (SELECT sc.* FROM special_collection sc
	LEFT JOIN followup_collection fc ON sc.objid=fc.objid
	WHERE sc.objid = $P{objid} AND IFNULL(sc.posted, 0) = 0) sc
INNER JOIN special_collection_loan scl ON sc.objid=scl.parentid
INNER JOIN field_collection_loan fcl ON scl.billingdetailid=fcl.objid
INNER JOIN field_collection_payment fcp ON fcl.objid=fcp.parentid
INNER JOIN loan_route lr ON scl.routecode=lr.code
INNER JOIN field_collection_route fcr ON fcr.routecode=lr.code
WHERE fcr.fieldcollectionid = fcl.parentid
GROUP BY lr.code

[getSpecialCollectionByBillingid]
SELECT sc.*
FROM special_collection sc
LEFT JOIN followup_collection fc ON sc.objid=fc.objid
WHERE sc.billingid=$P{objid}
	AND fc.objid IS NULL
ORDER BY sc.dtrequested

[changeState]
UPDATE special_collection SET state=$P{state}
WHERE objid=$P{objid}

[removeRouteBySpecialcollectionid]
DELETE FROM special_collection_route
WHERE specialcollectionid=$P{specialcollectionid}

[removeLoansByParentid]
DELETE FROM special_collection_loan
WHERE parentid=$P{parentid}

[getLoansByParentid]
SELECT * FROM special_collection_loan
WHERE parentid=$P{parentid}

[getSpecialCollectionsForCancellation]
SELECT sc.*
FROM special_collection sc
INNER JOIN loan_ledger_billing llb ON sc.billingid=llb.objid
WHERE llb.billdate < $P{date}
	AND sc.state IN ('PENDING', 'FOR_DOWNLOAD')

[routeUploaded]
UPDATE special_collection_route SET uploaded=1
WHERE billingid=$P{billingid}
	AND routecode=$P{routecode}

[collectionPosted]
UPDATE special_collection SET posted=1
WHERE objid=$P{objid}

[findUnpostedCollectionByBillingidAndRoutecode]
SELECT sc.objid
FROM special_collection_route scr
INNER JOIN special_collection sc ON scr.specialcollectionid=sc.objid
WHERE scr.uploaded > 0
	AND scr.routecode = $P{routecode}
	AND IFNULL(sc.posted, 0) = 0
	AND scr.billingid = $P{billingid}
