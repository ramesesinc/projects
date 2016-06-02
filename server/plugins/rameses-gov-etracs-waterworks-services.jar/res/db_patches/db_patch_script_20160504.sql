CREATE TABLE `waterworks_stubout_node` 
(
	`objid` varchar(50) NOT NULL,
	`stuboutid` varchar(50) DEFAULT NULL,
	`indexno` int(11) DEFAULT NULL,
	`acctid` varchar(50) DEFAULT NULL,   
	PRIMARY KEY (`objid`),
	UNIQUE KEY `uix_acctid` (`acctid`),
	KEY `ix_stuboutid` (`stuboutid`),
	CONSTRAINT `fk_stuboutnode_account` FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`),
	CONSTRAINT `fk_stuboutnode_stubout` FOREIGN KEY (`stuboutid`) REFERENCES `waterworks_stubout` (`objid`)
) 
ENGINE=InnoDB DEFAULT CHARSET=utf8
;

DROP VIEW IF EXISTS vw_waterworks_stubout_node
; 
CREATE VIEW vw_waterworks_stubout_node 
AS
SELECT 
	sn.objid,
	sn.stuboutid,
	sn.indexno, 
	wa.objid AS account_objid,
	wa.acctno AS account_acctno,
	wa.acctname AS account_acctname,
	wa.currentreading AS account_currentreading,
	wm.serialno AS account_meter_serialno
FROM waterworks_stubout_node sn
	LEFT JOIN waterworks_account wa ON sn.acctid=wa.objid
	LEFT JOIN waterworks_meter wm ON wa.meterid=wm.objid
;


insert ignore into waterworks_stubout_node (
	objid, stuboutid, indexno, acctid 
) 
select 
	concat('STN-',wa.objid) as objid, 
	wa.stuboutid, wa.stuboutindex as indexno, 
	wa.objid as acctid 
from waterworks_account wa 
	inner join waterworks_stubout ws on wa.stuboutid=ws.objid 
where wa.stuboutid <> '-' 
;

alter table waterworks_account add stuboutnodeid varchar(50) null
; 
create index ix_stuboutnodeid on waterworks_account (stuboutnodeid)
;

update waterworks_account wa, waterworks_stubout_node wsn set 
	wa.stuboutnodeid = wsn.objid 
where wa.stuboutid=wsn.stuboutid 
	and wa.stuboutindex=wsn.indexno 
	and wa.objid = wsn.acctid 
	and wa.stuboutid <> '-' 
;

alter table waterworks_account drop column stuboutindex
;
alter table waterworks_account drop column stuboutnodeid 
;
