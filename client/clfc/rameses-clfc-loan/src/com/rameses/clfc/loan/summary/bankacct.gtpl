<font class="bold">Bank Accounts</font>
<table width="1000">
    <tr>
        <td>
            <font class="bold">Savings Account(s)</font>
            <% if(savingaccts.isEmpty() || savingaccts == null) { %>
                <table>
                    <tr>
                        <td width="5">&nbsp;</td>
                        <td class="nowrap" colspan="2">
                            <i>-- No available information found --</i>                         
                        </td> 
                    </tr> 
                </table>
            <% } else { %>
                <table width="980" border="1" style="padding: 0px; margin-left: 10px;">
                    <thead>
                        <tr>
                            <th width="200">Bank Name</th>
                            <th>Remarks</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% savingaccts.each{ %>
                            <tr>
                                <td>$it.bankname</td>
                                <td>${ifnull(it.remarks, '-')}</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %><br/>
        </td>
    </tr>
    <tr>
        <td>
            <font class="bold">Checking Account(s)</font>
            <% if(checkingaccts.isEmpty() || checkingaccts == null) { %>
                <table>
                    <tr>
                        <td width="5">&nbsp;</td>
                        <td class="nowrap" colspan="2">
                            <i>-- No available information found --</i>                         
                        </td> 
                    </tr> 
                </table>
            <% } else { %>
                <table width="980" border="1" style="padding: 0px; margin-left: 10px;">
                    <thead>
                        <tr>
                            <th width="200">Bank Name</th>
                            <th>Remarks</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% checkingaccts.each{ %>
                            <tr>
                                <td>$it.bankname</td>
                                <td>${ifnull(it.remarks, '-')}</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %><br/>
        </td>
    </tr>
    <tr>
        <td>
            <font class="bold">Other Account(s)</font>
            <p style="margin-left: 10px;">${ifnull(otheracct?.remarks, '-')}</p>
        </td>
    </tr>
</table><br/>