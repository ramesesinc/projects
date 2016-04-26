[clearStubout]
UPDATE waterworks_account SET 
	stuboutid=NULL, stuboutindex=NULL 
WHERE 
	objid=$P{objid} 


[updateReading]
UPDATE waterworks_account SET 
	lastdateread=$P{lastdateread}, 
	currentreading=$P{currentreading}, 
	billingcycleid=$P{billingcycleid} 
WHERE 
	objid=$P{objid} 
