alter table rptledger_compromise add manualdiff decimal(16,2)
go 

update rptledger_compromise set manualdiff = 0 where manualdiff is null
go 