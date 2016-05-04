CREATE TABLE `waterworks_stubout_node` 
(
  `objid` varchar(50) NOT NULL,
  `stuboutid` varchar(50) 
DEFAULT NULL,
  `indexno` int(11) DEFAULT NULL,
  
`acctid` varchar(50) DEFAULT NULL,
  
PRIMARY KEY (`objid`),
 
 UNIQUE KEY `fk_stuboutnode_account` (`acctid`),
 
 KEY `fk_stubout_stuboutnode` (`stuboutid`),
  
CONSTRAINT `fk_stuboutnode_account` 
FOREIGN KEY (`acctid`) REFERENCES `waterworks_account` (`objid`),
  
CONSTRAINT `fk_stubout_stuboutnode` FOREIGN KEY (`stuboutid`) 
REFERENCES `waterworks_stubout` (`objid`)
) 
ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
LEFT JOIN waterworks_meter wm ON wa.meterid=wm.objid;