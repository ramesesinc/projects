[getList]
SELECT b.* 
FROM (
	SELECT objid FROM passbook b WHERE b.bank_objid LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM passbook b WHERE b.passbookno LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM passbook b WHERE b.acctno LIKE $P{searchtext} 
)bt 
	INNER JOIN passbook b ON bt.objid=b.objid 
ORDER BY b.dtcreated 

[getLookupList]
SELECT b.* 
FROM (
	SELECT objid FROM passbook b WHERE b.bank_objid LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM passbook b WHERE b.passbookno LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM passbook b WHERE b.acctno LIKE $P{searchtext} 
)bt 
	INNER JOIN passbook b ON bt.objid=b.objid 
ORDER BY b.bank_objid, b.passbookno

[getLookupListByState]
SELECT b.* 
FROM (
	SELECT objid FROM passbook b WHERE b.bank_objid LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM passbook b WHERE b.passbookno LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM passbook b WHERE b.acctno LIKE $P{searchtext} 
)bt 
	INNER JOIN passbook b ON bt.objid=b.objid 
WHERE b.txnstate = $P{state}
ORDER BY b.bank_objid, b.passbookno
