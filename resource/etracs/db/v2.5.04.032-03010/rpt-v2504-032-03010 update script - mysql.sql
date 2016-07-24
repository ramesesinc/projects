/* v2.5.04.032-03010 */

/*===========================================
* CERTIFICATION UPDATE
*===========================================*/

alter table rptcertification 
  add `year` int,
  add qtr int ;

  



/*===========================================
* SPECIFIC CLASS
*===========================================*/

CREATE TABLE `landspecificclass` (
  `objid` varchar(50) NOT NULL,
  `state` varchar(10) NOT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY  (`objid`),
  KEY `ux_landspecificclass_state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into landspecificclass(
  objid, state, code, name 
)
select 
  objid, 'APPROVED', code, name 
from lcuvspecificclass;


alter table lcuvspecificclass add landspecificclass_objid varchar(50);

create index ix_landspecificclass_objid on lcuvspecificclass(landspecificclass_objid );

alter table lcuvspecificclass 
  add constraint fk_lcuvspecificclass_landspecificclass 
  foreign key(landspecificclass_objid ) references landspecificclass(objid);

update lcuvspecificclass set 
  landspecificclass_objid  = objid
where landspecificclass_objid is null;



alter table landdetail add landspecificclass_objid varchar(50);

update landdetail set landspecificclass_objid = specificclass_objid where landspecificclass_objid is null;




alter table lcuvspecificclass 
  drop column code, 
  drop column name; 

