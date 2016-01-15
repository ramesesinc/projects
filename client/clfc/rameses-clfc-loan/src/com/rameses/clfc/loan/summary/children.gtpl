<font class="bold">Children</font>
<% if(children.isEmpty() || children == null) { %>
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
                <th width="250">Name</th>
                <th width="100">Age</th>
                <th width="300">Educational Attainment</th>
                <th>Remarks</th>
            </tr>
        </thead>
        <tbody>
            <% children.each{ %>
                <tr>
                    <td>$it.name</td>
                    <td>$it.age yrs. old</td>
                    <td>${ifnull(it.education, '-')}</td>
                    <td>${ifnull(it.remarks, '-')}</td>
                </tr>
            <%}%>
        </tbody>
    </table>
<% } %><br/>
