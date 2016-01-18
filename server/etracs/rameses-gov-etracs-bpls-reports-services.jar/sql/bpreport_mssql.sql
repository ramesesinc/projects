[getBarangayList]
SELECT objid AS barangayid, name AS barangayname FROM barangay ORDER BY name

[getClassificationList]
SELECT objid AS classificationid, name AS classification FROM lobclassification ORDER BY name

[getLobListByAppid]
SELECT * FROM business_application_lob
WHERE businessid=$P{businessid} AND activeyear=$P{activeyear} 
ORDER BY name 

[getBPPaymentsByAppid]
SELECT p.refno, p.refdate, p.amount 
FROM business_application a 
	INNER JOIN business b ON a.business_objid=b.objid 
	INNER JOIN business_payment p ON a.objid=p.applicationid 
WHERE a.business_objid=$P{businessid} 
	AND a.appyear=$P{activeyear} 
	AND b.state NOT IN ('CANCELLED') 
	AND p.voided=0 
ORDER BY p.refno 

[getLobList]
SELECT classification_objid, name FROM lob
ORDER BY classification_objid, name

[getTaxpayerMasterList]
SELECT
	b.objid, ISNULL(ba.apptype, b.apptype) AS apptype, b.activeyear, b.orgtype, b.tradename, 
	a.barangay_name, b.address_text as businessaddress, b.owner_name, b.owner_address_text as owner_address, 
	(SELECT TOP 1 permitno FROM business_permit WHERE applicationid=ba.objid ORDER BY version DESC) as permitno, 
	(SELECT sum(decimalvalue) FROM business_application_info WHERE businessid = b.objid and attribute_objid = 'DECLARED_CAPITAL' and activeyear=b.activeyear) AS capital,
	(SELECT sum(decimalvalue) FROM business_application_info WHERE businessid = b.objid and attribute_objid = 'DECLARED_GROSS' and  activeyear=b.activeyear) AS gross
FROM business b
	LEFT JOIN business_application ba ON (b.objid=ba.business_objid AND ba.appyear=$P{year} AND ba.parentapplicationid IS NULL) 
	LEFT JOIN business_address a ON b.address_objid=a.objid
WHERE b.activeyear=$P{year} ${filter} 
ORDER BY b.tradename 

[getApplicationList]
SELECT xx.*, 
	(CASE WHEN lobid=baselobid AND imale=1 THEN 1 ELSE 0 END) AS malecount, 
	(CASE WHEN lobid=baselobid AND ifemale=1 THEN 1 ELSE 0 END) AS femalecount, 
	(CASE WHEN lobid=baselobid THEN 1 ELSE 0 END) AS businesscount 
FROM ( 
	SELECT 
		a.appno, a.appyear, a.apptype, b.orgtype, ba.barangay_name, 
		b.tradename, b.address_text as businessaddress, b.owner_name, b.owner_address_text as owner_address, 
		bl.lobid, bl.name AS lobname, l.classification_objid, 
		(SELECT TOP 1 dtissued FROM business_permit WHERE applicationid=a.objid ORDER BY version DESC) AS dtissued, 
		ISNULL((
			SELECT SUM(decimalvalue) FROM business_application_info i 
			WHERE i.attribute_objid = 'DECLARED_CAPITAL' AND i.applicationid = a.objid AND i.lob_objid = bl.lobid
		),0.0) AS capital,
		ISNULL((
			SELECT SUM(decimalvalue) FROM business_application_info i 
			WHERE i.attribute_objid = 'DECLARED_GROSS' AND i.applicationid = a.objid AND i.lob_objid = bl.lobid
		),0.0) AS gross, 
		ISNULL((SELECT CASE WHEN gender='M' THEN 1 ELSE 0 END FROM entityindividual WHERE objid=b.owner_objid),0) AS imale, 
		ISNULL((SELECT CASE WHEN gender='F' THEN 1 ELSE 0 END FROM entityindividual WHERE objid=b.owner_objid),0) AS ifemale, 
		(SELECT TOP 1 lobid FROM business_application_lob WHERE applicationid=a.objid) AS baselobid 
	FROM business_application a
		INNER JOIN business b ON a.business_objid = b.objid 
		INNER JOIN business_application_lob bl ON bl.applicationid = a.objid
		INNER JOIN lob l ON l.objid = bl.lobid
		LEFT JOIN business_address ba ON b.address_objid=ba.objid 
	WHERE a.appyear=$P{year} AND a.parentapplicationid IS NULL 
		${filter} 
)xx 
ORDER BY appno, lobname 


