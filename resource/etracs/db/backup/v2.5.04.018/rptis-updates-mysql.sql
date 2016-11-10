alter table rptledger_compromise add cypaymentreceiptid varchar(50);
alter table rptledger_compromise add downpaymentreceiptid varchar(50);



update rptledger_compromise rc, cashreceipt cr set 
	rc.cypaymentreceiptid = cr.objid 
where rc.cypaymentorno = cr.receiptno;

update rptledger_compromise rc, cashreceipt cr set 
	rc.downpaymentreceiptid = cr.objid 
where rc.downpaymentorno = cr.receiptno;
	


alter table landadjustment add basemarketvalue decimal(16,2);
alter table landadjustment add marketvalue decimal(16,2);

update landadjustment set basemarketvalue = 0, marketvalue = 0;

	

update landadjustment set 
	basemarketvalue = (select sum(basemarketvalue) from landdetail where landrpuid = landadjustment.landrpuid);	