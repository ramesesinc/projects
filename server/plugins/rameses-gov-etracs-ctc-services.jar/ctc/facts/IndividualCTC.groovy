package ctc.facts;

class IndividualCTC {
    
    String profession;    
    String citizenship;    
    String gender;      
    String civilstatus;    
    boolean seniorcitizen;      
    boolean newbusiness;     
    double annualsalary = 0;     
    double businessgross = 0;     
    double propertyincome = 0;    
    
    String barangayid;    
    boolean additional;  

    double basictax = 0;      
    double salarytax = 0;    
    double businessgrosstax = 0;      
    double propertyincometax = 0;    
    double additionaltax = 0;         
    double interest = 0;     


    public IndividualCTC(def m) {
        if(m.profession!=null) profession = m.profession;
        if(m.citizenship!=null) citizenship = m.citizenship;
        if(m.gender  !=null) gender   = m.gender  ;
        if(m.civilstatus!=null)  civilstatus = m.civilstatus;
        if(m.seniorcitizen!=null) seniorcitizen = m.seniorcitizen;
        if(m.newbusiness!=null) newbusiness = m.newbusiness;
        if(m.annualsalary!=null) annualsalary = m.annualsalary;
        if(m.businessgross!=null) businessgross = m.businessgross;
        if(m.propertyincome!=null) propertyincome = m.propertyincome;
        if(m.barangayid!=null) barangayid = m.barangayid;
        if(m.additional!=null) additional = m.additional;
    }
    
    public double getTotaltax() {
        return basictax + salarytax + businessgrosstax + propertyincometax + additionaltax 
    }

    public double getAmountdue() {
        return getTotaltax();
    }

    public double getAmount() {
        return amountdue + interest;
    }

    public Map toMap() {
        def m = [:];
        m.basictax = basictax;
        m.salarytax = salarytax;
        m.businessgrosstax = businessgrosstax;
        m.propertyincometax = propertyincometax;
        m.additionaltax = additionaltax;
        m.interest = interest;
        m.amountdue = amountdue;
        m.totaltax = totaltax;
        m.interest = interest;
        m.amount = amount;
        return m;
    }

}
