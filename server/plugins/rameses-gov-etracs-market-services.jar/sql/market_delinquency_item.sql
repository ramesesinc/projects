[insertRecords]
INSERT INTO market_delinquency_item
(objid, parentid, acctid, months, days, rate, extrate, surcharge, interest, lastdatepaid)

SELECT 
   CONCAT(objid, $P{parentid} ), $P{parentid}, objid, 0, 0, 0, 0, 0, 0, IFNULL(lastdatepaid,startdate) 
FROM market_account WHERE IFNULL(lastdatepaid,startdate) < $P{period}   