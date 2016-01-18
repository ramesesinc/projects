[getList]
SELECT s.* 
FROM specialcollection s
LEFT JOIN followupcollection f ON s.objid = f.objid
WHERE f.objid IS NULL
	AND s.txntype = 'ONLINE'
	AND s.collector_name LIKE $P{searchtext}

[getRequestList]
SELECT s.*,
	CASE WHEN f.objid IS NULL THEN 'Special' ELSE 'Follow-up'
	END AS billingtype
FROM specialcollection s
LEFT JOIN followupcollection f ON s.objid = f.objid 
WHERE s.txntype = 'REQUEST'
	AND s.collector_name LIKE $P{searchtext}
ORDER BY s.txndate DESC

[getRequestListByState]
SELECT s.*,
	CASE WHEN f.objid IS NULL THEN 'Special' ELSE 'Follow-up'
	END AS billingtype
FROM specialcollection s
LEFT JOIN followupcollection f ON s.objid = f.objid 
WHERE s.txntype = 'REQUEST'
	AND s.collector_name LIKE $P{searchtext}
	AND s.state = $P{state}
ORDER BY s.txndate DESC

[getListByState]
SELECT s.* 
FROM specialcollection s
LEFT JOIN followupcollection f ON s.objid = f.objid
WHERE f.objid IS NULL
	AND s.txntype = 'ONLINE'
	AND s.collector_name LIKE $P{searchtext}
	AND s.state = $P{state}
ORDER BY s.txndate DESC

[getListByBilling]
SELECT s.* FROM specialcollection s
LEFT JOIN followupcollection f ON s.objid = f.objid
WHERE f.objid IS NULL
	AND s.txntype = 'ONLINE'
	AND s.collector_name LIKE $P{searchtext}
	AND s.billingid = $P{billingid}

[getLedgers]
SELECT l.*, r.code AS route_code, r.area AS route_area,
	r.description AS route_description, la.appno, 
	la.loanamount, b.address AS homeaddress, sd.objid AS scdetailid,
	l.appid AS loanappid
FROM specialcollection_detail sd
INNER JOIN loan_ledger l ON sd.loanapp_objid = l.appid
INNER JOIN loanapp la ON sd.loanapp_objid = la.objid
INNER JOIN borrower b ON la.borrower_objid = b.objid
INNER JOIN loan_route r ON sd.routecode = r.code
WHERE sd.parentid = $P{objid}

[getPastSpecialCollectionNotDownloaded]
SELECT * FROM specialcollection
WHERE state NOT IN ('CANCELLED', 'DOWNLOADED', 'APPROVED', 'DISAPPROVED')
	AND txndate < $P{date}

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
	AND ll.objid NOT IN (SELECT lbd.ledgerid 
				FROM ledger_billing lb
				INNER JOIN ledger_billing_detail lbd ON lb.objid = lbd.billingid
				WHERE lb.collector_objid = $P{collectorid}
					AND lb.billdate = $P{billdate})

[findBillingDetailByLedgeridAndBilldate]
SELECT d.*, b.collector_name AS collectorname
FROM ledger_billing b
INNER JOIN ledger_billing_item i ON b.objid = i.parentid
INNER JOIN ledger_billing_detail d ON i.objid = d.parentid
WHERE d.ledgerid = $P{ledgerid}
	AND b.billdate = $P{txndate}
	AND i.item_type <> 'route'

[removeDetail]
DELETE FROM specialcollection_detail
WHERE parentid = $P{objid}

[changeState]
UPDATE specialcollection SET state = $P{state}
WHERE objid = $P{objid}