[clearStubout]
UPDATE waterworks_account SET 
	stuboutid=NULL, stuboutindex=NULL 
WHERE objid=$P{objid} 
