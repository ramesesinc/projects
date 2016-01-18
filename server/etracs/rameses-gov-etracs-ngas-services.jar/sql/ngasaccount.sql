[getRootNodes]
SELECT a.* FROM ngasaccount a  WHERE a.parentid IS NULL and a.type='group' ORDER BY a.code

[getChildNodes]
SELECT a.* FROM ngasaccount a WHERE a.parentid=$P{objid} and a.type='group' ORDER BY a.code

[getList]
SELECT * FROM ngasaccount WHERE parentid=$P{objid} ORDER BY code 

[getListDetails]
SELECT DISTINCT a.* FROM 
( 
  SELECT * FROM ngasaccount WHERE code LIKE $P{searchtext}
  UNION 
  SELECT * FROM ngasaccount WHERE title LIKE $P{searchtext}
) a
WHERE a.type='detail'
ORDER BY a.code 

[getSearch]
SELECT DISTINCT a.* FROM 
( 
  SELECT * FROM ngasaccount WHERE code LIKE $P{searchtext}
  UNION 
  SELECT * FROM ngasaccount WHERE title LIKE $P{searchtext}
) a
 ORDER BY a.code 

[findInfo]
SELECT a.*, p.code AS parent_code, p.title AS parent_title 
FROM ngasaccount a
LEFT JOIN ngasaccount p ON a.parentid = p.objid
WHERE a.objid=$P{objid}

[getLookup]
SELECT a.* FROM 
(
	SELECT objid,code,title,type FROM ngasaccount t WHERE t.code LIKE $P{searchtext} AND type=$P{type} AND parentid LIKE $P{parentid}
	UNION
	SELECT objid,code,title,type FROM ngasaccount t WHERE t.title LIKE $P{searchtext} AND type=$P{type} AND parentid LIKE $P{parentid} 
) a
ORDER BY a.code

[getLookupForMapping]
SELECT a.* FROM 
(
	SELECT objid,code,title,type FROM ngasaccount t WHERE t.code LIKE $P{searchtext} 
	UNION
	SELECT objid,code,title,type FROM ngasaccount t WHERE t.title LIKE $P{searchtext}
) a
WHERE a.type IN ( 'detail', 'subaccount' )
ORDER BY a.code

[approve]
UPDATE ngasaccount SET state='APPROVED' WHERE objid=$P{objid} 

[changeParent]
UPDATE ngasaccount SET parentid=$P{parentid} WHERE objid=$P{objid} 

[getSubAccounts]
SELECT a.* FROM ngasaccount a WHERE a.parentid=$P{objid} AND a.type='subaccount' ORDER BY a.code

[getRevenueItemList]
SELECT r.objid,r.code,r.title,
a.objid AS account_objid, a.title AS account_title, a.code AS account_code
FROM itemaccount r 
LEFT JOIN ngas_revenue_mapping m ON m.revenueitemid=r.objid
LEFT JOIN ngasaccount a ON a.objid=m.acctid
WHERE r.title LIKE $P{searchtext} 
ORDER BY r.code

[getRevenueItemListByCode]
SELECT r.objid,r.code,r.title,
a.objid AS account_objid, a.title AS account_title, a.code AS account_code
FROM itemaccount r 
LEFT JOIN ngas_revenue_mapping m ON m.revenueitemid=r.objid
LEFT JOIN ngasaccount a ON a.objid=m.acctid
WHERE r.code LIKE $P{searchtext} 
ORDER BY r.code

