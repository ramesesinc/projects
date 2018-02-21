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


