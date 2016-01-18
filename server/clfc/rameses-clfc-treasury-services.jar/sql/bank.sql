[getList]
SELECT b.* 
FROM (
	SELECT objid FROM bank b WHERE b.code LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM bank b WHERE b.name LIKE $P{searchtext} 
)bt 
	INNER JOIN bank b ON bt.objid=b.objid 
ORDER BY b.name

[getListByState]
SELECT b.* 
FROM (
	SELECT objid FROM bank b WHERE b.code LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM bank b WHERE b.name LIKE $P{searchtext} 
)bt 
	INNER JOIN bank b ON bt.objid=b.objid
WHERE b.txnstate = $P{state}
ORDER BY b.name 

[getLookupList]
SELECT b.* 
FROM (
	SELECT objid FROM bank b WHERE b.code LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM bank b WHERE b.name LIKE $P{searchtext} 
)bt 
	INNER JOIN bank b ON bt.objid=b.objid 
ORDER BY b.name 

[getLookupListByState]
SELECT b.* 
FROM (
	SELECT objid FROM bank b WHERE b.code LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM bank b WHERE b.name LIKE $P{searchtext} 
)bt 
	INNER JOIN bank b ON bt.objid=b.objid
WHERE b.txnstate = $P{state}
ORDER BY b.name 
