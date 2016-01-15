<font class="bold">Other Source(s) of Income</font>
<% if(otherincomes.isEmpty() || otherincomes == null) { %>
    <table>
        <tr>
            <td width="5">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    </table>
<% } else { %>
    <table width="1000" border="1" style="padding: 0px; margin-left: 10px;">
        <thead>
            <tr>
                <th width="400">Business/Other Specs.</th>
                <th>Remarks</th>
            </tr>
        </thead>
        <tbody>
            <% otherincomes.each{ %>
                <tr>
                    <td>$it.name</td>
                    <td>${ifnull(it.remarks, '-')}</td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %><br/>