[getRootNodes]
SELECT a.* FROM sreaccount a WHERE a.parentid IS NULL and a.type='group' ORDER BY a.code 

[getChildNodes]
SELECT a.* FROM sreaccount a WHERE a.parentid=$P{objid} and a.type='group' ORDER BY a.code 

[getList]
SELECT * FROM sreaccount WHERE parentid=$P{objid} ORDER BY code

[getSearch]
SELECT DISTINCT a.* FROM 
( 
  SELECT * FROM sreaccount WHERE code LIKE $P{searchtext}
  UNION 
  SELECT * FROM sreaccount WHERE title LIKE $P{searchtext}
) a
 ORDER BY a.code 

[findInfo]
SELECT a.*, p.code AS parent_code, p.title AS parent_title 
FROM sreaccount a
LEFT JOIN sreaccount p ON a.parentid = p.objid
WHERE a.objid=$P{objid}


[getLookup]
SELECT a.*, s.title as parenttitle FROM 
(SELECT objid,code,title, parentid FROM sreaccount t WHERE t.code LIKE $P{searchtext} AND t.type=$P{type} 
UNION 
SELECT objid,code,title, parentid FROM sreaccount t WHERE t.title  LIKE $P{searchtext} AND t.type=$P{type} ) a
	left join sreaccount s on s.objid = a.parentid 
ORDER BY a.title 

[approve]
UPDATE sreaccount SET state='APPROVED' WHERE objid=$P{objid} 


[changeParent]
UPDATE sreaccount SET parentid=$P{parentid} WHERE objid=$P{objid} 

[getSubaccounts]
SELECT a.* FROM sreaccount a WHERE a.parentid=$P{objid} AND a.type='subaccount' ORDER BY a.code