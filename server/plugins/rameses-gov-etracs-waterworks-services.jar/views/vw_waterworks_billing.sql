DROP VIEW IF EXISTS vw_waterworks_billing
;
CREATE VIEW vw_waterworks_billing AS
SELECT 
   wb.*, wa.acctno, wa.acctname, wa.state as acctstate, wa.address_text as addresstext, wa.classificationid, 
   CASE WHEN wc.meterid IS NULL THEN 'UNMETERED' ELSE wm.state END AS meterstate,
   wc.prevreading, wc.reading, wc.volume, wc.amount, wc.amtpaid, wc.rate, wc.hold,
   ((wb.arrears + wb.otherfees + wb.surcharge + wb.interest) - wb.credits) AS subtotal,
   wm.objid AS meterid, wm.objid AS meter_objid, wm.capacity AS meter_capacity, 
   wm.sizeid AS meter_size, bs.fromperiod, bs.toperiod, wbb.readingdate, bs.discdate, bs.duedate, 
   z.objid as zone_objid, z.code as zone_code, sn.indexno, ((bs.year * 12) + bs.month) as periodindexno      
FROM waterworks_billing wb 
   INNER JOIN waterworks_batch_billing wbb ON wbb.objid = wb.batchid 
   INNER JOIN waterworks_consumption wc ON wc.objid = wb.consumptionid 
   INNER JOIN waterworks_account wa ON wa.objid = wb.acctid 
   LEFT JOIN waterworks_meter wm ON wm.objid = wc.meterid 
   LEFT JOIN waterworks_stubout_node sn ON sn.objid = wa.stuboutnodeid 
   LEFT JOIN waterworks_stubout ws on ws.objid = sn.stuboutid 
   LEFT JOIN waterworks_zone z on z.objid = ws.zoneid 
   LEFT JOIN waterworks_billing_schedule bs on bs.objid = wc.scheduleid 
;