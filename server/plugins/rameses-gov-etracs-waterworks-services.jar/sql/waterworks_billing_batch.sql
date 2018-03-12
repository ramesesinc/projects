[buildBillings]
INSERT INTO waterworks_billing ( 
	objid, state, acctid, batchid, 
	prevreadingdate, prevreading, readingdate, reading, readingmethod, 
	reader_objid, reader_name, volume, amount, month, year,
	discrate, surcharge, interest, otherfees, advance, 
	arrears, unpaidmonths, billed 
) 
SELECT 
	CONCAT(a.objid,'-',br.year,br.month) AS objid, 'DRAFT', a.objid, br.objid, 
	a.lastdateread, a.currentreading, br.readingdate, 0, 'PROCESSING', 
	br.reader_objid, br.reader_name, 0, 0.0, br.month, br.year,
	0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0  
FROM waterworks_billing_batch br 
	INNER JOIN waterworks_account a ON a.zoneid = br.zoneid  
	LEFT JOIN waterworks_consumption c ON (c.acctid = a.objid AND c.year = br.year AND c.month = br.month) 
WHERE br.objid = $P{batchid} 
	AND a.meterid IS NOT NULL 
	AND c.objid IS NULL 


[updateUnpaidAccounts]
update 
	waterworks_billing wb, (  
		select 
			tmp2.batchid, tmp2.acctid, 
			sum(tmp2.arrears) as arrears, 
			sum(tmp2.unpaidmonths) as unpaidmonths
		from ( 
			select b.batchid, b.acctid, sum(c.amount)-sum(c.amtpaid) as arrears, 0 as unpaidmonths  
			from waterworks_billing_batch bb 
				inner join waterworks_billing b on b.batchid = bb.objid 
				inner join waterworks_account a on a.objid = b.acctid 
				inner join waterworks_consumption c on c.acctid = a.objid 
			where bb.objid = $P{batchid}  
				and a.zoneid = bb.zoneid 
			group by b.batchid, b.acctid 
			having (sum(c.amount)-sum(c.amtpaid)) > 0 

			union all 

			select tmp1.batchid, tmp1.acctid, 0.0 as arrears, tmp1.unpaidmonths 
			from ( 
				select b.batchid, b.acctid, c.year, c.month, count(*) as unpaidmonths  
				from waterworks_billing_batch bb 
					inner join waterworks_billing b on b.batchid = bb.objid 
					inner join waterworks_account a on a.objid = b.acctid 
					inner join waterworks_consumption c on c.acctid = a.objid 
				where bb.objid = $P{batchid}  
					and a.zoneid = bb.zoneid 
					and (c.amount - c.amtpaid) > 0 
				group by b.batchid, b.acctid, c.year, c.month
			)tmp1
		)tmp2 
		group by tmp2.batchid, tmp2.acctid 
	)zz 
set 
	wb.arrears = zz.arrears, 
	wb.unpaidmonths = zz.unpaidmonths 
where wb.batchid = zz.batchid 
	and wb.acctid = zz.acctid 


[findBilledStatus]
select tmp1.*, (totalcount-billedcount) as balance 
from ( 
	select 
		(select count(*) from waterworks_billing where batchid = $P{batchid}) as totalcount, 
		(select count(*) from waterworks_billing where batchid = $P{batchid} and billed=1) as billedcount  
)tmp1 


[postConsumption]
INSERT INTO waterworks_consumption ( 
	objid, acctid, batchid, duedate, discdate, 
	prevreading, reading, readingmethod, reader_objid, reader_name, 
	volume, amount, amtpaid, MONTH, YEAR, readingdate, state 
) 
SELECT 
	wb.objid, wb.acctid, wb.batchid, bb.duedate, bb.discdate, 
	wb.prevreading, wb.reading, wb.readingmethod, wb.reader_objid, wb.reader_name, 
	wb.volume, wb.amount, 0.0 AS amtpaid, wb.month, wb.year, bb.readingdate, 'POSTED' AS state 
FROM waterworks_billing_batch bb 
	INNER JOIN waterworks_billing wb ON wb.batchid = bb.objid 
WHERE bb.objid = $P{batchid} 
