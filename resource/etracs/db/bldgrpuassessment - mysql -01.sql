create table bldgrpu_assessment
(
	objid varchar(50) primary key,
	bldgrpuid varchar(50) not null,
	actualuse_objid varchar(50) not null, 
	marketvalue numeric(16,2) not null,
	assesslevel numeric(16,2) not null,
	assessedvalue numeric(16,2) not null
) ENGINE=InnoDB;

alter table bldgrpu_assessment 
	add constraint FK_bldgrpuassessment_rpu foreign key (bldgrpuid)
	references bldgrpu(objid);


alter table bldgrpu_assessment 
	add constraint FK_bldgrpuassessment_bldgassesslevel foreign key (actualuse_objid)
	references bldgassesslevel(objid);

create index ix_bldgrpuassessment_bldgrpuid on bldgrpu_assessment(bldgrpuid);
create index ix_bldgrpuassessment_actualuseid on bldgrpu_assessment(actualuse_objid);


alter table bldguse change column assesslevel assesslevel numeric(16,2) null;
alter table bldguse change column assessedvalue assessedvalue numeric(16,2) null;


