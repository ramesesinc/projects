
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


-----------------------------------------
-----------------------------------------
-----------------------------------------

alter table business_permit 
	add remarks varchar(255) null 
; 

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('PER6f4d3c90:156bf265726:-7e42', 'BPLS.MASTER', 'lobclassification', 'delete', 'Delete LOB Classification');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('PER6f4d3c90:156bf265726:-7e43', 'BPLS.MASTER', 'lobclassification', 'edit', 'Edit LOB Classification');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('PER6f4d3c90:156bf265726:-7e44', 'BPLS.MASTER', 'lobclassification', 'read', 'Read LOB Classification');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('PER6f4d3c90:156bf265726:-7e45', 'BPLS.MASTER', 'lobclassification', 'create', 'Create LOB Classification');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('PER6f4d3c90:156bf265726:-7e46', 'BPLS.MASTER', 'lobclassification', 'view', 'View LOB Classification');

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businesslessor.create', 'BPLS.MASTER', 'businesslessor', 'create', 'Create Business Lessor');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businesslessor.delete', 'BPLS.MASTER', 'businesslessor', 'delete', 'Delete Business Lessor');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businesslessor.edit', 'BPLS.MASTER', 'businesslessor', 'edit', 'Edit Business Lessor');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businesslessor.read', 'BPLS.MASTER', 'businesslessor', 'read', 'Read Business Lessor');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businesslessor.view', 'BPLS.MASTER', 'businesslessor', 'view', 'View Business Lessor');

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businessrequirementtype.create', 'BPLS.MASTER', 'businessrequirementtype', 'create', 'Create Business Requirement Type');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businessrequirementtype.delete', 'BPLS.MASTER', 'businessrequirementtype', 'delete', 'Delete Business Requirement Type');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businessrequirementtype.edit', 'BPLS.MASTER', 'businessrequirementtype', 'edit', 'Edit Business Requirement Type');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businessrequirementtype.read', 'BPLS.MASTER', 'businessrequirementtype', 'read', 'Read Business Requirement Type');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('businessrequirementtype.view', 'BPLS.MASTER', 'businessrequirementtype', 'view', 'View Business Requirement Type');

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lob.create', 'BPLS.MASTER', 'lob', 'create', 'Create LOB');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lob.delete', 'BPLS.MASTER', 'lob', 'delete', 'Delete LOB');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lob.edit', 'BPLS.MASTER', 'lob', 'edit', 'Edit LOB');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lob.read', 'BPLS.MASTER', 'lob', 'read', 'Read LOB');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lob.view', 'BPLS.MASTER', 'lob', 'view', 'View LOB');

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lobattribute.create', 'BPLS.MASTER', 'lobattribute', 'create', 'Create LOB Attribute');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lobattribute.delete', 'BPLS.MASTER', 'lobattribute', 'delete', 'Delete LOB Attribute');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lobattribute.edit', 'BPLS.MASTER', 'lobattribute', 'edit', 'Edit LOB Attribute');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lobattribute.read', 'BPLS.MASTER', 'lobattribute', 'read', 'Read LOB Attribute');
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('lobattribute.view', 'BPLS.MASTER', 'lobattribute', 'view', 'View LOB Attribute');

