USE clfc2;

INSERT INTO `clfc2`.`onlinecollection`
            (`objid`,
             `state`,
             `txndate`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `collector_objid`,
             `collector_name`)
SELECT o.objid, o.state, o.txndate, o.dtcreated AS dtfiled, 'USR-52d9cfac:14370e49ac3:-7fa2' AS author_objid, 
	o.createdby AS author_name, o.collector_objid, o.collector_name
FROM online_collection o;


INSERT INTO `clfc2`.`onlinecollection_collector`
            (`objid`,
             `name`)
SELECT o.objid, o.name
FROM online_collection_collector o;

INSERT INTO `clfc2`.`onlinecollection_detail`
            (`objid`,
             `parentid`,
             `loanapp_objid`,
             `loanapp_appno`,
             `borrower_objid`,
             `borrower_name`,
             `route_code`,
             `route_description`,
             `route_area`,
             `refno`,
             `paytype`,
             `payoption`,
             `amount`,
             `bank_objid`,
             `bank_name`,
             `check_no`,
             `check_date`,
             `dtpaid`,
             `paidby`)
SELECT o.objid, o.parentid, o.loanapp_objid, o.loanapp_appno, o.borrower_objid, o.borrower_name,
	o.route_code, o.route_description, o.route_area, o.refno, o.type AS paytype, 'cash' AS payoption, 
	o.amount, NULL AS bank_objid, NULL AS bank_name, NULL AS check_no, NULL AS check_date, 
	o.dtpaid, o.paidby
FROM online_collection_detail o;