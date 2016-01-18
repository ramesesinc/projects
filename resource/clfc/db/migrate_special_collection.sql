USE clfc2;

INSERT INTO `clfc2`.`specialcollection`
            (`objid`,
             `state`,
             `billingid`,
             `txntype`,
             `txndate`,
             `dtfiled`,
             `author_objid`,
             `author_name`,
             `dtrequested`,
             `requester_objid`,
             `requester_name`,
             `collector_objid`,
             `collector_name`,
             `remarks`)
SELECT s.objid, s.state, s.billingid,
	CASE
		WHEN s.type = 'FIELD' THEN "REQUEST"
		ELSE s.type
	END AS txntype, s.dtrequested AS txndate, s.dtrequested AS dtfiled,
	'' AS author_objid, s.requestedby AS author_name, s.dtrequested, 
	'' AS requester_objid, s.requestedby AS requester_name, s.collector_objid,
	s.collector_name, s.remarks
FROM special_collection s;

INSERT INTO `clfc2`.`specialcollection_detail`
            (`objid`,
             `parentid`,
             `billingdetailid`,
             `routecode`,
             `borrower_objid`,
             `borrower_name`,
             `loanapp_objid`,
             `loanapp_appno`)
SELECT s.objid, s.parentid, s.billingdetailid, s.routecode, s.borrower_objid,
	s.borrower_name, s.loanapp_objid, s.loanapp_appno
FROM special_collection_loan s;

