[getStandardReport]
select 
  pc.name as classname, pc.orderno, pc.special,  
  sum(case when ri.revperiod='current' and ri.revtype = 'basic' then ri.amount else 0.0 end)  as basiccurrent,
  sum(case when ri.revperiod='current' and ri.revtype = 'basic'  then ri.discount else 0.0 end)  as basicdisc,
  sum(case when ri.revperiod in ('previous', 'prior') and ri.revtype = 'basic'  then ri.amount else 0.0 end)  as basicprev,
  sum(case when ri.revperiod='current' and ri.revtype = 'basic'  then ri.interest else 0.0 end)  as basiccurrentint,
  sum(case when ri.revperiod in ('previous', 'prior') and ri.revtype = 'basic'  then ri.interest else 0.0 end)  as basicprevint,
  sum(case when ri.revtype = 'basic' then ri.amount - ri.discount+ ri.interest else 0 end) as basicnet, 

  sum(case when ri.revperiod='current' and ri.revtype = 'sef' then ri.amount else 0.0 end)  as sefcurrent,
  sum(case when ri.revperiod='current' and ri.revtype = 'sef'  then ri.discount else 0.0 end)  as sefdisc,
  sum(case when ri.revperiod in ('previous', 'prior') and ri.revtype = 'sef'  then ri.amount else 0.0 end)  as sefprev,
  sum(case when ri.revperiod='current' and ri.revtype = 'sef'  then ri.interest else 0.0 end)  as sefcurrentint,
  sum(case when ri.revperiod in ('previous', 'prior') and ri.revtype = 'sef'  then ri.interest else 0.0 end)  as sefprevint,
  sum(case when ri.revtype = 'sef' then ri.amount - ri.discount+ ri.interest else 0 end) as sefnet, 

  sum(case when ri.revperiod='current' and ri.revtype = 'basicidle' then ri.amount else 0.0 end)  as idlecurrent,
  sum(case when ri.revperiod in ('previous', 'prior') and ri.revtype = 'basicidle'  then ri.amount else 0.0 end)  as idleprev,
  sum(case when ri.revperiod='current' and ri.revtype = 'basicidle'  then ri.discount else 0.0 end)  as idledisc,
  sum(case when ri.revtype = 'basicidle' then ri.interest else 0 end )  as idleint, 
  sum(case when ri.revtype = 'basicidle'then ri.amount - ri.discount + ri.interest else 0 end) as idlenet, 

  sum(case when ri.revperiod='current' and ri.revtype = 'sh' then ri.amount else 0.0 end)  as shcurrent,
  sum(case when ri.revperiod in ('previous', 'prior') and ri.revtype = 'sh' then ri.amount else 0.0 end)  as shprev,
  sum(case when ri.revperiod='current' and ri.revtype = 'sh' then ri.discount else 0.0 end)  as shdisc,
  sum(case when ri.revtype = 'sh' then ri.interest else 0 end)  as shint, 
  sum(case when ri.revtype = 'sh' then ri.amount - ri.discount + ri.interest else 0 end) as shnet, 

  sum(case when ri.revtype = 'firecode' then ri.amount - ri.discount + ri.interest else 0 end ) as firecode,

  0.0 as levynet 
from remittance rem 
  inner join collectionvoucher cv on cv.objid = rem.collectionvoucherid 
  inner join cashreceipt cr on cr.remittanceid = rem.objid 
  inner join rptpayment rp on cr.objid = rp.receiptid 
  inner join rptpayment_item ri on rp.objid = ri.parentid
  left join rptledger rl ON rp.refid = rl.objid  
  left join propertyclassification pc ON rl.classification_objid = pc.objid 
where ${filter} 
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  and ri.revperiod <> 'advance'
group by pc.name, pc.orderno, pc.special
order by pc.orderno 


[getAdvanceReport]
select 
  ri.year, pc.name as classname, pc.orderno, pc.special,  
  sum(case when ri.revtype = 'basic' then ri.amount else 0 end ) as basic, 
  sum(case when ri.revtype = 'basic' then ri.discount else 0 end ) as basicdisc, 
  sum(case when ri.revtype = 'basic' then ri.amount - ri.discount else 0 end ) as basicnet,
  sum(case when ri.revtype = 'sef' then ri.amount else 0 end ) as sef, 
  sum(case when ri.revtype = 'sef' then ri.discount else 0 end ) as sefdisc, 
  sum(case when ri.revtype = 'sef' then ri.amount - ri.discount else 0 end ) as sefnet,
  sum(case when ri.revtype = 'basicidle' then ri.amount - ri.discount else 0 end ) as idle,
  sum(case when ri.revtype = 'sh' then ri.amount - ri.discount else 0 end ) as sh,
  sum(case when ri.revtype = 'firecode' then ri.amount else 0 end ) as firecode,
  sum(ri.amount - ri.discount) as netgrandtotal
