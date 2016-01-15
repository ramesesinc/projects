<font class="bold">Educational Background(s)</font>
<% if(educations.isEmpty() || educations == null) { %>
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
                <th width="250">School Attended</th>
                <th width="300">Educational Attainment</th>
                <th>Remarks</th>
            </tr>
        </thead>
        <tbody>
            <% educations.each{ %>
                <tr>
                    <td>$it.school</td>
                    <td>$it.attainment</td>
                    <td>${ifnull(it.remarks, '-')}</td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %><br/>