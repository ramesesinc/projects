[getList]
SELECT * FROM loanapp_business WHERE parentid=$P{parentid} 

[removeItems]
DELETE FROM loanapp_business WHERE parentid=$P{parentid} 
