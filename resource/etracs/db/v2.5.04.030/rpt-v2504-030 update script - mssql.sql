
/*==================================
** V2.5.04.030
==================================*/
if not exists(select * from sys_rulegroup where namne = 'AFTER-SUMMARY' and ruleset = 'landassessment')
begin 
  INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'landassessment', 'After Summary Computation', '105');
  INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'bldgassessment', 'After Summary Computation', '105');
  INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'machassessment', 'After Summary Computation', '105');
  INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'miscassessment', 'After Summary Computation', '105');
  INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-SUMMARY', 'planttreeassessment', 'After Summary Computation', '105');
  INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MUASSESSLEVEL', 'machassessment', 'Actual Use Assess Level Computation', '50');
  INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('MUASSESSEDVALUE', 'machassessment', 'Actual Use Assessed Value Computation', '55');

end 
go 


UPDATE sys_rulegroup SET name='INITIAL', ruleset='machassessment', title='Initial Computation', sortorder='0' WHERE (name='INITIAL') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='BASEMARKETVALUE', ruleset='machassessment', title='Machine Base Market Value Computation', sortorder='5' WHERE (name='BASEMARKETVALUE') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='AFTER-BASEMARKETVALUE', ruleset='machassessment', title='After Machine Base Market Value Computation', sortorder='10' WHERE (name='AFTER-BASEMARKETVALUE') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='DEPRECIATION', ruleset='machassessment', title='Machine Depreciation Computation', sortorder='11' WHERE (name='DEPRECIATION') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='AFTER-DEPRECIATION', ruleset='machassessment', title='After Machine Depreciation Computation', sortorder='12' WHERE (name='AFTER-DEPRECIATION') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='MARKETVALUE', ruleset='machassessment', title='Machine Market Value Computation', sortorder='25' WHERE (name='MARKETVALUE') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='AFTER-MARKETVALUE', ruleset='machassessment', title='After Machine Market Value Computation', sortorder='30' WHERE (name='AFTER-MARKETVALUE') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='ASSESSLEVEL', ruleset='machassessment', title='Machine Assess Level Computation', sortorder='35' WHERE (name='ASSESSLEVEL') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='AFTER-ASSESSLEVEL', ruleset='machassessment', title='After Machine Assess Level Computation', sortorder='36' WHERE (name='AFTER-ASSESSLEVEL') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='ASSESSEDVALUE', ruleset='machassessment', title='Machine Assessed Value Computation', sortorder='40' WHERE (name='ASSESSEDVALUE') AND (ruleset='machassessment')
go 
UPDATE sys_rulegroup SET name='AFTER-ASSESSEDVALUE', ruleset='machassessment', title='After Machine Assessed Value Computation', sortorder='45' WHERE (name='AFTER-ASSESSEDVALUE') AND (ruleset='machassessment')
go 



IF NOT EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'info' AND Object_ID = Object_ID(N'rpt_redflag'))
begin 
	alter table rpt_redflag add info text
end 
go 


IF NOT EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'addlinfo' AND Object_ID = Object_ID(N'bldguse'))
BEGIN
    alter table bldguse add addlinfo varchar(255)
END
go 

IF NOT EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'provrecommender_objid' AND Object_ID = Object_ID(N'faas_signatory'))
BEGIN
  alter table faas_signatory add provrecommender_objid varchar(50);
  alter table faas_signatory add provrecommender_name  varchar(100);
  alter table faas_signatory add provrecommender_title varchar(50);
  alter table faas_signatory add provrecommender_dtsigned  datetime;
  alter table faas_signatory add provrecommender_taskid  varchar(50);
end 
go 

if exists(SELECT * FROM sys.indexes 
					WHERE name='ix_subdividedland_newtdno' AND object_id = OBJECT_ID('subdividedland'))
begin 
	drop index subdividedland.ix_subdividedland_newtdno
end  
go 


if exists(SELECT * FROM sys.indexes 
					WHERE name='ix_subdividedland_administrator_name' AND object_id = OBJECT_ID('subdividedland'))
begin 
	drop index subdividedland.ix_subdividedland_administrator_name
end 


if exists(SELECT * FROM sys.indexes 
					WHERE name='fk_subdividedland_newrpu' AND object_id = OBJECT_ID('subdividedland'))
begin 
	drop index subdividedland.fk_subdividedland_newrpu
end  
go 


if exists(SELECT * FROM sys.indexes 
					WHERE name='fk_subdividedland_faas' AND object_id = OBJECT_ID('subdividedland'))
begin 
	drop index subdividedland.fk_subdividedland_faas
end  
go 


IF EXISTS (SELECT * FROM sys.foreign_keys 
           WHERE object_id = OBJECT_ID(N'[dbo].[FK__subdivide__newrp__587208C1]') 
             AND parent_object_id = OBJECT_ID(N'[dbo].[subdividedland]'))
BEGIN
	alter table subdividedland drop constraint FK__subdivide__newrp__587208C1
END
go  


IF EXISTS (SELECT * FROM sys.foreign_keys 
           WHERE object_id = OBJECT_ID(N'[dbo].[FK__subdivide__newrp__587208C1]') 
             AND parent_object_id = OBJECT_ID(N'[dbo].[subdividedland]'))
