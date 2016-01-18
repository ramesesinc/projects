/*==================================
** V2.5.04.024
==================================*/

alter table rpttransmittal_log add filetype varchar(100);

INSERT INTO rptparameter (objid, state, name, caption, description, paramtype, minvalue, maxvalue) VALUES ('TOTAL_BMV', 'APPROVED', 'TOTAL_BMV', 'TOTAL BMV', NULL, 'decimal', '0', '0');
INSERT INTO rptparameter (objid, state, name, caption, description, paramtype, minvalue, maxvalue) VALUES ('TOTAL_MV', 'APPROVED', 'TOTAL_MV', 'TOTAL MV', '', 'decimal', '0', '0');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BEFORE-ASSESSLEVEL', 'miscassessment', 'Before Assess Level Computation', '34');



