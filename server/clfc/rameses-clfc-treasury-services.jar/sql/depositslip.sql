[getList]
SELECT * FROM depositslip
WHERE controlno LIKE $P{searchtext}
ORDER BY txndate DESC

[getListByState]
SELECT * FROM depositslip
WHERE controlno LIKE $P{searchtext}
	AND state = $P{state}
ORDER BY txndate DESC

[getActiveList]
SELECT * FROM depositslip
WHERE controlno LIKE $P{searchtext}
	AND state IN ('APPROVED', 'CLOSED')
ORDER BY txndate DESC

[getListByStateAndReftype]
SELECT * FROM depositslip
WHERE controlno LIKE $P{searchtext}
	AND state = $P{state}
	AND reftype = $P{reftype}
ORDER BY txndate DESC

[getChecks]
SELECT * FROM depositslip_check
WHERE parentid = $P{objid}

[getCbs]
SELECT d.*, c.collector_objid, c.collector_name,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM depositslip_cbs d
INNER JOIN collection_cb c ON d.cbsid = c.objid
LEFT JOIN collection_cb_encash e ON d.cbsid = e.objid
WHERE d.parentid = $P{objid}

[getCheckouts]
SELECT * FROM depositslip_checkout

[getCheckoutByTxndate]
SELECT dc.dtcheckedout, d.controlno AS slipno, p.bank_objid AS bank, d.amount,
	c.cbsno, dc.representative1_objid, dc.representative2_objid
FROM depositslip d
INNER JOIN depositslip_checkout dc ON d.objid = dc.objid
INNER JOIN passbook p ON d.passbook_objid = p.objid
LEFT JOIN collection_cb c ON d.objid = c.collection_objid AND d.objid = c.group_objid
WHERE dc.dtcheckedout BETWEEN $P{starttime} AND $P{endtime}
ORDER BY dc.dtcheckedout

[changeState]
UPDATE depositslip SET state = $P{state}
WHERE objid = $P{objid}

[approve]
UPDATE depositslip SET state = 'APPROVED'
WHERE objid = $P{objid}
	AND state = 'DRAFT'