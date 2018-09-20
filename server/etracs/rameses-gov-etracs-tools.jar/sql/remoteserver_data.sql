[getList]
SELECT r.*, 'POSTED' AS state FROM remoteserverdata r ORDER BY r.objid 

[getCollectionTypeOrgs]
select * from collectiontype_org where org_objid = $P{orgid} 


[getCollectionTypes]
select distinct ct.*, 
  o.org_objid as orgid, 
  o.org_name as orgname, 
  o.org_type as orgtype 
from collectiontype_org o 
  inner join collectiontype ct on ct.objid = o.collectiontypeid 
where o.org_objid = $P{orgid} 
order by ct.formno, ct.name 


[getCollectionTypeAccounts]
select distinct cta.*  
from collectiontype_org o 
  inner join collectiontype ct on ct.objid = o.collectiontypeid 
  inner join collectiontype_account cta on cta.collectiontypeid = ct.objid 
where o.org_objid = $P{orgid} 


[getCollectionGroups]
select * from collectiongroup where org_objid=$P{orgid} ORDER BY name 


[getCollectionGroupItems]
select b.* from collectiongroup a 
  inner join collectiongroup_revenueitem b on a.objid = b.collectiongroupid 
where a.org_objid=$P{orgid} 


[getAFs]
select distinct * 
from ( 
  select af.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join af on af.objid = ct.formno 
  where o.org_objid = $P{orgid} 
  union 
  select af.* from sys_usergroup_member ugm 
    inner join af_control afc on ugm.user_objid = afc.owner_objid 
    inner join af on afc.afid=af.objid 
  where ugm.org_objid = $P{orgid } 
  union 
  select af.* from af_control afc 
    inner join af on afc.afid = af.objid 
  where afc.org_objid = $P{orgid} 
)xx 


[getSearchFundGroups]
select * from fundgroup where ${filter}

[getSearchFunds]
select * from fund where ${filter} 

[getFunds]
select * 
from ( 
  select f.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join fund f on f.objid = ct.fund_objid 
  where o.org_objid = $P{orgid} 
  union 
  select f.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join collectiontype_account ca on ca.collectiontypeid = ct.objid 
    inner join itemaccount ia on ia.objid = ca.account_objid 
    inner join fund f on f.objid = ia.fund_objid 
  where o.org_objid = $P{orgid} 
  union 
  select f.* 
  from itemaccount ia 
    inner join fund f on ia.fund_objid=f.objid 
  where ia.org_objid = $P{orgid} 
)xx 


[getFundGroups]
select fg.* 
from ( 
  select f.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join fund f on f.objid = ct.fund_objid 
  where o.org_objid = $P{orgid} 
  union 
  select f.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join collectiontype_account ca on ca.collectiontypeid = ct.objid 
    inner join itemaccount ia on ia.objid = ca.account_objid 
    inner join fund f on f.objid = ia.fund_objid 
  where o.org_objid = $P{orgid} 
  union 
  select f.* 
  from itemaccount ia 
    inner join fund f on ia.fund_objid=f.objid 
  where ia.org_objid = $P{orgid} 
)xx, fundgroup fg  
where fg.objid = xx.groupid 


[getParentFunds]
select pf.* 
from ( 
  select f.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join fund f on f.objid = ct.fund_objid 
  where o.org_objid = $P{orgid} 
  union 
  select f.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join collectiontype_account ca on ca.collectiontypeid = ct.objid 
    inner join itemaccount ia on ia.objid = ca.account_objid 
    inner join fund f on f.objid = ia.fund_objid 
  where o.org_objid = $P{orgid} 
  union 
  select f.* 
  from itemaccount ia 
    inner join fund f on ia.fund_objid=f.objid 
  where ia.org_objid = $P{orgid} 
)xx, fund pf   
where pf.objid = xx.parentid 


