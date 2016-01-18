[findByRemittanceidAndState]
SELECT s.* 
FROM collection_remittance_sendback s
WHERE s.remittanceid = $P{remittanceid}
	and s.state = $P{state}

[findSendBackByRemittanceid]
SELECT * FROM collection_remittance_sendback
WHERE remittanceid = $P{remittanceid}

[changeState]
UPDATE collection_remittance_sendback SET state = $P{state}
WHERE objid = $P{objid}