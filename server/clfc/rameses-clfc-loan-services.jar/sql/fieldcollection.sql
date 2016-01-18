[findRouteFieldcollection]
SELECT fi.*, r.code AS route_code, r.area AS route_area,
	r.description AS route_description
FROM fieldcollection_item fi
INNER JOIN loan_route r ON fi.item_objid = r.code
WHERE fi.objid = $P{objid}

[findRemittedRouteFieldcollection]
SELECT fi.*, r.code AS route_code, r.area AS route_area,
	r.description AS route_description
FROM fieldcollection_item fi
INNER JOIN loan_route r ON fi.item_objid = r.code
WHERE fi.objid = $P{objid}
	AND fi.state = 'REMITTED'

[findItemByParentidAndItemid]
SELECT i.* 
FROM fieldcollection_item i
WHERE i.parentid = $P{parentid}
	AND i.item_objid = $P{itemid}

[findDetailPayment]
SELECT dp.*, 
	l.objid AS loanapp_objid, l.appno AS loanapp_appno, 
	l.borrower_objid, l.borrower_name, b.address AS borrower_address, 
	r.code AS route_code, r.description AS route_description, r.area AS route_area,
	d.remarks  
FROM fieldcollection_payment dp 
	INNER JOIN fieldcollection_loan d ON dp.parentid=d.objid 
	INNER JOIN loanapp l ON d.loanapp_objid=l.objid 
	INNER JOIN loan_route r ON l.route_code=r.code 
	INNER JOIN borrower b ON l.borrower_objid=b.objid  
WHERE 
	dp.objid=$P{objid} 

[findDetail]
SELECT d.objid, d.remarks, d.loanapp_objid, d.loanapp_appno, 
	l.borrower_objid, l.borrower_name, b.address AS borrower_address, 
	r.code AS route_code, r.description AS route_description, r.area AS route_area  
FROM fieldcollection_loan d
	INNER JOIN loanapp l ON d.loanapp_objid=l.objid 
	INNER JOIN loan_route r ON l.route_code=r.code 
	INNER JOIN borrower b ON l.borrower_objid=b.objid  
WHERE 
	d.objid=$P{objid} 

[findItemByFieldcollectionidAndItemid]
SELECT *
FROM fieldcollection_item
WHERE parentid = $P{objid}
	AND item_objid = $P{itemid}
LIMIT 1

[getFieldcollectionItems]
SELECT * FROM fieldcollection_item
WHERE parentid = $P{objid}

[getFieldcollectionLoans]
SELECT * FROM fieldcollection_loan
WHERE parentid = $P{objid}

[getFieldcollectionLoansWithInfo]
SELECT f.*, l.loanamount AS loanapp_loanamount, r.code AS route_code,
	r.area AS route_area, r.description AS route_description,
	ll.objid AS ledgerid
FROM (
	SELECT l.objid
	FROM fieldcollection_loan l
	INNER JOIN fieldcollection_payment p ON l.objid = p.parentid
	WHERE p.version = 1
		AND l.borrower_name LIKE $P{searchtext}
		AND l.parentid = $P{objid}
	UNION
	SELECT l.objid
	FROM fieldcollection_loan l
	LEFT JOIN fieldcollection_payment p ON l.objid = p.parentid
	WHERE p.objid IS NULL
		AND l.borrower_name LIKE $P{searchtext}
		AND l.parentid = $P{objid}
) qry INNER JOIN fieldcollection_loan f ON qry.objid = f.objid
INNER JOIN loanapp l ON f.loanapp_objid = l.objid
INNER JOIN loan_ledger ll ON l.objid = ll.appid
INNER JOIN loan_route r ON f.routecode = r.code
GROUP BY f.objid

[getPaymentsWithLoanInfo]
SELECT f.*, a.loanamount AS loanapp_loanamount, r.code AS route_code, r.area AS route_area, 
	r.description AS route_description, l.objid AS ledgerid, q.paymentid
FROM (
	SELECT l.objid AS loanid, p.objid AS paymentid
	FROM fieldcollection_loan l
	INNER JOIN fieldcollection_payment p ON l.objid = p.parentid
	WHERE p.version = 1
		AND l.borrower_name LIKE $P{searchtext}
		AND l.parentid = $P{objid}
	UNION
	SELECT l.objid AS loanid, NULL AS paymentid
	FROM fieldcollection_loan l
	LEFT JOIN fieldcollection_payment p ON l.objid = p.parentid
	WHERE p.objid IS NULL
		AND l.borrower_name LIKE $P{searchtext}
		AND l.parentid = $P{objid}
) q INNER JOIN fieldcollection_loan f ON q.loanid = f.objid
INNER JOIN loanapp a ON f.loanapp_objid = a.objid
INNER JOIN loan_ledger l ON a.objid = l.appid
INNER JOIN loan_route r ON f.routecode = r.code

[getFieldcollectionPayments]
SELECT p.*, d.objid AS remittancedetailid 
FROM fieldcollection_payment p
LEFT JOIN collection_remittance_detail d ON p.objid = d.refid
WHERE p.parentid = $P{objid}
ORDER BY p.dtpaid

[getDetails]
SELECT fl.*
FROM fieldcollection_loan fl
WHERE fl.parentid = $P{objid}

[changeState]
UPDATE fieldcollection_item SET state = $P{state}
WHERE objid = $P{objid}