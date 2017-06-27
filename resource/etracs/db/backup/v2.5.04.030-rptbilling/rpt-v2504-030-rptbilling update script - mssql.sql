/* v2.5.04.030b */

/* DROP DEFAULT CONSTRAINT */
IF OBJECT_ID (N'dbo.ufn_getDropDefaultConstraintStatement', N'FN') IS NOT NULL
    DROP FUNCTION dbo.ufn_getDropDefaultConstraintStatement
GO

CREATE FUNCTION dbo.ufn_getDropDefaultConstraintStatement (@table_name varchar(100), @col_name varchar(100))
RETURNS varchar(1000)
WITH EXECUTE AS CALLER
AS
BEGIN
  declare @Command  nvarchar(1000)

  select @Command = 'ALTER TABLE ' + @table_name + ' drop constraint ' + d.name
   from sys.tables t   
    join    sys.default_constraints d       
     on d.parent_object_id = t.object_id  
    join    sys.columns c      
     on c.object_id = t.object_id      
      and c.column_id = d.parent_column_id
   where t.name = @table_name
    and c.name = @col_name
  return @command 
END
GO


IF EXISTS(SELECT * FROM sys.indexes 
      WHERE name='ix_rptledger_statebrgy' AND object_id = OBJECT_ID('rptledger '))
BEGIN 
  drop index rptledger.ix_rptledger_statebrgy
end 



IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'firstqtrpaidontime' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column firstqtrpaidontime int null 
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'qtrlypaymentavailed' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column qtrlypaymentavailed int null 
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'qtrlypaymentpaidontime' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column qtrlypaymentpaidontime int null 
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'lastitemyear' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column lastitemyear int null 
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'lastreceiptid' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column lastreceiptid varchar(50)  null 
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'advancebill' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column advancebill int null 
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'lastbilledyear' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column lastbilledyear int null 
END
go 
IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'lastbilledqtr' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column lastbilledqtr int null 
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialbasic' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  declare @command varchar(1000)
  set @command = dbo.ufn_getDropDefaultConstraintStatement('rptledger', 'partialbasic')
  if @command is not null 
  begin 
    print '-> ' + @command 
    execute(@command);
  end 
  alter table rptledger alter column partialbasic decimal(16,2) null;
END
go 


IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialbasicint' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  declare @command varchar(1000)
  set @command = dbo.ufn_getDropDefaultConstraintStatement('rptledger', 'partialbasicint')
  if @command is not null 
  begin 
    execute(@command)
  end 
  alter table rptledger alter column partialbasicint decimal(16,2) null
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialbasicdisc' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  declare @command varchar(1000)
  set @command = dbo.ufn_getDropDefaultConstraintStatement('rptledger', 'partialbasicdisc')
  if @command is not null 
  begin 
    execute(@command)
  end 
  alter table rptledger alter column partialbasicdisc decimal(16,2) null
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialsef' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  declare @command varchar(1000)
  set @command = dbo.ufn_getDropDefaultConstraintStatement('rptledger', 'partialsef')
  if @command is not null 
  begin 
    execute(@command)
  end 
  alter table rptledger alter column partialsef decimal(16,2) null
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialsefint' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  declare @command varchar(1000)
  set @command = dbo.ufn_getDropDefaultConstraintStatement('rptledger', 'partialsefint')
  if @command is not null 
  begin 
    execute(@command)
  end 
  alter table rptledger alter column partialsefint decimal(16,2) null
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialsefdisc' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  declare @command varchar(1000)
  set @command = dbo.ufn_getDropDefaultConstraintStatement('rptledger', 'partialsefdisc')
  if @command is not null 
  begin 
    execute(@command)
  end 
  alter table rptledger alter column partialsefdisc decimal(16,2) null
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialledyear' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column partialledyear int null
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'partialledqtr' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger alter column partialledqtr int null 
END
go 


IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'updateflag' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger add updateflag varchar(50)
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'forcerecalcbill' AND Object_ID = Object_ID(N'rptledger'))
BEGIN
  alter table rptledger add forcerecalcbill int
