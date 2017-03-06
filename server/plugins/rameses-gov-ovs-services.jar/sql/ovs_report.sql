[getReportSummary]
SELECT 
	intday, SUM(txncount) AS totalcount, 
	SUM(paidcount) AS paidcount, 
	SUM(unpaidcount) AS unpaidcount 
FROM ( 
	SELECT 
		DAY(vt.apprehensiondate) AS intday, 
		(CASE WHEN vt.state='CLOSED' THEN 1 ELSE 0 END) AS paidcount, 
		(CASE WHEN vt.state='OPEN' THEN 1 ELSE 0 END) AS unpaidcount, 
		1 AS txncount 
	FROM ovs_violation_ticket vt 
	WHERE vt.apprehensiondate >= $P{startdate} 
		AND vt.apprehensiondate < $P{enddate} 
		AND vt.state IN ('OPEN','CLOSED')
)xxa 
GROUP BY intday 


[getReportListing]
SELECT 
	vt.apprehensiondate, vt.ticketno, vt.state, 
	vt.violator_name, vt.apprehensionofficer_name 
FROM ovs_violation_ticket vt 
WHERE vt.apprehensiondate >= $P{startdate} 
	AND vt.apprehensiondate < $P{enddate} 
	AND vt.state IN ('OPEN','CLOSED')
ORDER BY vt.apprehensiondate, vt.ticketno  

[getReportListingForEndorsement]
SELECT 
	vt.apprehensiondate, vt.ticketno, 
	vt.violator_name, vt.violator_objid, 
	vt.apprehensionofficer_name
FROM ovs_violation_ticket vt 
WHERE vt.apprehensiondate >= $P{startdate} 
	AND vt.apprehensiondate < $P{enddate} 
	AND vt.state IN ('OPEN') 
ORDER BY vt.apprehensiondate, vt.ticketno  