[getPermitListByYear]
SELECT 
	bp.*, b.owner_name, b.owner_address_text as owner_address, 
	b.tradename, b.objid as businessid 
FROM ( 
	SELECT permitno, MAX(version) AS version 
	FROM business_permit WHERE activeyear=$P{year} 
	GROUP BY permitno 
)x 
	INNER JOIN business_permit bp ON (x.permitno=bp.permitno AND x.version=bp.version) 
	INNER JOIN business b ON bp.businessid=b.objid 
	LEFT JOIN business_address addr ON b.address_objid=addr.objid  
WHERE ISNULL(addr.barangay_objid,'') LIKE $P{barangayid} 
ORDER BY b.owner_name 


[getLOBCountList]
SELECT  
	a.appyear, l.name, 
	SUM(CASE 
		WHEN a.apptype='NEW' THEN 1 
		WHEN a.apptype='RENEW' AND bl.assessmenttype='NEW' THEN 1 
		ELSE 0 
	END) as newcount, 
	SUM(CASE 
		WHEN a.apptype='RENEW' AND bl.assessmenttype='RENEW' THEN 1 
		ELSE 0 
	END) AS renewcount,
	SUM(CASE 
		WHEN a.apptype='ADDITIONAL' AND bl.assessmenttype='NEW' THEN 1 
		ELSE 0 
	END) AS addlobcount,
	SUM(CASE 
		WHEN bl.assessmenttype='RETIRE' THEN 1 
		ELSE 0 
	END) AS retirecount
FROM business_application a
	INNER JOIN business b ON a.business_objid = b.objid 
	INNER JOIN business_application_lob bl ON bl.applicationid = a.objid 
	INNER JOIN lob l ON l.objid = bl.lobid 
	LEFT JOIN business_address ba ON b.address_objid=ba.objid 
WHERE a.appyear = $P{year} ${filter}  
	AND ba.barangay_objid LIKE $P{barangayid}  
	AND l.classification_objid LIKE $P{classificationid} 
GROUP BY a.appyear, l.name 


[getBusinessTopList]
SELECT 
	ba.objid, ba.appno, ba.apptype, ba.tradename, ba.businessaddress, ba.ownername, ba.owneraddress, g2.amount, 
	(SELECT TOP 1 permitno FROM business_permit WHERE businessid=ba.business_objid ORDER BY version DESC) AS permitno 
FROM ( 
		SELECT DISTINCT TOP ${topsize} SUM(bi.decimalvalue) AS amount 
		FROM business_application ba 
			INNER JOIN business_application_info bi ON ba.objid = bi.applicationid 
			INNER JOIN lob lo ON bi.lob_objid = lo.objid 
		WHERE ba.appyear = $P{year} AND ba.state IN ('RELEASE','COMPLETED') 
			AND ba.apptype = $P{txntype} 
			AND bi.attribute_objid = $P{varname} 
			AND lo.classification_objid LIKE $P{classificationid} 
		GROUP BY ba.objid 
		ORDER BY SUM(bi.decimalvalue) DESC 
	)g1, ( 
		SELECT ba.objid, SUM(bi.decimalvalue) AS amount 
		FROM business_application ba 
			INNER JOIN business_application_info bi ON ba.objid = bi.applicationid 
			INNER JOIN lob lo ON bi.lob_objid = lo.objid 
		WHERE ba.appyear = $P{year} AND ba.state IN ('RELEASE','COMPLETED') 
			AND ba.apptype = $P{txntype} 
			AND bi.attribute_objid = $P{varname} 
			AND lo.classification_objid LIKE $P{classificationid} 
		GROUP BY ba.objid 
	)g2, 
	business_application ba 
WHERE 
	g1.amount = g2.amount AND 
	g2.objid = ba.objid 
ORDER BY 
	g2.amount DESC, ba.txndate 


[getBusinessPermitSummary]
SELECT 
	a.activeyear, a.iqtr, a.imonth, a.strmonth,
	SUM(a.newcount) AS newcount,
	SUM(a.newamount) AS newamount,
	SUM(a.renewcount) AS renewcount,
	SUM(a.renewamount) AS renewamount,
	SUM(a.retirecount) AS retirecount,
	SUM(a.retireamount) AS retireamount,
	SUM(a.total) AS total 
