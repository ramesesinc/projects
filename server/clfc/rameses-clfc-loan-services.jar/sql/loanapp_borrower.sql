[findPrincipalBorrower]
SELECT lb.type, lb.relation, b.* 
FROM loanapp_borrower lb 
	INNER JOIN borrower b ON lb.borrowerid=b.objid 
WHERE lb.parentid=$P{parentid} AND lb.type='PRINCIPAL'  

[removePrincipalBorrower]
DELETE FROM loanapp_borrower WHERE parentid=$P{parentid} AND type='PRINCIPAL' 

[findJointBorrower]
SELECT * FROM loanapp_borrower 
WHERE parentid = $P{objid} 
AND `type` = 'JOINT'

[getJointBorrowers]
SELECT b.*, 
	lb.objid AS refid, lb.type, lb.relation, lb.principalid, lb.relaterid 
FROM loanapp_borrower lb 
	INNER JOIN borrower b ON lb.borrowerid=b.objid 
WHERE lb.parentid=$P{parentid} AND lb.type='JOINT' 

[removeJointBorrowers]
DELETE FROM loanapp_borrower WHERE parentid=$P{parentid} AND type='JOINT' 

[getComakers]
SELECT lb.type, lb.relation, b.* 
FROM loanapp_borrower lb 
	INNER JOIN borrower b ON lb.borrowerid=b.objid 
WHERE lb.parentid=$P{parentid} AND lb.type='COMAKER' 

[removeComakers]
DELETE FROM loanapp_borrower WHERE parentid=$P{parentid} AND type='COMAKER' 

[getBorrowerNames]
SELECT 
	b.objid, b.name, lb.type, 
	CASE WHEN lb.type='PRINCIPAL' THEN 0 ELSE 1 END AS orderindex 
FROM loanapp_borrower lb 
	INNER JOIN borrower b ON lb.borrowerid = b.objid 
WHERE lb.parentid = $P{parentid} AND lb.type IN ('PRINCIPAL','JOINT') 
ORDER BY orderindex 

[removeItems]
DELETE FROM loanapp_borrower WHERE parentid=$P{parentid} 

[findNextto]
SELECT b.objid, bi.lastname, bi.firstname, b.address 
FROM loanapp_borrower_nextto lbn
INNER JOIN borrower b ON lbn.nexttoid = b.objid
INNER JOIN borrowerindividual bi ON b.objid = bi.objid
WHERE lbn.borrowerid=$P{borrowerid}

[getList]
SELECT b.objid, b.lastname, b.firstname, b.address, lb.borrowername
FROM loanapp_borrower lb
INNER JOIN borrower b ON lb.borrowerid=b.objid
INNER JOIN loanapp la ON lb.parentid=la.objid
WHERE b.lastname LIKE $P{searchtext}
	AND la.route_code=$P{routecode}
	AND lb.borrowerid <> $P{borrowerid}

[getListByNames]
SELECT b.objid, b.lastname, b.firstname, b.address, lb.borrowername
FROM loanapp_borrower lb
INNER JOIN borrower b ON lb.borrowerid=b.objid
INNER JOIN loanapp la ON lb.parentid=la.objid
WHERE b.lastname LIKE $P{searchtext}
	AND la.route_code=$P{routecode}
	AND lb.borrowerid <> $P{borrowerid}

UNION

SELECT b.objid, b.lastname, b.firstname, b.address, lb.borrowername
FROM loanapp_borrower lb
INNER JOIN borrower b ON lb.borrowerid=b.objid
INNER JOIN loanapp la ON lb.parentid=la.objid
WHERE b.firstname LIKE $P{searchtext}
	AND la.route_code=$P{routecode}
	AND lb.borrowerid <> $P{borrowerid}
