[getPaymentsForPosting]
select top 5 * from zz_etracs_afor56_forposting order by ordate, orno 

[moveToPosted]
insert into zz_etracs_afor56_posted (orno, ordate, tdn, remarks)
values($P{orno}, $P{ordate}, $P{tdn}, $P{remarks})

[deleteFromForPosting]
delete from zz_etracs_afor56_forposting 
where orno = $P{orno}
and ordate = $P{ordate}
and tdn = $P{tdn}

[getPaymentInfos]
select 
	tdn,
	receiptno,
	receiptdate,
	paidby_name,
	paidby_address,
	postedby,
	postedbytitle,
	dtposted,
	min(revperiod) as fromperiod,
	min(fromyear) as fromyear,
	min(fromqtr) as fromqtr,
	max(revperiod) as toperiod,
	max(toyear) as toyear,
	max(toqtr) as toqtr,
	sum(basic) as basic, 
	sum(basicpen) as basicpen,
	sum(basicdisc) as basicdisc,
	sum(sef) as sef, 
	sum(sefpen) as sefpen,
	sum(sefdisc) as sefdisc,
	sum(amount) as amount,
	collectingagency,
	voided
from  vw_etracs_payment_detail
where receiptno = $P{orno}
  and receiptdate = $P{ordate}
  and tdn = $P{tdn}
group by 
	tdn,
	receiptno,
	receiptdate,
	paidby_name,
	paidby_address,
	postedby,
	postedbytitle,
	dtposted,
	collectingagency,
	voided
