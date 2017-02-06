DROP VIEW `vehicledb`.`vehicle_application_tricycle`;

CREATE TABLE `vehicle_application_tricycle` (
  `objid` varchar(50) NOT NULL,
  `plateno` varchar(50) NOT NULL,
  `engineno` varchar(50) DEFAULT NULL,
  `chassisno` varchar(50) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `sidecarno` varchar(50) DEFAULT NULL,
  `bodyno` varchar(50) DEFAULT NULL,
  `sidecarcolor` varchar(50) DEFAULT NULL,
  `crname` varchar(255) DEFAULT NULL
  PRIMARY KEY (`objid`),
  CONSTRAINT `fk_tricycle_application_vehicle` FOREIGN KEY (`objid`) REFERENCES `vehicle_application` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO vehicle_application_tricycle 
SELECT appid, plateno, engineno, chassisno, make, model, color, sidecarno, 
bodyno, sidecarcolor, crname, NULL FROM vehicle_tricycle
WHERE NOT(appid IS NULL); 


INSERT INTO vehicle_application_tricycle (objid)
SELECT objid FROM vehicle_application WHERE objid NOT IN (SELECT objid FROM vehicle_application_tricycle);

##############################################################################################################
DROP VIEW `vehicledb`.`vehicle_franchise_tricycle`;

ALTER TABLE `vehicledb`.`vehicle_franchise` 
   ADD COLUMN `owner_objid` VARCHAR(50) NULL AFTER `dtregistered`, 
   ADD COLUMN `owner_name` VARCHAR(255) NULL AFTER `owner_objid`, 
   ADD COLUMN `owner_address_objid` VARCHAR(50) NULL AFTER `owner_name`, 
   ADD COLUMN `owner_address_text` VARCHAR(255) NULL AFTER `owner_address_objid`, 
   ADD COLUMN `barangay_objid` VARCHAR(50) NULL AFTER `owner_address_text`, 
   ADD COLUMN `barangay_name` VARCHAR(155) NULL AFTER `barangay_objid`,
   ADD COLUMN `activeyear` INT ; 

UPDATE vehicle_franchise f, vehicle_application a
SET f.owner_objid=a.owner_objid,
f.owner_name=a.owner_name,
f.owner_address_objid=a.owner_address_objid,
f.owner_address_text=a.owner_address_text,
f.barangay_objid=a.barangay_objid,
f.barangay_name=a.barangay_name,
f.activeyear=a.appyear
WHERE f.appid=a.objid;

CREATE TABLE `vehicle_franchise_tricycle` (
  `objid` VARCHAR(50) NOT NULL,
  `plateno` VARCHAR(50) NOT NULL,
  `engineno` VARCHAR(50) DEFAULT NULL,
  `chassisno` VARCHAR(50) DEFAULT NULL,
  `make` VARCHAR(50) DEFAULT NULL,
  `model` VARCHAR(50) DEFAULT NULL,
  `color` VARCHAR(50) DEFAULT NULL,
  `sidecarno` VARCHAR(50) DEFAULT NULL,
  `bodyno` VARCHAR(50) DEFAULT NULL,
  `sidecarcolor` VARCHAR(50) DEFAULT NULL,
  `crname` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`objid`),
  CONSTRAINT `fk_tricycle_franchise_vehicle` FOREIGN KEY (`objid`) REFERENCES `vehicle_franchise` (`objid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO vehicle_franchise_tricycle 
SELECT controlid, plateno, engineno, chassisno, make, model, color, sidecarno, 
bodyno, sidecarcolor, crname FROM vehicle_tricycle
WHERE NOT(controlid IS NULL); 

INSERT INTO vehicle_franchise_tricycle (objid)
SELECT objid FROM vehicle_franchise WHERE objid NOT IN (SELECT objid FROM vehicle_franchise_tricycle );

ALTER TABLE `vehicledb`.`vehicle_application` DROP COLUMN `vehicleid`;

DROP TABLE vehicle_tricycle;