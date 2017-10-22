/* 254032-03018*/

alter table faasbacktax alter column tdno varchar(25) null
go 



alter table landdetail alter column subclass_objid varchar(50) null
go 
alter table landdetail alter column specificclass_objid varchar(50) null
go 
alter table landdetail alter column actualuse_objid varchar(50) null
go 
alter table landdetail alter column landspecificclass_objid varchar(50) null
go 




/* RYSETTING ORDINANCE INFO */
alter table landrysetting add ordinanceno varchar(25)
go 
alter table landrysetting add ordinancedate date
go 


alter table bldgrysetting add ordinanceno varchar(25)
go 
alter table bldgrysetting add ordinancedate date
go 


alter table machrysetting add ordinanceno varchar(25)
go 
alter table machrysetting add ordinancedate date
go 


alter table miscrysetting add ordinanceno varchar(25)
go 
alter table miscrysetting add ordinancedate date
go 


alter table planttreerysetting add ordinanceno varchar(25)
go 
alter table planttreerysetting add ordinancedate date
go 


delete from sys_var where name in ('gr_ordinance_date','gr_ordinance_no')
go


