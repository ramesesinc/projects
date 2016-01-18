[getList]
select distinct RTRIM(Account_No) as code, RTRIM(Name) As name
from WaterBill 
where Account_No Like $P{searchtext} 
	or Name Like $P{searchtext} 
 
[findAccountInfo]
select RTRIM(Account_No) as code, RTRIM(Name) As name, 
		Amount as amountdue, RTRIM(Account_Type)as typeno, Period as period, 
		(AR + Amount1 + Amount2 + Amount3 + Surcharge + Others) as otheramount 
from WaterBill 
where Account_No = $P{code} 
order by Period desc 


[findDiscount]
select Discount as discount, Minimum_Amount as minamount, Penalty as penalty 
from Account_Type 
where RTRIM(Type_No) = $P{typeno}  
 
 [insertPayment]
insert into Payments (Remarks,Nature_Of_Collection_Code,AO_ID, Or_No,Date_Issued,Amount,Payor,Type_Of_Payment,Form_Description)
values ('OK',377,43, $P{series},$P{xreceiptdate},$P{amount},$P{paidby},'CASH','51')

[cancelPayment]
update Payments set Remarks = 'CANCELLED' where Or_No = $P{series}
