[getLookup]
select * from allergen 
where classification like $P{classification} 
	and name like $P{searchtext}

[getClassifcations]	
select distinct classification from allergen 

