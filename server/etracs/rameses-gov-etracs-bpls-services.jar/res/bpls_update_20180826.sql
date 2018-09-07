ALTER TABLE business_application ADD COLUMN taskid varchar( 50 );
ALTER TABLE  business_application_task CHANGE objid taskid VARCHAR(50);
ALTER TABLE business_application_task ADD COLUMN `dtcreated` datetime DEFAULT NULL;
ALTER TABLE business_application_task ADD COLUMN  `prevtaskid` varchar(50) DEFAULT NULL;
UPDATE business_application_task SET dtcreated=startdate;

UPDATE business_application ba, business_application_task bt 
SET ba.taskid=bt.taskid
WHERE ba.objid=bt.refid
AND bt.taskid = ( 
	SELECT taskid 
	FROM business_application_task 
	WHERE refid=ba.objid 
	AND enddate IS NULL
	ORDER BY dtcreated 
	LIMIT 1
)

UPDATE business_application ba, business_application_task bt 
SET ba.taskid=bt.taskid
WHERE ba.objid=bt.refid
AND ba.state = 'COMPLETED'
AND bt.state = 'end';


DROP TABLE business_active_lob;
DROP TABLE business_active_info;

CREATE TABLE `business_application_retire_lob` (
  `objid` varchar(50) NOT NULL,
  `applicationid` varchar(50) DEFAULT NULL,
  `businesslobid` varchar(50) NOT NULL,
  PRIMARY KEY (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE business_application_retire_lob 
ADD  KEY `applicationid` (`applicationid`);
ALTER TABLE business_application_retire_lob ADD UNIQUE KEY `UIX_business_retire_businesslobid` (`businesslobid`);

ALTER TABLE business_application_retire_lob ADD CONSTRAINT `FK_business_application_retire_lob_business_application` FOREIGN KEY (`applicationid`) REFERENCES `business_application` (`objid`);
ALTER TABLE business_application_retire_lob ADD   CONSTRAINT `FK_business_application_retire_lob_business_lob` FOREIGN KEY (`businesslobid`) REFERENCES `business_application_lob` (`objid`)  

ALTER TABLE business_application_lob ADD COLUMN particulars varchar(150);


ALTER TABLE businessrequirementtype DROP COLUMN code;
ALTER TABLE businessrequirementtype DROP COLUMN handler;
ALTER TABLE businessrequirementtype DROP COLUMN verifier;
ALTER TABLE businessrequirementtype CHANGE COLUMN agency org_name VARCHAR(50);
ALTER TABLE businessrequirementtype ADD COLUMN org_name VARCHAR(150);
ALTER TABLE businessrequirementtype ADD COLUMN requiredstate VARCHAR(150);

#business_lessor
ALTER TABLE business_lessor ADD COLUMN businessid VARCHAR(50) default null;
UPDATE business_lessor SET lessor_orgtype = 'GOV' WHERE government = 1;
ALTER TABLE business_lessor change column lessor_orgtype orgtype varchar(50);
UPDATE business_lessor SET orgtype='INDIVIDUAL' WHERE orgtype='SING';
UPDATE business_lessor bl, entityjuridical en
SET bl.orgtype = en.orgtype 
WHERE bl.lessor_objid = en.objid

