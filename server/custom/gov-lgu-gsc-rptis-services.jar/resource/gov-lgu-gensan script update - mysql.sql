
alter table rptacknowledgement_item 
	add reftype varchar(50),
	add refno varchar(50);

alter table rptacknowledgement_item 
	change column faas_objid ref_objid varchar(50);

update rptacknowledgement_item i, faas f set 
	i.reftype = 'faas', 
	i.refno = f.tdno 
where i.ref_objid = f.objid;


