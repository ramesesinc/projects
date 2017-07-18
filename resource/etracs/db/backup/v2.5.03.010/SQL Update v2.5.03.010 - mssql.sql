alter table faas add  prevareaha numeric(16,6)
go

alter table faas add  prevareasqm numeric(16,2)
go


alter table bldgstructure add floor int
go

update bldgstructure set floor = 1
go 
