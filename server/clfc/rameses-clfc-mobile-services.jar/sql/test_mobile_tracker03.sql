[getList]
SELECT * FROM test_mobile_tracker 
WHERE sessionid=$P{sessionid} ${filter} 
ORDER BY txndate 

[log]
INSERT INTO test_mobile_tracker 
	(objid, txndate, sessionid, lng, lat, accuracy) 
VALUES 
	($P{objid}, $P{txndate}, $P{sessionid}, $P{lng}, $P{lat}, $P{accuracy}) 