from remittance rem 
  inner join collectionvoucher cv on cv.objid = rem.collectionvoucherid 
  inner join cashreceipt cr on cr.remittanceid = rem.objid 
  inner join rptpayment rp on cr.objid = rp.receiptid 
  inner join rptpayment_item ri on rp.objid = ri.parentid
  inner join rptledger rl ON rp.refid = rl.objid  
  inner join propertyclassification pc ON rl.classification_objid = pc.objid 
where ${filter}  
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  and ri.revperiod = 'advance'
group by ri.year, pc.name, pc.orderno, pc.special
order by ri.year, pc.orderno 


[findStandardDispositionReport]
select 
  sum( provcitybasicshare ) as provcitybasicshare, 
  sum( munibasicshare ) as munibasicshare, 
  sum( brgybasicshare ) as brgybasicshare, 
  sum( provcitysefshare ) as provcitysefshare, 
  sum( munisefshare ) as munisefshare, 
  sum( brgysefshare ) as brgysefshare 
from ( 
  select   
    case when ri.revtype in ('basic', 'basicint', 'basicidle', 'basicidleint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitybasicshare,
    case when ri.revtype in ('basic', 'basicint', 'basicidle', 'basicidleint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munibasicshare,
    case when ri.revtype in ('basic', 'basicint') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgybasicshare,
    case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitysefshare,
    case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munisefshare,
    0.0 as brgysefshare 
  from remittance rem 
    inner join collectionvoucher cv on cv.objid = rem.collectionvoucherid 
    inner join cashreceipt cr on cr.remittanceid = rem.objid 
    inner join rptpayment rp on cr.objid = rp.receiptid 
    inner join rptpayment_share ri on rp.objid = ri.parentid
  where ${filter}  
    and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
    and ri.revperiod != 'advance' 
)t 


[findAdvanceDispositionReport]
select 
  sum( provcitybasicshare ) as provcitybasicshare,
  sum( munibasicshare ) as munibasicshare,
  sum( brgybasicshare ) as brgybasicshare,
  sum( provcitysefshare ) as provcitysefshare,
  sum( munisefshare ) as munisefshare,
  sum( brgysefshare ) as brgysefshare
from ( 
  select 
    case when ri.revtype in ('basic', 'basicint', 'basicidle', 'basicidleint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitybasicshare,
    case when ri.revtype in ('basic', 'basicint', 'basicidle', 'basicidleint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munibasicshare,
    case when ri.revtype in ('basic', 'basicint', 'basicidle', 'basicidleint') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgybasicshare,
    case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('province', 'city') then ri.amount else 0.0 end as provcitysefshare,
    case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('municipality') then ri.amount else 0.0 end as munisefshare,
    case when ri.revtype in ('sef', 'sefint') and ri.sharetype in ('barangay') then ri.amount else 0.0 end as brgysefshare 
  from remittance rem 
    inner join collectionvoucher cv on cv.objid = rem.collectionvoucherid 
    inner join cashreceipt cr on cr.remittanceid = rem.objid 
    inner join rptpayment rp on cr.objid = rp.receiptid 
    inner join rptpayment_share ri on rp.objid = ri.parentid
  where ${filter}  
    and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid)
    and ri.revperiod = 'advance' 
)t 



[findAdvanceDispositionReport2]
select 
  sum(case when ri.revtype = 'basic' then ri.amount else 0 end) as basic,
  sum(case when ri.revtype = 'basic' then ri.discount else 0 end) as basicdisc,
  sum(case when ri.revtype = 'basicidle' then ri.amount else 0 end) as basicidle,
  sum(case when ri.revtype = 'basicidle' then ri.discount else 0 end) as basicidledisc,
  sum(case when ri.revtype = 'sef' then ri.amount else 0 end) as sef,
  sum(case when ri.revtype = 'sef' then ri.discount else 0 end) as sefdisc
from remittance rem 
  inner join collectionvoucher cv on cv.objid = rem.collectionvoucherid 
  inner join cashreceipt cr on cr.remittanceid = rem.objid 
  inner join rptpayment rp on cr.objid = rp.receiptid 
  inner join rptpayment_item ri on rp.objid = ri.parentid
where ${filter}  
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid)
  and ri.revperiod = 'advance' 

