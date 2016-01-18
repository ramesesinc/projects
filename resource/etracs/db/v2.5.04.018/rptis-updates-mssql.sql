alter table rptledger_compromise add cypaymentreceiptid varchar(50)
go 

alter table rptledger_compromise add downpaymentreceiptid varchar(50)
go 


update rc set 
	rc.cypaymentreceiptid = cr.objid 
from rptledger_compromise rc, cashreceipt cr 
where rc.cypaymentorno = cr.receiptno
go 

update rc set 
	rc.downpaymentreceiptid = cr.objid 
from rptledger_compromise rc, cashreceipt cr 	
where rc.downpaymentorno = cr.receiptno
go 



alter table landadjustment add basemarketvalue decimal(16,2)
go 
alter table landadjustment add marketvalue decimal(16,2)
go

update landadjustment set basemarketvalue = 0, marketvalue = 0
go


update landadjustment set 
	basemarketvalue = (select sum(basemarketvalue) from landdetail where landrpuid = landadjustment.landrpuid)
go 