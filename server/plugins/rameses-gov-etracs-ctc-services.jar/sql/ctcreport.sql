[getAbstractCTCIndividual]
SELECT
	r.txnno,
	r.dtposted as txndate,
	c.receiptno AS orno,
	CASE WHEN cv.objid IS null THEN c.receiptdate ELSE NULL END AS ordate,
	CASE WHEN cv.objid IS null THEN c.paidby ELSE '*** VOIDED ***' END AS paidby,
	CASE WHEN cv.objid IS null THEN c.amount ELSE 0.0 END AS amount,
	r.collector_name as collectorname,
	r.collector_title as  collectortitle,
	r.liquidatingofficer_name as liquidatingofficername,
	r.liquidatingofficer_title as liquidatingofficertitle,
	CASE WHEN cv.objid IS null THEN 0 else 1 end as voided,
	ctci.basictax as basic, 
	(ctci.salarytax + ctci.propertyincometax + ctci.businessgrosstax + ctci.additionaltax) as additional,
	ctci.interestdue as penalty,
	ctci.amountdue as total 
FROM remittance r
	INNER JOIN remittance_cashreceipt rc ON rc.remittanceid = r.objid
	INNER JOIN cashreceipt  c on c.objid = rc.objid 
	INNER JOIN cashreceipt_ctc_individual ctci on ctci.objid = c.objid 
	LEFT JOIN cashreceipt_void cv on cv.receiptid = c.objid 
WHERE r.objid = $P{objid}
  AND c.formno = '0016' 