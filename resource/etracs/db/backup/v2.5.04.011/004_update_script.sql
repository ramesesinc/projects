
INSERT INTO sys_usergroup (
	objid, title, domain, userclass, orgclass, role
) VALUES (
	'ENTITY.APPROVER', 'ENTITY APPROVER', 'ENTITY', 'usergroup', NULL, 'APPROVER'
);


UPDATE sys_wf_node SET role='CEO' WHERE processname='business_application' AND name='ceo'
;
