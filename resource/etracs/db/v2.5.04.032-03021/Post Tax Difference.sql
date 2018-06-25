-- revised ledger with effectivity < lastyearpaid
drop table if exists tmp_rptledger_with_tax_difference
;

create table tmp_rptledger_with_tax_difference
select 
	rl.objid, rl.faasid, pf.objid as prevfaasid, rlf.objid as rptledgerfaasid, rl.tdno, rl.lastyearpaid, rl.lastqtrpaid, f.effectivityyear, 
	f.txntype_objid, f.totalav, pf.totalav as prevav, f.totalav - pf.totalav as avdifference
from rptledger rl 
inner join rptledgerfaas rlf on rl.faasid = rlf.faasid 
inner join faas_list f on rl.faasid = f.objid 
inner join faas_list pf on f.prevtdno = pf.tdno 
where rl.state = 'approved'
and rl.lastyearpaid >= 2018
and f.effectivityyear < rl.lastyearpaid
and f.ry = 2017
and pf.state = 'cancelled'
and f.totalav > pf.totalav 
and rlf.state = 'approved' 
and not exists(select * from cashreceiptitem_rpt_online where rptledgerfaasid = rlf.objid) 
;


-- year tables 

drop table if exists tmp_years
;

create table tmp_years 
select 2018 as year
union
select 2019 as year
union
select 2020 as year
union
select 2021 as year
union
select 2022 as year
;


-- insert avdifference
insert into rptledger_avdifference(
	objid,
	parent_objid,
	rptledgerfaas_objid,
	year,
	av,
	paid
)
select 
	concat(d.objid, ':', y.year) as objid,
	d.objid as parent_objid,
	d.rptledgerfaasid as rptledgerfaas_objid,
	y.year,
	d.avdifference as av,
	0 as paid
from tmp_rptledger_with_tax_difference d, tmp_years y 
where y.year >= d.effectivityyear 
and y.year <= d.lastyearpaid
;


-- insert difference ledger items 
-- insert basic 
insert into rptledger_item
(
	objid,
	parentid,
	rptledgerfaasid,
	remarks,
	basicav,
	sefav,
	av,
	revtype,
	year,
	amount,
	amtpaid,
	priority,
	taxdifference,
	system
)
select 
	concat(d.objid, ':basic') as objid,
	d.parent_objid as parentid,
	d.rptledgerfaas_objid as rptledgerfaasid,
	concat('Tax Difference ', d.year) as remarks,
	d.av as basicav,
	d.av as sefav,
	d.av,
	'basic' as revtype,
	d.year,
	d.av * 0.01 as amount,
	0 as amtpaid,
	10000 as priority,
	1 as taxdifference,
	0 as system
from rptledger_avdifference d
;



-- insert sef 
insert into rptledger_item
(
	objid,
	parentid,
	rptledgerfaasid,
	remarks,
	basicav,
	sefav,
	av,
	revtype,
	year,
	amount,
	amtpaid,
	priority,
	taxdifference,
	system
)
select 
	concat(d.objid, ':sef') as objid,
	d.parent_objid as parentid,
	d.rptledgerfaas_objid as rptledgerfaasid,
	concat('Tax Difference ', d.year) as remarks,
	d.av as basicav,
	d.av as sefav,
	d.av,
	'sef' as revtype,
	d.year,
	d.av * 0.01 as amount,
	0 as amtpaid,
	10000 as priority,
	1 as taxdifference,
	0 as system
from rptledger_avdifference d
;