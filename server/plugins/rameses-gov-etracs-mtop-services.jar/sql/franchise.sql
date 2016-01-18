[getList]
SELECT f.objid as franchiseid, f.franchiseno, u.make, u.motorno, 
		u.chassisno, u.plateno, m.owner_name, m.state, m.objid as applicationid  
FROM franchise f 
LEFT JOIN mtopunit u ON u.objid=f.mtopunit_objid 
LEFT JOIN mtopapplication m ON m.objid=u.currentappid
WHERE f.unittype_objid like $P{unittypeid} 
	${filter} 
ORDER BY f.franchiseno    

[findFranchiseByFranchiseNo]
select * from franchise where franchiseno=$P{franchiseno} 