[getList]
SELECT a.*, 
	t.title as accttype_title, 
	CASE 
		WHEN a.expirydate < CURDATE() THEN 'EXPIRED' 
		ELSE 'ACTIVE' 
	END AS txnstatus 
FROM ( 
	SELECT objid FROM specialpass_account a WHERE a.acctno LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM specialpass_account a WHERE a.name LIKE $P{searchtext} 
)bt 
	INNER JOIN specialpass_account a ON bt.objid=a.objid 
	INNER JOIN specialpass_type t ON a.accttype_objid=t.objid 
ORDER BY a.name 

[findByPrimaryKey]
SELECT a.*, 
	t.title AS accttype_title, t.indexno, 
 	CASE 
		WHEN a.expirydate < CURDATE() THEN 'EXPIRED' 
		ELSE 'ACTIVE' 
	END AS txnstatus 
FROM specialpass_account a 
	INNER JOIN specialpass_type t ON a.accttype_objid=t.objid 
WHERE a.objid=$P{objid} 