END
go 



update rptledger set 
    nextbilldate = null, forcerecalcbill = 0, updateflag = objid 
where forcerecalcbill is null
go 


IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basic' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basic decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicpaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicpaid decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicint' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicint  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicintpaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicintpaid  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicdisc' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicdisc decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicdisctaken' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicdisctaken  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicidle' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicidle decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicidlepaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicidlepaid decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicidledisc' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicidledisc decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicidledisctaken' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicidledisctaken  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicidleint' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicidleint  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'basicidleintpaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add basicidleintpaid  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'sef' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add sef decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'sefpaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add sefpaid decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'sefint' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add sefint  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'sefintpaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add sefintpaid  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'sefdisc' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add sefdisc decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'sefdisctaken' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add sefdisctaken  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'firecode' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add firecode  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'firecodepaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add firecodepaid  decimal(16,2) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'revperiod' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add revperiod varchar(50) null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'qtrly' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add qtrly integer null
END
go 

IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'fullypaid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem add fullypaid integer null
END
go 



update rptledgeritem set 
  fullypaid = paid,
  basic = 0.0,
  basicpaid = 0.0,
  basicint = 0.0,
  basicintpaid = 0.0,
  basicdisc = 0.0,
  basicdisctaken = 0.0,
  basicidle = 0.0,
  basicidlepaid = 0.0,
  basicidledisc = 0.0,
  basicidledisctaken = 0.0,
  basicidleint = 0.0,
  basicidleintpaid = 0.0,
  sef = 0.0,
  sefpaid = 0.0,
  sefint = 0.0,
  sefintpaid = 0.0,
  sefdisc = 0.0,
  sefdisctaken = 0.0,
  firecode = 0.0,
  firecodepaid = 0.0
where basic is null;



IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'paidqtr' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem drop column paidqtr
END
go 

IF EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'paid' AND Object_ID = Object_ID(N'rptledgeritem'))
BEGIN
  alter table rptledgeritem drop column paid
END
go 



IF  EXISTS (SELECT * FROM sys.objects 
            WHERE object_id = OBJECT_ID('rptledgeritem_qtrly') AND type in ('U'))
BEGIN
  drop table rptledgeritem_qtrly
END
go 


create table rptledgeritem_qtrly (
  objid varchar(50) not null,
  parentid varchar(50) not null,
  rptledgerid varchar(50) not null,
  basicav decimal(16,2) not null,
  sefav decimal(16,2) not null,
  av decimal(16,2) not null,
  year integer not null,
  qtr integer not null,
  basic decimal(16,2) not null,
  basicpaid decimal(16,2) not null,
  basicint decimal(16,2) not null,
  basicintpaid decimal(16,2) not null,
  basicdisc decimal(16,2) not null,
  basicdisctaken decimal(16,2) not null,
  basicidle decimal(16,2) not null,
  basicidlepaid decimal(16,2) not null,
  basicidledisc decimal(16,2) not null,
  basicidledisctaken decimal(16,2) not null,
  basicidleint decimal(16,2) not null,
  basicidleintpaid decimal(16,2) not null,
  sef decimal(16,2) not null,
  sefpaid decimal(16,2) not null,
  sefint decimal(16,2) not null,
  sefintpaid decimal(16,2) not null,
  sefdisc decimal(16,2) not null,
  sefdisctaken decimal(16,2) not null,
  firecode decimal(16,2) not null,
  firecodepaid decimal(16,2) not null,
  revperiod varchar(50) default null,
  partialled integer not null,
  fullypaid integer not null,
  primary key  (objid)
)
go 

  
create index FK_rptledgeritemqtrly_rptledgeritem on rptledgeritem_qtrly(parentid)
go 

create index FK_rptledgeritemqtrly_rptledger on rptledgeritem_qtrly(rptledgerid)
go 

create index ix_rptledgeritemqtrly_year on rptledgeritem_qtrly(year)
go 


alter table rptledgeritem_qtrly 
  add constraint FK_rptledgeritemqtrly_rptledger 
  foreign key (rptledgerid) references rptledger (objid)
