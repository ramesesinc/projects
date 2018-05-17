<html>
    <head>
        <style>
            body {font-family:arial; font-size:10px;}
            th { background-color: #848482; color:white; }
            table { border: 1px solid #848482; }
            td { background-color: #E0FFFF }
        </style>
    </head>
    <body>
        <table cellpadding="2" cellspacing="2" width="100%">
             <tr>
                <th align="left">AF No</th>
                <th align="left">Description</th>
                <th align="center">Unit</th>
                <th align="center">Qty</th>
                <th align="center">Qty Served</th>
                <th align="center">Unit Cost</th>
                <th align="center">Txn Type</th>
                <th>&nbsp;</th>
             </tr>
             
             <%entity.items.each { o-> %>
                <tr style="border-top:1px solid black;">
                   <td>${o.item.objid}</td>
                   <td>${o.item.title}</td>
                   <td align="center">${o.unit}</td>
                   <td align="center">${o.qty}</td>
                   <td align="center">${o.qtyserved}</td>
                   <td align="center">${o.cost}</td>
                   <td align="center">${o.txntype}</td>
                   <td align="center">
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('PURCHASE_RECEIPT|BEGIN_BALANCE|FORWARD') && (o.qty > o.qtyserved) ) {  %> 
                        <a href="addBatch" unit="${o.unit}" afid="${o.item.objid}" aftxnitemid="${o.objid}">Add Entry</a>
                      <% } %>
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('TRANSFER|RETURN') && (o.qty > o.qtyserved) ) {  %> 
                        <a href="moveBatch" aftxnitemid="${o.objid}">Select</a>
                      <% } %>
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('ISSUE')  && (o.qty > o.qtyserved) ) {  %> 
                        <a href="issueBatch" aftxnitemid="${o.objid}">Issue</a>
                      <% } %>
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('ISSUE')  && ( o.qtyserved > 0 ) && o.afunit.formtype == 'cashticket' ) {  %> 
                        &nbsp;&nbsp;<a href="editBatch" aftxnitemid="${o.objid}">Edit Qty</a>
                      <% } %>
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('FORWARD')  && ( o.qtyserved > 0 )) {  %> 
                        &nbsp;&nbsp;<a href="editBatch" aftxnitemid="${o.objid}">Edit</a>
                      <% } %>
                   </td>
                </tr>

                <%o.items.each { ii-> %>
                    <tr>
                        <td>&nbsp;</td>
                        <td colspan="6">
                            &nbsp;&nbsp;batch:${ii.batchno} 
                            <% if(o.afunit.formtype == 'serial') { %> 
                            ${ (ii.prefix==null)?'':ii.prefix } ${ii.startseries} - ${ii.endseries} ${(ii.suffix==null)?'':ii.suffix}
                            <% } %>
                            stub: ${ii.startstub} - ${ii.endstub} 
                            <%if( entity.state == 'DRAFT' && entity.txntype.matches('PURCHASE_RECEIPT|BEGIN_BALANCE|FORWARD|TRANSFER|RETURN') ) {  %>  
                                &nbsp;&nbsp;<a href="removeBatch" batchno="${ii.batchno}" aftxnitemid="${o.objid}">Remove</a></td>
                            <% } %>
                            <%if( entity.state == 'DRAFT' && entity.txntype.matches('ISSUE') ) {  %>  
                                &nbsp;&nbsp;<a href="revertBatch" batchno="${ii.batchno}" aftxnitemid="${o.objid}">Revert</a></td>
                            <% } %>
                        </td>
                    </tr>
                <% } %>

             <% } %>
        </table>
    </body>
</html>