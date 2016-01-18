
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

alter table subdividedland alter column itemno varchar(50) null;
alter table subdividedland alter column newtdno varchar(50) null;
alter table subdividedland alter column newutdno varchar(50) null;
alter table subdividedland alter column newtitletype varchar(50) null;
alter table subdividedland alter column newtitleno varchar(50) null;
alter table subdividedland drop constraint DF__subdivide__newti__1F198FD4;
alter table subdividedland alter column newtitledate varchar(50) null;
alter table subdividedland alter column areasqm decimal(16,6) null;
alter table subdividedland alter column areaha decimal(16,6) null;
alter table subdividedland alter column memoranda text null;
alter table subdividedland alter column administrator_objid varchar(50) null;
alter table subdividedland alter column administrator_name varchar(200) null;
alter table subdividedland alter column administrator_address varchar(200) null;
alter table subdividedland alter column taxpayer_objid varchar(50) null;
drop index subdividedland.ix_subdividedland_taxpayer_name;
alter table subdividedland alter column taxpayer_name text  null;
alter table subdividedland alter column taxpayer_address varchar(200) null;
alter table subdividedland alter column owner_name text null;
alter table subdividedland alter column owner_address varchar(200) null;

create UNIQUE INDEX ux_newpin on subdividedland(subdivisionid,newpin);

alter table subdivisionaffectedrpu add prevpin varchar(50);
alter table subdivisionaffectedrpu add prevtdno varchar(50);
alter table subdivisionaffectedrpu alter column newsuffix int null;

alter table subdivisionaffectedrpu alter column newtdno varchar(50) null;
alter table subdivisionaffectedrpu alter column newutdno varchar(50) null;
alter table subdivisionaffectedrpu alter column memoranda text null;
alter table subdivisionaffectedrpu alter column itemno varchar(50) null;




/* CONSOLIDATION */


alter table consolidation alter column txntype_objid varchar(50) null;
alter table consolidation alter column autonumber int  null;
alter table consolidation alter column effectivityyear int null;
alter table consolidation alter column effectivityqtr int null;
alter table consolidation alter column newtdno varchar(50) null;
alter table consolidation alter column newutdno varchar(50) null;
alter table consolidation alter column newtitletype varchar(50) null;
alter table consolidation alter column newtitleno varchar(50) null;
alter table consolidation drop constraint DF__consolida__newti__19CACAD2;
alter table consolidation alter column newtitledate varchar(50) null;
alter table consolidation alter column lguid varchar(50) null;
alter table consolidation alter column lgutype varchar(50) null;
alter table consolidation alter column taxpayer_objid varchar(50) null;
drop index consolidation.ix_consolidation_taxpayername;
alter table consolidation alter column taxpayer_name text null;
alter table consolidation alter column taxpayer_address varchar(200) null;
drop index consolidation.ix_consolidation_ownername;
alter table consolidation alter column owner_name text null;
alter table consolidation alter column owner_address varchar(200) null;
alter table consolidation alter column administrator_objid varchar(50) null;
alter table consolidation alter column administrator_name varchar(500) null;
alter table consolidation alter column administrator_address varchar(200) null;
alter table consolidation alter column administratorid varchar(50) null;
alter table consolidation alter column administratorname varchar(500) null;
alter table consolidation alter column administratoraddress varchar(200) null;
alter table consolidation alter column signatories text null;


alter table consolidationaffectedrpu alter column newtdno varchar(50);
alter table consolidationaffectedrpu alter column newutdno varchar(50);
alter table consolidationaffectedrpu alter column memoranda varchar(500);


alter table consolidation alter column txnno varchar(25) not null;
alter table subdivision alter column txnno varchar(25) not null;



alter table planttreedetail add areacovered decimal(16,2) null;
update planttreedetail set areacovered = 0 where areacovered is null;

alter table planttreedetail alter column nonproductiveage varchar(25);	

INSERT INTO sys_var (name, value, description, datatype, category) 
VALUES ('faas_formalize_owner_name', '0', 'Formalize Owner Name upon lookup', 'checkbox', 'ASSESSOR');


update ar set 
	ar.prevpin = pr.fullpin, ar.prevtdno = pf.tdno
from subdivision s
	inner join subdivisionaffectedrpu ar on s.objid = ar.subdivisionid
	inner join faas pf on ar.prevfaasid = pf.objid 
	inner join rpu pr on pf.rpuid = pr.objid 
where s.state ='draft' 
  and ar.prevpin is null;

  