[findByObjid]
SELECT l.*, 
	lr.description AS route_description, 
	lr.area AS route_area 
FROM loanapp l  
	LEFT JOIN loan_route lr ON l.route_code=lr.code 
WHERE l.objid=$P{objid} 

[findByAppno]
SELECT l.*, 
	lr.description AS route_description, 
	lr.area AS route_area 
FROM loanapp l  
	LEFT JOIN loan_route lr ON l.route_code=lr.code 
WHERE l.appno = $P{appno} 

[changeState]
UPDATE loanapp SET state=$P{state} WHERE objid=$P{objid} 

[findCurrentLoanForBorrower]
SELECT 
	l.objid, l.state, st.level, l.dtcreated, l.appno, 
	lb.borrowerid, lb.borrowername, lb.type AS borrowertype 
FROM loanapp_borrower lb 
	INNER JOIN loanapp l ON lb.parentid=l.objid 
	INNER JOIN loan_state_type st ON l.state=st.name AND st.level < 10 
WHERE 
	lb.borrowerid = $P{borrowerid} AND 
	lb.type IN ('PRINCIPAL','JOINT') AND
	l.loantype <> 'BRANCH'
ORDER BY l.dtcreated DESC 

[getListByBorrowerid]
SELECT a.* FROM loanapp a
WHERE a.borrower_objid = $P{borrowerid}

[getLookupList]
SELECT a.* FROM loanapp a
WHERE a.borrower_name LIKE $P{searchtext}
ORDER BY a.borrower_name, a.appno DESC

[getLookupListByState]
SELECT a.* FROM loanapp a
WHERE a.borrower_name LIKE $P{searchtext}
	AND a.state = $P{state}
ORDER BY a.borrower_name, a.appno DESC

[getBorrowers] 
SELECT 
	lb.borrowerid, lb.type as borrowertype,  
	b.lastname, b.firstname, b.middlename 
FROM loanapp_borrower lb 
	INNER JOIN borrower b ON lb.borrowerid=b.objid 
WHERE lb.parentid=$P{parentid} 

[getQualifiedIndexNames]
SELECT lb.borrowername FROM loanapp_borrower lb 
WHERE lb.parentid=$P{parentid} AND lb.type IN ('principal','joint') 

[findBorrower]
SELECT * FROM loanapp_borrower WHERE parentid=$P{parentid} AND type=$P{type}  

[removeBorrowerIndices]
DELETE FROM loanapp_search_index 
WHERE appid=$P{appid} and borrowerid IS NOT NULL 

[removeBorrowerIndex]
DELETE FROM loanapp_search_index
WHERE appid=$P{appid} AND borrowerid=$P{borrowerid} 

[findBorrowerIndex]
SELECT * FROM loanapp_search_index
WHERE appid = $P{appid} AND borrowerid = $P{borrowerid}

[findSearchIndex]
SELECT * FROM loanapp_search WHERE objid=$P{objid} 

[removeSearchIndex]
DELETE FROM loanapp_search WHERE objid=$P{objid} 

[findPrincipalBorrower]
SELECT b.* 
FROM loanapp_borrower lb 
	INNER JOIN borrower b ON lb.principalid=b.objid 
WHERE lb.parentid=$P{parentid} AND lb.principalid=$P{principalid} 

[updatePrincipalBorrower] 
UPDATE loanapp_borrower SET 
	principalid=$P{principalid}, relaterid=null, relation=null 
WHERE parentid=$P{parentid} AND type='PRINCIPAL' 

[updatePrincipalBorrowerIndex]
UPDATE loanapp_borrower_index SET 
	borrowerid=$P{borrowerid}, borrowername=$P{borrowername} 
WHERE loanappid=$P{loanappid} AND borrowertype='PRINCIPAL' 

[getBorrowerNames]
SELECT b.objid, b.lastname, b.firstname, b.middlename 
FROM loanapp_borrower lb  
	INNER JOIN borrower b ON lb.principalid=b.objid  
WHERE lb.parentid=$P{parentid} AND lb.type=$P{type} 

[updateFullBorrowerName]
UPDATE loanapp_search_index SET 
	fullborrowername=$P{fullborrowername} 
WHERE objid=$P{objid} 

[getBusinesses]
SELECT * FROM loanapp_business WHERE parentid=$P{parentid} 

[findRecommendation] 
SELECT * FROM loanapp_recommendation WHERE objid=$P{objid}  

[removeRecommendation]
DELETE FROM loanapp_recommendation WHERE objid=$P{objid}  

[findNew]
SELECT * FROM loanapp_new WHERE objid=$P{objid}  

[findRenew]
SELECT * FROM loanapp_renew WHERE objid=$P{objid}  

[findCapture]
SELECT * FROM loanapp_capture WHERE objid=$P{objid}  

[getBorrowerList]
SELECT * FROM loanapp_borrower WHERE parentid=$P{parentid} 

[getBusinessList]
SELECT * FROM loanapp_business WHERE parentid=$P{parentid} 

[getOtherLendingList]
SELECT * FROM loanapp_otherlending WHERE parentid=$P{parentid} 

[getCollateralApplianceList]
SELECT * FROM loanapp_collateral_appliance WHERE parentid=$P{parentid} 

[getCollateralPropertyList]
SELECT * FROM loanapp_collateral_property WHERE parentid=$P{parentid} 

[getCollateralVehicleList]
SELECT * FROM loanapp_collateral_vehicle WHERE parentid=$P{parentid} 

[getCollateralOtherList]
SELECT * FROM loanapp_collateral_other WHERE objid=$P{objid} 

[getExpiredListReportData]
SELECT q.*,
	CASE WHEN lb.objid IS NOT NULL THEN
		CONCAT(q.bname, ' AND ', lb.borrowername)
	ELSE
		q.bname
	END AS borrower_name
FROM (
	SELECT a.borrower_name AS bname, a.loanamount, a.appno, c.dtreleased, l.dtmatured, l.state, l.balance,
		(SELECT objid FROM loanapp_borrower WHERE parentid = l.appid AND `type` = 'JOINT' LIMIT 1) AS jointid
	FROM loan_ledger l
	INNER JOIN loanapp a ON l.appid = a.objid
	INNER JOIN loanapp_capture c ON a.objid = c.objid
	WHERE l.dtmatured BETWEEN $P{startdate} AND $P{enddate}
	ORDER BY l.dtmatured, a.borrower_name
) q LEFT JOIN loanapp_borrower lb ON q.jointid = lb.objid

[xgetExpiredListReportData]
SELECT a.borrower_name, a.loanamount, a.appno, c.dtreleased, l.dtmatured, l.state, l.balance
FROM loan_ledger l
INNER JOIN loanapp a ON l.appid = a.objid
INNER JOIN loanapp_capture c ON a.objid = c.objid
WHERE l.dtmatured BETWEEN $P{startdate} AND $P{enddate}
ORDER BY l.dtmatured, a.borrower_name
