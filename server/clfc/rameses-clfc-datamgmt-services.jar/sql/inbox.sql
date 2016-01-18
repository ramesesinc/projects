[getList]
SELECT * FROM inbox 
WHERE ${filter} sendername LIKE $P{searchtext} 
ORDER BY dtcreated 

[removeMessage]
DELETE FROM inbox WHERE objid=$P{objid} 

[removeMessageByRefid]
DELETE FROM inbox WHERE refid=$P{refid}
 