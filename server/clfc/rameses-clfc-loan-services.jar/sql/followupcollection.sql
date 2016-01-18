[getList]
SELECT s.* FROM followupcollection f
INNER JOIN specialcollection s ON f.objid = s.objid
WHERE s.collector_name LIKE $P{searchtext}

[getListByState]
SELECT s.* FROM specialcollection s
INNER JOIN followupcollection f ON s.objid = f.objid
WHERE s.collector_name LIKE $P{searchtext}
	AND s.state = $P{state}
ORDER BY s.txndate DESC

[getListByBilling]
SELECT s.* FROM specialcollection s
INNER JOIN followupcollection f ON s.objid = f.objid
WHERE s.collector_name LIKE $P{searchtext}
	AND s.billingid = $P{billingid}