DELIMITER $$

USE `waterworks`$$

DROP VIEW IF EXISTS `vw_waterworks_account`$$

CREATE VIEW `vw_waterworks_account` AS 
SELECT
  `wa`.`objid`            AS `objid`,
  `wa`.`state`            AS `state`,
  `wa`.`dtstarted`        AS `dtstarted`,
  `wa`.`acctno`           AS `acctno`,
  `wa`.`acctname`         AS `acctname`,
  `wa`.`owner_objid`      AS `ownerid`,
  `wa`.`owner_name`       AS `ownername`,
  `wa`.`address_text`     AS `address`,
  `wa`.`mobileno`         AS `mobileno`,
  `wa`.`phoneno`          AS `phoneno`,
  `wa`.`email`            AS `email`,
  `wa`.`classificationid` AS `classificationid`,
  `wa`.`lastreading`      AS `lastreading`,
  `wa`.`balance`          AS `balance`,
  `wa`.`lastreadingdate`  AS `lastreadingdate`,
  `wa`.`lastreadingmonth` AS `lastreadingmonth`,
  `wa`.`lastreadingyear`  AS `lastreadingyear`,
  `wa`.`lasttxndate`      AS `lasttxndate`,
  `wa`.`prevreading`      AS `prevreading`,
  `wm`.`brand`            AS `brand`,
  `wm`.`serialno`         AS `serialno`,
  `wm`.`capacity`         AS `capacity`,
  `war`.`assignee_objid`  AS `assigneeid`,
  `war`.`assignee_name`   AS `assigneename`
FROM ((`waterworks`.`waterworks_account` `wa`
    JOIN `waterworks`.`waterworks_meter` `wm`
      ON ((`wa`.`meterid` = `wm`.`objid`)))
   JOIN `waterworks`.`waterworks_area` `war`
     ON ((`wa`.`areaid` = `war`.`objid`)))$$

DELIMITER ;


DELIMITER $$
USE `waterworks`$$

DROP VIEW IF EXISTS `vw_waterworks_billing`$$

CREATE VIEW `vw_waterworks_billing` AS 
SELECT
  `wa`.`objid`            AS `objid`,
  `wa`.`acctno`           AS `acctno`,
  `wa`.`acctname`         AS `acctname`,
  `wa`.`address_text`     AS `address`,
  `wa`.`mobileno`         AS `mobileno`,
  `wa`.`phoneno`          AS `phoneno`,
  `wa`.`email`            AS `email`,
  `wm`.`serialno`         AS `serialno`,
  `wa`.`areaid`           AS `areaid`,
  `wa`.`balance`          AS `balance`,
  `wa`.`lastreading`      AS `lastreading`,
  `wa`.`lasttxndate`      AS `lasttxndate`,
  `war`.`name`            AS `areaname`,
  `wa`.`classificationid` AS `classificationid`,
  `wa`.`lastreadingyear`  AS `lastreadingyear`,
  `wa`.`lastreadingmonth` AS `lastreadingmonth`,
  `wa`.`lastreadingdate`  AS `lastreadingdate`
FROM ((`waterworks_account` `wa`
    JOIN `waterworks_meter` `wm`
      ON ((`wa`.`meterid` = `wm`.`objid`)))
   JOIN `waterworks_area` `war`
     ON ((`war`.`objid` = `wa`.`areaid`)))$$

DELIMITER ;




