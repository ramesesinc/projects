[getList]
SELECT * FROM field_collection

[findFieldCollection]
SELECT * FROM field_collection
WHERE collector_objid=$P{collectorid}
	AND state='FOR_POSTING'
	AND billdate=$P{billdate}

[getForPostingRoutesByFieldcollectionid]
SELECT lr.code AS route_code, lr.description AS route_description, lr.area AS route_area,
	SUM(fcp.payamount) AS total, fcr.trackerid
FROM field_collection_route fcr 
INNER JOIN loan_route lr ON fcr.routecode=lr.code
INNER JOIN field_collection_loan fcl ON fcr.routecode=fcl.routecode
INNER JOIN field_collection_payment fcp ON fcl.objid=fcp.parentid
LEFT JOIN void_payment vp ON fcp.objid=vp.paymentid
WHERE fcr.fieldcollectionid=$P{fieldcollectionid}
	AND fcl.parentid=$P{fieldcollectionid}
	AND vp.objid IS NULL
GROUP BY lr.code

[getCashBreakdownByFieldcollectionid]
SELECT * FROM field_collection_cashbreakdown
WHERE parentid=$P{parentid}
ORDER BY denomination DESC

[getCashBreakdownByFieldcollectionidAndRoutecode]
SELECT * FROM field_collection_cashbreakdown
WHERE parentid=$P{parentid}
	AND routecode=$P{routecode}
ORDER BY denomination DESC

[getLoansByParentidAndRoutecode]
SELECT fcl.*, la.loanamount AS loanapp_loanamount 
FROM field_collection_loan fcl
INNER JOIN loanapp la ON fcl.loanapp_objid=la.objid
WHERE fcl.parentid=$P{fieldcollectionid}
	AND fcl.routecode=$P{routecode}

[getFollowupLoansByParentidAndRoutecode]
SELECT fcl.*, la.loanamount AS loanapp_loanamount
FROM field_collection_loan fcl
INNER JOIN loanapp la ON fcl.loanapp_objid=la.objid
INNER JOIN special_collection_loan scl ON scl.billingdetailid=fcl.objid
INNER JOIN followup_collection fc ON scl.parentid=fc.objid
WHERE fcl.parentid=$P{fieldcollectionid}
	AND fcl.routecode=$P{routecode}
	AND scl.parentid=$P{collectionid}

[getSpecialLoansByParentidAndRoutecode]
SELECT fcl.*, la.loanamount AS loanapp_loanamount
FROM field_collection_loan fcl
INNER JOIN loanapp la ON fcl.loanapp_objid=la.objid
INNER JOIN special_collection_loan scl ON scl.billingdetailid=fcl.objid
LEFT JOIN followup_collection fc ON scl.parentid=fc.objid
WHERE fcl.parentid=$P{fieldcollectionid}
	AND fcl.routecode=$P{routecode}
	AND scl.parentid=$P{collectionid}
	AND fc.objid IS NULL

[getPaymentsByParentid]
SELECT * FROM field_collection_payment
WHERE parentid=$P{parentid}

[getPaymentsByFieldcollectionid]
SELECT * FROM field_collection_payment
WHERE fieldcollectionid=$P{fieldcollectionid}

[getRoutesByFieldcollectionid]
SELECT * FROM field_collection_route
WHERE fieldcollectionid=$P{fieldcollectionid}

[getUnpostedRoutes]
SELECT routecode FROM field_collection_route
WHERE fieldcollectionid=$P{objid}
	AND posted=0

[getFieldCollections]
SELECT fcl.*, la.loanamount AS loanapp_loanamount 
FROM field_collection fc
INNER JOIN field_collection_loan fcl ON fc.objid=fcl.parentid
INNER JOIN loanapp la ON fcl.loanapp_objid=la.objid
WHERE fc.billdate = $P{billdate}
	AND fc.collector_objid = $P{collectorid}
	AND fcl.routecode = $P{routecode}
ORDER BY fcl.borrower_name