go 

alter table rptledgeritem_qtrly 
  add constraint FK_rptledgeritemqtrly_rptledgeritem 
  foreign key (parentid) references rptledgeritem (objid)
go 




IF not EXISTS(SELECT * FROM sys.columns 
            WHERE Name = N'rptledgeritemqtrlyid' AND Object_ID = Object_ID(N'cashreceiptitem_rpt_online'))
BEGIN
  alter table cashreceiptitem_rpt_online add rptledgeritemqtrlyid varchar(50)
END
go 


IF not EXISTS (SELECT * 
           FROM sys.foreign_keys 
           WHERE object_id = OBJECT_ID('FK_cashreceiptitem_rpt_online_rptledgeritemqtrly') 
             AND parent_object_id = OBJECT_ID('cashreceiptitem_rpt_online'))
BEGIN
  alter table cashreceiptitem_rpt_online 
    add constraint FK_cashreceiptitem_rpt_online_rptledgeritemqtrly foreign key(rptledgeritemqtrlyid)
    references rptledgeritem_qtrly(objid)

END
go 


IF EXISTS (SELECT * FROM sys.objects 
WHERE object_id = OBJECT_ID('rptbill_ledger_account') AND type in (N'U'))

BEGIN
  DROP TABLE rptbill_ledger_account;
  DROP TABLE rptbill_ledger_item;
  DROP TABLE rptbill_ledger;
END
go 


IF not EXISTS (SELECT * FROM sys.objects 
WHERE object_id = OBJECT_ID('rptbill_ledger') AND type in (N'U'))
begin 

  CREATE TABLE rptbill_ledger (
    rptledgerid varchar(50) NOT NULL default '',
    billid varchar(50) NOT NULL default '',
    updateflag varchar(50),
    PRIMARY KEY  (rptledgerid,billid)
  );

  create index ix_rptbill_ledgter_rptbillid on  rptbill_ledger(billid);
  create index ix_rptbill_ledgter_rptledgerid on  rptbill_ledger(rptledgerid);

  alter table rptbill_ledger 
  add CONSTRAINT FK_rptbillledger_rptledger 
  FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid);

  alter table rptbill_ledger 
  add CONSTRAINT FK_rptbillledger_rptbill 
  FOREIGN KEY (billid) REFERENCES rptbill(objid);

end
go 


IF not EXISTS (SELECT * FROM sys.objects 
WHERE object_id = OBJECT_ID('rptbill_ledger_account') AND type in (N'U'))
begin 

  CREATE TABLE rptbill_ledger_account (
    objid varchar(50) NOT NULL,
    billid varchar(50) NOT NULL,
    rptledgerid varchar(50) NOT NULL,
    revperiod varchar(25) NOT NULL,
    revtype varchar(25) NOT NULL,
    item_objid varchar(50) NOT NULL,
    amount decimal(16,4) NOT NULL,
    sharetype varchar(25) NOT NULL,
    discount decimal(16,4) default NULL,
    PRIMARY KEY  (objid)
  );


  create index ix_rptbill_ledger_account_rptledger on rptbill_ledger_account(rptledgerid);
  create index ix_rptbillledgeraccount_revenueitem on rptbill_ledger_account(item_objid);
  create index FK_rptbillledgeraccount_rptbill on rptbill_ledger_account(billid);


  alter table rptbill_ledger_account 
  add CONSTRAINT FK_rptbillledgeraccount_rptbill 
  FOREIGN KEY (billid) REFERENCES rptbill (objid);

  alter table rptbill_ledger_account 
  add CONSTRAINT rptbill_ledger_account_ibfk_1 
  FOREIGN KEY (item_objid) REFERENCES itemaccount (objid);


  alter table rptbill_ledger_account 
  add CONSTRAINT rptbill_ledger_account_ibfk_2 
  FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid);

end 
go 


