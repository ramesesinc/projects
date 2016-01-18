USE clfc2;

INSERT IGNORE INTO field_collection_route(fieldcollectionid, routecode, totalcount, posted, trackerid) 
SELECT b.parentid AS fieldcollectionid, b.routecode, 
	(
	SELECT COUNT(objid) 
	FROM field_collection_loan fl
	WHERE fl.parentid = b.parentid
	) AS totalcount, 0 AS posted, f.trackerid
FROM (
SELECT DISTINCT routecode, parentid
FROM field_collection_loan fl
) b
INNER JOIN field_collection f ON b.parentid = f.objid;

INSERT INTO `clfc2`.`fieldcollection`
            (`objid`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `branchid`,
             `billdate`,
             `collector_objid`,
             `collector_name`)
SELECT f.objid, f.dtfiled, '' AS author_objid, f.filedby AS author_name, f.branchid,
	f.billdate, f.collector_objid, f.collector_name
FROM field_collection f;

INSERT INTO `clfc2`.`fieldcollection_item`
            (`objid`,
             `parentid`,
             `state`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `item_objid`,
             `item_type`,
             `trackerid`,
             `totalcount`,
             `totalamount`,
             `cbsno`)
SELECT CONCAT(f.objid, r.routecode) AS objid, f.objid AS parentid, f.state, f.dtfiled, '' AS author_objid, f.filedby AS author_name,
	r.routecode AS item_objid, 'route' AS item_type, r.trackerid, 
	(
	SELECT COUNT(objid) 
	FROM field_collection_loan
	WHERE parentid = f.objid
	) AS totalcount, 
	(
	SELECT SUM(fp.payamount)
	FROM field_collection_loan fl
	INNER JOIN field_collection_payment fp ON fl.objid = fp.parentid
	WHERE fl.parentid = f.objid
		AND fl.routecode = r.routecode
	) AS totalamount, NULL AS cbsno
FROM field_collection f
INNER JOIN field_collection_route r ON f.objid = r.fieldcollectionid;

INSERT IGNORE INTO field_collection_route(fieldcollectionid, routecode, totalcount, posted, trackerid) 
SELECT b.parentid AS fieldcollectionid, b.routecode, 
	(
	SELECT COUNT(objid) 
	FROM field_collection_loan fl
	WHERE fl.parentid = b.parentid
	) AS totalcount, 0 AS posted, f.trackerid
FROM (
SELECT DISTINCT routecode, parentid
FROM field_collection_loan fl
) b
INNER JOIN field_collection f ON b.parentid = f.objid;

INSERT INTO `clfc2`.`fieldcollection_loan`
            (`objid`,
             `parentid`,
             `fieldcollectionid`,
             `loanapp_objid`,
             `loanapp_appno`,
             `borrower_objid`,
             `borrower_name`,
             `routecode`,
             `noofpayments`,
             `remarks`)
SELECT f.objid, CONCAT(f.parentid, f.routecode) AS parentid, f.parentid AS fieldcollectionid, f.loanapp_objid, f.loanapp_appno,
	f.borrower_objid, f.borrower_name, f.routecode, f.noofpayments, f.remarks
FROM field_collection_loan f;

SET @rownum:=0;
INSERT INTO `clfc2`.`fieldcollection_payment`
            (`objid`,
             `parentid`,
             `itemid`,
             `fieldcollectionid`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `txnmode`,
             `dtpaid`,
             `refno`,
             `paytype`,
             `amount`,
             `paidby`,
             `payoption`,
             `bank_objid`,
             `bank_name`,
             `check_no`,
             `check_date`,
             `version`)
SELECT f.objid, f.parentid, CONCAT(fl.parentid, fl.routecode) AS itemid, f.fieldcollectionid, f.dtfiled, '' AS author_objid,
	f.filedby AS author_name, 'ONLINE' AS txnmode, f.dtfiled AS dtpaid, f.refno, f.paytype, f.payamount AS amount, f.paidby,
	f.payoption, f.bank_objid, f.bank_name, f.check_no, f.check_date, @rownum:=@rownum+1 AS `version`
FROM field_collection_payment f
INNER JOIN field_collection_loan fl ON f.parentid = fl.objid