[getFollowupCollections]
SELECT fcl.*, la.loanamount AS loanapp_loanamount
FROM special_collection_loan scl
INNER JOIN followup_collection flc ON scl.parentid=flc.objid
INNER JOIN field_collection_loan fcl ON scl.billingdetailid=fcl.objid
INNER JOIN field_collection fdc ON fcl.parentid=fdc.objid
INNER JOIN loanapp la ON fcl.loanapp_objid=la.objid
WHERE fdc.billdate=$P{billdate}
	AND fdc.collector_objid=$P{collectorid}
	AND scl.parentid=$P{collectionid}

[getSpecialCollections]
SELECT fcl.*, la.loanamount AS loanapp_loanamount
FROM special_collection_loan scl
INNER JOIN field_collection_loan fcl ON scl.billingdetailid=fcl.objid
INNER JOIN field_collection fdc ON fcl.parentid=fdc.objid
INNER JOIN loanapp la ON fcl.loanapp_objid=la.objid
LEFT JOIN followup_collection flc ON scl.parentid=flc.objid
WHERE fdc.billdate=$P{billdate}
	AND fdc.collector_objid=$P{collectorid}
	AND scl.parentid=$P{collectionid}
	AND flc.objid IS NULL

[getConsolidatedBreakdownByBilldate]
SELECT fcc.*, SUM(qty) AS totalqty, SUM(amount) AS totalamount
FROM field_collection fc
INNER JOIN field_collection_cashbreakdown fcc ON fc.objid=fcc.parentid
WHERE fc.billdate=$P{billdate}
GROUP BY fcc.denomination
ORDER BY fcc.denomination DESC

[getPercollectorBreakdownByBilldate]
SELECT fcc.*
FROM field_collection fc
INNER JOIN field_collection_cashbreakdown fcc ON fc.objid=fcc.parentid
WHERE fc.billdate=$P{billdate}
	AND fc.collector_objid=$P{collectorid}
	AND fcc.routecode=$P{routecode}
GROUP BY fcc.denomination
ORDER BY fcc.denomination DESC

[changeState]
UPDATE field_collection SET state=$P{state}
WHERE objid=$P{objid}

[findPaymentById]
SELECT fcp.*, fc.billdate
FROM field_collection_payment fcp
INNER JOIN field_collection fc ON fcp.fieldcollectionid=fc.objid
WHERE fcp.objid=$P{objid}

[findForPostingRouteByFieldcollectionidAndRoutecode]
SELECT lr.code AS route_code, lr.description AS route_description, lr.area AS route_area,
	SUM(fcp.payamount) AS total, fcr.trackerid
FROM field_collection_route fcr 
INNER JOIN loan_route lr ON fcr.routecode=lr.code
INNER JOIN field_collection_loan fcl ON fcr.routecode=fcl.routecode
INNER JOIN field_collection_payment fcp ON fcl.objid=fcp.parentid
LEFT JOIN void_payment vp ON fcp.objid=vp.paymentid
WHERE fcr.fieldcollectionid=$P{fieldcollectionid}
	AND fcl.parentid = $P{fieldcollectionid}
	AND fcr.routecode = $P{routecode}
	AND fcr.posted = 0
	AND vp.objid IS NULL
GROUP BY lr.code

[findRouteByFieldcollectionidAndRoutecode]
SELECT * FROM field_collection_route
WHERE fieldcollectionid=$P{fieldcollectionid}
	AND routecode=$P{routecode}

[findPendingVoidRequestByFieldcollectionid]
SELECT fcp.objid FROM field_collection_payment fcp
INNER JOIN void_request vr ON fcp.objid=vr.paymentid
WHERE fcp.fieldcollectionid=$P{fieldcollectionid}
	AND vr.state='PENDING'

[findPendingVoidRequestByFieldcollectionidAndRoutecode]
SELECT fcp.objid 
FROM field_collection_payment fcp
INNER JOIN void_request vr ON fcp.objid=vr.paymentid
WHERE fcp.fieldcollectionid=$P{objid}
	AND vr.routecode=$P{routecode}
	AND vr.state='PENDING'

