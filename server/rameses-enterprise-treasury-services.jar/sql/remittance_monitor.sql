[getUnremittedList]
select xx.*, 
	rem.txnno as lastremittanceno, 
	rem.remittancedate as lastremittancedate 
from ( 
	select xx.*, ( 
			select max(dtposted) from remittance 
			where collector_objid=xx.collector_objid
		) as maxdtposted 
	from ( 
		select 
			cr.collector_objid, cr.collector_name, 
			sum(cr.amount) as amount  
		from cashreceipt cr 
		where year(cr.receiptdate) >= $P{year} 
			and cr.objid not in ( 
				select objid from remittance_cashreceipt 
				where objid=cr.objid 
			) 
			and cr.objid not in ( 
				select objid from cashreceipt_void 
				where objid=cr.objid 
			)
			and cr.state IN ('POSTED') 
		group by 
			cr.collector_objid, cr.collector_name 
	)xx 
)xx 
	left join remittance rem on (rem.collector_objid=xx.collector_objid and rem.dtposted=xx.maxdtposted) 
order by 
	xx.collector_name 