USE clfc2;

INSERT INTO `clfc2`.`ledger_billing`
            (`objid`,
             `dtcreated`,
             `author_objid`,
             `author_name`,
             `collector_objid`,
             `collector_name`,
             `billdate`,
             `branchid`,
             `totalfordownload`,
             `totaldownloaded`,
             `totalposted`,
             `remarks`)
SELECT b.objid, b.dtcreated, '' AS author_objid, b.createdby AS author_name, b.collector_objid,
	b.collector_name, b.billdate, b.branchid, b.totalfordownload, b.totaldownloaded, b.totalposted,
	b.remarks
FROM loan_ledger_billing b;

INSERT INTO `clfc2`.`ledger_billing_item`
            (`objid`,
             `parentid`,
             `state`,
             `dtmodified`,
             `modifiedby_objid`,
             `modifiedby_name`,
             `item_objid`,
             `item_type`,
             `dtcancelled`,
             `cancelledby_objid`,
             `cancelledby_name`)
SELECT CONCAT(b.objid, r.routecode) AS objid, b.objid AS parentid, b.state, b.dtmodified, '' AS modifiedby_objid, 
	b.modifiedby AS modifiedby_name,r.routecode AS item_objid, 'route' AS item_type, b.dtcancelled, '' AS cancelledby_objid, 
	b.cancelledby AS cancelledby_name
FROM loan_ledger_billing b
INNER JOIN loan_ledger_billing_route r ON b.objid = r.billingid;

INSERT INTO `clfc2`.`ledger_billing_assist`
            (`objid`,
             `dtcreated`,
             `author_objid`,
             `author_name`,
             `dtmodified`,
             `modifiedby_objid`,
             `modifiedby_name`,
             `prevbillingid`,
             `prevcollector_objid`,
             `prevcollector_name`)
SELECT CONCAT(lb.objid, r.routecode) AS objid, lb.dtcreated, '' AS author_objid, lb.createdby AS author_name, 
	lb.dtmodified, '' AS modifiedby_objid, lb.modifiedby AS modifiedby_name, CONCAT(b.parentid, r.routecode) AS prevbillingid, 
	b.subcollector_objid AS prevcollector_objid, b.subcollector_name AS prevcollector_name
FROM loan_ledger_subbilling b
INNER JOIN loan_ledger_billing lb ON b.objid = lb.objid
INNER JOIN loan_ledger_billing_route r ON lb.objid = r.billingid;

INSERT INTO `clfc2`.`ledger_billing_detail`
            (`objid`,
             `parentid`,
             `billingid`,
             `ledgerid`,
             `route_code`,
             `acctid`,
             `acctname`,
             `loanamount`,
             `appno`,
             `dailydue`,
             `amountdue`,
             `overpaymentamount`,
             `balance`,
             `refno`,
             `txndate`,
             `dtmatured`,
             `isfirstbill`,
             `paymentmethod`,
             `loandate`,
             `term`,
             `loanappid`,
             `homeaddress`,
             `collectionaddress`,
             `penalty`,
             `interest`,
             `others`)
SELECT b.objid, CONCAT(b.parentid, b.route_code) AS parentid, b.parentid AS billingid, b.ledgerid, b.route_code, b.acctid, b.acctname,
	b.loanamount, b.appno, b.dailydue, b.amountdue, b.overpaymentamount, b.balance, b.refno, b.txndate, b.dtmatured, b.isfirstbill,
	b.paymentmethod, b.loandate, b.term, b.loanappid, b.homeaddress, b.collectionaddress, b.penalty, b.interest, b.others
FROM loan_ledger_billing_detail b;


-- Special collection billing

INSERT INTO `clfc2`.`ledger_billing_item`
            (`objid`,
             `parentid`,
             `state`,
             `dtmodified`,
             `modifiedby_objid`,
             `modifiedby_name`,
             `item_objid`,
             `item_type`,
             `dtcancelled`,
             `cancelledby_objid`,
             `cancelledby_name`)
