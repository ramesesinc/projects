[getList]
SELECT * FROM amnesty_extension
WHERE borrower_name LIKE $P{searchtext}
ORDER BY dtcreated DESC

[getListByState]
SELECT * FROM amnesty_extension
WHERE borrower_name LIKE $P{searchtext}
	AND txnstate = $P{state}
ORDER BY dtcreated DESC

[getExtensionsByAmnestyid]
SELECT * FROM amnesty_extension
WHERE amnesty_objid = $P{amnestyid}
	AND borrower_name LIKE $P{searchtext}

[changeState]
UPDATE amnesty_extension SET txnstate = $P{txnstate}
WHERE objid = $P{objid}