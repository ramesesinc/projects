[findAppInfo]
SELECT la.objid, la.appno, la.borrower_name, b.address AS borrower_address,
	lr.area AS route_area, lr.description AS route_description, 
	lac.dtreleased
FROM loanapp la
INNER JOIN loan_route lr ON la.route_code=lr.code
INNER JOIN borrower b ON la.borrower_objid=b.objid
LEFT JOIN loanapp_capture lac ON la.objid=lac.objid
WHERE la.objid = $P{objid}

