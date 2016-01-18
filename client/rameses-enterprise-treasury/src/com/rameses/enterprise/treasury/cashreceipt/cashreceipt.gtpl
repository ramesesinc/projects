<%
    /*
    def entity = [:];
    entity.paidby = 'FLORES, WORGIE';    
    entity.paidbyaddress = 'CEBU CITY';
    entity.items = [];
    entity.items << [ item: [code:'B11', title: 'MAYORS PERMIT'], amount: 560.0 ];    
    entity.items << [ item: [code:'B12', title: 'BUSINESS PERMIT'], amount: 570.0 ];
    entity.totalcash = 500.0
    entity.paymentitems = [];
    entity.paymentitems << [ type:'CHECK', particulars: 'BANK', amount: 560.0 ];    
    entity.paymentitems << [ type:'CHECK', particulars: 'BANK', amount: 570.0 ];
    */
    def df = new java.text.DecimalFormat("#,##0.00")
%>

<table width="380" cellpadding="0" >
    <tr>
        <td><font size="5"><b>Cash Tendered</b></font></td>
        <td>:</td>
        <td align="right"><font size="6"><b>${df.format(entity.totalcash)}</b></font></td>
    </tr>
    <tr style="color:red">
        <td><font size="5"><b>Change</b></font></td>
        <td>:</td>
        <td align="right"><font size="6"><b>${df.format(entity.cashchange)}</b></font></td>
    </tr>
</table>
<br>
<hr>
<br>
<table width="400">
    <%if(entity.voided){%>
        <tr>
            <td colspan="2">
                <h1><font color=red>VOID</font></h1>
            </td>
        </tr>
    <%}%>

    <tr>
        <td>Receipt No </td>
        <td><font size="4"> <b>${entity.receiptno}</font> </b></td>
    </tr>
    <tr>
        <td>Mode </td>
        <td><b>${entity.txnmode}</font> </b></td>
    </tr>
    <tr>
        <td>Receipt Date</td>
        <td><b>${entity.receiptdate}</b></td>
    </tr>
    <tr>
        <td>Payer</td>
        <td>${entity.paidby}</td>
    </tr>
    <tr>
        <td>Address</td>
        <td>${entity.paidbyaddress}</td>
    </tr>
    <tr>
        <td colspan="2">
            <br>
            ${ (entity.remarks)? entity.remarks: ''}
            </br>
        </td>
    </tr>
    
    <tr>
        <td colspan="2">
            <hr>
            <table width="100%">
                <tr>
                    <th>Item code</th>
                    <th>Title</th>
                    <th>Amount</th>
                    <th>Remarks</th>
                </tr>
                <%entity.items.each{ %>
                    <tr>
                        <td>${it?.item.code}</td>
                        <td>${it?.item.title}</td>
                        <td>${df.format(it?.amount)}</td>
                        <td>${  (it?.remarks) ? it.remarks : ''}</td>
                    </tr>
                <%}%>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <hr>
                <b>AMOUNT : ${df.format(entity.amount)}<BR></b>
           </hr>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <br>
            <%if( entity.paymentitems ){%> 
                <b>Checks and other payments</b>
                <br>
                <table>
                    <tr>
                        <th>Type</th>
                        <th>Particulars</th>
                        <th>Amount</th>
                    </tr>
                    <%entity.paymentitems.each{ %>
                        <tr>
                            <td>${it.reftype}</td>
                            <td>${it?.particulars}</td>
                            <td>${df.format(it?.amount)}</td>
                        </tr>
                    <%}%>
                </table>
              <%}%>  
        </td>
    </tr>
</table>

