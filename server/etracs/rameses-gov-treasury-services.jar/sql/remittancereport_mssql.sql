[getRCDCollectionTypes]
select 
    xx.formtypeindexno, xx.controlid, 
    xx.formno, xx.formtype, xx.stubno, 
    min(xx.receiptno) as fromseries, 
    max(xx.receiptno) as toseries, 
    sum(xx.amount) as amount 
from ( 
  select 
    cr.controlid, cr.series, cr.receiptno, 
    cr.formno, af.formtype, cr.stub as stubno, xx.voided, 
    (case when xx.voided > 0 then 0.0 else cr.amount end) as amount, 
    (case when af.formtype='serial' then 1 else 2 end) as formtypeindexno 
  from ( 
    select remc.*, 
      (select count(*) from cashreceipt_void where receiptid=remc.objid) as voided 
    from remittance_cashreceipt remc  
    where remittanceid = $P{remittanceid} 
  )xx  
    inner join cashreceipt cr on xx.objid=cr.objid 
    inner join af on (cr.formno=af.objid) 
)xx 
group by xx.formtypeindexno, xx.controlid, xx.formno, xx.formtype, xx.stubno
order by xx.formtypeindexno, xx.formno, min(xx.receiptno)  


[getRCDCollectionTypesByFund]
select 
    xx.formtypeindexno, xx.controlid, 
    xx.formno, xx.formtype, xx.stubno, 
    min(xx.receiptno) as fromseries, 
    max(xx.receiptno) as toseries, 
    sum(xx.amount) as amount 
from ( 
  select 
    cr.controlid, cr.series, cr.receiptno, 
    cr.formno, af.formtype, cr.stub as stubno, xx.voided, 
    (case when xx.voided > 0 then 0.0 else cri.amount end) as amount, 
    (case when af.formtype='serial' then 1 else 2 end) as formtypeindexno 
  from ( 
    select remc.*, 
      (select count(*) from cashreceipt_void where receiptid=remc.objid) as voided 
    from remittance_cashreceipt remc  
    where remittanceid = $P{remittanceid} 
  )xx  
    inner join cashreceipt cr on xx.objid=cr.objid 
    inner join cashreceiptitem cri on cr.objid=cri.receiptid 
    inner join itemaccount ia on cri.item_objid=ia.objid 
    inner join af on cr.formno=af.objid 
  where ia.fund_objid = $P{fundid} 
)xx 
group by xx.formtypeindexno, xx.controlid, xx.formno, xx.formtype, xx.stubno
order by xx.formtypeindexno, xx.formno, min(xx.receiptno)  


[getRCDCollectionSummaries]
select particulars, sum(amount) as amount 
from (  
  select  
    ('AF#'+ a.objid + ':' + ct.title + '-' + ia.fund_title) as particulars, 
    (case when xx.voided > 0 then 0.0 else cri.amount end) as amount 
  from ( 
    select rem.*, 
      (select count(*) from cashreceipt_void where receiptid=rem.objid) as voided 
    from remittance_cashreceipt rem 
    where rem.remittanceid = $P{remittanceid} 
  )xx 
    inner join cashreceipt cr on xx.objid=cr.objid 
    inner join cashreceiptitem cri on cr.objid=cri.receiptid 
    inner join itemaccount ia on cri.item_objid=ia.objid 
    inner join collectiontype ct on cr.collectiontype_objid=ct.objid    
    inner join af a on cr.formno=a.objid 
  where ia.fund_objid like $P{fundid} 
)xx 
group by particulars 


[getRCDOtherPayment]
select pc.particulars, pc.amount, pc.reftype 
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptpayment_noncash pc on cr.objid = pc.receiptid 
where xx.voided=0 


[getNonCashPayments]
select cc.* from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join remittance r on xx.remittanceid = r.objid 
  inner join cashreceiptpayment_noncash cc ON xx.objid = cc.receiptid 
where xx.voided = 0 
order by cc.bank, cc.refno    


[getReceiptsByRemittanceCollectionType]
select 
  cr.formno as afid, cr.receiptno as serialno, cr.receiptdate as txndate, cr.paidby,
  (case when rem.voided > 0 then 0.0 else cr.amount end) as amount, 
  (case 
    when rem.voided > 0 then '***VOIDED***' else 
    case when ct.title is null then cr.collectiontype_name else ct.title end  
  end) as collectiontype  
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc  
  where remittanceid = $P{remittanceid}  
)rem 
  inner join cashreceipt cr on rem.objid=cr.objid 
  left join collectiontype ct on cr.collectiontype_objid=ct.objid 
where cr.collectiontype_objid like '%' 
order by afid, serialno 


