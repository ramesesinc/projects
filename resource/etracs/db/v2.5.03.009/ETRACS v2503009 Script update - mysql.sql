/*
Navicat MySQL Data Transfer

Source Server         : mysql-local
Source Server Version : 50027
Source Host           : 127.0.0.1:3306
Source Database       : etracs25_kalibo

Target Server Type    : MYSQL
Target Server Version : 50027
File Encoding         : 65001

Date: 2014-08-09 09:30:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rpu_assessment
-- ----------------------------
DROP TABLE IF EXISTS `rpu_assessment`;
CREATE TABLE `rpu_assessment` (
  `objid` varchar(50) NOT NULL,
  `rpuid` varchar(50) NOT NULL,
  `classification_objid` varchar(50) NOT NULL,
  `actualuse_objid` varchar(50) NOT NULL,
  `areasqm` decimal(16,2) NOT NULL,
  `areaha` decimal(16,6) NOT NULL,
  `marketvalue` decimal(16,2) NOT NULL,
  `assesslevel` decimal(16,2) NOT NULL,
  `assessedvalue` decimal(16,2) NOT NULL,
  PRIMARY KEY  (`objid`),
  KEY `FK_rpuassessmetn_rpu` (`rpuid`),
  CONSTRAINT `FK_rpuassessmetn_rpu` FOREIGN KEY (`rpuid`) REFERENCES `rpu` (`objid`) ON DELETE CASCADE
) ENGINE=InnoDB;


DELETE FROM sys_ruleset WHERE name = 'planttreeassessment';

INSERT INTO sys_ruleset (name, title, packagename, domain, role, permission) VALUES ('planttreeassessment', 'Plant/Tree Assessment Rules', 'planttreeassessment', 'RPT', 'RULE_AUTHOR', NULL);

DELETE FROM sys_rulegroup WHERE ruleset = 'planttreeassessment';

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSEDVALUE', 'planttreeassessment', 'After Assessed Value Computation', '45');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ASSESSLEVEL', 'planttreeassessment', 'After Assess Level Computation', '36');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-BASEMARKETVALUE', 'planttreeassessment', 'After Base Market Value Computation', '10');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-MARKETVALUE', 'planttreeassessment', 'After Market Value Computation', '30');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSEDVALUE', 'planttreeassessment', 'Assessed Value Computation', '40');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ASSESSLEVEL', 'planttreeassessment', 'Assess Level Computation', '35');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BASEMARKETVALUE', 'planttreeassessment', 'Base Market Value Computation', '5');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('INITIAL', 'planttreeassessment', 'Initial Computation', '0');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MARKETVALUE', 'planttreeassessment', 'Market Value Computation', '25');

INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('SUMMARY', 'planttreeassessment', 'Summary Computation', '100');

