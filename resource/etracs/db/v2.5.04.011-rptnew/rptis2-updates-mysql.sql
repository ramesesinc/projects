
alter table faas_txntype add allowEditOwner int;
alter table faas_txntype add allowEditPin int;
alter table faas_txntype add allowEditPinInfo int;
alter table faas_txntype add allowEditAppraisal int;
alter table faas_txntype add opener varchar(50);

UPDATE faas_txntype SET objid='CC', name='Change Classification', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='CC');
UPDATE faas_txntype SET objid='CD', name='Change Depreciation', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='CD');
UPDATE faas_txntype SET objid='CE', name='Correction of Entry', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='CE');
UPDATE faas_txntype SET objid='CS', name='Consolidation', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener='consolidation', allowEditAppraisal='1' WHERE (objid='CS');
UPDATE faas_txntype SET objid='CS/SD', name='Consolidation/Subdivision', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener='subdivision', allowEditAppraisal='1' WHERE (objid='CS/SD');
UPDATE faas_txntype SET objid='CT', name='Change Taxability', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='CT');
UPDATE faas_txntype SET objid='CTD', name='Cancellation', newledger='0', newrpu='0', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='0' WHERE (objid='CTD');
UPDATE faas_txntype SET objid='DC', name='Data Capture', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='DC');
UPDATE faas_txntype SET objid='GR', name='General Revision', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='GR');
UPDATE faas_txntype SET objid='MC', name='Multiple Claim', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='MC');
UPDATE faas_txntype SET objid='MCS', name='Multiple Claim Settlement', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener='mcsettlement', allowEditAppraisal='0' WHERE (objid='MCS');
UPDATE faas_txntype SET objid='ND', name='New Discovery', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='ND');
UPDATE faas_txntype SET objid='RE', name='Reassessment', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='0', allowEditPin='1', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='RE');
UPDATE faas_txntype SET objid='RS', name='Resection', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='1' WHERE (objid='RS');
UPDATE faas_txntype SET objid='RV', name='Revision', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='0', allowEditPin='1', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='RV');
UPDATE faas_txntype SET objid='SD', name='Subdivision', newledger='1', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener='subdivision', allowEditAppraisal='1' WHERE (objid='SD');
UPDATE faas_txntype SET objid='TR', name='Transfer of Ownership', newledger='0', newrpu='0', newrealproperty='0', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='0', opener=NULL, allowEditAppraisal='0' WHERE (objid='TR');
UPDATE faas_txntype SET objid='TRC', name='Transfer with Correction', newledger='0', newrpu='1', newrealproperty='1', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='TRC');
UPDATE faas_txntype SET objid='TRE', name='Transfer with Reassessment', newledger='0', newrpu='1', newrealproperty='0', displaycode=NULL, allowEditOwner='1', allowEditPin='0', allowEditPinInfo='1', opener=NULL, allowEditAppraisal='1' WHERE (objid='TRE');


/* SUBDIVISION */

-- show engine innodb status

alter table subdividedland modify column itemno varchar(50) null;
alter table subdividedland modify column newtdno varchar(50) null;
alter table subdividedland modify column newutdno varchar(50) null;
alter table subdividedland modify column newtitletype varchar(50) null;
alter table subdividedland modify column newtitleno varchar(50) null;
alter table subdividedland modify column newtitledate varchar(50) null;
alter table subdividedland modify column areasqm decimal(16,6) null;
alter table subdividedland modify column areaha decimal(16,6) null;
alter table subdividedland modify column memoranda varchar(500) null;
alter table subdividedland modify column administrator_objid varchar(50) null;
alter table subdividedland modify column administrator_name varchar(200) null;
alter table subdividedland modify column administrator_address varchar(200) null;
alter table subdividedland modify column taxpayer_objid varchar(50) null;
alter table subdividedland modify column taxpayer_name varchar(200) null;
alter table subdividedland modify column taxpayer_address varchar(200) null;
alter table subdividedland modify column owner_name varchar(200) null;
alter table subdividedland modify column owner_address varchar(200) null;

ALTER TABLE subdividedland ADD UNIQUE INDEX ux_newpin (newpin);

alter table subdivisionaffectedrpu add prevpin varchar(50);
alter table subdivisionaffectedrpu add prevtdno varchar(50);
alter table subdivisionaffectedrpu modify newsuffix int null;

alter table subdivisionaffectedrpu modify column newtdno varchar(50) null;
alter table subdivisionaffectedrpu modify column newutdno varchar(50) null;
alter table subdivisionaffectedrpu modify column memoranda varchar(500) null;
alter table subdivisionaffectedrpu modify column itemno varchar(50) null;




/* CONSOLIDATION */
ALTER TABLE consolidation DROP FOREIGN KEY consolidation_ibfk_4;

alter table consolidation modify column txntype_objid varchar(50) null;
alter table consolidation modify column autonumber int  null;
alter table consolidation modify column effectivityyear int null;
alter table consolidation modify column effectivityqtr int null;
alter table consolidation modify column newtdno varchar(50) null;
alter table consolidation modify column newutdno varchar(50) null;
alter table consolidation modify column newtitletype varchar(50) null;
alter table consolidation modify column newtitleno varchar(50) null;
alter table consolidation modify column newtitledate varchar(50) null;
alter table consolidation modify column lguid varchar(50) null;
alter table consolidation modify column lgutype varchar(50) null;
alter table consolidation modify column taxpayer_objid varchar(50) null;
alter table consolidation modify column taxpayer_name text null;
alter table consolidation modify column taxpayer_address varchar(200) null;
alter table consolidation modify column owner_name text null;
alter table consolidation modify column owner_address varchar(200) null;
alter table consolidation modify column administrator_objid varchar(50) null;
alter table consolidation modify column administrator_name varchar(500) null;
alter table consolidation modify column administrator_address varchar(200) null;
alter table consolidation modify column administratorid varchar(50) null;
alter table consolidation modify column administratorname varchar(500) null;
alter table consolidation modify column administratoraddress varchar(200) null;
alter table consolidation modify column signatories varchar(500) null;


alter table consolidationaffectedrpu modify column newtdno varchar(50);
alter table consolidationaffectedrpu modify column newutdno varchar(50);
alter table consolidationaffectedrpu modify column memoranda varchar(500);


alter table consolidation modify column txnno varchar(25) not null;
alter table subdivision modify column txnno varchar(25) not null;

ALTER TABLE consolidation_task DROP FOREIGN KEY consolidation_task_ibfk_1;



alter table planttreedetail add areacovered decimal(16,2) null;
update planttreedetail set areacovered = 0 where areacovered is null;

alter table planttreedetail modify column nonproductiveage varchar(25);	


INSERT INTO sys_var (name, value, description, datatype, category) 
VALUES ('faas_formalize_owner_name', '0', 'Formalize Owner Name upon lookup', 'checkbox', 'ASSESSOR');


update subdivision s, subdivisionaffectedrpu ar, faas pf, rpu pr  set 
	ar.prevpin = pr.fullpin, ar.prevtdno = pf.tdno
where s.state ='draft' 
  and s.objid = ar.subdivisionid
	and ar.prevfaasid = pf.objid 
	and pf.rpuid = pr.objid 
  and ar.prevpin is null 


  