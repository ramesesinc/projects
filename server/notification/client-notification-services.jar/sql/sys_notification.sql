[getList]
SELECT * FROM sys_notification 
WHERE recipientid=$P{recipientid} AND recipienttype=$P{recipienttype} 
ORDER BY dtfiled 

[getAllMessages]
SELECT * FROM sys_notification 
WHERE recipientid IN (${recipientid}) 
ORDER BY dtfiled 

[getUserMessages]
SELECT * FROM sys_notification 
WHERE recipientid=$P{recipientid} AND recipienttype='user' 
ORDER BY dtfiled 

[getGroupMessages]
SELECT * FROM sys_notification 
WHERE recipientid=$P{recipientid} AND recipienttype='group' 
ORDER BY dtfiled 

[findByObjid]
SELECT * FROM sys_notification WHERE objid=$P{objid} 

[findByNotificationid]
SELECT * FROM sys_notification WHERE notificationid=$P{notificationid} 
