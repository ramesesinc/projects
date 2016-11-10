CREATE TABLE `itemaccount_tag` (
  `acctid` VARCHAR(50) CHARACTER SET utf8 NOT NULL,
  `tag` VARCHAR(50) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`acctid`,`tag`),
  CONSTRAINT `itemaccount_tag_itemaccount` FOREIGN KEY (`acctid`) REFERENCES `itemaccount` (`objid`)
) ENGINE=INNODB 