[getList]
SELECT l.*, lr.description AS route_description, lr.area AS route_area,
	lc.dtreleased
FROM loanapp l
INNER JOIN loanapp_capture lc ON l.objid = lc.objid
INNER JOIN loan_route lr ON l.route_code = lr.code
WHERE l.borrower_name LIKE $P{searchtext}

[updateLoanApp]
UPDATE loanapp SET 
	appno = $P{appno},
	loanamount = $P{loanamount},
	borrower_name = $P{borrowername},
	route_code = $P{routecode}
WHERE objid = $P{objid}

[updateLoanAppCapture]
UPDATE loanapp_capture SET 
	dtreleased = $P{dtreleased}
WHERE objid = $P{objid}

[updateLoanLedger]
UPDATE loan_ledger SET 
	acctname = $P{borrowername},
	dailydue = $P{dailydue},
	totalprincipal = $P{loanamount},
	dtstarted = $P{dtstarted},
	dtmatured = $P{dtmatured}
WHERE appid = $P{objid}