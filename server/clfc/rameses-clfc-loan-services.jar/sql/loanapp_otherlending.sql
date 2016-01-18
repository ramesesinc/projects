[getList]
SELECT * FROM loanapp_otherlending WHERE parentid=$P{parentid} 

[removeItems]
DELETE FROM loanapp_otherlending WHERE parentid=$P{parentid} 