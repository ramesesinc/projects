[getList]
SELECT * FROM business_payment WHERE businessid=$P{objid} 
ORDER BY refdate DESC

[getItems]
SELECT * FROM business_payment_item WHERE parentid=$P{objid}

[getApplicationPayments]
SELECT * FROM business_payment 
WHERE applicationid=$P{applicationid} AND voided=0 
ORDER BY refdate 

[updateReceivables]
update r set 
	r.amtpaid = r.amtpaid + bpi.amount,
	r.surcharge = r.surcharge + bpi.surcharge,
	r.interest = r.interest + bpi.interest, 
	r.discount = r.discount + bpi.discount,  
	r.lastqtrpaid = bpi.qtr, 
	r.[partial] = bpi.[partial] 
from business_receivable r, (
		select 
			receivableid, SUM(amount) as amount, SUM(surcharge) as surcharge, 
			SUM(interest) as interest, SUM(discount) as discount, max(qtr) as qtr, 
			MAX([partial]) as [partial] 
		from business_payment_item 
		where parentid=$P{paymentid}  
		group by receivableid 
	)bpi 
where r.objid=bpi.receivableid 

[voidReceivables]
update r set 
	r.amtpaid = r.amtpaid - bpi.amount,
	r.surcharge = r.surcharge - bpi.surcharge,
	r.interest = r.interest - bpi.interest, 
	r.discount = r.discount - bpi.discount
from business_receivable r, (
		select 
			receivableid, SUM(amount) as amount, SUM(surcharge) as surcharge, 
			SUM(interest) as interest, SUM(discount) as discount 
		from business_payment_item 
		where parentid=$P{paymentid}  
		group by receivableid 
	)bpi 
where r.objid=bpi.receivableid 

[removePaymentItems]
delete from business_payment_item where parentid=$P{paymentid} 

[findLastQtrPaid] 
select 
	py.applicationid, py.refno, year(py.refdate) as [year], 
	pyi.qtr, max(pyi.[partial]) as [partial] 
from business_payment py 
	inner join business_payment_item pyi on py.objid=pyi.parentid 
where py.applicationid=$P{applicationid} and voided=0 
group by py.applicationid, py.refno, py.refdate, pyi.qtr 
having max(pyi.[partial])=0 
order by py.refdate desc, pyi.qtr desc 

[findLastQtrPaidWithLob] 
select 
	py.applicationid, py.refno, year(py.refdate) as [year], 
	pyi.qtr, max(pyi.[partial]) as [partial], pyi.lob_objid as lobid 
from business_payment py 
	inner join business_payment_item pyi on py.objid=pyi.parentid 
where py.applicationid=$P{applicationid} and pyi.lob_objid=$P{lobid} and voided=0 
group by py.applicationid, py.refno, py.refdate, pyi.qtr, pyi.lob_objid 
having max(pyi.[partial])=0 
order by py.refdate desc, pyi.qtr desc 
