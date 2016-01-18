alter table bldgrpu_structuraltype add classification_objid nvarchar(50);


alter table bldgrpu_structuraltype 
add CONSTRAINT FK_bldgrpu_structuraltype foreign key(classification_objid)
references propertyclassification(objid);


ALTER TABLE bldgrpu_assessment 
	ADD classification_objid  nvarchar(50) NOT NULL;

ALTER TABLE bldgrpu_assessment 
	ADD areasqm  decimal(16,2) NOT NULL DEFAULT 0;

ALTER TABLE bldgrpu_assessment 
	ADD areaha  decimal(16,6) NOT NULL DEFAULT 0;

ALTER TABLE bldgrpu_assessment 
	ADD CONSTRAINT FK_bldgrpu_assessment_classificationid foreign key (classification_objid)
	references propertyclassification(objid);


