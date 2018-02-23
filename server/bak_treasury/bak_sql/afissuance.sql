[getList]
SELECT afc.* 
FROM ( 
   SELECT 
      afc.objid,
      af.objid as afid, 
      afc.owner_objid as respcenterid, 
      afc.owner_name as respcentername, 
      case when af.formtype = 'serial' then afc.startseries else null end as startseries,
      case when af.formtype = 'serial' then afc.currentseries else null end as currentseries,
      case when af.formtype = 'serial' then afc.endseries else null end as endseries,
      afc.stubno, 
      af.formtype,
      (afc.endseries-afc.startseries)+1 as qtyreceived, 
      (afc.currentseries-afc.startseries) AS qtyissued,
      (afc.endseries-afc.currentseries)+1 as balance, 
      afc.txnmode, 
      af.serieslength, 
      af.denomination,
      afc.assignee_name as assigneename
   FROM af_control afc
      INNER JOIN af ON af.objid=afc.afid
   WHERE afc.currentseries <= afc.endseries 
      AND owner_objid  like $P{collectorid}
)afc 
