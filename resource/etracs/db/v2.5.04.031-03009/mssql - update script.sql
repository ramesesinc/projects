alter table subdivision add mothertdnos varchar(1000)
go 
alter table subdivision add motherpins varchar(1000)
go 


create index ix_subdivision_mothertdnos on subdivision(mothertdnos)
go 
create index ix_subdivision_motherpins on subdivision(motherpins)
go 

update s set 
	s.mothertdnos = f.tdno,
	s.motherpins = f.fullpin
from subdivision s, subdivision_motherland sm, faas f  
where s.objid = sm.subdivisionid
  and sm.landfaasid = f.objid 
  and  1 = (select count(*) from subdivision_motherland 
			      where subdivisionid = s.objid )
go 



alter table rptledger add administrator_name varchar(150)
go 



create index ix_rptledger_administartorname on rptledger(administrator_name)
go 

update rl set 
	rl.administrator_name = f.administrator_name
from  rptledger rl, faas f
where rl.faasid = f.objid
go 

			      	