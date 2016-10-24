alter table faas add column prevareaha numeric(16,6);
alter table faas add column prevareasqm numeric(16,2);


alter table bldgstructure add column floor int;
update bldgstructure set floor = 1;