SELECT s.objid, s.billingid AS parentid, s.state, s.dtrequested AS dtmodified, '' AS modifiedby_objid,
	s.requestedby AS modifiedby_name, s.objid AS item_objid, 'special' AS item_type, NULL AS dtcancelled,
	NULL AS cancelledby_objid, NULL AS cancelledby_name
FROM special_collection s
LEFT JOIN followup_collection f ON s.objid = f.objid
WHERE f.objid IS NULL;

DELETE FROM ledger_billing_detail
WHERE objid IN (SELECT billingdetailid FROM special_collection_loan);

INSERT INTO `clfc2`.`ledger_billing_detail`
            (`objid`,
             `parentid`,
             `billingid`,
             `ledgerid`,
             `route_code`,
             `acctid`,
             `acctname`,
             `loanamount`,
             `appno`,
             `dailydue`,
             `amountdue`,
             `overpaymentamount`,
             `balance`,
             `refno`,
             `txndate`,
             `dtmatured`,
             `isfirstbill`,
             `paymentmethod`,
             `loandate`,
             `term`,
             `loanappid`,
             `homeaddress`,
             `collectionaddress`,
             `penalty`,
             `interest`,
             `others`)
SELECT l.objid, s.parentid, l.parentid AS billingid, l.ledgerid, l.route_code, l.acctid, l.acctname, l.loanamount, l.appno,
	l.dailydue, l.amountdue, l.overpaymentamount, l.balance, l.refno, l.txndate, l.dtmatured, l.isfirstbill, l.paymentmethod,
	l.loandate, l.term, l.loanappid, l.homeaddress, l.collectionaddress, l.penalty, l.interest, l.others
FROM special_collection_loan s
INNER JOIN loan_ledger_billing_detail l ON s.billingdetailid = l.objid
LEFT JOIN followup_collection f ON s.parentid = f.objid
WHERE f.objid IS NULL;


-- Follow-up collection billing

INSERT INTO `clfc2`.`ledger_billing_item`
            (`objid`,
             `parentid`,
             `state`,
             `dtmodified`,
             `modifiedby_objid`,
             `modifiedby_name`,
             `item_objid`,
             `item_type`,
             `dtcancelled`,
             `cancelledby_objid`,
             `cancelledby_name`)
SELECT s.objid, s.billingid AS parentid, s.state, s.dtrequested AS dtmodified, '' AS modifiedby_objid,
	s.requestedby AS modifiedby_name, s.objid AS item_objid, 'followup' AS item_type, NULL AS dtcancelled,
	NULL AS cancelledby_objid, NULL AS cancelledby_name
FROM special_collection s
INNER JOIN followup_collection f ON s.objid = f.objid;

INSERT INTO `clfc2`.`ledger_billing_detail`
            (`objid`,
             `parentid`,
             `billingid`,
             `ledgerid`,
             `route_code`,
             `acctid`,
             `acctname`,
             `loanamount`,
             `appno`,
             `dailydue`,
             `amountdue`,
             `overpaymentamount`,
             `balance`,
             `refno`,
             `txndate`,
             `dtmatured`,
             `isfirstbill`,
             `paymentmethod`,
             `loandate`,
             `term`,
             `loanappid`,
             `homeaddress`,
             `collectionaddress`,
             `penalty`,
             `interest`,
             `others`)
SELECT l.objid, s.parentid, l.parentid AS billingid, l.ledgerid, l.route_code, l.acctid, l.acctname, l.loanamount, l.appno,
	l.dailydue, l.amountdue, l.overpaymentamount, l.balance, l.refno, l.txndate, l.dtmatured, l.isfirstbill, l.paymentmethod,
	l.loandate, l.term, l.loanappid, l.homeaddress, l.collectionaddress, l.penalty, l.interest, l.others
FROM special_collection_loan s
INNER JOIN loan_ledger_billing_detail l ON s.billingdetailid = l.objid
INNER JOIN followup_collection f ON s.parentid = f.objid;
