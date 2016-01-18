[getList]
SELECT * FROM branch WHERE name LIKE $P{searchtext} 
ORDER BY name 

[findBranchByObjid]
SELECT * FROM branch WHERE objid=$P{objid} 

