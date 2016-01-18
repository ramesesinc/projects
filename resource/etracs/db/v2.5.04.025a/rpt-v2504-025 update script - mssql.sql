/*==================================
** V2.5.04.025a
==================================*/

alter table rptcertification add asofyear int
go

update rptcertification set asofyear = year(txndate) where asofyear is null
go 


alter table assessmentnotice add administrator_name varchar(150)
go 
alter table assessmentnotice add administrator_address varchar(150)
go 