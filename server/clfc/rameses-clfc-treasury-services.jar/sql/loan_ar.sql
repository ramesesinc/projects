[getItems]
SELECT a.* FROM loan_ar_detail a
WHERE parentid = $P{objid}

[getList]
SELECT a.* FROM loan_ar a
WHERE a.refno LIKE $P{searchtext}
ORDER BY a.dtcreated DESC

[getListByState]
SELECT a.* FROM loan_ar a
WHERE a.refno LIKE $P{searchtext}
	AND a.txnstate = $P{txnstate}
ORDER BY a.dtcreated DESC

[getLookupList]
SELECT a.* FROM loan_ar a
WHERE a.refno LIKE $P{searchtext}
ORDER BY a.dtcreated DESC

[getLookupListByState]
SELECT a.* FROM loan_ar a
WHERE a.refno LIKE $P{searchtext}
	AND a.txnstate = $P{txnstate}
ORDER BY a.dtcreated DESC

[changeState]
UPDATE loan_ar SET txnstate = $P{txnstate}
WHERE objid = $P{objid}