FROM (
	SELECT 
		ba.objid,
		p.activeyear,
		MONTH(p.dtissued) as imonth,
		CASE
			WHEN MONTH(p.dtissued) >= 1 and MONTH(p.dtissued) <= 3 then 1
			WHEN MONTH(p.dtissued) >= 4 and MONTH(p.dtissued) <= 6 then 2
			WHEN MONTH(p.dtissued) >= 7 and MONTH(p.dtissued) <= 9 then 3
			WHEN MONTH(p.dtissued) >= 10 and MONTH(p.dtissued) <= 12 then 4
		END AS iqtr,
		CASE 
			WHEN MONTH(p.dtissued) = 1 THEN 'JANUARY'
			WHEN MONTH(p.dtissued) = 2 THEN 'FEBRUARY'
			WHEN MONTH(p.dtissued) = 3 THEN 'MARCH'
			WHEN MONTH(p.dtissued) = 4 THEN 'APRIL'
			WHEN MONTH(p.dtissued) = 5 THEN 'MAY'
			WHEN MONTH(p.dtissued) = 6 THEN 'JUNE'
			WHEN MONTH(p.dtissued) = 7 THEN 'JULY'
			WHEN MONTH(p.dtissued) = 8 THEN 'AUGUST'
			WHEN MONTH(p.dtissued) = 9 THEN 'SEPTEMBER'
			WHEN MONTH(p.dtissued) = 10 THEN 'OCTOBER'
			WHEN MONTH(p.dtissued) = 11 THEN 'NOVEMBER'
			WHEN MONTH(p.dtissued) = 12 THEN 'DECEMBER'
		END AS strmonth,
		CASE WHEN ba.apptype IN ('NEW','ADDITIONAL') THEN 1 ELSE 0 END AS newcount,
		SUM(CASE WHEN ba.apptype IN ('NEW', 'ADDITIONAL') THEN bp.amount ELSE 0.0 END) AS newamount,
		CASE WHEN ba.apptype = 'RENEW' THEN 1 ELSE 0 END AS renewcount,
		SUM(CASE WHEN ba.apptype = 'RENEW' THEN bp.amount ELSE 0.0 END) AS renewamount,
		CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN 1 ELSE 0 END AS retirecount,
		SUM(CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN bp.amount ELSE 0.0 END) AS retireamount,
		SUM(bp.amount) AS total 
	FROM business_application ba
		INNER JOIN business_permit p ON p.applicationid = ba.objid
		INNER JOIN business_payment bp ON bp.applicationid = ba.objid
	WHERE p.activeyear = $P{year}
		AND bp.voided = 0
	GROUP BY ba.objid, p.activeyear, MONTH(p.dtissued), ba.apptype
) a
GROUP BY a.activeyear, a.iqtr, a.imonth, a.strmonth
ORDER BY a.activeyear, a.iqtr, a.imonth


[getQtrlyPaidBusinessList]
SELECT 
	x.bin, x.businessname, x.businessaddress, 
	SUM(ISNULL(x.q1,0.0)) AS q1, 
	SUM(ISNULL(x.q2,0.0)) AS q2, 
	SUM(ISNULL(x.q3,0.0)) AS q3, 
	SUM(ISNULL(x.q4,0.0)) AS q4, 
	CASE 
		WHEN SUM(ISNULL(x.balance, 0.0)) < 0.0 THEN 0.0 
		ELSE SUM(ISNULL(x.balance, 0.0)) 
	END AS balance 
FROM ( 
		SELECT
		  b.bin, b.businessname, b.address_text AS businessaddress, 
		  (SELECT SUM(amount) FROM business_payment WHERE applicationid=a.objid AND DATEPART(QUARTER,refdate)=1 AND voided=0 GROUP BY applicationid) AS q1,
		  (SELECT SUM(amount) FROM business_payment WHERE applicationid=a.objid AND DATEPART(QUARTER,refdate)=2 AND voided=0 GROUP BY applicationid) AS q2,
		  (SELECT SUM(amount) FROM business_payment WHERE applicationid=a.objid AND DATEPART(QUARTER,refdate)=3 AND voided=0 GROUP BY applicationid) AS q3,
		  (SELECT SUM(amount) FROM business_payment WHERE applicationid=a.objid AND DATEPART(QUARTER,refdate)=4 AND voided=0 GROUP BY applicationid) AS q4,
		  (SELECT SUM(amount-amtpaid-discount) FROM business_receivable WHERE applicationid=a.objid GROUP BY applicationid) AS balance
		FROM business_application a
			INNER JOIN business b ON a.business_objid=b.objid 
		WHERE 
			a.appyear=$P{year} AND a.state IN ('RELEASE', 'COMPLETED') 
)x  
GROUP BY x.bin, x.businessname, x.businessaddress 
ORDER BY bin  


