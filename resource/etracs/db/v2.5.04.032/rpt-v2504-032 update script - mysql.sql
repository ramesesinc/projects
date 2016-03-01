/* v2.5.04.032 */
INSERT INTO `faas_txntype` (`objid`, `name`, `newledger`, `newrpu`, `newrealproperty`, `displaycode`, `allowEditOwner`, `checkbalance`, `allowEditPin`, `allowEditPinInfo`, `allowEditAppraisal`, `opener`) VALUES ('CA', 'Change Administrator', '0', '0', '0', 'DP', '0', '1', '0', '0', '1', NULL);


alter table subdivision add mothertdnos varchar(1000);
alter table subdivision add motherpins varchar(1000);


create index ix_subdivision_mothertdnos on subdivision(mothertdnos);
create index ix_subdivision_motherpins on subdivision(motherpins);

update subdivision s, subdivision_motherland sm, faas f  set 
	s.mothertdnos = f.tdno,
	s.motherpins = f.fullpin
where s.objid = sm.subdivisionid
  and sm.landfaasid = f.objid 
  and  1 = (select count(*) from subdivision_motherland 
			      where subdivisionid = s.objid );


alter table rptledger add administrator_name varchar(150);  



create index ix_rptledger_administartorname on rptledger(administrator_name);

			      	
			      	
update rptledger rl, faas f set 
	rl.administrator_name = f.administrator_name
where rl.faasid = f.objid;


