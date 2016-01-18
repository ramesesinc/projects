USE clfc2;

DELETE FROM sys_usergroup_permission
 WHERE usergroup_objid = 'TREASURY_CASHIER'
	AND object = 'passbook';

INSERT IGNORE INTO sys_usergroup_permission 
  (objid, usergroup_objid, object, permission, title) 
VALUES 
  ('TREASURY_ACCT_ASSISTANT_PBKA', 'TREASURY_ACCT_ASSISTANT', 'passbook', 'approve', 'approve'),  
  ('TREASURY_ACCT_ASSISTANT_PBKC', 'TREASURY_ACCT_ASSISTANT', 'passbook', 'create', 'create'),  
  ('TREASURY_ACCT_ASSISTANT_PBKD', 'TREASURY_ACCT_ASSISTANT', 'passbook', 'delete', 'delete'),  
  ('TREASURY_ACCT_ASSISTANT_PBKE', 'TREASURY_ACCT_ASSISTANT', 'passbook', 'edit', 'edit'),  
  ('TREASURY_ACCT_ASSISTANT_PBKR', 'TREASURY_ACCT_ASSISTANT', 'passbook', 'read', 'read'); 
  
INSERT IGNORE INTO sys_usergroup_permission 
  (objid, usergroup_objid, object, permission, title) 
VALUES 
  ('TREASURY_ACCT_ASSISTANT_A', 'TREASURY_ACCT_ASSISTANT', 'bank', 'approve', 'approve'),  
  ('TREASURY_ACCT_ASSISTANT_C', 'TREASURY_ACCT_ASSISTANT', 'bank', 'create', 'create'),  
  ('TREASURY_ACCT_ASSISTANT_D', 'TREASURY_ACCT_ASSISTANT', 'bank', 'delete', 'delete'),  
  ('TREASURY_ACCT_ASSISTANT_E', 'TREASURY_ACCT_ASSISTANT', 'bank', 'edit', 'edit'),  
  ('TREASURY_ACCT_ASSISTANT_R', 'TREASURY_ACCT_ASSISTANT', 'bank', 'read', 'read'); 

INSERT IGNORE INTO sys_usergroup 
  (objid, title, domain, role, userclass, orgclass) 
VALUES 
  ('TREASURY_VAULT_REPRESENTATIVE', 'VAULT_REPRESENTATIVE', 'TREASURY', 'VAULT_REPRESENTATIVE', 'usergroup', NULL); 