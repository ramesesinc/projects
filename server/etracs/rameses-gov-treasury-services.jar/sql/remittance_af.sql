[updateRemittanceAF]
INSERT INTO remittance_af ( objid, remittanceid )
SELECT ad.objid, $P{remittanceid} AS remittanceid 
FROM af_inventory_detail ad
  INNER JOIN af_inventory ai ON ai.objid=ad.controlid
  LEFT JOIN remittance_af af ON af.objid=ad.objid
WHERE ai.respcenter_objid = $P{collectorid} 
  AND ad.reftype <> 'stocksale'
  AND af.objid IS NULL


[getUnremittedAF]
SELECT b.*, 
  (b.receivedendseries-b.receivedstartseries+1) AS qtyreceived,
  (b.beginendseries-b.beginstartseries+1) AS qtybegin,
  (b.issuedendseries-b.issuedstartseries+1) AS qtyissued,
  (b.endingendseries-b.endingstartseries+1) AS qtyending
FROM (
  SELECT 
    a.formno, a.controlid,
    MIN( a.receivedstartseries ) AS receivedstartseries,
    MAX( a.receivedendseries ) AS receivedendseries,
    MAX( a.beginstartseries ) AS beginstartseries,
    MAX( a.beginendseries ) AS beginendseries,
    MIN( a.issuedstartseries ) AS issuedstartseries, 
    MIN( a.issuedendseries ) AS  issuedendseries,  
    MAX( a.endingstartseries ) AS endingstartseries,
    MAX( a.endingendseries ) AS endingendseries
  FROM (
    SELECT 
      ai.afid AS formno,   
      ad.controlid,
      MIN( ad.receivedstartseries ) AS receivedstartseries,
      MAX( ad.receivedendseries ) AS receivedendseries,
      MAX( ad.beginstartseries ) AS beginstartseries,
      MAX( ad.beginendseries ) AS beginendseries,
      NULL AS issuedstartseries, 
      NULL AS  issuedendseries,  
      MAX( ad.endingstartseries ) AS endingstartseries,
      MAX( ad.endingendseries ) AS endingendseries
    FROM af_inventory_detail ad 
      INNER JOIN af_inventory ai ON ad.controlid=ai.objid
      LEFT JOIN remittance_af r ON r.objid=ad.objid
    WHERE r.remittanceid IS NULL
      AND ai.respcenter_objid = $P{collectorid} 
    GROUP BY ai.afid, ad.controlid
    UNION ALL
    SELECT 
      c.formno, c.controlid,  
      NULL AS receivedstartseries,
      NULL AS receivedendseries,
      NULL AS beginstartseries,
      NULL AS beginendseries,
      MIN(c.series) AS issuedstartseries,
      MAX(c.series) AS issuedendseries,
      MAX(c.series)+1 AS endingstartseries,
      NULL AS endingendseries
    FROM cashreceipt c
      INNER JOIN af_inventory ai on ai.objid = c.controlid 
      LEFT JOIN remittance_cashreceipt r ON r.objid=c.objid
    WHERE r.objid IS NULL 
      AND c.state = 'POSTED'   
      AND c.collector_objid = $P{collectorid} 
    GROUP BY c.formno, c.controlid
  )a 
  GROUP BY a.formno, a.controlid
)b 
ORDER BY b.formno, b.endingendseries


[getRemittanceForBalanceForward]
SELECT 
  ad.controlid,
  MAX(ad.endingstartseries) AS startseries,
  MAX(ad.endingendseries) AS endseries
FROM af_inventory_detail ad 
  INNER JOIN remittance_af rf ON ad.objid=rf.objid 
  INNER JOIN af_inventory ai ON ad.controlid=ai.objid 
  INNER JOIN af ON ai.afid=af.objid 
WHERE rf.remittanceid = $P{remittanceid} 
  AND ai.currentseries <= ai.endseries  
GROUP BY ad.controlid


[getRemittedAF]
select xx.*, 
  (xx.receivedendseries - xx.receivedstartseries)+1 as qtyreceived, 
  (xx.beginendseries - xx.beginstartseries)+1 as qtybegin, 
  (xx.issuedendseries - xx.issuedstartseries)+1 as qtyissued, 
  (xx.endingendseries - xx.endingstartseries)+1 as qtyending  
from ( 
  select xx.*, 
    case 
      when xx.issuedstartseries > 0 then xx.issuedstartseries 
      when xx.beginstartseries > 0 then xx.beginstartseries 
      WHEN xx.receivedstartseries > 0 then xx.receivedstartseries 
      else xx.endingstartseries 
    end as sortseries 
  from ( 
    select 
      xx.controlid, afi.afid as formno, af.formtype, af.serieslength, af.denomination,  
      afi.respcenter_objid as ownerid, afi.respcenter_name as ownername, 
      (select receivedstartseries from af_inventory_detail 
        where controlid=xx.controlid and lineno between xx.minlineno and xx.maxlineno and receivedstartseries > 0 
          order by lineno limit 1) as receivedstartseries, 
      (select receivedendseries from af_inventory_detail 
        where controlid=xx.controlid and lineno between xx.minlineno and xx.maxlineno and receivedendseries > 0 
          order by lineno limit 1) as receivedendseries, 
      (select beginstartseries from af_inventory_detail 
        where controlid=xx.controlid and lineno between xx.minlineno and xx.maxlineno and beginstartseries > 0 
          order by lineno limit 1) as beginstartseries, 
      (select beginendseries from af_inventory_detail 
        where controlid=xx.controlid and lineno between xx.minlineno and xx.maxlineno and beginendseries > 0 
          order by lineno desc limit 1) as beginendseries, 
      (select issuedstartseries from af_inventory_detail 
        where controlid=xx.controlid and lineno between xx.minlineno and xx.maxlineno and issuedstartseries > 0 
          order by lineno limit 1) as issuedstartseries, 
      (select issuedendseries from af_inventory_detail 
        where controlid=xx.controlid and lineno between xx.minlineno and xx.maxlineno and issuedendseries > 0 
          order by lineno desc limit 1) as issuedendseries , 
      (select endingstartseries from af_inventory_detail 
        where controlid=xx.controlid and lineno between xx.minlineno and xx.maxlineno and endingstartseries > 0 
          order by lineno desc limit 1) as endingstartseries , 
      (select endingendseries from af_inventory_detail 
        where controlid=xx.controlid and endingendseries > 0 
          order by lineno limit 1) as endingendseries
    from ( 
      select ad.controlid, min(ad.lineno) as minlineno, max(ad.lineno) as maxlineno 
      from remittance_af r 
        inner join af_inventory_detail ad on r.objid = ad.objid 
      where r.remittanceid = $P{objid} 
      group by ad.controlid 
    )xx 
      inner join af_inventory afi on xx.controlid=afi.objid 
      inner join af on afi.afid=af.objid 
    where 1=1 ${filter} 
  )xx 
)xx 
order by formno, sortseries 
