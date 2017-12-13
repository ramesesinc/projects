<html>
    <body>
        <table style="border:1px solid black;" width="100%">
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
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('PURCHASE_RECEIPT') && (o.qty > o.qtyserved) ) {  %> 
                        <a href="addBatchEntry" unit="${o.unit}" afid="${o.item.objid}">Add Entry</a>
                      <% } %>
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('ISSUE')  && (o.qty > o.qtyserved) ) {  %> 
                        <a href="issueBatch" unit="${o.unit}" afid="${o.item.objid}">Issue</a>
                      <% } %>
                      <%if( entity.state == 'DRAFT' && entity.txntype.matches('ISSUE')  && ( o.qtyserved > 0 ) && o.afunit.formtype == 'cashticket' ) {  %> 
                        &nbsp;&nbsp;<a href="editEntry" unit="${o.unit}" afid="${o.item.objid}">Edit</a>
                      <% } %>

                   </td>
                </tr>

                <%o.items.each { ii-> %>
                    <tr>
                        <td>&nbsp;</td>
                        <td colspan="6">
                            batch:${ii.batchno} 
                            <% if(o.afunit.formtype == 'serial') { %> 
                            ${ (ii.prefix==null)?'':ii.prefix } ${ii.startseries} - ${ii.endseries} ${(ii.suffix==null)?'':ii.suffix}
                            <% } %>
                            stub: ${ii.startstub} - ${ii.endstub} 
                            <%if( entity.state == 'DRAFT' ) {  %>  
                                &nbsp;&nbsp;<a href="removeBatchEntry" batchno="${ii.batchno}" unit="${o.unit}" afid="${o.item.objid}">Remove</a></td>
                            <% } %>
                        </td>
                    </tr>
                <% } %>

             <% } %>
        </table>
    </body>
</html>