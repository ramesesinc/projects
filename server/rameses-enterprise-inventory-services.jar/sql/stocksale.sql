[getList]
SELECT r.* 
FROM stocksale r WHERE state =$P{state} 
order by issueno desc 

[getItems]
SELECT s.*, af.formtype as aftype FROM stocksaleitem s 
	inner join af on af.objid = s.item_objid 
WHERE parentid=$P{objid}


[closeRequest]
UPDATE stockrequest SET state='CLOSED' WHERE objid=$P{objid}

[getListbyIssueNo]
select * from stocksale
where state =$P{state}  
	and issueno like $P{searchtext}
order by issueno desc 	

[getListbyReqNo]
select * from stocksale
where state =$P{state}  
	and request_reqno like $P{searchtext}	
order by issueno desc 	

[getListbyRequester]
select * from stocksale
where state =$P{state}  
	and issueto_name like $P{searchtext}		
order by issueno desc 