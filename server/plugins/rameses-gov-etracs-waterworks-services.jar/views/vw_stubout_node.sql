DROP VIEW IF EXISTS vw_waterworks_stubout_node;
CREATE VIEW vw_waterworks_stubout_node AS 
SELECT 
son.objid,son.indexno,so.objid AS stubout_objid,so.code AS stubout_code,
zon.objid AS zone_objid, zon.code AS zone_code, sec.objid AS sector_objid, sec.code AS sector_code,
so.barangay_objid, so.barangay_name, sked.objid AS schedule_objid, wa.objid AS acctid 
FROM waterworks_stubout_node son 
INNER JOIN waterworks_stubout so ON son.stuboutid = so.objid
INNER JOIN waterworks_zone zon ON so.zoneid = zon.objid
INNER JOIN waterworks_sector sec ON zon.sectorid = sec.objid  
LEFT JOIN waterworks_block_schedule sked ON zon.schedule_objid = sked.objid 
LEFT JOIN waterworks_account wa ON wa.stuboutnodeid = son.objid;