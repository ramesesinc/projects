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
    select c.objid, 
      (select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
    from cashreceipt c  
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
    select c.objid, 
      (select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
    from cashreceipt c  
    where remittanceid = $P{remittanceid} 
  )xx  
    inner join cashreceipt cr on xx.objid=cr.objid 
    inner join cashreceiptitem cri on cr.objid=cri.receiptid 
    inner join itemaccount ia on cri.item_objid=ia.objid 
    inner join af on cr.formno=af.objid 
  where ia.fund_objid = $P{fundid} 
)xx 
where xx.formno like $P{formno} 
group by xx.formtypeindexno, xx.controlid, xx.formno, xx.formtype, xx.stubno
order by xx.formtypeindexno, xx.formno, min(xx.receiptno)  


[getRCDCollectionSummaries]
select particulars, sum(amount) as amount 
from (  
  select  
    ('AF#'+ a.objid +':'+ ct.title +'-'+ ia.fund_title) as particulars, 
    (case when xx.voided > 0 then 0.0 else cri.amount end) as amount 
  from ( 
    select c.objid, c.collectiontype_objid, c.formno, 
      (select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
    from cashreceipt c  
    where c.remittanceid = $P{remittanceid} 
  )xx 
    inner join cashreceiptitem cri on cri.receiptid = xx.objid 
    inner join itemaccount ia on ia.objid = cri.item_objid 
    inner join collectiontype ct on ct.objid = xx.collectiontype_objid 
    inner join af a on a.objid = xx.formno 
  where ia.fund_objid like $P{fundid} 
    and a.objid like $P{formno}  
)xx 
group by particulars 


[getRCDOtherPayment]
select particulars, amount, reftype 
from ( 
  select nc.particulars, nc.amount, nc.reftype, nc.refdate, bank.name as bankname 
  from remittance_noncashpayment remnc 
    inner join cashreceiptpayment_noncash nc on nc.objid=remnc.objid 
    inner join checkpayment cp on cp.objid = nc.refid 
    inner join bank on bank.objid = cp.bankid 
  where remnc.remittanceid = $P{remittanceid} 

  union all 

  select nc.particulars, nc.amount, nc.reftype, nc.refdate, bank.name as bankname 
  from remittance_noncashpayment remnc 
    inner join cashreceiptpayment_noncash nc on nc.objid=remnc.objid 
    inner join creditmemo cm on cm.objid = nc.refid 
    inner join bankaccount ba on ba.objid = cm.bankaccount_objid 
    inner join bank on bank.objid = ba.bank_objid 
  where remnc.remittanceid = $P{remittanceid} 
)tmp1
order by bankname, refdate, amount  


[getNonCashPayments]
select cc.* from ( 
  select rc.*, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join remittance r on xx.remittanceid = r.objid 
  inner join cashreceiptpayment_noncash cc ON xx.objid = cc.receiptid 
where xx.voided = 0 
order by cc.particulars   


[getReceiptsByRemittanceCollectionType]
select 
  objid, afid, serialno, txndate, paidby, remarks, 
  case when voided=0 then amount else 0.0 end as amount, 
  case when voided=0 then collectiontype else '***VOIDED***' end as collectiontype 
from ( 
  select 
    c.objid, c.formno as afid, c.receiptno as serialno, 
    c.receiptdate as txndate, c.paidby, c.amount, c.remarks, 
    case when ct.title is null then c.collectiontype_name else ct.title end as collectiontype, 
    case when v.objid is null then 0 else 1 end as voided 
  from cashreceipt c  
    left join collectiontype ct on ct.objid = c.collectiontype_objid 
    left join cashreceipt_void v on v.receiptid = c.objid 
  where c.remittanceid = $P{remittanceid} 
    and c.collectiontype_objid like $P{collectiontypeid} 
)t1 
order by afid, serialno 


[getReceiptsByRemittanceFund]
select 
  c.objid as receiptid, t1.voided, c.formno as afid, c.receiptno as serialno, 
  c.receiptdate as txndate, c.remarks as remarks, t1.fundid, fund.title as fundname, 
  case when t1.voided=0 then c.paidby else '***VOIDED***' END AS payer,
  case when t1.voided=0 then t1.acctname else '***VOIDED***' END AS particulars,
  case when t1.voided=0 then c.paidbyaddress else '' END AS payeraddress,
  case when t1.voided=0 then (t1.amount-t1.share) else 0.0 END AS amount, 
  case when t1.voided=0 then t1.remarks else null end AS itemremarks 
from ( 
  select 
    aa.*, ISNULL((
      select sum(amount) from vw_remittance_cashreceiptshare 
      where receiptid = aa.receiptid and refacctid = aa.acctid
    ), 0.0) as share    
  from ( 
    select 
      v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, 
      v.remarks, v.voided, sum(v.amount) as amount 
    from vw_remittance_cashreceiptitem v 
    where v.remittanceid = $P{remittanceid} 
    group by v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, v.remarks, v.voided    
  )aa  

  union all 

  select 
    v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, 
    null as remarks, v.voided, sum(v.amount) as amount, 0.0 as share 
  from vw_remittance_cashreceiptshare v 
  where v.remittanceid = $P{remittanceid} 
  group by v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, v.voided 

)t1, cashreceipt c, fund 
where c.objid = t1.receiptid 
  and fund.objid = t1.fundid 
  ${fundfilter} 
order by c.formno, c.receiptno, c.paidby, t1.acctname 


[getSerialReceiptsByRemittanceFund]
select 
  c.objid, c.formno as afid, c.receiptno as serialno, c.receiptdate as txndate, 
  c.paidby as payer, t2.fundid, fund.title as fundname, t2.acctname as particulars, t2.amount 
from ( 
  select 
    t1.receiptid, t1.fundid, t1.acctid, t1.acctname, sum(t1.amount-t1.share) as amount 
  from ( 
    select 
      aa.*, ISNULL((
        select sum(amount) from vw_remittance_cashreceiptshare 
        where receiptid = aa.receiptid and refacctid = aa.acctid
      ), 0.0) as share    
    from ( 
      select 
        v.receiptid, v.fundid, v.acctid, v.acctname, v.voided, sum(v.amount) as amount 
      from vw_remittance_cashreceiptitem v 
      where v.remittanceid = $P{remittanceid} 
        and v.formtype = 'serial' 
      group by v.receiptid, v.fundid, v.acctid, v.acctname, v.voided    
    )aa  

    union all 

    select 
      v.receiptid, v.fundid, v.acctid, v.acctname, 
      v.voided, sum(v.amount) as amount, 0.0 as share 
    from vw_remittance_cashreceiptshare v 
    where v.remittanceid = $P{remittanceid} 
      and v.formtype = 'serial' 
    group by v.receiptid, v.fundid, v.acctid, v.acctname, v.voided 

  )t1
  group by t1.receiptid, t1.fundid, t1.acctid, t1.acctname 
  having sum(t1.amount-t1.share) > 0 
)t2, cashreceipt c, fund 
where c.objid = t2.receiptid 
  and fund.objid = t2.fundid 
  ${fundfilter} 
order by c.formno, t2.acctname, c.receiptno  


[getNonSerialReceiptDetailsByFund]
select 
  c.objid, c.formno as afid, c.receiptno as serialno, c.receiptdate as txndate, 
  c.paidby as payer, t2.fundid, fund.title as fundname, t2.acctname as particulars, t2.amount 
from ( 
  select 
    t1.receiptid, t1.fundid, t1.acctid, t1.acctname, sum(t1.amount-t1.share) as amount 
  from ( 
    select 
      aa.*, ISNULL((
        select sum(amount) from vw_remittance_cashreceiptshare 
        where receiptid = aa.receiptid and refacctid = aa.acctid
      ), 0.0) as share    
    from ( 
      select 
        v.receiptid, v.fundid, v.acctid, v.acctname, v.voided, sum(v.amount) as amount 
      from vw_remittance_cashreceiptitem v 
      where v.remittanceid = $P{remittanceid} 
        and v.formtype <> 'serial' 
      group by v.receiptid, v.fundid, v.acctid, v.acctname, v.voided    
    )aa  

    union all 

    select 
      v.receiptid, v.fundid, v.acctid, v.acctname, 
      v.voided, sum(v.amount) as amount, 0.0 as share 
    from vw_remittance_cashreceiptshare v 
    where v.remittanceid = $P{remittanceid} 
      and v.formtype <> 'serial' 
    group by v.receiptid, v.fundid, v.acctid, v.acctname, v.voided 

  )t1
  group by t1.receiptid, t1.fundid, t1.acctid, t1.acctname 
  having sum(t1.amount-t1.share) > 0 
)t2, cashreceipt c, fund 
where c.objid = t2.receiptid 
  and fund.objid = t2.fundid 
  ${fundfilter} 
order by c.formno, t2.acctname, c.receiptno  


[getRevenueItemSummaryByFund]
select 
  t1.fundid, fund.title as fundname, t1.acctid, t1.acctcode, t1.acctname, sum(t1.amount-t1.share) as amount 
from ( 
  select 
    aa.*, ISNULL((
      select sum(amount) from vw_remittance_cashreceiptshare 
      where receiptid = aa.receiptid and refacctid = aa.acctid
    ), 0.0) as share    
  from ( 
    select 
      v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, v.voided, sum(v.amount) as amount 
    from vw_remittance_cashreceiptitem v 
    where v.remittanceid = $P{remittanceid} 
    group by v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, v.voided    
  )aa  

  union all 

  select 
    v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, 
    v.voided, sum(v.amount) as amount, 0.0 as share 
  from vw_remittance_cashreceiptshare v 
  where v.remittanceid = $P{remittanceid} 
  group by v.receiptid, v.fundid, v.acctid, v.acctcode, v.acctname, v.voided 

)t1, fund 
where fund.objid = t1.fundid  ${fundfilter} 
group by t1.fundid, fund.title, t1.acctid, t1.acctcode, t1.acctname
order by fund.title, t1.acctcode   


[getReceiptsGroupByFund]
select 
  fundid, fundname, formno, receiptno, paidby, sum(amount) as amount 
from ( 
  select 
    v.formno, v.receiptno, v.fundid, fund.title as fundname, v.amount, 
    case when v.voided = 0 then v.paidby else '*** VOIDED ***' end as paidby 
  from vw_remittance_cashreceiptitem v, fund  
  where v.remittanceid = $P{remittanceid} 
    and fund.objid = v.fundid 
)aa 
group by fundid, fundname, formno, receiptno, paidby 
order by fundname, formno, receiptno 


[getFundlist]
select distinct 
  ai.fund_objid as objid, ai.fund_title as title 
from ( 
  select rc.objid, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from cashreceipt rc 
  where remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join cashreceiptitem cri on cr.objid = cri.receiptid 
  inner join itemaccount ai on cri.item_objid = ai.objid 


[getCollectionType]
select distinct 
  ct.objid, ct.title 
from ( 
  select c.*, 
    (select count(*) from cashreceipt_void where receiptid=c.objid) as voided 
  from cashreceipt c 
  where c.remittanceid = $P{remittanceid} 
)xx 
  inner join cashreceipt cr on xx.objid = cr.objid 
  inner join collectiontype ct on cr.collectiontype_objid = ct.objid 
order by ct.title 


[getCashTicketCollectionSummaries]
select formno, particulars, sum(amount) as amount 
from ( 
  select distinct 
    c.objid, c.formno, isnull(c.subcollector_name, c.collector_name) as particulars, 
    case when v.objid is null then c.amount else 0.0 end as amount,   
    case when v.objid is null then 0 else 1 end as voided  
  from cashreceipt c 
    inner join af on (af.objid = c.formno and af.formtype = 'cashticket') 
    left join cashreceipt_void v on v.receiptid = c.objid 
  where c.remittanceid = $P{remittanceid} 
)t1 
group by formno, particulars 
having sum(amount) > 0 


[getAbstractSummaryOfCollectionByFund]
select 
  remid, remno, remdate, dtposted, total, collector_name, collector_title, 
  liquidatingofficer_name, liquidatingofficer_title, formno, controlid, series, 
  receiptno, receiptdate, acctcode, accttitle, paidby, sum(amount) as amount 
from ( 
  select 
    r.objid as remid, r.controlno as remno, r.controldate as remdate, r.dtposted, r.amount as total, 
    r.collector_name, r.collector_title, r.liquidatingofficer_name, r.liquidatingofficer_title, 
    c.formno, c.controlid, c.series, c.receiptno, c.receiptdate, fund.code as acctcode, fund.title as accttitle, 
    (case when v.voided=0 then c.paidby else '*** VOIDED ***' end) as paidby, v.amount 
  from vw_remittance_cashreceiptitem v 
    inner join remittance r on r.objid = v.remittanceid 
    inner join cashreceipt c on c.objid = v.receiptid 
    inner join fund on fund.objid = v.fundid 
  where v.remittanceid = $P{remittanceid} 
)aa 
group by 
  remid, remno, remdate, dtposted, total, collector_name, collector_title, 
  liquidatingofficer_name, liquidatingofficer_title, formno, controlid, series, 
  receiptno, receiptdate, acctcode, accttitle, paidby
order by 
  receiptdate, formno, controlid, series 


[getAFList]
select 
  ia.fund_objid, cr.formno, af.title as formtitle   
from remittance rem 
  inner join cashreceipt cr on cr.remittanceid = rem.objid 
  inner join cashreceiptitem cri on cri.receiptid = cr.objid 
  inner join itemaccount ia on ia.objid = cri.item_objid 
  inner join af on af.objid = cr.formno 
where rem.objid = $P{remittanceid} 
group by ia.fund_objid, cr.formno, af.title 
order by ia.fund_objid, cr.formno 
