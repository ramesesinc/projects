/* 255-03016 */

/*================================================================
*
* LANDTAX SHARE POSTING
*
================================================================*/
alter table rptpayment_share add iscommon int
go 

alter table rptpayment_share add year int
go 

update rptpayment_share set iscommon = 0 where iscommon is null 
go 




CREATE TABLE cashreceipt_rpt_share_forposting (
  objid varchar(50) NOT NULL,
  receiptid varchar(50) NOT NULL,
  rptledgerid varchar(50) NOT NULL,
  txndate datetime,
  error int NOT NULL,
  msg text,
  PRIMARY KEY (objid)
) 
go 


create UNIQUE index ux_receiptid_rptledgerid on cashreceipt_rpt_share_forposting (receiptid,rptledgerid)
go 
create index fk_cashreceipt_rpt_share_forposing_rptledger on cashreceipt_rpt_share_forposting (rptledgerid)
go 
create index fk_cashreceipt_rpt_share_forposing_cashreceipt on cashreceipt_rpt_share_forposting (receiptid)
go 

alter table cashreceipt_rpt_share_forposting add CONSTRAINT fk_cashreceipt_rpt_share_forposing_rptledger 
FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid)
go 
alter table cashreceipt_rpt_share_forposting add CONSTRAINT fk_cashreceipt_rpt_share_forposing_cashreceipt 
FOREIGN KEY (receiptid) REFERENCES cashreceipt (objid)
go 




/*==================================================
**
** BLDG DATE CONSTRUCTED SUPPORT 
**
===================================================*/

alter table bldgrpu add dtconstructed date
go 

