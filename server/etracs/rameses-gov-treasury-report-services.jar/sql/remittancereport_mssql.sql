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
  c.objid as receiptid, t2.voided, c.formno as afid, c.receiptno as serialno, 
  c.receiptdate as txndate, c.remarks as remarks, t2.fundid, fund.title as fundname, 
  case when t2.voided=0 then c.paidby else '***VOIDED***' END AS payer,
  case when t2.voided=0 then t2.item_title else '***VOIDED***' END AS particulars,
  case when t2.voided=0 then c.paidbyaddress else '' END AS payeraddress,
  case when t2.voided=0 then t2.amount else 0.0 END AS amount, 
  case when t2.voided=0 then t2.remarks else null end AS itemremarks 
from ( 
  select 
    t1.objid, t1.fundid, t1.item_objid, t1.item_code, t1.item_title, 
    t1.voided, t1.remarks, sum( t1.amount - t1.share ) as amount 
  from ( 
    select 
      objid, fundid, item_objid, item_code, item_title, amount, sum(share) as share, voided, remarks 
    from ( 
      select 
        c.objid, cri.item_fund_objid as fundid, cri.item_objid, cri.item_code, cri.item_title, 
        case when v.objid is null then cri.amount else 0.0 end as amount, 
        case when v.objid is null then isnull(cs.amount,0.0) else 0.0 end as share, 
        case when v.objid is null then 0 else 1 end as voided, cri.remarks 
      from cashreceipt c 
        inner join cashreceiptitem cri on cri.receiptid = c.objid 
        inner join itemaccount ia on ia.objid = cri.item_objid 
        left join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
        left join cashreceipt_void v on v.receiptid = c.objid 
      where c.remittanceid = $P{remittanceid}  
    )tt1 
    group by objid, fundid, item_objid, item_code, item_title, amount, voided, remarks

    union all 

    select 
      c.objid, ia.fund_objid as fundid, ia.objid as item_objid, ia.code as item_code, ia.title as item_title, 
      case when v.objid is null then cs.amount else 0.0 end as amount, 0.0 as share, 
      case when v.objid is null then 0 else 1 end as voided, null as remarks 
    from cashreceipt c 
      inner join cashreceiptitem cri on cri.receiptid = c.objid 
      inner join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
      inner join itemaccount ia on ia.objid = cs.payableitem_objid 
      left join cashreceipt_void v on v.receiptid = c.objid 
    where c.remittanceid = $P{remittanceid}  
  )t1 
  group by 
    t1.objid, t1.fundid, t1.item_objid, t1.item_code, t1.item_title, t1.voided, t1.remarks 

)t2, cashreceipt c, fund 
where c.objid = t2.objid 
  and fund.objid = t2.fundid 
  ${fundfilter} 
order by c.formno, c.receiptno, c.paidby 


[getSerialReceiptsByRemittanceFund]
select 
  t1.objid, t1.afid, t1.serialno, t1.txndate, t1.payer, t1.fundid, 
  fund.title as fundname, t1.particulars, sum(t1.amount) as amount 
from ( 
  select 
    objid, afid, serialno, txndate, payer, fundid, particulars, (amount-sum(share)) as amount 
  from ( 
    select 
      c.objid, c.formno as afid, c.receiptno as serialno, c.receiptdate as txndate, 
      c.paidby as payer, cri.item_fund_objid as fundid, cri.item_title as particulars, 
      case when v.objid is null then cri.amount else 0.0 end as amount, 
      case when v.objid is null then isnull(cs.amount, 0.0) else 0.0 end as share 
    from cashreceipt c 
      inner join af on (af.objid = c.formno and af.formtype = 'serial') 
      inner join cashreceiptitem cri on cri.receiptid = c.objid 
      left join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
      left join cashreceipt_void v on v.receiptid = c.objid 
    where c.remittanceid = $P{remittanceid}  
  )tt1 
  group by objid, afid, serialno, txndate, payer, fundid, particulars, amount 

  union all 

  select 
      c.objid, c.formno as afid, c.receiptno as serialno, c.receiptdate as txndate, 
      c.paidby as payer, ia.fund_objid as fundid, ia.title as particulars, 
      case when v.objid is null then cs.amount else 0.0 end as amount  
    from cashreceipt c 
      inner join af on (af.objid = c.formno and af.formtype = 'serial') 
      inner join cashreceiptitem cri on cri.receiptid = c.objid 
      inner join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
      inner join itemaccount ia on ia.objid = cs.payableitem_objid 
      left join cashreceipt_void v on v.receiptid = c.objid 
    where c.remittanceid = $P{remittanceid} 
)t1, fund 
where fund.objid = t1.fundid ${fundfilter} 
group by 
  t1.objid, t1.afid, t1.serialno, t1.txndate, t1.payer, 
  t1.fundid, fund.title, t1.particulars
order by 
  t1.afid, t1.particulars, t1.serialno 



[getNonSerialReceiptDetailsByFund]
select 
  t1.objid, t1.afid, t1.txndate, t1.payer, t1.fundid, 
  fund.title as fundname, t1.particulars, sum(t1.amount) as amount 
