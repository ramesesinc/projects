[insertRemittanceAF]
INSERT INTO remittance_af 
( objid,remittanceid,controlid,
  qtyreceived,
  beginstartseries,beginendseries,qtybegin,
  issuedstartseries,issuedendseries,qtyissued,
  endingstartseries,endingendseries,qtyending,
  qtycancelled
 )
SELECT 
objid, remittanceid, afcontrolid,
0,
fromseries, endseries, endseries - fromseries + 1,
fromseries, toseries, toseries - fromseries + 1,
CASE WHEN toseries < endseries THEN toseries + 1 ELSE NULL END, 
CASE WHEN toseries < endseries THEN endseries ELSE NULL END, 
CASE WHEN toseries < endseries THEN endseries - toseries + 2 ELSE 0 END, 
0 
FROM cashreceipt_af_summary 
WHERE remittanceid = $P{remittanceid} 

[insertRemittanceFund]
INSERT INTO remittance_fund
( objid,remittanceid,controlno,fund_objid,fund_title,amount,totalcash,totalcheck,totalcr,cashbreakdown )
SELECT 
objid, remittanceid, controlno, fund_objid,fund_title, amount, 0, 0, 0, '[]'
FROM cashreceipt_fund_summary WHERE remittanceid =  $P{remittanceid} 

[updateNonCashPayment]
UPDATE cashreceiptpayment_noncash 
SET remittancefundid = ( 
    SELECT rf.objid  
    FROM remittance_fund rf 
    INNER JOIN cashreceipt cr ON rf.remittanceid = cr.remittanceid 
    WHERE rf.remittanceid = $P{remittanceid} 
    AND cr.objid = cashreceiptpayment_noncash.receiptid 
    AND rf.fund_objid = cashreceiptpayment_noncash.fund_objid  
)
WHERE receiptid IN ( 
  SELECT objid FROM cashreceipt WHERE remittanceid = $P{remittanceid}
)