[getItemAccounts]
select distinct * 
from ( 
  select ia.* 
  from collectiontype_org o 
    inner join collectiontype ct on ct.objid = o.collectiontypeid 
    inner join collectiontype_account ca on ca.collectiontypeid = ct.objid 
    inner join itemaccount ia on ia.objid = ca.account_objid 
    inner join fund f on f.objid = ia.fund_objid 
  where o.org_objid = $P{orgid} 
  union 
  select ia.* 
  from itemaccount ia 
    inner join fund f on f.objid = ia.fund_objid 
  where ia.org_objid = $P{orgid}  
  union 
  select ia.* 
  from collectiongroup a 
    inner join collectiongroup_revenueitem b on b.collectiongroupid = a.objid 
    inner join itemaccount ia on ia.objid = b.revenueitemid 
    inner join fund on fund.objid = ia.fund_objid 
  where a.org_objid = $P{orgid} 
)xx 


[getUserGroups]
select xx.* from ( 
  select ug.* 
  from sys_usergroup_member ugm 
    inner join sys_usergroup ug on ug.objid = ugm.usergroup_objid 
  where ugm.org_objid = $P{orgid} 
  union 
  select * from sys_usergroup 
  where objid = 'TREASURY.LIQUIDATING_OFFICER' 
)xx 


[getUsers]
select distinct xx.* 
from ( 
  select u.* 
  from sys_usergroup_member ugm 
    inner join sys_usergroup ug on ug.objid = ugm.usergroup_objid 
    inner join sys_user u on u.objid = ugm.user_objid 
  where ugm.org_objid = $P{orgid}  
  union 
  select u.* 
  from sys_usergroup_member ugm  
    inner join sys_usergroup ug on ug.objid = ugm.usergroup_objid 
    inner join sys_user u on u.objid = ugm.user_objid 
  where ugm.usergroup_objid = 'TREASURY.LIQUIDATING_OFFICER' 
)xx 


[getUserMemberships]
select um.* 
from ( 
  select objid 
  from sys_usergroup_member 
  where org_objid = $P{orgid}   
  union 
  select objid 
  from sys_usergroup_member 
  where usergroup_objid = 'TREASURY.LIQUIDATING_OFFICER'  
)xx, sys_usergroup_member um 
where um.objid = xx.objid 


[getUserCashBooks]
select * from cashbook 
where subacct_objid in ( 
    select distinct su.objid from sys_usergroup_member sm 
      inner join sys_user su on sm.user_objid = su.objid 
    where sm.org_objid = $P{orgid} 
  ) and fund_objid in ( 
    select distinct f.objid from collectiontype c 
      inner join collectiontype_account ca on ca.collectiontypeid  = c.objid 
      inner join itemaccount ia on ia.objid = ca.account_objid 
      inner join fund f on f.objid = ia.fund_objid 
    where c.org_objid = $P{orgid} 
  )

[getOrgs]
select distinct * 
from ( 
  select * from sys_org where root=1 
  union 
  select * from sys_org where objid in (  
    select parent_objid from sys_org where objid=$P{orgid} 
  )
  union 
  select * from sys_org where objid=$P{orgid} 
)xx 


[getOrgClasses]
select * from sys_orgclass 


[getBanks]
select * from bank


[getCashBooksDetail]
select * from cashbook_entry where parentid=$P{objid} 


[insertUserMembership]
INSERT INTO sys_usergroup_member(
   objid,
   state,
   usergroup_objid,
   user_objid,
   user_username,
   user_firstname,
   user_lastname,
   org_objid,
   org_name,
   org_orgclass,
   securitygroup_objid,
   exclude,
   displayname,
   jobtitle)
VALUES (
   $P{objid},
   $P{state},
   $P{usergroup_objid},
   $P{user_objid},
   $P{user_username},
   $P{user_firstname},
   $P{user_lastname},
   $P{org_objid},
   $P{org_name},
   $P{org_orgclass},
   $P{securitygroup_objid},
   $P{exclude},
   $P{displayname},
   $P{jobtitle} 
)


[insertFund]
INSERT INTO fund(
   objid
  ,parentid
  ,state
  ,code
  ,title
  ,type
  ,special
)
VALUES (
  $P{objid}
  ,$P{parentid}
  ,$P{state}
  ,$P{code}
  ,$P{title}
  ,$P{type}
  ,$P{special}
)


[insertSpecialAccountSetting]
INSERT INTO specialaccountsetting
  (objid,
   item_objid,
   amount,
   collectiontypeid,
   revtype)
VALUES
(
  $P{objid},
  $P{item_objid},
  $P{amount},
  $P{collectiontypeid},
  $P{revtype} 
) 