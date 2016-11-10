INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('BEFORE-ADDITIONAL', 'bldgassessment', 'Before Additional Item Computation', '17');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('ADDITIONAL', 'bldgassessment', 'Additional Item Computation', '18');
INSERT INTO sys_rulegroup (name, ruleset, title, sortorder) VALUES ('AFTER-ADDITIONAL', 'bldgassessment', 'After Additional Item Computation', '19');
INSERT INTO rptparameter (objid, state, name, caption, description, paramtype, minvalue, maxvalue) VALUES ('RP1cb5fe2e:14cdb1a6034:-78d9', 'APPROVED', 'TOTAL_ADDITIONAL', 'TOTAL_ADDITIONAL', NULL, 'decimal', '0.00', '0.00');



create table rpt_changeinfo
(
	objid varchar(50) primary key,
	faasid varchar(50),
	rpid varchar(50),
	rpuid varchar(50),
	action varchar(100) not null,
	reason text not null,
	newinfo text not null, 
	previnfo text not null,
	dtposted datetime not null,
	postedbyid varchar(50),
	postedby varchar(100) not null,
	postedbytitle varchar(100) not null
);



update planttreedetail set assesslevel = 20 where assesslevel = 0;

create table faas_signatory
(
	objid varchar(50) not null,
	taxmapper_objid varchar(50),
	taxmapper_name varchar(100),
	taxmapper_title varchar(50),
	taxmapper_dtsigned datetime,
	taxmapper_taskid varchar(50),

	taxmapperchief_objid varchar(50),
	taxmapperchief_name varchar(100),
	taxmapperchief_title varchar(50),
	taxmapperchief_dtsigned datetime,
	taxmapperchief_taskid varchar(50),

	appraiser_objid varchar(50),
	appraiser_name varchar(100),
	appraiser_title varchar(50),
	appraiser_dtsigned datetime,
	appraiser_taskid varchar(50),

	appraiserchief_objid varchar(50),
	appraiserchief_name varchar(100),
	appraiserchief_title varchar(50),
	appraiserchief_dtsigned datetime,
	appraiserchief_taskid varchar(50),

	recommender_objid varchar(50),
	recommender_name varchar(100),
	recommender_title varchar(50),
	recommender_dtsigned datetime,
	recommender_taskid varchar(50),

	provtaxmapper_objid varchar(50),
	provtaxmapper_name varchar(100),
	provtaxmapper_title varchar(50),
	provtaxmapper_dtsigned datetime,
	provtaxmapper_taskid varchar(50),

	provtaxmapperchief_objid varchar(50),
	provtaxmapperchief_name varchar(100),
	provtaxmapperchief_title varchar(50),
	provtaxmapperchief_dtsigned datetime,
	provtaxmapperchief_taskid varchar(50),

	provappraiser_objid varchar(50),
	provappraiser_name varchar(100),
	provappraiser_title varchar(50),
	provappraiser_dtsigned datetime,
	provappraiser_taskid varchar(50),

	provappraiserchief_objid varchar(50),
	provappraiserchief_name varchar(100),
	provappraiserchief_title varchar(50),
	provappraiserchief_dtsigned datetime,
	provappraiserchief_taskid varchar(50),

	approver_objid varchar(50),
	approver_name varchar(100),
	approver_title varchar(50),
	approver_dtsigned datetime,
	approver_taskid varchar(50),

	primary key(objid)
)	;

alter table faas_signatory 
add constraint FK_faas_faas_signatory foreign key (objid) 
references faas(objid);


-- create signatory temp table
create table xsignatory
(
	taskid varchar(50),
	objid varchar(50),
	userid varchar(50),
	name varchar(100),
	title varchar(100),
	dtsigned datetime,
	type varchar(50),
	primary key(taskid)
);

create index ix_xsignatory_objid on xsignatory(objid);



insert into faas_signatory (objid)
select objid from faas; 

-- insert resolved signatories to temp table 
insert into xsignatory 
	(taskid, objid, userid, name, title, dtsigned, type)
SELECT  DISTINCT
		ft.objid, x.refid, 
		ft.actor_objid as userid, ft.actor_name as name, ft.actor_title as title,
		x.assignee_dtsigned as dtsigned, x.type 
	FROM faas_task ft, 
	(
		SELECT refid, state AS type, max(enddate) AS assignee_dtsigned
		FROM faas_task 
		WHERE actor_name IS NOT NULL
		GROUP BY refid, state
	) x
	where ft.refid = x.refid
	and ft.state = x.type 
	and ft.enddate = x.assignee_dtsigned;

-- update faas_signatory using temp signatory table 

update faas_signatory fs, xsignatory x set 
	fs.taxmapper_taskid = x.taskid,
	fs.taxmapper_objid = x.userid,
	fs.taxmapper_name = x.name,
	fs.taxmapper_title = x.title,
	fs.taxmapper_dtsigned = x.dtsigned
where fs.objid = x.objid  and x.type = 'taxmapper';

update faas_signatory fs, xsignatory x set 
	fs.taxmapperchief_taskid = x.taskid,
	fs.taxmapperchief_objid = x.userid,
	fs.taxmapperchief_name = x.name,
	fs.taxmapperchief_title = x.title,
	fs.taxmapperchief_dtsigned = x.dtsigned
where fs.objid = x.objid  and x.type = 'taxmapper_chief';


update faas_signatory fs, xsignatory x set 
	fs.appraiser_taskid = x.taskid,
	fs.appraiser_objid = x.userid,
	fs.appraiser_name = x.name,
	fs.appraiser_title = x.title,
	fs.appraiser_dtsigned = x.dtsigned
where fs.objid = x.objid  and x.type = 'appraiser';

update faas_signatory fs, xsignatory x set 
	fs.appraiserchief_taskid = x.taskid,
	fs.appraiserchief_objid = x.userid,
	fs.appraiserchief_name = x.name,
	fs.appraiserchief_title = x.title,
	fs.appraiserchief_dtsigned = x.dtsigned
where fs.objid = x.objid  and x.type = 'appaiser_chief';


update faas_signatory fs, xsignatory x set 
	fs.recommender_taskid = x.taskid,
	fs.recommender_objid = x.userid,
	fs.recommender_name = x.name,
	fs.recommender_title = x.title,
	fs.recommender_dtsigned = x.dtsigned
where fs.objid = x.objid  and x.type = 'recommender';



update faas_signatory fs, xsignatory x set 
	fs.approver_taskid = x.taskid,
	fs.approver_objid = x.userid,
	fs.approver_name = x.name,
	fs.approver_title = x.title,
	fs.approver_dtsigned = x.dtsigned
where fs.objid = x.objid  and x.type = 'approver';


