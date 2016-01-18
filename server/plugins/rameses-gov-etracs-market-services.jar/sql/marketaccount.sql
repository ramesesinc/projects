[getList]
SELECT b.* FROM 
(SELECT m.*, 
	ru.code AS rentalunit_code,
	ru.name AS rentalunit_name,
	mk.name AS rentalunit_market_name,
	ms.name AS rentalunit_section_name,
	ms.title AS rentalunit_section_title
FROM marketaccount m
INNER JOIN market_rentalunit ru ON m.rentalunit_objid=ru.objid
INNER JOIN market mk ON ru.marketid=mk.objid
INNER JOIN marketsection ms ON ru.section_objid=ms.objid
) b
where b.owner_name like $P{searchtext} 

[approve]
UPDATE marketaccount SET state='APPROVED' WHERE objid=$P{objid}

[findByAcctno]
SELECT * FROM marketaccount WHERE acctno=$P{acctno}

[findLastYearMonthPaid]
SELECT TOP 1 a.*, a.rate-a.amtpaid AS balance 
FROM
(SELECT mi.imonth, mi.iyear, 
SUM(mi.amtpaid) AS amtpaid, 
SUM(mi.surchargepaid) AS surchargepaid,  
SUM(mi.interestpaid) AS interestpaid,
MAX(m.rate) AS rate
FROM market_payment_item mi 
INNER JOIN market_payment mp ON mi.parentid=mp.objid
INNER JOIN marketaccount m ON mp.acctid=m.objid
WHERE mp.acctid = $P{acctid}
GROUP BY mp.acctid, mi.iyear, mi.imonth) a
ORDER BY a.iyear DESC, a.imonth DESC