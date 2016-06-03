
CREATE TABLE `entity_relation` (
  `objid` varchar(50) NOT NULL,
  `entity_objid` varchar(50) DEFAULT NULL,
  `relateto_objid` varchar(50) DEFAULT NULL,
  `relation` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_sender_receiver` (`entity_objid`,`relateto_objid`),
  KEY `ix_sender_objid` (`entity_objid`) ,
  KEY `ix_receiver_objid` (`relateto_objid`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8
;

alter table entity_relation 
	add CONSTRAINT `fk_entityrelation_entity_objid` 
	FOREIGN KEY (`entity_objid`) REFERENCES `entity` (`objid`); 
alter table entity_relation 
	add CONSTRAINT `fk_entityrelation_relateto_objid` 
	FOREIGN KEY (`relateto_objid`) REFERENCES `entity` (`objid`);


drop view if exists vw_entityindividual
;
create view vw_entityindividual 
as 
select ei.*, 
	e.entityno, e.type, e.name, e.entityname, e.mobileno, e.phoneno, 
	e.address_objid, e.address_text
from entityindividual ei 
	inner join entity e on e.objid=ei.objid 
;

drop view if exists vw_entityindividual_lookup
;
create view vw_entityindividual_lookup 
as 
select 
	e.objid, e.entityno, e.name, e.address_text as addresstext, 
	e.type, ei.lastname, ei.firstname, ei.middlename, ei.gender, ei.birthdate, 
	e.mobileno, e.phoneno 
from entity e 
	inner join entityindividual ei on ei.objid=e.objid 
;

drop view if exists vw_entityrelation
;
create view vw_entityrelation 
as 
select er.*, e.entityno, e.name, e.address_text as addresstext, e.type  
from entity_relation er 
	inner join entity e on e.objid=er.relateto_objid 
order by e.name 
;

drop view if exists vw_entityrelation_lookup
;
create view vw_entityrelation_lookup 
as
select er.*, 
	e.entityno, e.name, e.address_text as addresstext, e.type, 
	ei.lastname, ei.firstname, ei.middlename, ei.gender, ei.birthdate, 
	e.mobileno, e.phoneno 
from entity_relation er  
  inner join entityindividual ei on ei.objid=er.relateto_objid 
	inner join entity e on e.objid=ei.objid 
;
