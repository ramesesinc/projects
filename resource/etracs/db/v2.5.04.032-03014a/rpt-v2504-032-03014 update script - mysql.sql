#254032-03014a

CREATE TABLE `rptledgeritem_qtrly_partial` (
  `objid` varchar(50) NOT NULL,
  `rptledgerid` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `qtr` int(11) NOT NULL,
  `basicpaid` decimal(16,2) NOT NULL,
  `basicintpaid` decimal(16,2) NOT NULL,
  `basicdisctaken` decimal(16,2) NOT NULL,
  `basicidlepaid` decimal(16,2) NOT NULL,
  `basicidledisctaken` decimal(16,2) NOT NULL,
  `sefpaid` decimal(16,2) NOT NULL,
  `sefintpaid` decimal(16,2) NOT NULL,
  `sefdisctaken` decimal(16,2) NOT NULL,
  `firecodepaid` decimal(16,2) NOT NULL,
  PRIMARY KEY (`objid`),
  KEY `FK_rptledgeritemqtrlypartial_rptledger` (`rptledgerid`),
  CONSTRAINT `FK_rptledgeritemqtrlypartial_rptledger` FOREIGN KEY (`rptledgerid`) REFERENCES `rptledger` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;