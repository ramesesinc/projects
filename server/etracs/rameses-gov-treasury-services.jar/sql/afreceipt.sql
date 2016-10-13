[getList]
select 
	sr.objid, sr.receiptno, sr.state, sr.request_objid, sr.request_reqno, 
	sr.dtfiled, sr.reqtype, sr.user_objid, sr.user_name, sr.org_objid, sr.org_name, 
	sreq.itemclass, sreq.dtfiled as request_dtfiled, sreq.requester_name  
from ( 
	select objid from stockreceipt where receiptno like $P{searchtext} 
	union 
	select objid from stockreceipt where request_reqno like $P{searchtext} 
	union 
	select objid from stockreceipt where user_name like $P{searchtext} 
)xx 
	inner join stockreceipt sr on xx.objid=sr.objid 
	inner join stockrequest sreq on sr.request_objid=sreq.objid 
order by sr.dtfiled desc 

[findInfo]
select 
	sr.*, sreq.itemclass, 
	sreq.dtfiled as request_dtfiled, sreq.requester_name   
from stockreceipt sr 
	inner join stockrequest sreq on sr.request_objid=sreq.objid 
where sr.objid = $P{objid} 
