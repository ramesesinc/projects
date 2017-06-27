
CREATE TABLE cashreceiptpayment_noncash (
  objid VARCHAR(50) NOT NULL,
  receiptid VARCHAR(50) DEFAULT NULL,
  bank VARCHAR(50) DEFAULT NULL,
  refno VARCHAR(25) DEFAULT NULL,
  refdate DATETIME DEFAULT NULL,
  reftype VARCHAR(50) DEFAULT NULL,
  amount DECIMAL(16,2) DEFAULT NULL,
  particulars VARCHAR(255) DEFAULT NULL,
  bankid VARCHAR(50) DEFAULT NULL,
  deposittype VARCHAR(50) DEFAULT NULL,
  account_objid VARCHAR(50) DEFAULT NULL,
  account_code VARCHAR(50) DEFAULT NULL,
  account_name VARCHAR(100) DEFAULT NULL,
  account_fund_objid VARCHAR(50) DEFAULT NULL,
  account_fund_name VARCHAR(50) DEFAULT NULL,
  account_bank VARCHAR(100) DEFAULT NULL
) 
GO 
CREATE INDEX ix_bankid ON cashreceiptpayment_noncash (bankid)
go 
CREATE INDEX ix_receiptid ON cashreceiptpayment_noncash (receiptid)
go 
alter table cashreceiptpayment_noncash add PRIMARY KEY (objid) 
go 
alter table cashreceiptpayment_noncash 
  add CONSTRAINT cashreceiptpayment_noncash_ibfk_1 
  FOREIGN KEY (receiptid) REFERENCES cashreceipt (objid)
go 
alter table cashreceiptpayment_noncash 
  add CONSTRAINT cashreceiptpayment_noncash_ibfk_2 
  FOREIGN KEY (bankid) REFERENCES bank (objid)
go 


insert into cashreceiptpayment_noncash ( 
	objid, receiptid, bank, refno, refdate, reftype, 
	amount, particulars, bankid, deposittype 
)
select 
	objid, receiptid, bank, checkno, checkdate, 'CHECK'  as reftype,
	amount, particulars, bankid, deposittype 
from cashreceiptpayment_check 
go 


EXEC sp_rename 'remittance_checkpayment', 'remittance_noncashpayment' 
go 
EXEC sp_rename 'liquidation_checkpayment', 'liquidation_noncashpayment' 
go 

alter table remittance_noncashpayment drop constraint remittance_checkpayment_ibfk_1 
go 
alter table remittance_noncashpayment 
  add CONSTRAINT fk_remittance_noncashpayment_objid 
  FOREIGN KEY (objid) REFERENCES cashreceiptpayment_noncash (objid)
go 

alter table liquidation_noncashpayment drop constraint FK_liquidation_checkpayment 
go
alter table liquidation_noncashpayment drop constraint liquidation_checkpayment_ibfk_1 
go
alter table liquidation_noncashpayment 
  add CONSTRAINT fk_liquidation_noncashpayment_objid 
  FOREIGN KEY (objid) REFERENCES cashreceiptpayment_noncash (objid)
go 

alter table bankdeposit_entry_check drop constraint bankdeposit_entry_check_ibfk_1 
go 
alter table bankdeposit_entry_check 
  add CONSTRAINT fk_bankdeposit_entry_check_objid 
  FOREIGN KEY (objid) REFERENCES cashreceiptpayment_noncash (objid)
go 

drop table cashreceiptpayment_check 
go
