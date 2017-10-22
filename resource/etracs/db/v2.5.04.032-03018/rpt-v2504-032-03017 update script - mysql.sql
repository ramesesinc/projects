/* 254032-03018 */

alter table faasbacktax modify column tdno varchar(25) null;


alter table landdetail modify column subclass_objid varchar(50) null;
alter table landdetail modify column specificclass_objid varchar(50) null;
alter table landdetail modify column actualuse_objid varchar(50) null;
alter table landdetail modify column landspecificclass_objid varchar(50) null;



/* RYSETTING ORDINANCE INFO */
alter table landrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table bldgrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table machrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table miscrysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


alter table planttreerysetting 
	add ordinanceno varchar(25),
	add ordinancedate date;


delete from sys_var where name in ('gr_ordinance_date','gr_ordinance_no');

	