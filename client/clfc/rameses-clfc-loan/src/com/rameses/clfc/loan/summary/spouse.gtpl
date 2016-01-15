<font class="bold">Spouse Information</font>
<table>
    <% if(spouse.objid == null ) { %>
        <tr>
            <td width="5">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    <% } else { %>
        <tr>
            <td width="100">Name: </td>
            <td>$spouse.lastname, $spouse.firstname ${ifnull(spouse.middlename, '')}</td>
        </tr>
        <tr>
            <td>Address: </td>
            <td>${ifnull(spouse.address, '-')}</td>
        </tr>
        <tr>
            <td>Birth Date: </td>
            <td>${ifnull(spouse.birthdate, '-')}</td>
        </tr>
        <tr>
            <td>Marital Status: </td>
            <td>${ifnull(spouse.civilstatus, '-')}</td>
        </tr>
        <tr>
            <td>Citizenship: </td>
            <td>${ifnull(spouse.citizenship, '-')}</td>
        </tr>
        <tr>
            <td>Contact No.: </td>
            <td>${ifnull(spouse.contactno, '-')}</td>
        </tr>
        <tr>
            <td colspan="4" class="bold">Residency</td>
            <td colspan="4" class="bold">Lot Occupancy</td>

        </tr>
        <tr>
            <td width="100">Type: </td>
            <td width="150">${ifnull(spouse.residency?.type, '-')}</td>
            <td width="100">Since: </td>
            <td width="150">${ifnull(spouse.residency?.since, '-')}</td>
            <td width="100">Type: </td>
            <td width="150">${ifnull(spouse.occupancy?.type, '-')}</td>
            <td width="100">Since: </td>
            <td width="150">${ifnull(spouse.occupancy?.since, '-')}</td>
        </tr>
        <tr>
            <td>Rent Type: </td>
            <td>${ifnull(spouse.residency?.renttype, '-')}</td>
            <td>Rent Amount: </td>
            <td>${ifnull(spouse.residency?.rentamount, '-')}</td>
            <td>Rent Type: </td>
            <td>${ifnull(spouse.occupancy?.renttype, '-')}</td>
            <td>Rent Amount: </td>
            <td>${ifnull(spouse.occupancy?.rentamount, '-')}</td>
        </tr>
        <tr>
            <td>Remarks: </td>
            <td colspan="3">${ifnull(spouse.residency?.remarks, '-')}</td>
            <td>Remarks: </td>
            <td colspan="3">${ifnull(spouse.occupancy?.remarks, '-')}</td>
        </tr>
    <% } %>
</table><br/>