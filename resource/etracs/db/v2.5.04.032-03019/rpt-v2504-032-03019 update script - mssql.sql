/* 254032-03019 */

/*==================================================
*
*  CDU RATING SUPPORT 
*
=====================================================*/

alter table bldgrpu add cdurating varchar(15)
go 

alter table bldgtype add usecdu int
go 
update bldgtype set usecdu = 0 where usecdu is null
go 


alter table bldgtype_depreciation add excellent decimal(16,2)
go 
alter table bldgtype_depreciation add verygood decimal(16,2)
go 
alter table bldgtype_depreciation add good decimal(16,2)
go 
alter table bldgtype_depreciation add average decimal(16,2)
go 
alter table bldgtype_depreciation add fair decimal(16,2)
go 
alter table bldgtype_depreciation add poor decimal(16,2)
go 
alter table bldgtype_depreciation add verypoor decimal(16,2)
go 
alter table bldgtype_depreciation add unsound decimal(16,2)
go 


alter table batchgr_error drop column barangayid
go 
alter table batchgr_error drop column barangay
go 
alter table batchgr_error drop column tdno
go 

drop view vw_batchgr_error
go 

create view vw_batchgr_error 
as 
select 
    err.*,
		f.tdno,
    f.prevtdno, 
    f.fullpin as fullpin, 
    rp.pin as pin,
		b.name as barangay,
		o.name as lguname
from batchgr_error err 
inner join faas f on err.objid = f.objid 
inner join realproperty rp on f.realpropertyid = rp.objid 
inner join barangay b on rp.barangayid = b.objid 
inner join sys_org o on f.lguid = o.objid
go 






/*=============================================================
*
* SKETCH 
*
==============================================================*/
CREATE TABLE faas_sketch (
  objid varchar(50) NOT NULL DEFAULT '',
  drawing text NOT NULL,
  PRIMARY KEY (objid)
)
go 



create index FK_faas_sketch_faas  on faas_sketch(objid)
go 

alter table faas_sketch 
  add constraint FK_faas_sketch_faas foreign key(objid) 
  references faas(objid)
go   



  
/*=============================================================
*
* CUSTOM RPU SUFFIX SUPPORT
*
==============================================================*/  

CREATE TABLE rpu_type_suffix (
  objid varchar(50) NOT NULL,
  rputype varchar(20) NOT NULL,
  [from] int NOT NULL,
  [to] int NOT NULL,
  PRIMARY KEY (objid)
) 
go 


insert into rpu_type_suffix (
  objid, rputype, [from], [to]
)
select 'LAND', 'land', 0, 0
union 
select 'BLDG-1001-1999', 'bldg', 1001, 1999
union 
select 'MACH-2001-2999', 'mach', 2001, 2999
union 
select 'PLANTTREE-3001-6999', 'planttree', 3001, 6999
union 
select 'MISC-7001-7999', 'misc', 7001, 7999
go 





/*=============================================================
*
* MEMORANDA TEMPLATE UPDATE 
*
==============================================================*/  
alter table memoranda_template add fields text
go 

update memoranda_template set fields = '[]' where fields is null
go 