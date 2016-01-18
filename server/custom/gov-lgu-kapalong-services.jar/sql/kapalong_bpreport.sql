[getList]
SELECT 
	a.objid, a.appno, a.apptype, a.tradename, a.businessaddress, a.ownername, a.owneraddress, b.bin, b.orgtype, 
	(SELECT permitno FROM business_permit WHERE applicationid=a.objid AND activeyear=a.appyear ORDER BY VERSION DESC LIMIT 1) AS permitno, 

	zinfo.intvalue AS numofemployee, a.dtfiled, 
	
	IFNULL((
		SELECT (CASE WHEN a.apptype='NEW' THEN SUM(decimalvalue) ELSE 0.0 END) 
		FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='CAPITAL' 
	),0) AS capital, 
	IFNULL((
		SELECT (CASE WHEN a.apptype='NEW' THEN SUM(decimalvalue) ELSE 0.0 END) 
		FROM business_application a0 
			INNER JOIN business_application_info bai ON a0.objid=bai.applicationid 
		WHERE a0.parentapplicationid=a.objid AND a0.state IN ('RELEASE','COMPLETED') AND bai.attribute_objid='CAPITAL'
	),0) AS extcapital, 

	IFNULL((
		SELECT (CASE WHEN a.apptype='RENEW' THEN SUM(decimalvalue) ELSE 0.0 END) 
		FROM business_application_info WHERE applicationid=a.objid AND attribute_objid='GROSS' 
	),0) AS gross, 
	IFNULL((
		SELECT (CASE WHEN a.apptype='RENEW' THEN SUM(decimalvalue) ELSE 0.0 END) 
		FROM business_application a0 
			INNER JOIN business_application_info bai ON a0.objid=bai.applicationid 
		WHERE a0.parentapplicationid=a.objid AND a0.state IN ('RELEASE','COMPLETED') AND bai.attribute_objid='GROSS'
	),0) AS extgross, 

	IFNULL((
		SELECT SUM(amount) FROM business_receivable 
		WHERE applicationid=a.objid AND taxfeetype='TAX'
	),0) AS tax,
	IFNULL((
		SELECT SUM(r0.amount) FROM business_application a0 
			INNER JOIN business_receivable r0 ON a0.objid=r0.applicationid 
		WHERE a0.parentapplicationid=a.objid AND r0.taxfeetype='TAX' 
	),0) AS exttax, 

	IFNULL((
		SELECT SUM(amount) FROM business_receivable 
		WHERE applicationid=a.objid AND taxfeetype='REGFEE'
	),0) AS regfee,
	IFNULL((
		SELECT SUM(r0.amount) FROM business_application a0
			INNER JOIN business_receivable r0 ON a0.objid=r0.applicationid 
		WHERE a0.parentapplicationid=a.objid AND r0.taxfeetype='REGFEE' 
	),0) AS extregfee,

	IFNULL(( 
		SELECT SUM(amount) FROM business_receivable br 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid
		WHERE br.applicationid=a.objid AND rm.objid IN ('PLATE','STICKER') 
	),0) AS platestkr, 
	IFNULL(( 
		SELECT SUM(amount) FROM business_application ba  
			INNER JOIN business_receivable br ON ba.objid=br.applicationid 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid 
		WHERE ba.parentapplicationid=a.objid AND rm.objid IN ('PLATE','STICKER') 
	),0) AS extplatestkr,	

	IFNULL(( 
		SELECT SUM(amount) FROM business_receivable br 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid
		WHERE br.applicationid=a.objid AND rm.objid='WM'
	),0) AS wm, 
	IFNULL(( 
		SELECT SUM(amount) FROM business_application ba  
			INNER JOIN business_receivable br ON br.applicationid=ba.objid 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid 
		WHERE ba.parentapplicationid=a.objid AND rm.objid='WM' 
	),0) AS extwm,

	IFNULL(( 
		SELECT SUM(amount) FROM business_receivable br 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid
		WHERE br.applicationid=a.objid AND rm.objid='MP'
	),0) AS mp, 
	IFNULL(( 
		SELECT SUM(amount) FROM business_application ba 
			INNER JOIN business_receivable br ON br.applicationid=ba.objid 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid 
		WHERE ba.parentapplicationid=a.objid AND rm.objid='MP' 
	),0) AS extmp, 

	IFNULL(( 
		SELECT SUM(amount) FROM business_receivable br 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid
		WHERE br.applicationid=a.objid AND rm.objid='OCF'
	),0) AS ocf, 
	IFNULL(( 
		SELECT SUM(amount) FROM business_application ba 
			INNER JOIN business_receivable br ON br.applicationid=ba.objid 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid 
		WHERE ba.parentapplicationid=a.objid AND rm.objid='OCF' 
	),0) AS extocf, 

	IFNULL(( 
		SELECT SUM(amount) FROM business_receivable br 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid
		WHERE br.applicationid=a.objid AND rm.objid='GF'
	),0) AS gf, 
	IFNULL(( 
		SELECT SUM(amount) FROM business_application ba 
			INNER JOIN business_receivable br ON br.applicationid=ba.objid 
			INNER JOIN report_itemaccount_mapping rm ON br.account_objid=rm.accountid 
		WHERE ba.parentapplicationid=a.objid AND rm.objid='GF' 
	),0) AS extgf, 

	IFNULL(( 
		SELECT SUM(bpi.surcharge) FROM business_payment bp 
			INNER JOIN business_payment_item bpi ON bp.objid=bpi.parentid 
		WHERE bp.applicationid=a.objid AND bp.voided=0 
	),0) AS surcharge, 
	IFNULL(( 
		SELECT SUM(bpi.interest) FROM business_payment bp 
			INNER JOIN business_payment_item bpi ON bp.objid=bpi.parentid 
		WHERE bp.applicationid=a.objid AND bp.voided=0 
	),0) AS interest, 

	(SELECT GROUP_CONCAT(refno) FROM business_payment WHERE applicationid=a.objid AND voided=0) AS orno, 
	(SELECT GROUP_CONCAT(p0.refno) FROM business_application a0, business_payment p0 
		WHERE a0.parentapplicationid=a.objid AND a0.objid=p0.applicationid AND p0.voided=0 
	) AS extorno, 
	
	(SELECT GROUP_CONCAT(refdate) FROM business_payment WHERE applicationid=a.objid AND voided=0) AS ordate, 
	(SELECT GROUP_CONCAT(p0.refdate) FROM business_application a0, business_payment p0 
		WHERE a0.parentapplicationid=a.objid AND a0.objid=p0.applicationid AND p0.voided=0 
	) AS extordate,  
	
	(SELECT GROUP_CONCAT(NAME) FROM business_active_lob WHERE businessid=a.business_objid) AS activelobs 
	
