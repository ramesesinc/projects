[getStandardReport]
select 
  pc.name as classname, pc.orderno, pc.special,  
  sum(case when ri.revperiod='current' then ri.basic else 0.0 end)  as basiccurrent,
  sum(case when ri.revperiod='current' then ri.basicdisc else 0.0 end)  as basicdisc,
  sum(case when ri.revperiod in ('previous', 'prior') then ri.basic else 0.0 end)  as basicprev,
  sum(case when ri.revperiod='current' then ri.basicint else 0.0 end)  as basiccurrentint,
  sum(case when ri.revperiod in ('previous', 'prior') then ri.basicint else 0.0 end)  as basicprevint,
  sum(ri.basic - ri.basicdisc + ri.basicint) as basicnet, 

  sum(case when ri.revperiod='current' then ri.sef else 0.0 end)  as sefcurrent,
  sum(case when ri.revperiod='current' then ri.sefdisc else 0.0 end)  as sefdisc,
  sum(case when ri.revperiod in ('previous', 'prior') then ri.sef else 0.0 end)  as sefprev,
  sum(case when ri.revperiod='current' then ri.sefint else 0.0 end)  as sefcurrentint,
  sum(case when ri.revperiod in ('previous', 'prior') then ri.sefint else 0.0 end) as sefprevint,
  sum(ri.sef - ri.sefdisc + ri.sefint) as sefnet,  

  sum(case when ri.revperiod='current' then ri.basicidle else 0.0 end)  as idlecurrent,
  sum(case when ri.revperiod in ('previous', 'prior') then ri.basicidle else 0.0 end)  as idleprev,
  sum(case when ri.revperiod='current' then ri.basicidledisc else 0.0 end)  as idledisc,
  sum(ri.basicidleint)  as idleint, 
  sum(ri.basicidle-ri.basicidledisc+ri.basicidleint) as idlenet, 

  sum(case when ri.revperiod='current' then ri.sh else 0.0 end)  as shcurrent,
  sum(case when ri.revperiod in ('previous', 'prior') then ri.sh else 0.0 end)  as shprev,
  sum(case when ri.revperiod='current' then ri.shdisc else 0.0 end)  as shdisc,
  sum(ri.shint)  as shint, 
  sum(ri.sh-ri.shdisc+ri.shint) as shnet, 

  sum(ri.firecode) as firecode,

  0.0 as levynet 
from remittance rem 
  inner join collectionvoucher liq on liq.objid = rem.collectionvoucherid 
  inner join cashreceipt cr on cr.remittanceid = rem.objid 
  inner join rptpayment rp on cr.objid = rp.receiptid 
  inner join vw_rptpayment_item ri on rp.objid = ri.parentid
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
  sum(ri.basic) as basic, 
  sum(ri.basicdisc) as basicdisc, 
  sum( ri.basic - ri.basicdisc) as basicnet,
  sum(ri.sef) as sef, 
  sum(ri.sefdisc) as sefdisc, 
  sum(ri.sef - ri.sefdisc) as sefnet,
  sum(ri.basicidle - ri.basicidledisc) as idle,
  sum(ri.sh - ri.shdisc) as sh,
  sum(ri.firecode) as firecode,
  sum( ri.basic - ri.basicdisc + ri.sef - ri.sefdisc + 
    ri.basicidle - ri.basicidledisc ) as netgrandtotal
from remittance rem 
  inner join collectionvoucher liq on liq.objid = rem.collectionvoucherid 
  inner join cashreceipt cr on cr.remittanceid = rem.objid 
  inner join rptpayment rp on cr.objid = rp.receiptid 
  inner join vw_rptpayment_item ri on rp.objid = ri.parentid
  inner join rptledger rl ON rp.refid = rl.objid  
  inner join propertyclassification pc ON rl.classification_objid = pc.objid 
where ${filter}  
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  and ri.revperiod = 'advance'
  and ri.year = $P{advanceyear}
group by ri.year, pc.name, pc.orderno, pc.special
order by pc.orderno 


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
    inner join collectionvoucher liq on liq.objid = rem.collectionvoucherid 
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
    inner join collectionvoucher liq on liq.objid = rem.collectionvoucherid 
    inner join cashreceipt cr on cr.remittanceid = rem.objid 
    inner join rptpayment rp on cr.objid = rp.receiptid 
    inner join rptpayment_share ri on rp.objid = ri.parentid
  where ${filter}  
    and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid)
    and ri.revperiod = 'advance' 
)t 



[findAdvanceDispositionReport2]
select 
  sum(ri.basic) as basic,
  sum(ri.basicdisc) as basicdisc,
  sum(ri.basicidle) as basicidle,
  sum(ri.basicidledisc) as basicidledisc,
  sum(ri.sef) as sef,
  sum(ri.sefdisc) as sefdisc
from remittance rem 
  inner join collectionvoucher liq on liq.objid = rem.collectionvoucherid 
  inner join cashreceipt cr on cr.remittanceid = rem.objid 
  inner join rptpayment rp on cr.objid = rp.receiptid 
  inner join vw_rptpayment_item ri on rp.objid = ri.parentid
where ${filter}  
  and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid)
  and ri.revperiod = 'advance' 
  and ri.year = $P{advanceyear}
