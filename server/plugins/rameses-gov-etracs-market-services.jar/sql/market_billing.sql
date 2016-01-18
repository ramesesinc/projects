[findCompromise]
SELECT * FROM market_compromise 
WHERE acctid=$P{acctid} AND state='APPROVED'

[findLastPaidMonthYear]
SELECT mpi.imonth, mpi.iyear, ma.rate -  mpi.amtpaid AS balance 
FROM market_payment_item mpi  
INNER JOIN market_payment mp ON mp.objid=mpi.parentid 
INNER JOIN marketaccount ma ON ma.objid=mp.acctid
WHERE ma.objid = $P{acctid}
ORDER BY mpi.iyear DESC, mpi.imonth DESC
