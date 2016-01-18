USE clfc2;

ALTER TABLE `field_collection_cashbreakdown`
ADD COLUMN `routecode` VARCHAR(40) AFTER `parentid`;

CREATE TABLE `online_collection_route` (
  `onlinecollectionid` VARCHAR(40) NOT NULL,
  `routecode` VARCHAR(40) NOT NULL,
  PRIMARY KEY  (`onlinecollectionid`,`routecode`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

ALTER TABLE `online_collection_cashbreakdown`
ADD COLUMN `routecode` VARCHAR(40) AFTER `parentid`;