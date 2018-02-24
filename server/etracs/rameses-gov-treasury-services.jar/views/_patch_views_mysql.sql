drop view if exists vw_remittance_cashreceipt_af 
;
create view vw_remittance_cashreceipt_af as 
select 
	cr.remittanceid, cr.collector_objid, cr.controlid, 
	min(cr.receiptno) as fromreceiptno, max(cr.receiptno) as toreceiptno, 
	min(cr.series) as fromseries, max(cr.series) as toseries, 
	count(cr.objid) as qty, sum(cr.amount) as amount, 
	0 as qtyvoided, 0.0 as voidamt, 
	0 as qtycancelled, 0.0 as cancelledamt, 
	af.formtype, af.serieslength, af.denomination, 
	cr.formno, afc.stubno, afc.startseries, afc.endseries, afc.prefix, afc.suffix  
from cashreceipt cr
	inner join remittance rem on rem.objid = cr.remittanceid 
	inner join af_control afc ON cr.controlid=afc.objid 
	inner join af ON afc.afid = af.objid 
group by 
	cr.remittanceid, cr.collector_objid, cr.controlid, 
	af.formtype, af.serieslength, af.denomination, 
	cr.formno, afc.stubno, afc.endseries 

union all 

select 
	cr.remittanceid, cr.collector_objid, cr.controlid, 
	null as fromreceiptno, null as toreceiptno, 
	null as fromseries, null as toseries, 
	0 as qty, 0.0 as amount, 
	count(cr.objid) as qtyvoided, sum(cr.amount) as voidamt, 
	0 as qtycancelled, 0.0 as cancelledamt, 
	af.formtype, af.serieslength, af.denomination, 
	cr.formno, afc.stubno, afc.startseries, afc.endseries, afc.prefix, afc.suffix  
from cashreceipt cr 
	inner join cashreceipt_void cv on cv.receiptid = cr.objid 
	inner join remittance rem on rem.objid = cr.remittanceid 
	inner join af_control afc ON cr.controlid=afc.objid 
	inner join af ON afc.afid = af.objid 
group by 
	cr.remittanceid, cr.collector_objid, cr.controlid, 
	af.formtype, af.serieslength, af.denomination, 
	cr.formno, afc.stubno, afc.endseries 

union all 

select 
	cr.remittanceid, cr.collector_objid, cr.controlid, 
	null as fromreceiptno, null as toreceiptno, 
	null as fromseries, null as toseries, 
	0 as qty, 0.0 as amount, 0 as qtyvoided, 0.0 as voidamt, 
	count(cr.objid) as qtycancelled, sum(cr.amount) as cancelledamt, 
	af.formtype, af.serieslength, af.denomination, 
	cr.formno, afc.stubno, afc.startseries, afc.endseries, afc.prefix, afc.suffix  
from cashreceipt cr 
	inner join remittance rem on rem.objid = cr.remittanceid 
	inner join af_control afc ON cr.controlid=afc.objid 
	inner join af ON afc.afid = af.objid 
where cr.state = 'CANCELLED' 
group by 
	cr.remittanceid, cr.collector_objid, cr.controlid, 
	af.formtype, af.serieslength, af.denomination, 
	cr.formno, afc.stubno, afc.endseries 
;


drop view if exists vw_remittance_cashreceipt_afsummary 
;
create view vw_remittance_cashreceipt_afsummary as 
select 
	concat( remittanceid, collector_objid, controlid ) as objid, 
	remittanceid, collector_objid, controlid, 
	min(fromreceiptno) as fromreceiptno, max(toreceiptno) as toreceiptno, 
	min(fromseries) as fromseries, max(toseries) as toseries, 
	sum(qty) as qty, sum(amount) as amount, 
	sum(qtyvoided) as qtyvoided, sum(voidamt) as voidamt, 
	sum(qtycancelled) as qtycancelled, sum(cancelledamt) as cancelledamt, 
	formtype, serieslength, denomination, formno, stubno, 
	startseries, endseries, prefix, suffix 
from vw_remittance_cashreceipt_af 
group by 
	remittanceid, collector_objid, controlid, 
	formtype, serieslength, denomination, formno, stubno, 
	startseries, endseries, prefix, suffix 
;

