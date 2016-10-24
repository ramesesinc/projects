alter table bldgrpu_structuraltype add column classification_objid varchar(50);


alter table bldgrpu_structuraltype 
add CONSTRAINT FK_bldgrpu_structuraltype foreign key(classification_objid)
references propertyclassification(objid);



ALTER TABLE `bldgrpu_assessment`
ADD COLUMN `classification_objid`  varchar(50) NOT NULL AFTER `bldgrpuid`,
ADD COLUMN `areasqm`  decimal(16,2) NOT NULL AFTER `actualuse_objid`,
ADD COLUMN `areaha`  decimal(16,6) NOT NULL AFTER `areasqm`;



ALTER TABLE bldgrpu_assessment 
	ADD CONSTRAINT FK_bldgrpu_assessment_classificationid foreign key (classification_objid)
	references propertyclassification(objid);