[getEmployerList]
SELECT 
	b.objid, b.bin, b.tradename, b.address_text AS businessaddress, 
	b.owner_name, x.*, (x.nummale + x.numfemale) AS numemployee, 
	(SELECT TOP 1 permitno FROM business_permit WHERE applicationid=x.appid ORDER BY version DESC) AS permitno, 
	CASE 
		WHEN b.orgtype='SING' THEN (SELECT tin FROM entityindividual WHERE objid=b.owner_objid) 
		ELSE (SELECT tin FROM entityjuridical WHERE objid=b.owner_objid) 
	END AS tin, 
	'' AS sss 
FROM ( 
	SELECT appid, apptype, appno, appstate, business_objid, 
		SUM(ISNULL(numfemale, 0)) AS numfemale, 
		SUM(ISNULL(nummale, 0)) AS nummale, 
		SUM(ISNULL(numresident, 0)) AS numresident
	FROM ( 
		SELECT 
			a.objid AS appid, a.apptype, a.appno, a.state as appstate, a.business_objid, 
			(SELECT state FROM business WHERE objid=a.business_objid) AS business_state, 
			(SELECT SUM(intvalue) FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='NUM_EMPLOYEE_FEMALE') AS numfemale,
			(SELECT SUM(intvalue) FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='NUM_EMPLOYEE_MALE') AS nummale,
			(SELECT SUM(intvalue) FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='NUM_EMPLOYEE_RESIDENT') AS numresident
		FROM business_application a 
		WHERE appyear=$P{year} AND state IN ('COMPLETED', 'RELEASE') 
			AND business_objid NOT IN (SELECT business_objid FROM business_application WHERE business_objid=a.business_objid AND apptype='RETIRE') 
			AND apptype NOT IN ('ADDITIONAL') 
		UNION ALL 
		SELECT 
			a.parentapplicationid AS appid, a.apptype, a.appno, a.state as appstate, a.business_objid, 
			(SELECT state FROM business WHERE objid=a.business_objid) AS business_state, 
			(SELECT SUM(intvalue) FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='NUM_EMPLOYEE_FEMALE') AS numfemale,
			(SELECT SUM(intvalue) FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='NUM_EMPLOYEE_MALE') AS nummale,
			(SELECT SUM(intvalue) FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='NUM_EMPLOYEE_RESIDENT') AS numresident
		FROM business_application a 
		WHERE appyear=$P{year} AND state IN ('COMPLETED', 'RELEASE') AND apptype='ADDITIONAL' 
			AND business_objid NOT IN (SELECT business_objid FROM business_application WHERE business_objid=a.business_objid AND apptype='RETIRE') 
	)xx   
	WHERE business_state IN ('ACTIVE','PROCESSING') 
	GROUP BY appid, apptype, appno, appstate, business_objid 
)x 
	INNER JOIN business b ON x.business_objid=b.objid 


[getBusinessesByBarangay]
SELECT 
	b.objid as businessid, b.owner_objid, b.owner_name, b.owner_address_text as owner_address,
	b.tradename, b.address_text as businessaddress, a.appno, a.apptype, a.appyear
FROM business b
INNER JOIN business_application a ON a.business_objid = b.objid
LEFT JOIN business_address ba ON b.address_objid=ba.objid
WHERE 
  ba.barangay_objid LIKE $P{barangayid}
ORDER BY a.appyear, b.owner_name


[getBPCollectionSummary]
SELECT 
	a.activeyear, a.iqtr, a.imonth, a.strmonth,
	SUM(a.newcount) AS newcount,
	SUM(a.newamount) AS newamount,
	SUM(a.renewcount) AS renewcount,
	SUM(a.renewamount) AS renewamount,
	SUM(a.retirecount) AS retirecount,
	SUM(a.retireamount) AS retireamount,
	SUM(a.total) AS total 
