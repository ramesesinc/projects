[getList]
SELECT 
	o.objid, o.strpayor AS paidby, o.strpayoraddress AS paidbyaddress,
	strorno AS receiptno, dtordate AS receiptdate, curamount AS amount,
	o.intvoid AS voided
FROM tblor o
WHERE o.strorno LIKE $P{orno} 
ORDER BY o.strorno 


[findById]
SELECT 
	o.objid, o.strpayor AS paidby, o.strpayoraddress AS paidbyaddress,
	strorno AS receiptno, dtordate AS receiptdate, curamount AS amount,
	o.intvoid AS voided
FROM tblor o
WHERE objid = $P{objid}



[getItems]
SELECT
	ori.id, ori.curamount AS amount, tfa.stracctcode AS item_code, 
	tfa.strdescription AS item_title, strRemarks as remarks, tr.strTDNo as tdno, 
	case when tr.objid is not null then 1 else 0 end as landtax  
FROM tbloritem ori
	INNER JOIN tbltaxfeeaccount tfa on tfa.objid = ori.stracctid
	left JOIN tblrptledger tr on tr.objid = ori.strledgerid 
WHERE ori.parentid = $P{objid} 
order by  tdno, item_title 
 
