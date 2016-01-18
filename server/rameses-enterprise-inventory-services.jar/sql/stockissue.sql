[getList]
SELECT r.* 
FROM stockissue r WHERE state =$P{state} 
order by issueno desc 

[getItems]
SELECT s.*, af.formtype as aftype FROM stockissueitem s 
	inner join af on af.objid = s.item_objid 
WHERE parentid=$P{objid}

[closeRequest]
UPDATE stockrequest SET state='CLOSED' WHERE objid=$P{objid}

[getListbyIssueNo]
select * from stockissue
where state =$P{state}  
	and issueno like $P{searchtext}
order by issueno desc 	

[getListbyReqNo]
select * from stockissue
where state =$P{state}  
	and request_reqno like $P{searchtext}	
order by issueno desc 	

[getListbyRequester]
select * from stockissue
where state =$P{state}  
	and issueto_name like $P{searchtext}		
order by issueno desc 