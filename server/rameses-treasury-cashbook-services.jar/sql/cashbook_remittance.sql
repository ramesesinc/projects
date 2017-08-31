[getRemittanceFunds]
select 
  rem.dtposted as refdate, rem.objid as refid, rem.txnno as refno, 
  remf.fund_objid, remf.fund_title, remf.amount, 
  rem.collector_objid as subacct_objid, 
  rem.collector_name as subacct_name 
from remittance_fund remf 
  inner join remittance rem on remf.remittanceid=rem.objid 
where remf.remittanceid = $P{remittanceid} 
