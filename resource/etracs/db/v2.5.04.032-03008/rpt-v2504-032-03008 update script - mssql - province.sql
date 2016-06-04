/* v2.5.04.032-03008 */
alter table sys_rule_actiondef add actionclass varchar(100)
go 

create table faas_restriction_type
(
	objid varchar(50),
	name varchar(100) not null,
	idx int not null,
	isother int not null,
	primary key(objid)
)
go 


INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('BOUNDARY_CONFLICT', 'Boundary Conflict', '4', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('BSP_GSP', 'BSP / GSP', '9', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('CARP', 'Under CARP', '1', '0')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('RED_AREAS', 'Red Areas', '3', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('RP_NIA', 'RP / NIA', '5', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('TELECOM', 'Telecom', '6', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('UNDER_LITIGATION', 'Under Litigation', '2', '0')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('UNDETERMINED', 'Undermined', '7', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('UNLOCATED_OWNER', 'Unlocated Owner', '8', '1')
go 
INSERT INTO faas_restriction_type (objid, name, idx, isother) VALUES ('RESTRICTED', 'Restricted', '9', '1')
go 



INSERT INTO sys_usergroup (objid, title, domain, userclass, orgclass, role) 
VALUES ('RPT.REPORT', 'REPORT', 'RPT', 'usergroup', NULL, 'REPORT')
go 

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('RPT.REPORT-faas-titled-report-viewreport', 'RPT.REPORT', 'faas-titled-report', 'viewreport', 'View Report')
go 


alter table subdivision_cancelledimprovement add lasttaxyear int
go 
alter table subdivision_cancelledimprovement add lguid varchar(50)
go 
alter table subdivision_cancelledimprovement add reason_objid varchar(50)
go 



/* RPTLEDGER: add blockno info */
alter table rptledger add blockno varchar(50)
go 

create index ix_rptledger_blockno on rptledger(blockno)
go 

update rl set 
	rl.blockno = rp.blockno
from rptledger rl, faas f, realproperty rp
where rl.faasid = f.objid and f.realpropertyid = rp.objid
go 




/* PC and DT txn types */
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode, allowEditOwner, checkbalance, allowEditPin, allowEditPinInfo, allowEditAppraisal, opener) VALUES ('PC', 'Physical Obsolence', '0', '1', '0', 'PC', '0', '0', '0', '0', '1', NULL)
go 
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode, allowEditOwner, checkbalance, allowEditPin, allowEditPinInfo, allowEditAppraisal, opener) VALUES ('DT', 'Destruction of Property', '0', '1', '0', 'DT', '0', '0', '0', '0', '1', NULL)
go 