IF not EXISTS (SELECT * FROM sys.objects 
WHERE object_id = OBJECT_ID('rptbill_ledger_item') AND type in (N'U'))
begin 
  CREATE TABLE rptbill_ledger_item (
    objid varchar(75) NOT NULL,
    billid varchar(50) NOT NULL,
    rptledgerid varchar(50) NOT NULL,
    rptledgeritemid varchar(50) NULL,
    rptledgeritemqtrlyid varchar(50) NULL,
    rptledgerfaasid varchar(50) NOT NULL,
    av decimal(16,2) default NULL,
    basicav decimal(16,2) default NULL,
    sefav decimal(16,2) default NULL,
    year integer NOT NULL,
    qtr integer NOT NULL,
    basic decimal(16,2) NOT NULL,
    basicint decimal(16,2) NOT NULL,
    basicdisc decimal(16,2) NOT NULL,
    sef decimal(16,2) NOT NULL,
    sefint decimal(16,2) NOT NULL,
    sefdisc decimal(16,2) NOT NULL,
    firecode decimal(10,2) default NULL,
    revperiod varchar(25) default NULL,
    basicnet decimal(16,2) default NULL,
    sefnet decimal(16,2) default NULL,
    total decimal(16,2) default NULL,
    partialled integer NOT NULL,
    basicidle decimal(16,2) default NULL,
    basicidledisc decimal(16,2) default NULL,
    basicidleint decimal(16,2) default NULL,
    taxdifference integer default NULL,
    PRIMARY KEY  (objid)  
  );

  create index FK_rptbillledgeritem_rptledger on rptbill_ledger_item(rptledgerid);
  create index FK_rptbillledgeritem_rptledgerfaas on rptbill_ledger_item(rptledgerfaasid);
  create index FK_rptbillledgeritem_rptledgeritem on rptbill_ledger_item(rptledgeritemid);
  create index FK_rptbillledgeritem_rptledgeritemqtrly on rptbill_ledger_item(rptledgeritemqtrlyid);
  create index FK_rptbillledgeritem_rptbill on rptbill_ledger_item(billid);

  alter table rptbill_ledger_item
  add CONSTRAINT FK_rptbillledgeritem_rptbill 
  FOREIGN KEY (billid) REFERENCES rptbill (objid);

  alter table rptbill_ledger_item
  add CONSTRAINT FK_rptbillledgeritem_rptledger 
  FOREIGN KEY (rptledgerid) REFERENCES rptledger (objid);

  alter table rptbill_ledger_item
  add CONSTRAINT FK_rptbillledgeritem_rptledgerfaas 
  FOREIGN KEY (rptledgerfaasid) REFERENCES rptledgerfaas (objid);

  alter table rptbill_ledger_item
  add CONSTRAINT FK_rptbillledgeritem_rptledgeritem 
  FOREIGN KEY (rptledgeritemid) REFERENCES rptledgeritem(objid);

  alter table rptbill_ledger_item
  add CONSTRAINT FK_rptbillledgeritem_rptledgeritemqtrly 
  FOREIGN KEY (rptledgeritemqtrlyid) REFERENCES rptledgeritem_qtrly (objid);


end 
go 


if not exists(select * from sys_usergroup_permission where objid = 'LANDTAX.ADMIN-add_new_ledger_faas')
begin 
  INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('LANDTAX.ADMIN-add_new_ledger_faas', 'LANDTAX.ADMIN', 'rptledger', 'add_new_ledger_faas', 'Add New Ledger FAAS');
  INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('LANDTAX.ADMIN-change_faas_reference', 'LANDTAX.ADMIN', 'rptledger', 'change_faas_reference', 'Change FAAS Reference');
  INSERT INTO sys_usergroup_permission (objid, usergroup_objid, object, permission, title) VALUES ('LANDTAX.ADMIN-fix_ledger_faas', 'LANDTAX.ADMIN', 'rptledger', 'fix_ledger_faas', 'Fix Ledger FAAS');
end 
go 




/* DROP DEFAULT CONSTRAINT */
IF OBJECT_ID (N'dbo.ufn_getDropDefaultConstraintStatement', N'FN') IS NOT NULL
    DROP FUNCTION dbo.ufn_getDropDefaultConstraintStatement
GO
