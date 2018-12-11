DROP VIEW IF EXISTS vw_waterworks_account
;
CREATE VIEW vw_waterworks_account AS 
SELECT   
  wa.objid, 
  wa.acctno, 
  wa.acctname, 
  wa.address_text,
  wa.classificationid, 
  wa.classificationid AS classification, 
  z.sectorid, 
  z.objid as zoneid, 
  z.code as zone_code,  
  sn.indexno, 
  ws.code AS stuboutnode_stubout_code,
  m.objid AS meterid, 
  m.serialno  AS meter_serialno,
  m.brand  AS meter_branch, 
  m.capacity  AS meter_capacity,
  ms.title AS meter_size_title,
  m.state  AS meter_state,
  m.lastreading  AS meter_lastreading,
  m.lastreadingdate  AS meter_lastreadingdate,
  CASE WHEN m.objid IS NULL THEN 'UNMETERED' WHEN m.state = 'ACTIVE' THEN 'METERED' ELSE m.state END AS meterstatus
FROM waterworks_account wa 
   LEFT JOIN waterworks_meter m ON wa.meterid = m.objid 
   LEFT JOIN waterworks_metersize ms ON m.sizeid = ms.objid 
   LEFT JOIN waterworks_stubout_node sn ON wa.stuboutnodeid = sn.objid 
   LEFT JOIN waterworks_stubout ws ON sn.stuboutid = ws.objid 
   LEFT JOIN waterworks_zone z on z.objid = ws.zoneid 
;