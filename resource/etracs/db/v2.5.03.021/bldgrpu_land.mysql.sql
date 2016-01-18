
CREATE TABLE `bldgrpu_land` (
  `objid` varchar(50) NOT NULL,
  `bldgrpuid` varchar(50) NOT NULL,
  `landfaas_objid` varchar(50) NOT NULL,
  PRIMARY KEY  (`objid`),
  UNIQUE KEY `ux_bldgrpu_land_bldgrpu_landfaas` (`bldgrpuid`,`landfaas_objid`),
  KEY `ix_bldgrpu_land_bldgrpuid` (`bldgrpuid`),
  KEY `ix_bldgrpu_land_landfaasid` (`landfaas_objid`) 
) ENGINE=InnoDB;  


ALTER TABLE `bldgrpu_land` 
	ADD CONSTRAINT `FK_bldgrpu_land_bldgrpu` 
	FOREIGN KEY (`bldgrpuid`) REFERENCES `bldgrpu` (`objid`);

ALTER TABLE `bldgrpu_land` 
	ADD CONSTRAINT `FK_bldgrpu_land_landfaas` 
	FOREIGN KEY (`landfaas_objid`) REFERENCES `faas` (`objid`);
