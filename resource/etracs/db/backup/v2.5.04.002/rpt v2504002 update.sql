alter table rptledger_compromise add manualdiff decimal(16,2);
update rptledger_compromise set manualdiff = 0 where manualdiff is null;