INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('ENTITY-MASTER-editname', 'ENTITY.MASTER', 'individualentity', 'editname', 'Edit Name');

INSERT INTO sys_usergroup (objid, title, domain, userclass, orgclass, role) 
VALUES ('TREASURY.ADMIN', 'TREASURY ADMIN', 'TREASURY', 'usergroup', NULL, 'ADMIN');

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('TREASURY-ADMIN-receipt-void', 'TREASURY.ADMIN', 'receipt', 'void', 'Void Receipt');

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('TREASURY-COLLECTOR-receipt-void', 'TREASURY.COLLECTOR', 'receipt', 'void', 'Void Receipt');

INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) 
VALUES ('TREASURY-SUBCOLLECTOR-receipt-void', 'TREASURY.SUBCOLLECTOR', 'receipt', 'void', 'Void Receipt');
