[getList]
SELECT * FROM datatxnlog 
WHERE txnid=$P{txnid} 
ORDER BY txndate DESC 