from ( 
  select 
    objid, afid, serialno, txndate, payer, fundid, particulars, (amount-sum(share)) as amount 
  from ( 
    select 
      c.objid, c.formno as afid, c.receiptno as serialno, c.receiptdate as txndate, 
      c.paidby as payer, cri.item_fund_objid as fundid, cri.item_title as particulars, 
      case when v.objid is null then cri.amount else 0.0 end as amount, 
      case when v.objid is null then isnull(cs.amount, 0.0) else 0.0 end as share 
    from cashreceipt c 
      inner join af on (af.objid = c.formno and af.formtype <> 'serial') 
      inner join cashreceiptitem cri on cri.receiptid = c.objid 
      left join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
      left join cashreceipt_void v on v.receiptid = c.objid 
    where c.remittanceid = $P{remittanceid}  
  )tt1 
  group by objid, afid, serialno, txndate, payer, fundid, particulars, amount 

  union all 

  select 
      c.objid, c.formno as afid, c.receiptno as serialno, c.receiptdate as txndate, 
      c.paidby as payer, ia.fund_objid as fundid, ia.title as particulars, 
      case when v.objid is null then cs.amount else 0.0 end as amount  
    from cashreceipt c 
      inner join af on (af.objid = c.formno and af.formtype <> 'serial') 
      inner join cashreceiptitem cri on cri.receiptid = c.objid 
      inner join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
      inner join itemaccount ia on ia.objid = cs.payableitem_objid 
      left join cashreceipt_void v on v.receiptid = c.objid 
    where c.remittanceid = $P{remittanceid} 
)t1, fund 
where fund.objid = t1.fundid ${fundfilter} 
group by 
  t1.objid, t1.afid, t1.txndate, t1.payer, 
  t1.fundid, fund.title, t1.particulars
order by 
  t1.afid, t1.particulars  



[getRevenueItemSummaryByFund]
select 
  t1.fundid, fund.title as fundname, t1.acctid, t1.acctcode, t1.acctname, 
  sum( t1.amount - t1.share ) as amount 
from ( 
  select 
    cri.receiptid, cri.item_fund_objid as fundid, cri.item_objid as acctid, 
    cri.item_code as acctcode, cri.item_title as acctname, 
    cri.amount, sum(isnull(cs.amount,0.0)) as share  
  from cashreceipt c 
    inner join cashreceiptitem cri on cri.receiptid = c.objid 
    inner join itemaccount ia on ia.objid = cri.item_objid 
    left join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
    left join cashreceipt_void v on v.receiptid = c.objid 
  where c.remittanceid = $P{remittanceid} 
    and v.objid is null 
  group by 
    cri.receiptid, cri.item_fund_objid, cri.item_objid, 
    cri.item_code, cri.item_title, cri.amount

  union all 

  select 
    cri.receiptid, ia.fund_objid as fundid, ia.objid as acctid, ia.code as acctcode, 
    ia.title as acctname, cs.amount, 0.0 as share 
  from cashreceipt c 
    inner join cashreceiptitem cri on cri.receiptid = c.objid 
    inner join cashreceipt_share cs on (cs.receiptid = c.objid and cs.refitem_objid = cri.item_objid)
    inner join itemaccount ia on ia.objid = cs.payableitem_objid 
    left join cashreceipt_void v on v.receiptid = c.objid 
  where c.remittanceid = $P{remittanceid} 
    and v.objid is null 
)t1, fund 
where fund.objid = t1.fundid ${fundfilter} 
group by t1.fundid, fund.title, t1.acctid, t1.acctcode, t1.acctname 
order by fund.title, t1.acctcode 



[getReceiptsGroupByFund]
select 
  fundid, fundname, formno, receiptno, paidby, sum(amount) as amount 
from ( 
  select 
    c.formno, c.receiptno, fund.objid as fundid, fund.title as fundname, 
    case when v.objid is null then c.paidby else '*** VOIDED ***' end as paidby,
    case when v.objid is null then cri.amount else 0.0 end as amount 
  from cashreceipt c 
    inner join cashreceiptitem cri on cri.receiptid = c.objid  
    inner join fund on fund.objid = cri.item_fund_objid 
    left join cashreceipt_void v on v.receiptid = c.objid 
  where c.remittanceid = $P{remittanceid} 
)t1 
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
select  
  CASE 
    WHEN subcollector_name IS NULL THEN cr.collector_name 
    ELSE cr.subcollector_name 
  END AS particulars,
  SUM(cr.amount) AS amount 
from ( 
  select rc.objid, 
    (select count(*) from cashreceipt_void where receiptid=rc.objid) as voided 
  from cashreceipt rc 
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
    rem.objid as remid, rem.controlno as remno, rem.controldate as remdate, rem.dtposted, rem.amount as total, 
    rem.collector_name, rem.collector_title, rem.liquidatingofficer_name, rem.liquidatingofficer_title, 
    cr.formno, cr.controlid, cr.series, cr.receiptno, cr.receiptdate, ia.fund_code as acctcode, ia.fund_title as accttitle, 
    (case when xx.voided=0 then cr.paidby else '*** VOIDED ***' end) as paidby, 
    (case when xx.voided=0 then cri.amount else 0.0 end) as amount 
  from ( 
    select remc.objid, remc.remittanceid,  
      (select count(*) from cashreceipt_void where receiptid=remc.objid) as voided 
    from cashreceipt remc 
    where remittanceid = $P{remittanceid}     
  )xx 
    inner join remittance rem on xx.remittanceid = rem.objid 
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
