[findApp]
select * from business_application where objid = $P{objid} 

[changeAppMode]
update business_application set txnmode = $P{txnmode} where objid = $P{objid} 

