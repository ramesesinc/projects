[getRaafData]
SELECT  
    bt.afid, bt.qtybegin, bt.qtyreceived, bt.qtyissued, bt.qtyending, bt.remarks, af.formtype, 
    CASE WHEN af.formtype = 'serial' THEN bt.beginstartseries ELSE NULL END AS beginstartseries,
    CASE WHEN af.formtype = 'serial' THEN bt.beginendseries ELSE NULL END AS beginendseries,
    CASE WHEN af.formtype = 'serial' THEN bt.receivedstartseries ELSE NULL END AS receivedstartseries,
    CASE WHEN af.formtype = 'serial' THEN bt.receivedendseries ELSE NULL END AS receivedendseries,
    CASE WHEN af.formtype = 'serial' THEN bt.issuedstartseries ELSE NULL END AS issuedstartseries,  
    CASE WHEN af.formtype = 'serial' THEN bt.issuedendseries ELSE NULL END AS issuedendseries,  
    CASE WHEN af.formtype = 'serial' THEN bt.endingstartseries ELSE NULL END AS endingstartseries,  
    CASE WHEN af.formtype = 'serial' THEN bt.endingendseries ELSE NULL END AS endingendseries 
FROM ( 
  SELECT t.afid,
    CASE WHEN t.receivedstartseries IS NULL THEN xd.qtyending ELSE NULL END AS qtybegin,
    CASE WHEN t.receivedstartseries IS NULL THEN xd.endingstartseries ELSE NULL END AS beginstartseries,
    CASE WHEN t.receivedendseries IS NULL THEN xd.endingendseries  ELSE NULL END AS beginendseries,
    t.qtyreceived, t.receivedstartseries, t.receivedendseries, 
    t.qtyissued, t.issuedstartseries, t.issuedendseries, 
    CASE WHEN t.issuedendseries = t.endingendseries THEN NULL ELSE t.qtyending END AS qtyending,
    CASE WHEN t.issuedendseries = t.endingendseries THEN NULL ELSE t.endingstartseries END AS endingstartseries,
    CASE WHEN t.issuedendseries = t.endingendseries THEN NULL ELSE t.endingendseries END AS endingendseries,
    CASE WHEN t.issuedendseries = t.endingendseries THEN 'CONSUMED' ELSE NULL END AS remarks 
  FROM (
    SELECT 
      ai.objid, ai.respcenter_name, ai.afid,
            MAX(aid.qtybegin) AS qtybegin,
            MIN(aid.beginstartseries) AS beginstartseries,
            MAX(aid.beginendseries) AS beginendseries,
            SUM(aid.qtyreceived) AS qtyreceived,
            MIN(aid.receivedstartseries) AS receivedstartseries,
            MAX(aid.receivedendseries) AS receivedendseries,
            SUM(aid.qtyissued) AS qtyissued,
            MIN(aid.issuedstartseries) AS issuedstartseries,
            MAX(aid.issuedendseries) AS issuedendseries,
            MIN(aid.qtyending) AS qtyending,
            MAX(aid.endingstartseries) AS endingstartseries,
            MAX(aid.endingendseries) AS endingendseries,
            CASE WHEN MIN([lineno]) = 1 THEN 2 ELSE MIN(aid.[lineno]) END AS minlineno
    FROM af_inventory ai
      INNER JOIN af_inventory_detail aid ON ai.objid = aid.controlid 
    WHERE ai.respcenter_objid = $P{collectorid}  
      AND YEAR(aid.refdate) = $P{year}  
      AND MONTH(aid.refdate) = $P{month}  
    GROUP BY ai.objid, ai.respcenter_name, ai.afid 
  )t LEFT JOIN af_inventory_detail xd ON t.objid = xd.controlid 
  WHERE t.minlineno - 1 = xd.[lineno]
)bt INNER JOIN af ON af.objid = bt.afid 
ORDER BY bt.afid, bt.receivedstartseries, bt.beginstartseries 


[getCollectorFundList]
select distinct 
  rf.fund_objid as fundid, f.code, f.title 
from remittance r 
  inner join remittance_fund rf on rf.remittanceid = r.objid
  inner join fund f on f.objid = rf.fund_objid 
where r.remittancedate between $P{fromdate} and $P{todate}
  and r.collector_objid = $P{collectorid} 


[getCollectorDailyCollectionByFund] 
select 
  t.xdate, ${sqlqry} 
from (
  select 
    day(r.remittancedate) as xdate, ${subqry} 
  from remittance r 
    inner join remittance_fund rf on rf.remittanceid = r.objid
    inner join fund f on f.objid = rf.fund_objid
  where r.remittancedate between $P{fromdate} and $P{todate} 
    and r.collector_objid =  $P{collectorid} 
)t 
group by t.xdate 
order by t.xdate 
