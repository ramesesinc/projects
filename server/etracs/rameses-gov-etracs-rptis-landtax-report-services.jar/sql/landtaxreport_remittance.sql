[getAbstractOfRPTCollection] 
select t.*
from (
  select
    cr.objid as receiptid, 
    rl.objid as rptledgerid,
    1 as idx,
    min(cri.year) as minyear,
    min(rp.fromqtr) as minqtr,
    max(cri.year) as maxyear, 
    max(rp.toqtr) as maxqtr, 
    'BASIC' as type, 
    cr.receiptdate as ordate, 
    case when cv.objid is null then cr.payer_name else '*** VOIDED ***' end as taxpayername, 
    case when cv.objid is null then rl.tdno else '' end as tdno, 
    cr.receiptno as orno, 
    case when m.name is null then c.name else m.name end as municityname, 
    b.name as barangay, 
    case when cv.objid is null  then rl.classcode else '' end as classification, 
    case when cv.objid is null then rl.totalav else 0.0 end as assessvalue,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basic else 0.0 end) as currentyear,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basic else 0.0 end) as previousyear,
    sum(case when cv.objid is null  then cri.basicdisc else 0.0 end) as discount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicint else 0.0 end) as penaltycurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicint else 0.0 end) as penaltyprevious,

    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidle else 0.0 end) as basicidlecurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidle else 0.0 end) as basicidleprevious,
    sum(case when cv.objid is null  then cri.basicidledisc else 0.0 end) as basicidlediscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidleint else 0.0 end) as basicidlecurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidleint else 0.0 end) as basicidlepreviouspenalty,

    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sh else 0.0 end) as shcurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sh else 0.0 end) as shprevious,
    sum(case when cv.objid is null  then cri.shdisc else 0.0 end) as shdiscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.shint else 0.0 end) as shcurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.shint else 0.0 end) as shpreviouspenalty,

    sum(case when cv.objid is null then cri.firecode else 0.0 end) as firecode,
    sum(
        case when cv.objid is null then 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.sh - cri.shdisc + cri.shint + 
            cri.firecode 
        else 0.0 end 
    ) as total,

    max(case when cv.objid is null then cri.partialled else 0 end) as partialled
  from remittance rem 
    inner join cashreceipt cr on cr.remittanceid = rem.objid 
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid 
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where rem.objid=$P{objid} 
    and cri.year <= $P{year} 
    and cr.collector_objid like $P{collectorid} 
    ${filter} 
  group by cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name , rl.totalav 
   
  union all

  select
    cr.objid as receiptid,
    rl.objid as rptledgerid,
    2 as idx,
    min(cri.year) as minyear,
    min(rp.fromqtr) as minqtr,
    max(cri.year) as maxyear, 
    max(rp.toqtr) as maxqtr, 
    'SEF' as type, 
    cr.receiptdate as ordate, 
    case when cv.objid is null then cr.payer_name else '*** VOIDED ***' end as taxpayername, 
    case when cv.objid is null then rl.tdno else '' end as tdno, 
    cr.receiptno as orno, 
    case when m.name is null then c.name else m.name end as municityname, 
    b.name as barangay, 
    case when cv.objid is null  then rl.classcode else '' end as classification, 
    case when cv.objid is null then rl.totalav else 0.0 end as assessvalue,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sef else 0.0 end) as currentyear,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sef else 0.0 end) as previousyear,
    sum(case when cv.objid is null  then cri.basicdisc else 0.0 end) as discount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sefint else 0.0 end) as penaltycurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sefint else 0.0 end) as penaltyprevious,
    
    sum(0) as basicidlecurrent,
    sum(0) as basicidleprevious,
    sum(0) as basicidlediscount,
    sum(0) as basicidlecurrentpenalty,
    sum(0) as basicidlepreviouspenalty,

    sum(0) as shcurrent,
    sum(0) as shprevious,
    sum(0) as shdiscount,
    sum(0) as shcurrentpenalty,
    sum(0) as shpreviouspenalty,

    sum(0) as firecode,
    sum(case when cv.objid is null then cri.sef - cri.sefdisc + cri.sefint else 0.0 end ) as total,
    max(case when cv.objid is null then cri.partialled else 0.0 end) as partialled
  from remittance rem 
    inner join cashreceipt cr on cr.remittanceid = rem.objid 
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid 
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where rem.objid=$P{objid} 
    and cri.year <= $P{year} 
    and cr.collector_objid like $P{collectorid} 
    ${filter} 
  group by cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name , rl.totalav 
   
) t
order by t.municityname, t.idx, t.orno 



