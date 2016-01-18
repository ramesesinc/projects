[getList]
SELECT e.*, ec.checkno, ec.bank_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid 
WHERE ec.checkno LIKE $P{searchtext}
ORDER BY e.txndate DESC, e.dtcreated DESC

[xgetList]
SELECT e.*, ec.checkno, ec.bank_objid, p.passbookno AS passbook_passbookno
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid 
LEFT JOIN passbook p ON ec.passbook_objid = p.objid
WHERE ec.checkno LIKE $P{searchtext}
ORDER BY e.txndate DESC, e.dtcreated DESC

[getListByState]
SELECT e.*, ec.checkno, ec.bank_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
WHERE ec.checkno LIKE $P{searchtext}
	AND e.txnstate = $P{state}
ORDER BY e.txndate DESC, e.dtcreated DESC

[xgetListByState]
SELECT e.*, ec.checkno, ec.bank_objid, p.passbookno AS passbook_passbookno
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid 
LEFT JOIN passbook p ON ec.passbook_objid = p.objid
WHERE ec.checkno LIKE $P{searchtext}
	AND e.txnstate = $P{state}
ORDER BY e.txndate DESC, e.dtcreated DESC

[getListByTxndate]
SELECT e.*, ec.checkno AS check_checkno, ec.txndate AS check_txndate,
	ec.amount AS check_amount, ec.bank_objid AS check_bank_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
WHERE e.txndate = $P{txndate}

[xgetListByTxndate]
SELECT e.*, ec.checkno AS check_checkno, ec.txndate AS check_txndate,
	ec.amount AS check_amount, ec.bank_objid AS check_bank_objid,
	p.passbookno AS check_passbook_passbookno, p.objid AS check_passbook_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
LEFT JOIN passbook p ON ec.passbook_objid = p.objid
WHERE e.txndate = $P{txndate}

[getListByTxndateAndTxnstate]
SELECT e.*
FROM encashment e
WHERE e.txndate = $P{txndate}
	AND e.txnstate = $P{txnstate}

[xgetListByTxndateAndTxnstate]
SELECT e.*, ec.checkno AS check_checkno, ec.txndate AS check_txndate,
	ec.amount AS check_amount, ec.bank_objid AS check_bank_objid,
	p.passbookno AS check_passbook_passbookno, p.objid AS check_passbook_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
LEFT JOIN passbook p ON ec.passbook_objid = p.objid
WHERE e.txndate = $P{txndate}
	AND e.txnstate = $P{txnstate}

[getLookupList]
SELECT e.*, ec.checkno AS check_checkno, ec.txndate AS check_txndate,
	ec.amount AS check_amount, ec.bank_objid AS check_bank_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
WHERE ec.checkno LIKE $P{searchtext}
ORDER BY e.txndate DESC, e.dtcreated DESC

[xgetLookupList]
SELECT e.*, ec.checkno AS check_checkno, ec.txndate AS check_txndate,
	ec.amount AS check_amount, ec.bank_objid AS check_bank_objid,
	p.passbookno AS check_passbook_passbookno, p.objid AS check_passbook_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
LEFT JOIN passbook p ON ec.passbook_objid = p.objid
WHERE ec.checkno LIKE $P{searchtext}
ORDER BY e.txndate DESC, e.dtcreated DESC

[getLookupListByState]
SELECT e.*, ec.checkno AS check_checkno, ec.txndate AS check_txndate,
	ec.amount AS check_amount, ec.bank_objid AS check_bank_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
WHERE ec.checkno LIKE $P{searchtext}
	AND e.txnstate = $P{state}
ORDER BY e.txndate DESC, e.dtcreated DESC

[xgetLookupListByState]
SELECT e.*, ec.checkno AS check_checkno, ec.txndate AS check_txndate,
	ec.amount AS check_amount, ec.bank_objid AS check_bank_objid,
	p.passbookno AS check_passbook_passbookno, p.objid AS check_passbook_objid
FROM encashment e
INNER JOIN encashment_check ec ON e.objid = ec.parentid
LEFT JOIN passbook p ON ec.passbook_objid = p.objid
WHERE ec.checkno LIKE $P{searchtext}
	AND e.txnstate = $P{state}
ORDER BY e.txndate DESC, e.dtcreated DESC

[getChange]
SELECT c.* FROM encashment_change c
WHERE c.parentid = $P{objid}
ORDER BY c.denomination DESC

[getCBDetails]
SELECT * FROM encashment_cb_detail
WHERE parentid = $P{objid}
ORDER BY denomination DESC

[getCBDetailsByEncashmentid]
SELECT * FROM encashment_cb_detail
WHERE encashmentid = $P{encashmentid}
ORDER BY denomination DESC

[getCbsReferences]
SELECT * FROM encashment_cbs_reference
WHERE parentid = $P{objid}
ORDER BY denomination DESC

[getCbsDetails]
SELECT * FROM encashment_cbs_detail
WHERE parentid = $P{objid}
ORDER BY denomination DESC

[getCbsChange]
SELECT * FROM encashment_cbs_change
WHERE parentid = $P{objid}
ORDER BY denomination DESC

[getCbs]
SELECT * FROM encashment_cbs
WHERE parentid = $P{objid}
ORDER BY cbsno

[getEncashmentCbsByRefid]
SELECT c.*
FROM encashment_cbs c
INNER JOIN encashment e ON c.parentid = e.objid
WHERE c.refid = $P{refid}
	AND e.txnstate <> 'DISAPPROVED'

[xgetEncashmentsByCashBreakdown]
SELECT ec.objid, c.checkno, c.txndate, c.amount, c.bank_objid, 
		c.passbook_objid, p.passbookno AS passbook_passbookno, 
		p.acctname AS passbook_acctname	
FROM encashment_cbs ec
INNER JOIN encashment_check c ON ec.parentid = c.parentid
INNER JOIN passbook p ON c.passbook_objid = p.objid
WHERE ec.refid = $P{cbsid}

[findEncashmentCbsByFilter]
SELECT c.*
FROM encashment_cbs c
INNER JOIN encashment e ON c.parentid = e.objid
${filter}

[findOverage]
SELECT o.* FROM encashment_overage o
WHERE o.parentid = $P{objid}

[findCheck]
SELECT * FROM encashment_check
WHERE parentid = $P{objid}

[findCashBreakdownByParentid]
SELECT * FROM encashment_cb
WHERE parentid = $P{objid}

[changeState]
UPDATE encashment SET txnstate = $P{txnstate}
WHERE objid = $P{objid}

[removeCbsReferenceByParentid]
DELETE FROM encashment_cbs_reference
WHERE parentid = $P{objid}
