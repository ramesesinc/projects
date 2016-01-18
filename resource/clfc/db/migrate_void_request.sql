USE clfc2;

INSERT INTO `clfc2`.`voidrequest`
            (`objid`,
             `state`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `txncode`,
             `paymentid`,
             `routecode`,
             `collectionid`,
             `loanapp_objid`,
             `loanapp_appno`,
             `borrower_objid`,
             `borrower_name`,
             `collector_objid`,
             `collector_name`,
             `reason`,
             `dtposted`,
             `poster_objid`,
             `poster_name`,
             `posterremarks`)
SELECT v.objid, v.state, v.dtfiled, v.collector_objid AS author_objid, v.filedby AS author_name, v.txncode, v.paymentid, v.routecode, 
	v.collectionid, v.loanapp_objid, v.loanapp_appno, l.borrower_objid, l.borrower_name, v.collector_objid, v.collector_name, v.reason, 
	v.dtapproved AS dtposted, '' AS poster_objid, v.approvedby AS poster_name, v.approvedremarks AS posterremarks
FROM void_request v 
INNER JOIN loanapp l ON v.loanapp_objid = l.objid