FROM (
	SELECT 
		ba.objid,
		bp.appyear AS activeyear,
		MONTH(bp.refdate) AS imonth,
		CASE
			WHEN MONTH(bp.refdate) >= 1 AND MONTH(bp.refdate) <= 3 THEN 1
			WHEN MONTH(bp.refdate) >= 4 AND MONTH(bp.refdate) <= 6 THEN 2
			WHEN MONTH(bp.refdate) >= 7 AND MONTH(bp.refdate) <= 9 THEN 3
			WHEN MONTH(bp.refdate) >= 10 AND MONTH(bp.refdate) <= 12 THEN 4
		END AS iqtr,
		CASE 
			WHEN MONTH(bp.refdate) = 1 THEN 'JANUARY'
			WHEN MONTH(bp.refdate) = 2 THEN 'FEBRUARY'
			WHEN MONTH(bp.refdate) = 3 THEN 'MARCH'
			WHEN MONTH(bp.refdate) = 4 THEN 'APRIL'
			WHEN MONTH(bp.refdate) = 5 THEN 'MAY'
			WHEN MONTH(bp.refdate) = 6 THEN 'JUNE'
			WHEN MONTH(bp.refdate) = 7 THEN 'JULY'
			WHEN MONTH(bp.refdate) = 8 THEN 'AUGUST'
			WHEN MONTH(bp.refdate) = 9 THEN 'SEPTEMBER'
			WHEN MONTH(bp.refdate) = 10 THEN 'OCTOBER'
			WHEN MONTH(bp.refdate) = 11 THEN 'NOVEMBER'
			WHEN MONTH(bp.refdate) = 12 THEN 'DECEMBER'
		END AS strmonth,
		CASE WHEN ba.apptype IN ('NEW','ADDITIONAL') THEN 1 ELSE 0 END AS newcount,
		SUM(CASE WHEN ba.apptype IN ('NEW', 'ADDITIONAL') THEN bp.amount ELSE 0.0 END) AS newamount,
		CASE WHEN ba.apptype = 'RENEW' THEN 1 ELSE 0 END AS renewcount,
		SUM(CASE WHEN ba.apptype = 'RENEW' THEN bp.amount ELSE 0.0 END) AS renewamount,
		CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN 1 ELSE 0 END AS retirecount,
		SUM(CASE WHEN ba.apptype IN ('RETIRE', 'RETIRELOB') THEN bp.amount ELSE 0.0 END) AS retireamount,
		SUM(bp.amount) AS total 
	FROM business_payment bp
		INNER JOIN business_application ba ON bp.applicationid = ba.objid
	WHERE bp.appyear = $P{year} 
	  	AND bp.voided = 0 
	GROUP BY ba.objid, bp.appyear, MONTH(bp.refdate), ba.apptype
)a 
GROUP BY a.activeyear, a.iqtr, a.imonth, a.strmonth 
ORDER BY a.activeyear, a.iqtr, a.imonth 


[getBPTaxFeeTopList]
SELECT 
	ba.objid, ba.appno, ba.apptype, ba.tradename, ba.businessaddress, ba.ownername, ba.owneraddress, g2.amount AS total, 
	(SELECT TOP 1 permitno FROM business_permit WHERE businessid=ba.business_objid ORDER BY version DESC) AS permitno, 
	(SELECT SUM(amount) FROM business_receivable WHERE applicationid=ba.objid AND taxfeetype='TAX') AS tax, 
	(SELECT SUM(amount) FROM business_receivable WHERE applicationid=ba.objid AND taxfeetype='REGFEE') AS regfee, 
	(SELECT SUM(amount) FROM business_receivable WHERE applicationid=ba.objid AND taxfeetype='OTHERCHARGE') AS othercharge 
FROM ( 
		SELECT DISTINCT TOP ${topsize} SUM(r.amount) AS amount 
		FROM business_application ba 
			INNER JOIN business_receivable r ON ba.objid=r.applicationid 
		WHERE ba.appyear=$P{year} AND ba.state IN ('RELEASE','COMPLETED') ${filter} 
		GROUP BY ba.objid 
		ORDER BY SUM(r.amount) DESC 
	)g1, ( 
		SELECT ba.objid, SUM(r.amount) AS amount 
		FROM business_application ba 
			INNER JOIN business_receivable r ON ba.objid=r.applicationid 
		WHERE ba.appyear=$P{year} AND ba.state IN ('RELEASE','COMPLETED') ${filter} 
		GROUP BY ba.objid 
	)g2, 
	business_application ba 
WHERE 
	g1.amount = g2.amount AND 
	g2.objid = ba.objid 
ORDER BY 
	g2.amount DESC, ba.txndate 
