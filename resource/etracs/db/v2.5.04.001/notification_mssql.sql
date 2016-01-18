
CREATE TABLE sms_outbox_pending (
  objid VARCHAR(50) NOT NULL,
  dtexpiry DATETIME DEFAULT NULL,
  dtretry DATETIME DEFAULT NULL,
  retrycount INT DEFAULT 0 
) 
GO
CREATE INDEX ix_dtexpiry ON sms_outbox_pending (dtexpiry)
GO 
CREATE INDEX ix_dtretry ON sms_outbox_pending (dtretry)
GO 
ALTER TABLE sms_outbox_pending ADD PRIMARY KEY  (objid) 
GO 
