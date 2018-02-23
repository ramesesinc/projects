DROP VIEW IF EXISTS vw_af_control_detail 
;
CREATE VIEW vw_af_control_detail AS 
SELECT 
	afd.*, 
	afc.afid, afc.unit, af.formtype, af.denomination, af.serieslength, 
	afu.qty, afu.saleprice, afc.stubno, afc.prefix, afc.suffix, afc.cost, afc.state AS controlstate, afc.batchno, 
	afc.startseries, afc.endseries, afc.currentseries,
	(afc.endseries-afc.currentseries+1) AS qtybalance,	
	CASE 
		WHEN afd.issuedstartseries IS NOT NULL THEN afd.issuedstartseries 
		WHEN afd.beginstartseries IS NOT NULL THEN afd.beginstartseries 
		WHEN afd.receivedstartseries IS NOT NULL THEN afd.receivedstartseries 
		WHEN afd.endingstartseries IS NOT NULL THEN afd.endingstartseries 
	END AS _startseries, 
	CASE 
		WHEN afd.issuedstartseries IS NOT NULL THEN afd.issuedendseries 
		WHEN afd.beginstartseries IS NOT NULL THEN afd.beginendseries 
		WHEN afd.receivedstartseries IS NOT NULL THEN afd.receivedendseries 
		WHEN afd.endingstartseries IS NOT NULL THEN afd.endingendseries 
	END AS _endseries  	
FROM af_control_detail afd 
	INNER JOIN af_control afc ON afc.objid = afd.controlid 
	INNER JOIN af ON af.objid = afc.afid 
	INNER JOIN afunit afu ON (afu.itemid=af.objid AND afu.unit=afc.unit) 
;
