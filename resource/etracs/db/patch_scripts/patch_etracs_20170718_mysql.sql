
create index ix_acctid on itemaccount_tag (acctid); 
alter table itemaccount_tag add objid varchar(50) null; 
update itemaccount_tag set objid=concat('ACCTAG-',UUID()) where objid is null;
alter table itemaccount_tag DROP PRIMARY KEY, ADD PRIMARY KEY(objid);
create unique index uix_acctid_tag on itemaccount_tag (acctid, tag);
