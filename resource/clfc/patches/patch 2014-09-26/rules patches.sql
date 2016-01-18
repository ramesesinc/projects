
USE `clfc2`;

SET FOREIGN_KEY_CHECKS=0;

/*Table structure for table `sys_ruleset` */

DROP TABLE IF EXISTS `sys_ruleset`;
DROP TABLE IF EXISTS `sys_rulegroup`;
DROP TABLE IF EXISTS `sys_rule`;
DROP TABLE IF EXISTS `sys_rule_action`;
DROP TABLE IF EXISTS `sys_rule_action_param`;
DROP TABLE IF EXISTS `sys_rule_actiondef`;
DROP TABLE IF EXISTS `sys_rule_actiondef_param`;
DROP TABLE IF EXISTS `sys_rule_condition`;
DROP TABLE IF EXISTS `sys_rule_condition_constraint`;
DROP TABLE IF EXISTS `sys_rule_condition_var`;
DROP TABLE IF EXISTS `sys_rule_deployed`;
DROP TABLE IF EXISTS `sys_rule_fact`;
DROP TABLE IF EXISTS `sys_rule_fact_field`;
DROP TABLE IF EXISTS `sys_ruleset_actiondef`;
DROP TABLE IF EXISTS `sys_ruleset_fact`;

CREATE TABLE `sys_ruleset` (
  `name` VARCHAR(50) NOT NULL,
  `title` VARCHAR(160) DEFAULT NULL,
  `packagename` VARCHAR(50) DEFAULT NULL,
  `domain` VARCHAR(50) DEFAULT NULL,
  `role` VARCHAR(50) DEFAULT NULL,
  `permission` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`name`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rulegroup` */


CREATE TABLE `sys_rulegroup` (
  `name` VARCHAR(50) NOT NULL,
  `ruleset` VARCHAR(50) NOT NULL,
  `title` VARCHAR(160) DEFAULT NULL,
  `sortorder` INT(11) DEFAULT NULL,
  PRIMARY KEY  (`name`,`ruleset`),
  KEY `ruleset` (`ruleset`),
  CONSTRAINT `sys_rulegroup_ibfk_1` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule` */


CREATE TABLE `sys_rule` (
  `objid` VARCHAR(50) NOT NULL,
  `state` VARCHAR(25) DEFAULT NULL,
  `name` VARCHAR(50) NOT NULL,
  `ruleset` VARCHAR(50) NOT NULL,
  `rulegroup` VARCHAR(50) DEFAULT NULL,
  `title` VARCHAR(250) DEFAULT NULL,
  `description` LONGTEXT,
  `salience` INT(11) DEFAULT NULL,
  `effectivefrom` DATE DEFAULT NULL,
  `effectiveto` DATE DEFAULT NULL,
  `dtfiled` DATETIME DEFAULT NULL,
  `user_objid` VARCHAR(50) DEFAULT NULL,
  `user_name` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `rulegroup` (`rulegroup`,`ruleset`),
  KEY `ruleset` (`ruleset`),
  CONSTRAINT `sys_rule_ibfk_1` FOREIGN KEY (`rulegroup`, `ruleset`) REFERENCES `sys_rulegroup` (`name`, `ruleset`),
  CONSTRAINT `sys_rule_ibfk_2` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_action` */


CREATE TABLE `sys_rule_action` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `actiondef_objid` VARCHAR(50) DEFAULT NULL,
  `actiondef_name` VARCHAR(50) DEFAULT NULL,
  `pos` INT(11) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_action_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_action_param` */


CREATE TABLE `sys_rule_action_param` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `actiondefparam_objid` VARCHAR(50) DEFAULT NULL,
  `stringvalue` VARCHAR(255) DEFAULT NULL,
  `booleanvalue` INT(11) DEFAULT NULL,
  `var_objid` VARCHAR(50) DEFAULT NULL,
  `var_name` VARCHAR(50) DEFAULT NULL,
  `expr` LONGTEXT,
  `exprtype` VARCHAR(25) DEFAULT NULL,
  `pos` INT(11) DEFAULT NULL,
  `obj_key` VARCHAR(50) DEFAULT NULL,
  `obj_value` VARCHAR(255) DEFAULT NULL,
  `listvalue` LONGTEXT,
  `lov` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`),
  KEY `var_objid` (`var_objid`),
  CONSTRAINT `sys_rule_action_param_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_action` (`objid`),
  CONSTRAINT `sys_rule_action_param_ibfk_2` FOREIGN KEY (`var_objid`) REFERENCES `sys_rule_condition_var` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_actiondef` */


CREATE TABLE `sys_rule_actiondef` (
  `objid` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `title` VARCHAR(250) DEFAULT NULL,
  `sortorder` INT(11) DEFAULT NULL,
  `actionname` VARCHAR(50) DEFAULT NULL,
  `domain` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_actiondef_param` */


CREATE TABLE `sys_rule_actiondef_param` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `name` VARCHAR(50) NOT NULL,
  `sortorder` INT(11) DEFAULT NULL,
  `title` VARCHAR(50) DEFAULT NULL,
  `datatype` VARCHAR(50) DEFAULT NULL,
  `handler` VARCHAR(50) DEFAULT NULL,
  `lookuphandler` VARCHAR(50) DEFAULT NULL,
  `lookupkey` VARCHAR(50) DEFAULT NULL,
  `lookupvalue` VARCHAR(50) DEFAULT NULL,
  `vardatatype` VARCHAR(50) DEFAULT NULL,
  `lovname` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_actiondef_param_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_actiondef` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_condition` */


CREATE TABLE `sys_rule_condition` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `fact_name` VARCHAR(50) DEFAULT NULL,
  `fact_objid` VARCHAR(50) DEFAULT NULL,
  `varname` VARCHAR(50) DEFAULT NULL,
  `pos` INT(11) DEFAULT NULL,
  `ruletext` LONGTEXT,
  `displaytext` LONGTEXT,
  `dynamic_datatype` VARCHAR(50) DEFAULT NULL,
  `dynamic_key` VARCHAR(50) DEFAULT NULL,
  `dynamic_value` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `fact_objid` (`fact_objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_condition_ibfk_1` FOREIGN KEY (`fact_objid`) REFERENCES `sys_rule_fact` (`objid`),
  CONSTRAINT `sys_rule_condition_ibfk_2` FOREIGN KEY (`parentid`) REFERENCES `sys_rule` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_condition_constraint` */


CREATE TABLE `sys_rule_condition_constraint` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `field_objid` VARCHAR(50) DEFAULT NULL,
  `fieldname` VARCHAR(50) DEFAULT NULL,
  `varname` VARCHAR(50) DEFAULT NULL,
  `operator_caption` VARCHAR(50) DEFAULT NULL,
  `operator_symbol` VARCHAR(50) DEFAULT NULL,
  `usevar` INT(11) DEFAULT NULL,
  `var_objid` VARCHAR(50) DEFAULT NULL,
  `var_name` VARCHAR(50) DEFAULT NULL,
  `decimalvalue` DECIMAL(16,2) DEFAULT NULL,
  `intvalue` INT(11) DEFAULT NULL,
  `stringvalue` VARCHAR(255) DEFAULT NULL,
  `listvalue` LONGTEXT,
  `datevalue` DATE DEFAULT NULL,
  `pos` INT(11) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`),
  KEY `var_objid` (`var_objid`),
  CONSTRAINT `sys_rule_condition_constraint_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_condition` (`objid`),
  CONSTRAINT `sys_rule_condition_constraint_ibfk_2` FOREIGN KEY (`var_objid`) REFERENCES `sys_rule_condition_var` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_condition_var` */


CREATE TABLE `sys_rule_condition_var` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `ruleid` VARCHAR(50) DEFAULT NULL,
  `varname` VARCHAR(50) DEFAULT NULL,
  `datatype` VARCHAR(50) DEFAULT NULL,
  `pos` INT(11) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_condition_var_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_condition` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_deployed` */


CREATE TABLE `sys_rule_deployed` (
  `objid` VARCHAR(50) NOT NULL,
  `ruletext` LONGTEXT,
  PRIMARY KEY  (`objid`),
  CONSTRAINT `sys_rule_deployed_ibfk_1` FOREIGN KEY (`objid`) REFERENCES `sys_rule` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_fact` */


CREATE TABLE `sys_rule_fact` (
  `objid` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `title` VARCHAR(160) DEFAULT NULL,
  `factclass` VARCHAR(50) DEFAULT NULL,
  `sortorder` INT(11) DEFAULT NULL,
  `handler` VARCHAR(50) DEFAULT NULL,
  `defaultvarname` VARCHAR(25) DEFAULT NULL,
  `dynamic` INT(11) DEFAULT NULL,
  `lookuphandler` VARCHAR(50) DEFAULT NULL,
  `lookupkey` VARCHAR(50) DEFAULT NULL,
  `lookupvalue` VARCHAR(50) DEFAULT NULL,
  `lookupdatatype` VARCHAR(50) DEFAULT NULL,
  `dynamicfieldname` VARCHAR(50) DEFAULT NULL,
  `builtinconstraints` VARCHAR(50) DEFAULT NULL,
  `domain` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_rule_fact_field` */


CREATE TABLE `sys_rule_fact_field` (
  `objid` VARCHAR(50) NOT NULL,
  `parentid` VARCHAR(50) DEFAULT NULL,
  `name` VARCHAR(50) NOT NULL,
  `title` VARCHAR(160) DEFAULT NULL,
  `datatype` VARCHAR(50) DEFAULT NULL,
  `sortorder` INT(11) DEFAULT NULL,
  `handler` VARCHAR(50) DEFAULT NULL,
  `lookuphandler` VARCHAR(50) DEFAULT NULL,
  `lookupkey` VARCHAR(50) DEFAULT NULL,
  `lookupvalue` VARCHAR(50) DEFAULT NULL,
  `lookupdatatype` VARCHAR(50) DEFAULT NULL,
  `multivalued` INT(11) DEFAULT NULL,
  `required` INT(11) DEFAULT NULL,
  `vardatatype` VARCHAR(50) DEFAULT NULL,
  `lovname` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY  (`objid`),
  KEY `parentid` (`parentid`),
  CONSTRAINT `sys_rule_fact_field_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sys_rule_fact` (`objid`)
) ENGINE=INNODB ;

/*Table structure for table `sys_ruleset_actiondef` */


CREATE TABLE `sys_ruleset_actiondef` (
  `ruleset` VARCHAR(50) NOT NULL,
  `actiondef` VARCHAR(50) NOT NULL,
  PRIMARY KEY  (`ruleset`,`actiondef`),
  KEY `actiondef` (`actiondef`),
  CONSTRAINT `sys_ruleset_actiondef_ibfk_1` FOREIGN KEY (`actiondef`) REFERENCES `sys_rule_actiondef` (`objid`),
  CONSTRAINT `sys_ruleset_actiondef_ibfk_2` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=INNODB ;

/*Table structure for table `sys_ruleset_fact` */


CREATE TABLE `sys_ruleset_fact` (
  `ruleset` VARCHAR(50) NOT NULL,
  `rulefact` VARCHAR(50) NOT NULL,
  PRIMARY KEY  (`ruleset`,`rulefact`),
  KEY `rulefact` (`rulefact`),
  CONSTRAINT `sys_ruleset_fact_ibfk_1` FOREIGN KEY (`rulefact`) REFERENCES `sys_rule_fact` (`objid`),
  CONSTRAINT `sys_ruleset_fact_ibfk_2` FOREIGN KEY (`ruleset`) REFERENCES `sys_ruleset` (`name`)
) ENGINE=INNODB ;

SET FOREIGN_KEY_CHECKS=1;