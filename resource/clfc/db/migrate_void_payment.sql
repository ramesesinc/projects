USE clfc2;

INSERT IGNORE INTO `clfc2`.`voidpayment`
            (`objid`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `borrower_objid`,
             `borrower_name`,
             `collector_objid`,
             `collector_name`,
             `routecode`,
             `paymentid`,
             `dtpaid`,
             `refno`,
             `amount`,
             `paidby`,
             `paytype`,
             `payoption`,
             `bank_objid`,
             `bank_name`,
             `check_no`,
             `check_date`)
SELECT v.objid, v.dtfiled, '' AS author_objid, v.filedby AS author_name, v.borrower_objid,
      v.borrower_name, v.collector_objid, v.collector_name, v.routecode, v.paymentid, v.dtpaid, v.refno, v.amount, v.paidby,
      `type` AS paytype, 'cash' AS payoption, NULL AS bank_objid, NULL AS bank_name, NULL AS check_no, NULL AS check_date
FROM void_payment v