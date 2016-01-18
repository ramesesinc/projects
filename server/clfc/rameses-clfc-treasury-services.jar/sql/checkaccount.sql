[getList]
SELECT c.*, b.name AS bank_name
FROM checkaccount c
INNER JOIN bank b ON c.bank_objid = b.objid
WHERE c.checkno LIKE $P{searchtext}
ORDER BY c.txndate DESC, c.dtcreated DESC

[getListByState]
SELECT c.*, b.name AS bank_name
FROM checkaccount c
INNER JOIN bank b ON c.bank_objid = b.objid
WHERE c.checkno LIKE $P{searchtext}
	AND c.state = $P{state}
ORDER BY c.txndate DESC, c.dtcreated DESC

[findByRefid]
SELECT * FROM checkaccount
WHERE refid = $P{refid}

[getLookupForDepositSlip]
SELECT c.*, b.name AS bank_name
FROM checkaccount c
INNER JOIN bank b ON c.bank_objid = b.objid
WHERE c.checkno LIKE $P{searchtext}
	AND c.state <> 'REJECTED'
ORDER BY c.txndate DESC, c.dtcreated DESC