/* v2.5.04.032-03005 */

alter table rpt_changeinfo add refid varchar(50)
go 

update rpt_changeinfo set refid = faasid where refid is null
go 



/* LEDGER RESTRICTION SUPPORT */
create table rptledger_restriction
(
	objid nvarchar(50) not null,
	parentid nvarchar(50) not null, 
	restrictionid nvarchar(50) not null,
	remarks nvarchar(150),
	primary key (objid)
)
GO 


alter table rptledger_restriction 
	add constraint FK_rptledger_restriction_rptledger 
	foreign key(parentid) references rptledger(objid)
GO 

create unique index ux_rptledger_restriction on rptledger_restriction(parentid, restrictionid)
GO 
