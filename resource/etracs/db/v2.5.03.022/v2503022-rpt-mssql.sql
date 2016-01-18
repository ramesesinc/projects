
/*============================================================
*
* MULTIPLE EXAMINATION SUPPORT 
*
*============================================================ */
alter table examiner_finding 
	add parent_objid varchar(50) null
go 
alter table examiner_finding 	
	add dtinspected date null
go 
alter table examiner_finding 	
	add recommendations varchar(500) null
go 
alter table examiner_finding 	
	add inspectors varchar(500) null
go 
alter table examiner_finding 	
	add notedby varchar(100) null
go 
alter table examiner_finding 	
	add notedbytitle varchar(50) null
go 
alter table examiner_finding 	
	add photos varchar(255) null
go 
	


update examiner_finding set parent_objid = objid, inspectors= '[]', photos='[]'
go 


/*============================================================
*
* CONSOLIDATION UPDATE
*
*============================================================ */
alter table consolidationaffectedrpu alter column newsuffix int null
go 