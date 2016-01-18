[getList]
SELECT c.*,
	CASE 
		WHEN c.txntype = 'vault' THEN 'To Vault'
		WHEN c.txntype = 'bank' THEN 'To Bank'
	END AS depositto
FROM (
	SELECT c.objid
	FROM deposit c
	WHERE IFNULL(c.representative1_name, '') LIKE $P{searchtext}
	UNION
	SELECT c.objid 
	FROM deposit c
	WHERE IFNULL(c.representative2_name, '') LIKE $P{searchtext}
) q INNER JOIN deposit c ON q.objid = c.objid
ORDER BY c.txndate DESC, c.dtcreated DESC

[getListByState]
SELECT c.*,
	CASE 
		WHEN c.txntype = 'vault' THEN 'To Vault'
		WHEN c.txntype = 'bank' THEN 'To Bank'
	END AS depositto
FROM (
	SELECT c.objid
	FROM deposit c
	WHERE IFNULL(c.representative1_name, '') LIKE $P{searchtext}
		AND c.txnstate = $P{state}
	UNION
	SELECT c.objid 
	FROM deposit c
	WHERE IFNULL(c.representative2_name, '') LIKE $P{searchtext}
		AND c.txnstate = $P{state}
) q INNER JOIN deposit c ON q.objid = c.objid
ORDER BY c.txndate DESC, c.dtcreated DESC

[xgetList]
SELECT * FROM deposit
ORDER BY txndate DESC

[getDetails]
SELECT * FROM deposit_detail
WHERE parentid = $P{objid}

[getDetailsWithInfo]
SELECT ds.controlno AS slipno, p.bank_objid AS bank, ds.amount, c.cbsno
FROM deposit_detail d
INNER JOIN depositslip ds ON d.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
LEFT JOIN collection_cb c ON ds.objid = c.collection_objid AND ds.objid = c.group_objid
WHERE d.parentid = $P{objid}

[getListByTxntype]
SELECT * FROM deposit
WHERE txntype = $P{txntype}
ORDER BY txndate DESC

[getListByTxntypeAndStartdateAndEnddateWithInfo]
SELECT d.txndate, ds.controlno AS slipno, p.bank_objid AS bank, ds.amount, c.cbsno,
	d.author_objid, d.author_name, d.representative1_objid, d.representative1_name
FROM deposit d
INNER JOIN deposit_detail dd ON d.objid = dd.parentid
INNER JOIN depositslip ds ON dd.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
LEFT JOIN collection_cb c ON ds.objid = c.collection_objid AND ds.objid = c.group_objid
WHERE txntype = $P{txntype}
	AND d.txndate BETWEEN $P{startdate} AND $P{enddate}

[getByTxndateAndTxntype]
SELECT d.dtcreated, ds.controlno AS slipno, p.bank_objid AS bank, ds.amount,
	c.cbsno, d.author_objid, d.author_name,
	d.representative1_objid, d.representative1_name
FROM deposit d
INNER JOIN deposit_detail dd ON d.objid = dd.parentid
INNER JOIN depositslip ds ON dd.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
LEFT JOIN collection_cb c ON ds.objid = c.collection_objid AND ds.objid = c.group_objid
WHERE d.txntype = $P{txntype}
	AND d.txndate = $P{txndate}
ORDER BY d.dtcreated

[getReportDataByDepositid]
SELECT ds.objid, ds.controlno AS slipno, p.bank_objid AS bank,
	p.passbookno, p.acctno, p.acctname, ds.amount
FROM deposit_detail d
INNER JOIN depositslip ds ON d.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
WHERE d.parentid = $P{depositid}
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

[xgetReportDataByDepositid]
SELECT ds.controlno AS slipno, p.bank_objid AS bank, c.cbsno, 
	(SELECT SUM(amount) FROM collection_cb_detail WHERE parentid = c.objid) AS amount,
	CASE WHEN e.objid IS NULL THEN 0 ELSE 1 END AS isencashed
FROM deposit_detail d
INNER JOIN depositslip ds ON d.refid = ds.objid
INNER JOIN passbook p ON ds.passbook_objid = p.objid
INNER JOIN depositslip_cbs dc ON ds.objid = dc.parentid
INNER JOIN collection_cb c ON dc.cbsid = c.objid
LEFT JOIN collection_cb_encash e ON c.objid = e.objid
WHERE d.parentid = $P{depositid}
ORDER BY ds.controlno, c.cbsno

[changeState]
UPDATE deposit SET txnstate = $P{txnstate}
WHERE objid = $P{objid}