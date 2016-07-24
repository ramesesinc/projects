/* v2.5.04.032-03010 */

/*===========================================
* CERTIFICATION UPDATE
*===========================================*/

alter table rptcertification add [year] int
go 
alter table rptcertification add [qtr] int
go 

  



/*===========================================
* SPECIFIC CLASS
*===========================================*/

CREATE TABLE landspecificclass (
  objid varchar(50) NOT NULL,
  state varchar(10) NOT NULL,
  code varchar(50) NOT NULL,
  name varchar(100) NOT NULL,
  PRIMARY KEY  (objid)
) 
go

create index ux_landspecificclass_state on landspecificclass(state) 
go 


insert into landspecificclass(
  objid, state, code, name 
)
select 
  objid, 'APPROVED', code, name 
from lcuvspecificclass
go 


alter table lcuvspecificclass add landspecificclass_objid varchar(50)
go 

create index ix_landspecificclass_objid on lcuvspecificclass(landspecificclass_objid )

go 

alter table lcuvspecificclass 
  add constraint fk_lcuvspecificclass_landspecificclass 
  foreign key(landspecificclass_objid ) references landspecificclass(objid)

 go 

update lcuvspecificclass set 
  landspecificclass_objid  = objid
where landspecificclass_objid is null
go 



alter table landdetail add landspecificclass_objid varchar(50)
go 

update landdetail set landspecificclass_objid = specificclass_objid where landspecificclass_objid is null
go 


alter table lcuvspecificclass drop column code
go 
alter table lcuvspecificclass drop column name
go 

