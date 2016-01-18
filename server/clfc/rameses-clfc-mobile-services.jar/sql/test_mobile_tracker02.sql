[log]
INSERT INTO test_mobile_tracker 
	(objid, txndate, sessionid, lng, lat) 
VALUES 
	($P{objid}, $P{txndate}, $P{sessionid}, $P{lng}, $P{lat}) 

[getList]
SELECT * FROM test_mobile_tracker WHERE sessionid=$P{sessionid} ORDER BY txndate 