[getReceiptsByRemittanceFund]
select 
  cr.formno as afid, cr.receiptno as serialno, cr.receiptdate as txndate, 
  ai.fund_title as fundname, cr.remarks as remarks, 
  case when xx.voided=0 then cr.paidby else '***VOIDED***' END AS payer,
  case when xx.voided=0 then cri.item_title else '***VOIDED***' END AS particulars,
  case when xx.voided=0 then cr.paidbyaddress else '' END AS payeraddress,
  case when xx.voided=0 then cri.amount else 0.0 END AS amount, 
  case when xx.voided=0 then cri.remarks else null end AS itemremarks   
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 
where ai.fund_objid like $P{fundid}  
order by afid, serialno, payer 


[getSerialReceiptsByRemittanceFund]
select 
  cr.formno as afid, cr.receiptno as serialno, cr.receiptdate as txndate, 
  ai.fund_title as fundname, cr.paidby as payer, cri.item_title as particulars, 
  case when xx.voided=0 then cri.amount else 0.0 end as amount 
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 
  inner join af a on cr.formno = a.objid 
where ai.fund_objid like $P{fundid} 
  and a.formtype = 'serial' 
order by afid, particulars, serialno 


[getNonSerialReceiptDetailsByFund]
select 
  cr.formno as afid, null as serialno, cr.receiptdate as txndate, 
  ai.fund_title as fundname, cr.paidby as payer, cri.item_title as particulars, 
  case when xx.voided=0 then cri.amount else 0.0 end as amount 
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 
  inner join af a on cr.formno = a.objid 
where ai.fund_objid like $P{fundid} 
  and a.formtype = 'cashticket' 
order by afid, particulars, serialno 


[getRevenueItemSummaryByFund]
select 
  ai.fund_title as fundname, cri.item_objid as acctid, 
  cri.item_title as acctname, cri.item_code as acctcode, 
  sum( cri.amount ) as amount 
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 
where ai.fund_objid like $P{fundid} and xx.voided=0
group by 
  ai.fund_title, cri.item_objid,  
  cri.item_title, cri.item_code 
order by fundname, acctcode 


[getReceiptsGroupByFund]
select 
  ai.fund_title as fundname, cr.formno, cr.receiptno, 
  min(cr.paidby) as paidby, sum(cri.amount) as amount  
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 
where xx.voided=0 
group by ai.fund_title, cr.formno, cr.receiptno 
order by fundname, formno, receiptno 


[getFundlist]
select distinct 
  ai.fund_objid as objid, ai.fund_title as title 
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 


[getCollectionType]
select distinct 
  ct.objid, ct.title 
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join collectiontype ct on cr.collectiontype_objid = ct.objid 


[getCashTicketCollectionSummaries]
select  
  CASE 
    WHEN subcollector_name IS NULL THEN cr.collector_name 
    ELSE cr.subcollector_name 
  END AS particulars,
  SUM(cr.amount) AS amount 
from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from remittance_cashreceipt rc 
  where remittanceid = $P{objid}  
)xx 
  inner join cashreceipt cr ON xx.objid = cr.objid 
  inner join af on cr.formno = af.objid 
where xx.voided=0 and af.formtype='cashticket' 
group by cr.collector_name, cr.subcollector_name 


[getAbstractSummaryOfCollectionByFund]
select 
  remid, remno, remdate, dtposted, total, collector_name, collector_title, 
  liquidatingofficer_name, liquidatingofficer_title, formno, controlid, series, 
  receiptno, receiptdate, acctcode, accttitle, paidby, sum(amount) as amount 
from ( 
  select 
    rem.objid as remid, rem.txnno as remno, rem.remittancedate as remdate, rem.dtposted, rem.amount as total, 
    rem.collector_name, rem.collector_title, rem.liquidatingofficer_name, rem.liquidatingofficer_title, 
    cr.formno, cr.controlid, cr.series, cr.receiptno, cr.receiptdate, ia.fund_code as acctcode, ia.fund_title as accttitle, 
    (case when xx.voided=0 then cr.paidby else '*** VOIDED ***' end) as paidby, 
    (case when xx.voided=0 then cri.amount else 0.0 end) as amount 
  from ( 
    select remc.*, 
      (select count(*) from cashreceipt_void where receiptid=remc.objid) as voided 
    from remittance_cashreceipt remc 
    where remittanceid = $P{remittanceid}     
  )xx inner join remittance rem on xx.remittanceid = rem.objid 
    inner join cashreceipt cr on xx.objid=cr.objid 
    inner join cashreceiptitem cri on cr.objid=cri.receiptid 
    inner join itemaccount ia on cri.item_objid = ia.objid 
)xx 
group by 
  remid, remno, remdate, dtposted, total, collector_name, collector_title, 
  liquidatingofficer_name, liquidatingofficer_title, formno, controlid, series, 
  receiptno, receiptdate, acctcode, accttitle, paidby
order by 
  receiptdate, formno, controlid, series 
