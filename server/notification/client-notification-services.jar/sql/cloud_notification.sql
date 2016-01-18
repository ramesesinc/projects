[getList]
SELECT * FROM cloud_notification WHERE groupid=$P{groupid} ORDER BY dtfiled 

[getAttachments]
SELECT * FROM cloud_notification_attachment 
WHERE parentid=${objid} 
ORDER BY indexno 

[getPendingMessages]
SELECT * FROM cloud_notification_pending ORDER BY dtfiled 

[reschedulePending]
UPDATE cloud_notification_pending SET 
	dtretry=$P{dtretry} 
WHERE 
	objid=$P{objid} 

[removePending]
DELETE FROM cloud_notification_pending WHERE objid=$P{objid} 

[removeFailed]
DELETE FROM cloud_notification_failed WHERE refid=$P{refid} 

[removeDelivered]
DELETE FROM cloud_notification_delivered WHERE objid=$P{objid} 

[removeReceived]
DELETE FROM cloud_notification_received WHERE objid=$P{objid} 


#
# script for cloud notification listing 
#
[getAllNotifications]
SELECT * FROM cloud_notification ORDER BY dtfiled DESC 

[getFailedNotifications]
select distinct n.* 
from cloud_notification_failed f 
	inner join cloud_notification n on f.refid=n.objid 
order by n.dtfiled 

[getPendingNotifications]
select distinct n.* 
from cloud_notification_pending t 
	inner join cloud_notification n on t.objid=n.objid 
order by n.dtfiled 

[getDeliveredNotifications]
select distinct n.* 
from cloud_notification_delivered t 
	inner join cloud_notification n on t.objid=n.objid 
order by n.dtfiled desc 

[findFailedMessage]
select n.*, t.objid as failid, t.reftype 
from cloud_notification_failed t 
	inner join cloud_notification n on t.refid=n.objid 
where t.refid=$P{objid} 
