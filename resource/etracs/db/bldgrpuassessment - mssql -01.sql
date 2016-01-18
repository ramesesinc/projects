
create table bldgrpu_assessment
(
	objid nvarchar(50) primary key,
	bldgrpuid nvarchar(50) not null,
	actualuse_objid nvarchar(50) not null, 
	marketvalue numeric(16,2) not null,
	assesslevel numeric(16,2) not null,
	assessedvalue numeric(16,2) not null
);

alter table bldgrpu_assessment 
	add constraint FK_bldgrpuassessment_rpu foreign key (bldgrpuid)
	references bldgrpu(objid);


alter table bldgrpu_assessment 
	add constraint FK_bldgrpuassessment_bldgassesslevel foreign key (actualuse_objid)
	references bldgassesslevel(objid);

create index ix_bldgrpuassessment_bldgrpuid on bldgrpu_assessment(bldgrpuid);
create index ix_bldgrpuassessment_actualuseid on bldgrpu_assessment(actualuse_objid);


alter table bldguse alter column  assesslevel numeric(16,2) null;
alter table bldguse alter column  assessedvalue numeric(16,2) null;


