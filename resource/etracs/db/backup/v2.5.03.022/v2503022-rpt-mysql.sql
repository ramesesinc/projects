
/*============================================================
*
* MULTIPLE EXAMINATION SUPPORT 
*
*============================================================ */
alter table examiner_finding 
	add column parent_objid varchar(50) null,
	add column dtinspected date null,
	add column recommendations varchar(500) null,
	add column inspectors varchar(500) null,
	add column notedby varchar(100) null,
	add column notedbytitle varchar(50) null,
	add column photos varchar(255) null;
	


update examiner_finding set parent_objid = objid, inspectors= '[]', photos='[]';


/*============================================================
*
* CONSOLIDATION UPDATE
*
*============================================================ */
alter table consolidationaffectedrpu modify column newsuffix int null; 