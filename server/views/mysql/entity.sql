drop VIEW if exists `vw_entityindividual` 
; 
CREATE VIEW `vw_entityindividual` AS 
select 
`ei`.`objid` AS `objid`, 
`ei`.`lastname` AS `lastname`, 
`ei`.`firstname` AS `firstname`, 
`ei`.`middlename` AS `middlename`, 
`ei`.`birthdate` AS `birthdate`, 
`ei`.`birthplace` AS `birthplace`, 
`ei`.`citizenship` AS `citizenship`, 
`ei`.`gender` AS `gender`, 
`ei`.`civilstatus` AS `civilstatus`, 
`ei`.`profession` AS `profession`, 
`ei`.`tin` AS `tin`, 
`ei`.`sss` AS `sss`, 
`ei`.`height` AS `height`, 
`ei`.`weight` AS `weight`, 
`ei`.`acr` AS `acr`, 
`ei`.`religion` AS `religion`, 
`ei`.`photo` AS `photo`, 
`ei`.`thumbnail` AS `thumbnail`, 
`ei`.`profileid` AS `profileid`, 
`e`.`entityno` AS `entityno`, 
`e`.`type` AS `type`, 
`e`.`name` AS `name`, 
`e`.`entityname` AS `entityname`, 
`e`.`mobileno` AS `mobileno`, 
`e`.`phoneno` AS `phoneno`, 
`e`.`address_objid` AS `address_objid`, 
`e`.`address_text` AS `address_text`  
from `entityindividual` `ei` 
	inner join `entity` `e` on `e`.`objid` = `ei`.`objid` 
; 


drop VIEW if exists `vw_entityindividual_lookup` 
; 
CREATE VIEW `vw_entityindividual_lookup` AS 
select 
`e`.`objid` AS `objid`, 
`e`.`entityno` AS `entityno`, 
`e`.`name` AS `name`, 
`e`.`address_text` AS `addresstext`, 
`e`.`type` AS `type`, 
`ei`.`lastname` AS `lastname`, 
`ei`.`firstname` AS `firstname`, 
`ei`.`middlename` AS `middlename`, 
`ei`.`gender` AS `gender`, 
`ei`.`birthdate` AS `birthdate`, 
`e`.`mobileno` AS `mobileno`, 
`e`.`phoneno` AS `phoneno`  
from `entity` `e` 
	join `entityindividual` `ei` on `ei`.`objid` = `e`.`objid` 
; 


drop VIEW if exists `vw_entityrelation_lookup` 
; 
CREATE VIEW `vw_entityrelation_lookup` AS 
select 
`er`.`objid` AS `objid`, 
`er`.`entity_objid` AS `entity_objid`, 
`er`.`relateto_objid` AS `relateto_objid`, 
`er`.`relation_objid` AS `relation_objid`, 
`e`.`entityno` AS `entityno`, 
`e`.`name` AS `name`, 
`e`.`address_text` AS `addresstext`, 
`e`.`type` AS `type`, 
`ei`.`lastname` AS `lastname`, 
`ei`.`firstname` AS `firstname`, 
`ei`.`middlename` AS `middlename`, 
`ei`.`gender` AS `gender`, 
`ei`.`birthdate` AS `birthdate`, 
`e`.`mobileno` AS `mobileno`, 
`e`.`phoneno` AS `phoneno`  
from `entity_relation` `er` 
	inner join `entityindividual` `ei` on `ei`.`objid` = `er`.`relateto_objid` 
	inner join `entity` `e` on `e`.`objid` = `ei`.`objid` 
; 



drop VIEW if exists `vw_entity_relation` 
; 
CREATE VIEW `vw_entity_relation` AS 
select 
`er`.`objid` AS `objid`, 
`er`.`entity_objid` AS `ownerid`, 
`ei`.`objid` AS `entityid`, 
`ei`.`entityno` AS `entityno`, 
`ei`.`name` AS `name`, 
`ei`.`firstname` AS `firstname`, 
`ei`.`lastname` AS `lastname`, 
`ei`.`middlename` AS `middlename`, 
`ei`.`birthdate` AS `birthdate`, 
`ei`.`gender` AS `gender`, 
`er`.`relation_objid` AS `relationship`  
from `entity_relation` `er` 
	inner join `vw_entityindividual` `ei` on `er`.`relateto_objid` = `ei`.`objid` 
union all 
select 
`er`.`objid` AS `objid`, 
`er`.`relateto_objid` AS `ownerid`, 
`ei`.`objid` AS `entityid`, 
`ei`.`entityno` AS `entityno`, 
`ei`.`name` AS `name`, 
`ei`.`firstname` AS `firstname`, 
`ei`.`lastname` AS `lastname`, 
`ei`.`middlename` AS `middlename`, 
`ei`.`birthdate` AS `birthdate`, 
`ei`.`gender` AS `gender`, 
(case 
	when `ei`.`gender` = 'M' then `et`.`inverse_male` 
	when `ei`.`gender` = 'F' then `et`.`inverse_female`
  	else `et`.`inverse_any` 
end) AS `relationship` 
from `entity_relation` `er` 
	inner join `vw_entityindividual` `ei` on `er`.`entity_objid` = `ei`.`objid` 
	inner join `entity_relation_type` `et` on `er`.`relation_objid` = `et`.`objid` 
; 

