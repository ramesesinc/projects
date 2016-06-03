
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
