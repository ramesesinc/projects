#254032-03013

create table memoranda_template
(
	objid varchar(50) primary key,
	code varchar(25) not null,
	template varchar(500) not null
)
go 


alter table rpu_assessment alter column classification_objid varchar(50)  null
go 
alter table rpu_assessment alter column actualuse_objid varchar(50)  null
go 


INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-bldgkind-create', 'RPT.MASTER', 'bldgkind', 'create', 'Create Kind of Building')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-bldgkind-delete', 'RPT.MASTER', 'bldgkind', 'delete', 'Delete Kind of Building')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-bldgkind-edit', 'RPT.MASTER', 'bldgkind', 'edit', 'Edit Kind of Building')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-bldgkind-read', 'RPT.MASTER', 'bldgkind', 'read', 'Open Kind of Building')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-landspecificclass-delete', 'RPT.MASTER', 'landspecificclass', 'delete', 'Delete Land Specific Class')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-landspecificclass-create', 'RPT.MASTER', 'landspecificclass', 'create', 'Create Land Specific Class')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-landspecificclass-read', 'RPT.MASTER', 'landspecificclass', 'read', 'Open Land Specific Class')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-landspecificclass-edit', 'RPT.MASTER', 'landspecificclass', 'edit', 'Edit Land Specific Class')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-machine-create', 'RPT.MASTER', 'machine', 'create', 'Create Machine')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-machine-read', 'RPT.MASTER', 'machine', 'read', 'Open Machine')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-machine-edit', 'RPT.MASTER', 'machine', 'edit', 'Edit Machine')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-machine-delete', 'RPT.MASTER', 'machine', 'delete', 'Delete Machine')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-approve', 'RPT.MASTER', 'master', 'approve', 'Approve Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-sync', 'RPT.MASTER', 'master', 'sync', 'Synchronize Master Files')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-import', 'RPT.MASTER', 'master', 'import', 'Import Master Files')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-disapprove', 'RPT.MASTER', 'master', 'disapprove', 'Disapprove Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-viewlist', 'RPT.MASTER', 'master', 'viewlist', 'View Master Files')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-export', 'RPT.MASTER', 'master', 'export', 'Export Master Files')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-material-delete', 'RPT.MASTER', 'material', 'delete', 'Delete Material')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-material-edit', 'RPT.MASTER', 'material', 'edit', 'Edit Material')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-material-read', 'RPT.MASTER', 'material', 'read', 'Open Material')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-material-create', 'RPT.MASTER', 'material', 'create', 'Create Material')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-plant-read', 'RPT.MASTER', 'planttree', 'read', 'Open Plant/Tree')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-plant-edit', 'RPT.MASTER', 'planttree', 'edit', 'Edit Plant/Tree')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-plant-create', 'RPT.MASTER', 'planttree', 'create', 'Create Plant/Tree')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-plant-delete', 'RPT.MASTER', 'planttree', 'delete', 'Delete Plant/Tree')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-read', 'RPT.MASTER', 'rptis_memoranda_template', 'read', 'Open Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-delete', 'RPT.MASTER', 'rptis_memoranda_template', 'delete', 'Delete Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-create', 'RPT.MASTER', 'rptis_memoranda_template', 'create', 'Create Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-edit', 'RPT.MASTER', 'rptis_memoranda_template', 'edit', 'Edit Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-read', 'RPT.MASTER', 'miscitem', 'read', 'Open Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-edit', 'RPT.MASTER', 'miscitem', 'edit', 'Edit Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-create', 'RPT.MASTER', 'miscitem', 'create', 'Create Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-delete', 'RPT.MASTER', 'miscitem', 'delete', 'Delete Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-plant-create', 'RPT.MASTER', 'planttree', 'create', 'Create Plant/Tree')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-plant-delete', 'RPT.MASTER', 'planttree', 'delete', 'Delete Plant/Tree')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-read', 'RPT.MASTER', 'rptis_memoranda_template', 'read', 'Open Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-delete', 'RPT.MASTER', 'rptis_memoranda_template', 'delete', 'Delete Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-create', 'RPT.MASTER', 'rptis_memoranda_template', 'create', 'Create Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-memoranda-edit', 'RPT.MASTER', 'rptis_memoranda_template', 'edit', 'Edit Memoranda Template')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-read', 'RPT.MASTER', 'miscitem', 'read', 'Open Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-edit', 'RPT.MASTER', 'miscitem', 'edit', 'Edit Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-create', 'RPT.MASTER', 'miscitem', 'create', 'Create Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-miscitem-delete', 'RPT.MASTER', 'miscitem', 'delete', 'Delete Miscellaneous Item')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-exemptiontype-create', 'RPT.MASTER', 'exemptiontype', 'create', 'Create Exemption Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-exemptiontype-delete', 'RPT.MASTER', 'exemptiontype', 'delete', 'Delete Exemption Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-exemptiontype-edit', 'RPT.MASTER', 'exemptiontype', 'edit', 'Edit Exemption Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-exemptiontype-read', 'RPT.MASTER', 'exemptiontype', 'read', 'Open Exemption Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-rptparameter-create', 'RPT.MASTER', 'rptparameter', 'create', 'Create Parameter')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-rptparameter-delete', 'RPT.MASTER', 'rptparameter', 'delete', 'Delete Parameter')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-rptparameter-edit', 'RPT.MASTER', 'rptparameter', 'edit', 'Edit Parameter')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-rptparameter-read', 'RPT.MASTER', 'rptparameter', 'read', 'Open Parameter')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-classification-create', 'RPT.MASTER', 'propertyclassification', 'create', 'Create Property Classification')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-classification-delete', 'RPT.MASTER', 'propertyclassification', 'delete', 'Delete Property Classification')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-classification-edit', 'RPT.MASTER', 'propertyclassification', 'edit', 'Edit Property Classification')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-classification-read', 'RPT.MASTER', 'propertyclassification', 'read', 'Open Property Classification')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-structure-create', 'RPT.MASTER', 'structure', 'create', 'Create Structure')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-structure-delete', 'RPT.MASTER', 'structure', 'delete', 'Delete Structure')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-structure-edit', 'RPT.MASTER', 'structure', 'edit', 'Edit Structure')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-structure-read', 'RPT.MASTER', 'structure', 'read', 'Open Structure')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-requirementtype-create', 'RPT.MASTER', 'requirementtype', 'create', 'Create Requirement Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-requirementtype-delete', 'RPT.MASTER', 'requirementtype', 'delete', 'Delete Requirement Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-requirementtype-edit', 'RPT.MASTER', 'requirementtype', 'edit', 'Edit Requirement Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-requirementtype-read', 'RPT.MASTER', 'requirementtype', 'read', 'Open Requirement Type')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-faastxntype-edit', 'RPT.MASTER', 'faastxntype', 'edit', 'Edit Transaction Types')
go 
INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('RPT.MASTER-faastxntype-read', 'RPT.MASTER', 'faastxntype', 'read', 'Open Transaction Types')
go 
