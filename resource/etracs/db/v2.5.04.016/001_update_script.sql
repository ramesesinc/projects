
ALTER TABLE creditmemo ADD bankaccount_code VARCHAR(50) NULL;

INSERT INTO creditmemotype (objid, title, issuereceipt, HANDLER, sortorder, fund_objid, fund_code, fund_title) VALUES ('SEN_CIT', 'DSWD TRUST (SENIOR CIT)', '1', 'basic', '0', 'TRUST', '03', 'TRUST');
INSERT INTO creditmemotype (objid, title, issuereceipt, HANDLER, sortorder, fund_objid, fund_code, fund_title) VALUES ('SPECIAL_EDUCATION_FUND', 'SEF', '0', 'basic', '0', 'SEF', '02', 'SEF');
INSERT INTO creditmemotype (objid, title, issuereceipt, HANDLER, sortorder, fund_objid, fund_code, fund_title) VALUES ('STANDARD', 'STANDARD (General)', '0', 'basic', '0', 'GENERAL', '01', 'GENERAL');
INSERT INTO creditmemotype (objid, title, issuereceipt, HANDLER, sortorder, fund_objid, fund_code, fund_title) VALUES ('STANDARD_TRUST', 'STANDARD (Trust)', '0', 'basic', '0', 'TRUST', '03', 'TRUST');

INSERT INTO collectiontype (objid, state, `name`, title, formno, `handler`, allowbatch, barcodekey, allowonline, allowoffline, sortorder, org_objid, org_name, fund_objid, fund_title, category) VALUES ('CREDIT_MEMO_COLLECTION', 'DRAFT', 'CREDIT_MEMO_COLLECTION', 'Credit Memo Collection', '51', 'creditmemo', NULL, NULL, '1', NULL, '50', NULL, NULL, NULL, NULL, NULL);

