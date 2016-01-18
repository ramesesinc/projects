/* v2504031 */

/* CONSOLIDATOIN / SUBDIVISION SUPPORT */

drop table if exists subdivision_consolidatedland;

drop table if exists subdivision_motherland;


CREATE TABLE subdivision_motherland (
  objid varchar(50) NOT NULL,
  subdivisionid varchar(50) NOT NULL,
  landfaasid varchar(50) NOT NULL,
  rpuid varchar(50) NOT NULL,
  rpid varchar(50) NOT NULL,
  PRIMARY KEY  (objid),
  KEY FK_consolidatedland_faas (landfaasid),
  KEY FK_consolidatedland_subdivision (subdivisionid),
  CONSTRAINT subdivision_motherland_ibfk_2 FOREIGN KEY (landfaasid) REFERENCES faas (objid),
  CONSTRAINT subdivison_motherland_ibfk_1 FOREIGN KEY (subdivisionid) REFERENCES subdivision (objid)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


alter table subdivision modify column motherfaasid varchar(50) null;


alter table faas modify column prevav text null,
  modify column prevmv text null,
  modify column prevareaha text null,
  modify column prevareasqm text null;



insert  into subdivision_motherland(
  objid,
  subdivisionid,
  landfaasid,
  rpuid,
  rpid
)
select 
  s.objid,
  s.objid as subdivisionid,
  s.motherfaasid as landfaasid,
  f.rpuid,
  f.realpropertyid as rpid
from subdivision s 
  inner join faas f on s.motherfaasid = f.objid;

  
ALTER TABLE subdivision DROP foreign key subdivision_ibfk_1;