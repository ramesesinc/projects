[getList]
SELECT c.*
FROM (
	SELECT c.objid
	FROM checkout c
	WHERE IFNULL(c.representative1_name, '') LIKE $P{searchtext}
	UNION
	SELECT c.objid 
	FROM checkout c
	WHERE IFNULL(c.representative2_name, '') LIKE $P{searchtext}
) q INNER JOIN checkout c ON q.objid = c.objid
ORDER BY c.txndate DESC, c.dtcreated DESC

[getListByState]
SELECT c.*
FROM (
	SELECT c.objid
	FROM checkout c
	WHERE IFNULL(c.representative1_name, '') LIKE $P{searchtext}
		AND c.txnstate = $P{state}
	UNION
	SELECT c.objid 
	FROM checkout c
	WHERE IFNULL(c.representative2_name, '') LIKE $P{searchtext}
		AND c.txnstate = $P{state}
) q INNER JOIN checkout c ON q.objid = c.objid
ORDER BY c.txndate DESC, c.dtcreated DESC

[getDetails]
SELECT * FROM checkout_detail
WHERE parentid = $P{objid}

[getDetailsWithInfo]
SELECT ds.controlno AS slipno, p.bank_objid AS bank, ds.amount, c.cbsno
FROM checkout_detail d
INNER JOIN depositslip ds ON d.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
LEFT JOIN collection_cb c ON ds.objid = c.collection_objid AND ds.objid = c.group_objid
WHERE d.parentid = $P{objid}


[getReportDataByCheckoutid]
SELECT ds.objid, ds.controlno AS slipno, p.bank_objid AS bank,
	p.passbookno, p.acctno, p.acctname, ds.amount
FROM checkout_detail d
INNER JOIN depositslip ds ON d.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
WHERE d.parentid = $P{checkoutid}
ORDER BY ds.controlno

[getDepositSlipCBSReportDataByDepositSlipid]
SELECT c.*
FROM depositslip_cbs c
WHERE c.parentid = $P{depositslipid}
ORDER BY c.txndate, c.cbsno

[getDepositSlipCheckReportDataByDepositSlipid]
SELECT c.*
FROM depositslip_check c
WHERE c.parentid = $P{depositslipid}
ORDER BY c.checkno

[xgetReportDataByCheckoutid]
SELECT ds.controlno AS slipno, p.bank_objid AS bank, c.cbsno, 
	(SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM checkout_detail d
INNER JOIN depositslip ds ON d.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
INNER JOIN depositslip_cbs dc ON ds.objid = dc.parentid
INNER JOIN collection_cb c ON dc.cbsid = c.objid
LEFT JOIN collection_cb_encash e ON c.objid = e.objid
WHERE d.parentid = $P{checkoutid}
ORDER BY ds.controlno, c.cbsno

[findByTxndate]
SELECT * FROM checkout
WHERE txndate = $P{txndate}

[findDetailByRefid]
SELECT d.* 
FROM checkout_detail d 
WHERE d.refid = $P{refid} 

[changeState]
UPDATE checkout SET txnstate = $P{txnstate}
WHERE objid = $P{objid}