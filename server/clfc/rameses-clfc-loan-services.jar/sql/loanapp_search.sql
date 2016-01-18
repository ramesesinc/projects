[getList]
SELECT DISTINCT 
	s.*, l.dtcreated, l.createdby, l.appmode   
FROM loanapp_search_index si 
	INNER JOIN loanapp_search s ON si.appid=s.objid 
	INNER JOIN loanapp l ON s.objid=l.objid 
WHERE si.searchtext LIKE $P{searchtext} AND s.state=$P{state} 
ORDER BY si.searchtext 

[getCaptureList]
SELECT DISTINCT 
	s.*, l.dtcreated, l.createdby, l.appmode   
FROM loanapp_search_index si 
	INNER JOIN loanapp_search s ON si.appid=s.objid 
	INNER JOIN loanapp l ON s.objid=l.objid 
WHERE 
	si.searchtext LIKE $P{searchtext} AND 
	s.state=$P{state} AND 
	l.appmode='CAPTURE' 
ORDER BY si.searchtext 