FROM business_application a 
	INNER JOIN business b ON a.business_objid=b.objid 
	LEFT JOIN (
		SELECT objid, SUM(IFNULL(intvalue,0)) AS intvalue    
		FROM ( 
			SELECT a.objid, ai.intvalue FROM business_application a 
				INNER JOIN business_application_info ai ON a.objid=ai.applicationid 
			WHERE ai.attribute_objid='NUM_EMPLOYEE' AND ai.activeyear=$P{year} AND a.parentapplicationid IS NULL 
			UNION ALL 
			SELECT a.parentapplicationid AS objid, ai.intvalue FROM business_application a 
				INNER JOIN business_application_info ai ON a.objid=ai.applicationid 
			WHERE ai.attribute_objid='NUM_EMPLOYEE' AND ai.activeyear=$P{year} AND a.parentapplicationid IS NOT NULL 
		)xx 
		GROUP BY objid 
	)zinfo ON a.objid=zinfo.objid 
WHERE a.appyear=$P{year} AND a.state='COMPLETED' 
	AND a.apptype IN ('NEW','RENEW') AND a.parentapplicationid IS NULL 
	AND a.objid IN (SELECT applicationid FROM business_permit WHERE applicationid=a.objid) 
	and MONTH(a.dtfiled) BETWEEN $P{startmonth} and $P{endmonth} 
	AND b.address_objid IN (
		SELECT b.address_objid AS objid FROM(SELECT 1)xx 
		WHERE '%'=$P{barangayid}  
		UNION 
		SELECT objid FROM business_address  
		WHERE objid=b.address_objid 
			AND barangay_objid LIKE $P{barangayid} 
			AND NOT('%'=$P{barangayid}) 
	) 
