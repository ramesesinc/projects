/* 03021 */

/*============================================
*
* TAX DIFFERENCE
*
*============================================*/

CREATE TABLE `rptledger_avdifference` (
  `objid` varchar(50) NOT NULL,
  `parent_objid` varchar(50) NOT NULL,
  `rptledgerfaas_objid` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `av` decimal(16,2) NOT NULL,
  `paid` int(11) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

create index `fk_rptledger` on rptledger_avdifference (`parent_objid`)
;

create index `fk_rptledgerfaas` on rptledger_avdifference (`rptledgerfaas_objid`)
;
 
alter table rptledger_avdifference 
	add CONSTRAINT `fk_rptledgerfaas` FOREIGN KEY (`rptledgerfaas_objid`) 
	REFERENCES `rptledgerfaas` (`objid`)
;

alter table rptledger_avdifference 
	add CONSTRAINT `fk_rptledger` FOREIGN KEY (`parent_objid`) 
	REFERENCES `rptledger` (`objid`)
;



create view vw_rptledger_avdifference
as 
select 
  rlf.objid,
  'APPROVED' as state,
  d.parent_objid as rptledgerid,
  rl.faasid,
  rl.tdno,
  rlf.txntype_objid,
  rlf.classification_objid,
  rlf.actualuse_objid,
  rlf.taxable,
  rlf.backtax,
  d.year as fromyear,
  1 as fromqtr,
  d.year as toyear,
  4 as toqtr,
  d.av as assessedvalue,
  1 as systemcreated,
  rlf.reclassed,
  rlf.idleland,
  1 as taxdifference
from rptledger_avdifference d 
inner join rptledgerfaas rlf on d.rptledgerfaas_objid = rlf.objid 
inner join rptledger rl on d.parent_objid = rl.objid 
; 