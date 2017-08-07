[getList]
SELECT ia.* FROM (
	SELECT objid FROM itemaccount WHERE (title LIKE $P{searchtext} or description LIKE $P{searchtext})  
	UNION 
	SELECT objid FROM itemaccount WHERE code LIKE $P{searchtext}
) r, itemaccount ia 
WHERE ia.objid=r.objid 
ORDER BY ia.code

[changeState-approved]
UPDATE itemaccount SET state='APPROVED' WHERE objid=$P{objid} AND state='DRAFT'

[approve]
UPDATE itemaccount SET state='APPROVED' WHERE objid=$P{objid} AND state='DRAFT'

[updateCode]
UPDATE itemaccount SET code=$P{code}, title=$P{title} WHERE objid=$P{objid} 

[getLookup]
SELECT r.* FROM itemaccount r 
WHERE  (r.title LIKE $P{title} OR r.code LIKE $P{code} OR r.description LIKE $P{title}) 
	AND r.state = 'APPROVED' 
	${filter} 
ORDER BY r.title 

[getLookupByCollectionType]
SELECT 
	r.objid, r.state, r.code, r.title, r.description, 
	r.type, ca.valuetype, ca.defaultvalue, 
	r.fund_objid, f.code as fund_code, r.fund_title, 
	r.org_objid, r.org_name, r.parentid 
FROM collectiontype_account ca 
	INNER JOIN itemaccount r on ca.account_objid = r.objid 
	INNER JOIN fund f on r.fund_objid = f.objid 
WHERE ca.collectiontypeid=$P{collectiontypeid}   
	AND (r.title LIKE $P{title} OR r.code LIKE $P{code} r.description LIKE $P{title}) 
	AND r.state = 'APPROVED' 
	${filter} 
ORDER BY r.title 

[findHasCollectionTypeAccount]
SELECT COUNT(*) AS count FROM  collectiontype_account  WHERE collectiontypeid=$P{collectiontypeid}

[findAccount]
SELECT r.objid, r.code, r.title, r.fund_objid, r.fund_code, r.fund_title 
FROM itemaccount r WHERE objid=$P{objid}

[getTags]
SELECT * FROM itemaccount_tag WHERE acctid=$P{acctid}

[removeTags]
DELETE FROM itemaccount_tag WHERE acctid=$P{acctid}


[getAccountsByTag]
SELECT a.* FROM itemaccount a
	INNER JOIN itemaccount_tag t ON a.objid=t.acctid
WHERE t.tag = $P{tag} 
	AND a.state = 'APPROVED' 
