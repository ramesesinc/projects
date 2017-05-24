/* 254032-03016 */
INSERT INTO `rptparameter` (`objid`, `state`, `name`, `caption`, `description`, `paramtype`, `minvalue`, `maxvalue`) VALUES ('DISTANCE_TO_AWR', 'APPROVED', 'DISTANCE_TO_AWR', 'DISTANCE TO AWR', '', 'decimal', '0', '0');
INSERT INTO `rptparameter` (`objid`, `state`, `name`, `caption`, `description`, `paramtype`, `minvalue`, `maxvalue`) VALUES ('DISTANCE_TO_LTC', 'APPROVED', 'DISTANCE_TO_LTC', 'DISTANCE TO LTC', '', 'decimal', '0', '0');


INSERT INTO `sys_var` (`name`, `value`, `description`, `datatype`, `category`) VALUES ('faas_land_auto_agricultural_adjustment', '0', 'Land RPU Auto Adjustment', 'checkbox', 'ASSESSOR');

alter table landrpu 
	add distanceawr decimal(16,2),
	add distanceltc decimal(16,2);