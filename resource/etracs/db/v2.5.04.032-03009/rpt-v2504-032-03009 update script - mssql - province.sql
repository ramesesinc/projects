/* v2.5.04.032-03009 */
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