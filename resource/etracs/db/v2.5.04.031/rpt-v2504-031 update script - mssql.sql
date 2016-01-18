/* v2504031 */

/* CONSOLIDATOIN / SUBDIVISION SUPPORT */
IF  EXISTS (SELECT * FROM sys.objects 
WHERE object_id = OBJECT_ID('subdivision_consolidatedland') AND type in (N'U'))
BEGIN
  drop table subdivision_consolidatedland 
END
go 



IF  EXISTS (SELECT * FROM sys.objects 
WHERE object_id = OBJECT_ID('subdivision_motherland') AND type in (N'U'))
BEGIN
  drop table subdivision_motherland
END
go 



CREATE TABLE subdivision_motherland (
  objid nvarchar(50) NOT NULL,
  subdivisionid nvarchar(50) NOT NULL,
  landfaasid nvarchar(50) NOT NULL,
  rpuid varchar(50) NOT NULL,
  rpid varchar(50) NOT NULL,
  PRIMARY KEY  (objid),
  CONSTRAINT FK_subdivision_motherland_faas FOREIGN KEY (landfaasid) REFERENCES faas (objid),
  CONSTRAINT FK_subdivison_motherland_subdivision FOREIGN KEY (subdivisionid) REFERENCES subdivision (objid)
) 
go

create index FK_consolidatedland_faas on subdivision_motherland(landfaasid)
go 
create index FK_consolidatedland_subdivision on subdivision_motherland(subdivisionid)
go 
   


drop index subdivision.fk_subdivision_faas
go 

alter table subdivision drop constraint FK__subdivisi__mothe__5A5A5133
go 

alter table subdivision alter column motherfaasid varchar(50) null
go 



alter table faas alter column prevav varchar(200) null
go 
alter table faas alter column prevmv varchar(200) null
go 
alter table faas alter column prevareaha varchar(200) null
go 
alter table faas alter column prevareasqm varchar(200) null
go 



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
  inner join faas f on s.motherfaasid = f.objid
go 


ALTER TABLE subdivision DROP foreign key  subdivision_ibfk_1
go