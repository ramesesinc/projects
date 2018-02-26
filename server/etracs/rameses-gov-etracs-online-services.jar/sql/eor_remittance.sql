[insertRemittanceFund]
INSERT INTO eor_remittance_fund
(objid,remittanceid,fundid,amount)

SELECT CONCAT( eor.remittanceid, ':', eoi.item_fund_objid ) , 
   eor.remittanceid,
   eoi.item_fund_objid,
   SUM(eoi.amount)
FROM eor_item eoi 
INNER JOIN eor eor ON eoi.parentid = eor.objid 
WHERE eor.remittanceid = $P{remittanceid}
GROUP BY eor.remittanceid, eoi.item_fund_objid