[getAbstractOfRPTCollectionAdvance] 
select t.*
from (
  select
    cr.objid as receiptid,
    rl.objid as rptledgerid,
    1 as idx,
    min(cri.year) as minyear,
    min(rp.fromqtr) as minqtr,
    max(cri.year) as maxyear, 
    max(rp.toqtr) as maxqtr, 
    'BASIC' as type, 
    cr.receiptdate as ordate, 
    case when cv.objid is null then cr.payer_name else '*** VOIDED ***' end as taxpayername, 
    case when cv.objid is null then rl.tdno else '' end as tdno, 
    cr.receiptno as orno, 
    case when m.name is null then c.name else m.name end as municityname, 
    b.name as barangay, 
    case when cv.objid is null  then rl.classcode else '' end as classification, 
    case when cv.objid is null then rl.totalav else 0.0 end as assessvalue,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basic else 0.0 end) as currentyear,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basic else 0.0 end) as previousyear,
    sum(case when cv.objid is null  then cri.basicdisc else 0.0 end) as discount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicint else 0.0 end) as penaltycurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicint else 0.0 end) as penaltyprevious,
    
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidle else 0.0 end) as basicidlecurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidle else 0.0 end) as basicidleprevious,
    sum(case when cv.objid is null  then cri.basicidledisc else 0.0 end) as basicidlediscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.basicidleint else 0.0 end) as basicidlecurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.basicidleint else 0.0 end) as basicidlepreviouspenalty,

    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sh else 0.0 end) as shcurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sh else 0.0 end) as shprevious,
    sum(case when cv.objid is null  then cri.shdisc else 0.0 end) as shdiscount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.shint else 0.0 end) as shcurrentpenalty,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.shint else 0.0 end) as shpreviouspenalty,

    sum(case when cv.objid is null then cri.firecode else 0.0 end) as firecode,

    sum(
        case when cv.objid is null then 
            cri.basic - cri.basicdisc + cri.basicint + 
            cri.basicidle - cri.basicidledisc + cri.basicidleint + 
            cri.sh - cri.shdisc + cri.shint + 
            cri.firecode 
        else 0.0 end 
    ) as total

  from remittance rem 
    inner join cashreceipt cr on cr.remittanceid = rem.objid 
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid 
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where rem.objid=$P{objid} 
    and cri.year > $P{year} 
    and cr.collector_objid like $P{collectorid} 
    ${filter} 
  group by cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name, rl.totalav 
   
  union all

  select
    cr.objid as receiptid,
    rl.objid as rptledgerid,
    2 as idx,
    min(cri.year) as minyear,
    min(rp.fromqtr) as minqtr,
    max(cri.year) as maxyear, 
    max(rp.toqtr) as maxqtr, 
    'SEF' as type, 
    cr.receiptdate as ordate, 
    case when cv.objid is null then cr.payer_name else '*** VOIDED ***' end as taxpayername, 
    case when cv.objid is null then rl.tdno else '' end as tdno, 
    cr.receiptno as orno, 
    case when m.name is null then c.name else m.name end as municityname, 
    b.name as barangay, 
    case when cv.objid is null  then rl.classcode else '' end as classification, 
    case when cv.objid is null then rl.totalav else 0.0 end as assessvalue,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sef else 0.0 end) as currentyear,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sef else 0.0 end) as previousyear,
    sum(case when cv.objid is null  then cri.basicdisc else 0.0 end) as discount,
    sum(case when cv.objid is null  and cri.revperiod in ('current','advance') then cri.sefint else 0.0 end) as penaltycurrent,
    sum(case when cv.objid is null  and cri.revperiod in ('previous','prior') then cri.sefint else 0.0 end) as penaltyprevious,
    
    sum(0) as basicidlecurrent,
    sum(0) as basicidleprevious,
    sum(0) as basicidlediscount,
    sum(0) as basicidlecurrentpenalty,
    sum(0) as basicidlepreviouspenalty,

    sum(0) as shcurrent,
    sum(0) as shprevious,
    sum(0) as shdiscount,
    sum(0) as shcurrentpenalty,
    sum(0) as shpreviouspenalty,

    sum(0) as firecode,
    sum(case when cv.objid is null then cri.sef - cri.sefdisc + cri.sefint else 0.0 end ) as total
  from remittance rem 
    inner join cashreceipt cr on cr.remittanceid = rem.objid 
    left join cashreceipt_void cv on cr.objid = cv.receiptid 
    inner join rptpayment rp on cr.objid = rp.receiptid 
    inner join vw_rptpayment_item cri on rp.objid = cri.parentid
    inner join rptledger rl on rp.refid = rl.objid 
    inner join barangay b on rl.barangayid = b.objid 
    left join district d on b.parentid = d.objid 
    left join city c on d.parentid = c.objid 
    left join municipality m on b.parentid = m.objid 
  where rem.objid=$P{objid} 
    and cri.year > $P{year} 
    and cr.collector_objid like $P{collectorid} 
    ${filter} 
  group by cr.objid, cr.receiptdate, cr.payer_name, cr.receiptno, rl.objid, rl.tdno, b.name, 
            rl.classcode, cv.objid, m.name, c.name, rl.totalav 
   
) t
order by t.municityname, t.idx, t.orno 