[findPendingVoidRequestFollowupCollection]
SELECT fcp.objid
FROM field_collection_payment fcp
INNER JOIN void_request vr ON fcp.objid=vr.paymentid
INNER JOIN special_collection_loan scl ON fcp.parentid=scl.billingdetailid 
INNER JOIN followup_collection fc ON scl.parentid=fc.objid
WHERE fcp.fieldcollectionid=$P{objid}
	AND scl.parentid=$P{collectionid}
	AND vr.state='PENDING'

[findPendingVoidRequestSpecialCollection]
SELECT fcp.objid
FROM field_collection_payment fcp
INNER JOIN void_request vr ON fcp.objid=vr.paymentid
INNER JOIN special_collection_loan scl ON fcp.parentid=scl.billingdetailid 
LEFT JOIN followup_collection fc ON scl.parentid=fc.objid
WHERE fcp.fieldcollectionid=$P{objid}
	AND scl.parentid=$P{collectionid}
	AND fc.objid IS NULL
	AND vr.state='PENDING'

[findFieldCollectionByRouteAndId]
SELECT fc.* FROM field_collection fc
INNER JOIN field_collection_route fcr ON fc.objid=fcr.fieldcollectionid
WHERE fcr.routecode=$P{routecode}
	AND fcr.fieldcollectionid=$P{fieldcollectionid}

[findFieldCollectionLoanByParentidAndRoutecode]
SELECT * FROM field_collection_loan
WHERE parentid=$P{fieldcollectionid}
	AND routecode=$P{routecode}

[findFieldCollectionLoanByLoanappid]
SELECT * FROM field_collection_loan
WHERE loanapp_objid=$P{loanappid}
	AND parentid=$P{parentid}
	AND routecode=$P{routecode}

[findFieldCollectionRouteByFieldcollectionidAndRoutecode]
SELECT * FROM field_collection_route
WHERE fieldcollectionid=$P{fieldcollectionid}
	AND routecode=$P{routecode}

[updateTotalcountByFieldcollectionidAndRoutecode]
UPDATE field_collection_route SET totalcount=$P{totalcount}
WHERE fieldcollectionid=$P{fieldcollectionid}
	AND routecode=$P{routecode}

[routePosted]
UPDATE field_collection_route SET posted=1
WHERE fieldcollectionid=$P{fieldcollectionid}
	AND routecode=$P{routecode}

[findUnremittedRouteByFieldcollectionid]
SELECT fieldcollectionid FROM field_collection_route
WHERE fieldcollectionid=$P{fieldcollectionid}
	AND totalcount=0

[findUnpostedCollection]
SELECT fc.objid FROM field_collection fc
INNER JOIN field_collection_route fcr ON fc.objid=fcr.fieldcollectionid
WHERE fc.state IN ('DRAFT', 'FOR_POSTING')
	AND fc.objid=$P{fieldcollectionid}
	AND fcr.routecode=$P{routecode}

[removeRouteByFieldcollectionid]
DELETE FROM field_collection_route
WHERE fieldcollectionid=$P{fieldcollectionid}

[removeVoidRequestsByFieldcolletionid]
DELETE FROM void_request
WHERE collectionid=$P{fieldcollectionid}

[removePaymentsByFieldcollectionid]
DELETE FROM field_collection_payment
WHERE fieldcollectionid=$P{fieldcollectionid}

[removeLoanByParentid]
DELETE FROM field_collection_loan
WHERE parentid=$P{parentid}

[findDetailPayment]
SELECT dp.*, 
	l.objid AS loanapp_objid, l.appno AS loanapp_appno, 
	l.borrower_objid, l.borrower_name, b.address AS borrower_address, 
	r.code AS route_code, r.description AS route_description, r.area AS route_area  
FROM field_collection_payment dp 
	INNER JOIN field_collection_loan d ON dp.parentid=d.objid 
	INNER JOIN loanapp l ON d.loanapp_objid=l.objid 
	INNER JOIN loan_route r ON l.route_code=r.code 
	INNER JOIN borrower b ON l.borrower_objid=b.objid  
WHERE 
	dp.objid=$P{objid} 

[findUnpostedRouteByFieldcollectionid]
SELECT routecode FROM field_collection_route
WHERE fieldcollectionid=$P{objid}
	AND posted=0
LIMIT 1
