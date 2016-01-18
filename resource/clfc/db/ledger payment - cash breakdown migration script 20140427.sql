USE clfc2;

INSERT INTO `clfc2`.`loan_ledger_payment`
	    (`objid`,
	     `parentid`,
	     `refno`,
	     `txndate`,
	     `amount`)
SELECT objid, parentid, refno, dtpaid, amtpaid AS amount
FROM loan_ledger_detail
WHERE amtpaid > 0 AND state = 'RECEIVED';	

INSERT INTO `clfc2`.`collection_cashbreakdown`
            (`objid`,
             `dtfiled`,
             `filedby`,
             `collection_objid`,
             `collection_type`,
             `group_objid`,
             `group_type`)
SELECT objid, NOW() AS dtfiled, 'SYSTEM' AS filedby, objid AS collection_objid,
	'ONLINE' AS collection_type, objid AS group_objd, 'online' AS group_type
FROM online_collection;

INSERT INTO `clfc2`.`collection_cashbreakdown_detail`
            (`objid`,
             `parentid`,
             `denomination`,
             `qty`,
             `amount`)
SELECT objid, parentid, denomination, qty, amount
FROM online_collection_cashbreakdown;