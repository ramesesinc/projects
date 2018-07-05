# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.40)
# Database: etracs_epayment_tagbilaran
# Generation Time: 2018-07-05 01:20:01 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table entity_relation_type
# ------------------------------------------------------------

DROP TABLE IF EXIST entity_relation;

CREATE TABLE `entity_relation` (
  `objid` varchar(50) NOT NULL,
  `entity_objid` varchar(50) DEFAULT NULL,
  `relateto_objid` varchar(50) DEFAULT NULL,
  `relation_objid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  UNIQUE KEY `uix_sender_receiver` (`entity_objid`,`relateto_objid`),
  KEY `ix_sender_objid` (`entity_objid`),
  KEY `ix_receiver_objid` (`relateto_objid`),
  KEY `fk_entity_relation_relation` (`relation_objid`),
  CONSTRAINT `fk_entity_relation_relation` FOREIGN KEY (`relation_objid`) REFERENCES `entity_relation_type` (`objid`),
  CONSTRAINT `fk_entityrelation_entity_objid` FOREIGN KEY (`entity_objid`) REFERENCES `entity` (`objid`),
  CONSTRAINT `fk_entityrelation_relateto_objid` FOREIGN KEY (`relateto_objid`) REFERENCES `entity` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `entity_relation_type` (
  `objid` varchar(50) NOT NULL DEFAULT '',
  `gender` varchar(1) DEFAULT NULL,
  `inverse_any` varchar(50) DEFAULT NULL,
  `inverse_male` varchar(50) DEFAULT NULL,
  `inverse_female` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `entity_relation_type` WRITE;
/*!40000 ALTER TABLE `entity_relation_type` DISABLE KEYS */;

INSERT INTO `entity_relation_type` (`objid`, `gender`, `inverse_any`, `inverse_male`, `inverse_female`)
VALUES
	('AUNT','F','NEPHEW/NIECE','NEPHEW','NIECE'),
	('BROTHER','M','SIBLING','BROTHER','SISTER'),
	('COUSIN',NULL,'COUSIN','COUSIN','COUSIN'),
	('DAUGHTER','F','PARENT','FATHER','MOTHER'),
	('FATHER','M','CHILD','SON','DAUGHTER'),
	('GRANDDAUGHTER','F','GRANDPARENT','GRANDFATHER','GRANDMOTHER'),
	('GRANDSON','M','GRANDPARENT','GRANDFATHER','GRANDMOTHER'),
	('HUSBAND','M','SPOUSE','SPOUSE','WIFE'),
	('MOTHER','F','CHILD','SON','DAUGHTER'),
	('NEPHEW','M','UNCLE/AUNT','UNCLE','AUNT'),
	('NIECE','F','UNCLE/AUNT','UNCLE','AUNT'),
	('SISTER','F','SIBLING','BROTHER','SISTER'),
	('SON','M','PARENT','FATHER','MOTHER'),
	('SPOUSE',NULL,'SPOUSE','HUSBAND','WIFE'),
	('UNCLE','M','NEPHEW/NIECE','NEPHEW','NIECE'),
	('WIFE','F','SPOUSE','HUSBAND','SPOUSE');

/*!40000 ALTER TABLE `entity_relation_type` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
