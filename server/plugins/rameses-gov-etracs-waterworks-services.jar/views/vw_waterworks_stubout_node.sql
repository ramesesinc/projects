DROP VIEW IF EXISTS vw_waterworks_stubout_node
;
CREATE VIEW vw_waterworks_stubout_node AS 
SELECT 
	son.objid, son.indexno, so.objid AS stubout_objid, so.code AS stubout_code,
	z.objid AS zone_objid, z.code AS zone_code, 
	sec.objid AS sector_objid, sec.code AS sector_code,
	so.barangay_objid, so.barangay_name, 
	sked.objid AS schedule_objid, 
	wa.objid AS acctid, wa.acctno, wa.acctname, 
	wa.objid as currentacctid, so.objid as stuboutid  
FROM waterworks_stubout_node son 
	INNER JOIN waterworks_stubout so ON so.objid = son.stuboutid 
	INNER JOIN waterworks_zone z ON z.objid = so.zoneid 
	INNER JOIN waterworks_sector sec ON sec.objid = z.sectorid  
	LEFT JOIN waterworks_block_schedule sked ON sked.objid = z.schedule_objid  
	LEFT JOIN waterworks_account wa ON wa.stuboutnodeid = son.objid 
;