
update sys_rule_actiondef_param set 
	lovname='WATERWORKS_TXNTYPE' 
where lovname='WATERWORKS_BILLING_GROUP_OPTION'
;

CREATE TABLE `waterworks_txntype` ( 
  `objid` varchar(50) NOT NULL, 
  `title` varchar(50) DEFAULT NULL, 
  `priority` int(11) DEFAULT NULL, 
  PRIMARY KEY (`objid`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8; 

INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('APPFEE', 'APP FEE', '5');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('AR_MATERIALS', 'AR MATERIALS', '4');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('COMPROMISE', 'COMPROMISE', '7');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('ENV', 'ENVIRONMENT', '3');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('INTEREST', 'INTEREST', '9');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('OTHER', 'OTHER FEES', '4');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('SURCHARGE', 'SURCHARGE', '8');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('WATERSALES', 'WATER SALES', '0');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('WATERSALES_ARREARS_CURRENT', 'WATER SALES CURRENT ARREARS', '1');
INSERT INTO `waterworks_txntype` (`objid`, `title`, `priority`) VALUES ('WATERSALES_ARREARS_PRIOR', 'WATER SALES PRIOR ARREARS ', '2');

alter table waterworks_payment_item 
	add ledgertype varchar(50) null, 
	add year int null,
	add month int null; 

alter table waterworks_payment_item 
	drop column txntype;


rename table waterworks_account_consumption to waterworks_consumption 
;

update 
	waterworks_account wa, 
	( 
		select acctid, max(billingcycleid) as maxbcid 
		from waterworks_consumption wac 
		group by acctid 
	)xx 
set 
	wa.billingcycleid=xx.maxbcid 
where wa.objid=xx.acctid 
;
update 
	waterworks_account wa, 
	waterworks_consumption wac, 
	waterworks_billing_cycle wbc 
set 
	wa.currentreading = wac.reading,
	wa.lastdateread = wbc.readingdate
where wa.objid=wac.acctid 
	and wa.billingcycleid=wac.billingcycleid 
	and wac.billingcycleid=wbc.objid 
;

update waterworks_consumption set amount=0.0 where amount is null;
update waterworks_consumption set amtpaid=0.0 where amtpaid is null;
