[getStockIssuances]
select si.*, 
	issueno as txnno, dtfiled as txndate,  reqtype as txntype 
from stockissue si 
order by dtfiled desc 

[getStockSales]
select ss.*, 
	 issueno as txnno, dtfiled as txndate,  reqtype as txntype 
from stocksale ss 
order by dtfiled desc 

[getStockReceipts]
select sr.*,  
	receiptno as txnno, dtfiled as txndate,  reqtype as txntype 
from stockreceipt sr 
order by dtfiled desc 
