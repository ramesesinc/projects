
CREATE TABLE sms_outbox_pending (
  objid varchar(50) NOT NULL,
  dtexpiry datetime default NULL,
  dtretry datetime default NULL,
  retrycount int default '0',
  CONSTRAINT pk_sms_outbox_pending_objid  PRIMARY KEY  (objid)
);

CREATE INDEX ix_dtexpiry ON sms_outbox_pending (dtexpiry); 
CREATE INDEX ix_dtretry ON sms_outbox_pending (dtretry); 
