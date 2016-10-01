[getPendingMessages]
SELECT m.*, d.dtexpiry, d.dtretry, d.retrycount 
FROM sms_inbox_pending d 
	INNER JOIN sms_inbox m ON d.objid=m.objid 
WHERE 1=1 	
ORDER BY m.dtfiled 

[rescheduleInboxPending]
UPDATE sms_inbox_pending SET 
	dtretry=$P{dtretry}, retrycount=retrycount+1  
WHERE 
	objid=$P{objid} 

[removeInboxPending]
DELETE FROM sms_inbox_pending WHERE objid=$P{objid} 

[markInboxAsFailed]
UPDATE sms_inbox SET state='FAILED' WHERE objid=$P{objid} 

[markInboxAsSuccess]
UPDATE sms_inbox SET state='SUCCESS' WHERE objid=$P{objid} 


[findInbox]
SELECT * FROM sms_inbox WHERE objid=$P{objid}  

[getOutboxPendingMessages]
SELECT o.*, p.retrycount, p.dtretry, p.dtexpiry 
FROM sms_outbox_pending p 
	INNER JOIN sms_outbox o ON p.objid=o.objid 
ORDER BY o.dtfiled 

[markOutboxAsFailed]
UPDATE sms_outbox SET state='FAILED' WHERE objid=$P{objid} 

[removeOutboxPending]
DELETE FROM sms_outbox_pending WHERE objid=$P{objid} 

[rescheduleOutboxPending]
UPDATE sms_outbox_pending SET 
	dtretry=$P{dtretry}, retrycount=retrycount+1  
WHERE 
	objid=$P{objid} 

[markOutboxAsSend]
UPDATE sms_outbox SET 
	state='SEND', remarks=$P{remarks}, 
	dtsend=$P{dtsend}, traceid=$P{traceid} 
WHERE 
	objid=$P{objid} 