BEGIN
	alter table subdividedland drop constraint FK__subdivide__newrp__587208C1
END
go  

IF EXISTS (SELECT * FROM sys.foreign_keys 
           WHERE object_id = OBJECT_ID(N'[dbo].[FK__subdivide__newfa__5689C04F]') 
             AND parent_object_id = OBJECT_ID(N'[dbo].[subdividedland]'))
BEGIN
	alter table subdividedland drop constraint FK__subdivide__newfa__5689C04F
END
go  

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'itemno' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column itemno varchar(10) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtdno' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column newtdno  varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newutdno' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column newutdno varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtitletype' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column newtitletype varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtitleno' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column newtitleno varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtitledate' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column newtitledate varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'areasqm' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column areasqm  decimal(16,6) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'areaha' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column areaha decimal(16,6) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'memoranda' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column memoranda  varchar(500)  null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administrator_objid' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column administrator_objid  varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administrator_name' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column administrator_name varchar(200)  null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administrator_address' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column administrator_address  varchar(200)  null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'taxpayer_objid' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column taxpayer_objid varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'taxpayer_name' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column taxpayer_name  varchar(200)  null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'taxpayer_address' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column taxpayer_address varchar(200)  null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'owner_name' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column owner_name varchar(200)  null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'owner_address' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column owner_address  varchar(200)  null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newrpuid' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column newrpuid varchar(50) null
END 

go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newfaasid' AND Object_ID = Object_ID(N'subdividedland'))
BEGIN
  alter table subdividedland alter column newfaasid  varchar(50) null
END 
go 


 

if exists(SELECT * FROM sys.indexes 
					WHERE name='txntype_objid' AND object_id = OBJECT_ID('consolidation'))
begin 
	drop index consolidation.txntype_objid
end  
go 

if exists(SELECT * FROM sys.indexes 
					WHERE name='ix_consolidation_newtdno' AND object_id = OBJECT_ID('consolidation'))
begin 
	drop index consolidation.ix_consolidation_newtdno
end  
go 

if exists(SELECT * FROM sys.indexes 
					WHERE name='fk_consolidation_newrp' AND object_id = OBJECT_ID('consolidation'))
begin 
	drop index consolidation.fk_consolidation_newrp
end  
go 

IF EXISTS (SELECT * FROM sys.foreign_keys 
           WHERE object_id = OBJECT_ID('FK__consolida__newrp__07970BFE') 
             AND parent_object_id = OBJECT_ID('consolidation'))
BEGIN
	alter table consolidation drop constraint FK__consolida__newrp__07970BFE
END
go  


if exists(SELECT * FROM sys.indexes 
					WHERE name='fk_consolidation_newrpu' AND object_id = OBJECT_ID('consolidation'))
begin 
	drop index consolidation.fk_consolidation_newrpu
end  
go 

IF EXISTS (SELECT * FROM sys.foreign_keys 
           WHERE object_id = OBJECT_ID('FK__consolida__newrp__088B3037') 
             AND parent_object_id = OBJECT_ID('consolidation'))
BEGIN
	alter table consolidation drop constraint FK__consolida__newrp__088B3037
END
go  



if exists(SELECT * FROM sys.indexes 
					WHERE name='fk_consolidation_newfaas' AND object_id = OBJECT_ID('consolidation'))
begin 
	drop index consolidation.fk_consolidation_newfaas
end  
go 

IF EXISTS (SELECT * FROM sys.foreign_keys 
           WHERE object_id = OBJECT_ID('FK__consolida__newfa__06A2E7C5') 
             AND parent_object_id = OBJECT_ID('consolidation'))
BEGIN
	alter table consolidation drop constraint FK__consolida__newfa__06A2E7C5
END
go  



IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'txndate' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column txndate datetime null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'txntype_objid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column txntype_objid varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'autonumber' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column autonumber  integer null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'effectivityyear' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column effectivityyear integer null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'effectivityqtr' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column effectivityqtr  integer null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtdno' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newtdno varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newutdno' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newutdno  varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtitletype' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newtitletype  varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtitleno' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newtitleno  varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newtitledate' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newtitledate  varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'memoranda' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column memoranda text null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'lguid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column lguid varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'lgutype' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column lgutype varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newrpid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newrpid varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newrpuid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newrpuid  varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'newfaasid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column newfaasid varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'taxpayer_objid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column taxpayer_objid  varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'taxpayer_name' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column taxpayer_name text null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'taxpayer_address' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column taxpayer_address  varchar(200) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'owner_name' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column owner_name  text null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'owner_address' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column owner_address varchar(200) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administrator_objid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column administrator_objid varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administrator_name' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column administrator_name  varchar(500) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administrator_address' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column administrator_address varchar(200) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administratorid' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column administratorid varchar(50) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administratorname' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column administratorname varchar(500) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'administratoraddress' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column administratoraddress  varchar(200) null
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'signatories' AND Object_ID = Object_ID(N'consolidation'))
BEGIN
  alter table consolidation alter column signatories varchar(500) null
END
go 

