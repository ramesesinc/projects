INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('BEFORE-ADJUSTMENT', 'landassessment', 'Before Adjustment Computation', '13');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('ADJUSTMENT', 'landassessment', 'Adjustment Computation', '14');
INSERT INTO `sys_rulegroup` (`name`, `ruleset`, `title`, `sortorder`) VALUES ('AFTER-ADJUSTMENT', 'landassessment', 'After Adjustment Computation', '15');


alter table planttreedetail modify column areacovered decimal(16,4);
