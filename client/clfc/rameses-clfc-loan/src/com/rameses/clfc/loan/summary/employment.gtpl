<font class="bold">Employment(s)</font>
<% if(employments.isEmpty() || employments == null) { %>
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
                <th width="250">Employer</th>
                <th width="150">Tel. No.</th>
                <th>Address</th>
            </tr>
        </thead>
        <tbody>
            <% employments.each{ %>
                <tr>
                    <td>$it.employer</td>
                    <td>${ifnull(it.contactno, '-')}</td>
                    <td>${ifnull(it.address, '-')}</td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %><br/>