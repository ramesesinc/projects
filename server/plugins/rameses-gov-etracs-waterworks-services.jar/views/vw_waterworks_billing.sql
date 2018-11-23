DROP VIEW IF EXISTS vw_waterworks_billing;
CREATE VIEW vw_waterworks_billing AS
SELECT wb.*, 
   wa.acctno, 
   wa.acctname, 
   wa.classificationid, 
   sn.indexno,
   CASE WHEN wc.meterid IS NULL THEN 'UNMETERED' ELSE wm.state END AS meterstate,
   wc.prevreading,
   wc.reading,
   wc.volume,
   wc.rate,
   wc.amount,
   ((wb.arrears + wb.otherfees + wb.surcharge + wb.interest) - wb.credits) AS subtotal,
   wm.objid AS meter_objid,
   wm.capacity AS meter_capacity,
   wm.sizeid AS meter_size
FROM waterworks_billing wb
INNER JOIN waterworks_consumption wc ON wb.consumptionid=wc.objid
LEFT JOIN waterworks_meter wm ON wc.meterid = wm.objid
INNER JOIN waterworks_account wa ON wb.acctid = wa.objid
LEFT JOIN waterworks_stubout_node sn ON wa.stuboutnodeid = sn.objid
