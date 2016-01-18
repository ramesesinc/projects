[getList]
SELECT e.* 
FROM ( 
	SELECT objid FROM exemptiontype e WHERE e.code LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM exemptiontype e WHERE e.name LIKE $P{searchtext} 
)bt 
	INNER JOIN exemptiontype e ON bt.objid=e.objid 
ORDER BY e.name 

[getLookupList]
SELECT e.* 
FROM ( 
	SELECT objid FROM exemptiontype e WHERE e.code LIKE $P{searchtext} 
	UNION 
	SELECT objid FROM exemptiontype e WHERE e.name LIKE $P{searchtext} 
)bt 
	INNER JOIN exemptiontype e ON bt.objid=e.objid 
ORDER BY e.name 
