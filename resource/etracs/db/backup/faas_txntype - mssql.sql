
CREATE TABLE faas_txntype (
  objid varchar(50) NOT NULL,
  name varchar(100) NOT NULL,
  newledger integer NOT NULL,
  newrpu integer NOT NULL,
  newrealproperty integer NOT NULL,
  displaycode varchar(10) default NULL,
  PRIMARY KEY  (objid)
) 
go


INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('CC', 'Change Classification', '0', '1', '0', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('CD', 'Change Depreciation', '0', '1', '0', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('CE', 'Correction of Entry', '0', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('CS', 'Consolidation', '1', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('CT', 'Change Taxability', '0', '1', '0', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('CTD', 'Cancellation', '0', '0', '0', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('DC', 'Data Capture', '1', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('GR', 'General Revision', '0', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('MC', 'Multiple Claim', '1', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('MCS', 'Multiple Claim Settlement', '0', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('ND', 'New Discovery', '1', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('RE', 'Reassessment', '0', '1', '0', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('RS', 'Resection', '0', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('RV', 'Revision', '0', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('SD', 'Subdivision', '1', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('TR', 'Transfer of Ownership', '0', '0', '0', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('TRC', 'Transfer with Correction', '0', '1', '1', NULL);
INSERT INTO faas_txntype (objid, name, newledger, newrpu, newrealproperty, displaycode) VALUES ('TRE', 'Transfer with Reassessment', '0', '1', '0', NULL);
