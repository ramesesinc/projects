/* v2504-023*/
alter table bldgrpu add condocerttitle varchar(50);
alter table bldgrpu add dtcertcompletion date;
alter table bldgrpu add dtcertoccupancy date;

alter table faas_signatory add provapprover_objid varchar(50);
alter table faas_signatory add provapprover_name varchar(100);
alter table faas_signatory add provapprover_title varchar(75);
alter table faas_signatory add provapprover_dtsigned datetime;
alter table faas_signatory add provapprover_taskid varchar(50);