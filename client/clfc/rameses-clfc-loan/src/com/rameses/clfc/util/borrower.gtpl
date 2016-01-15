<font class="bold" style="margin-left: 10px;">Personal Information</font>
<table style="margin-left: 10px;">
    <tbody>
        <tr>
            <td>Name: </td>
            <td>$borrower.name</td>
        </tr>
        <tr>
            <td>Address: </td>
            <td>${ifNull(borrower.address, '-')}</td>
        </tr>
        <tr>
            <td>Birth Date: </td>
            <td>${ifNull(borrower.birthdate, '-')}</td>
        </tr>
        <tr>
            <td>Marital Status: </td>
            <td>${ifNull(borrower.civilstatus, '-')}</td>
        </tr>
        <tr>
            <td>Citizenship: </td>
            <td>${ifNull(borrower.citizenship, '-')}</td>
        </tr>
        <tr>
            <td>Contact No.: </td>
            <td>${ifNull(borrower.contactno, '-')}</td>
        </tr>
    </tbody>
</table><br/>
<font class="bold" style="margin-left: 10px;">Residency</font>
<table style="margin-left: 10px;">
    <tbody>
        <tr>
            <td width="120px">Type: </td>
            <td width="150px">${ifNull(borrower.residency?.type, '-')}</td>
            <td width="120px">Since: </td>
            <td width="150px">${ifNull(borrower.residency?.since, '-')}</td>
        </tr>
        <tr>
            <td>Rent Type: </td>
            <td>${ifNull(borrower.residency?.renttype, '-')}</td>
            <td>Rent Amount: </td>
            <td>${ifNull(borrower.residency?.rentamount, '-')}</td>
        </tr>
        <tr>
            <td>Remarks: </td>
            <td colspan="3">${ifNull(borrower.residency?.remarks, '-')}</td>
        </tr>
    </tbody>
</table><br/>
<font class="bold" style="margin-left: 10px;">Lot Occupancy</font>
<table style="margin-left: 10px;">
    <tbody>
        <tr>
            <td width="120px">Type: </td>
            <td width="150px">${ifNull(borrower.occupancy?.type, '-')}</td>
            <td width="120px">Since: </td>
            <td width="150px">${ifNull(borrower.occupancy?.since, '-')}</td>
        </tr>
        <tr>
            <td>Rent Type: </td>
            <td>${ifNull(borrower.occupancy?.renttype, '-')}</td>
            <td>Rent Amount: </td>
            <td>${ifNull(borrower.occupancy?.rentamount, '-')}</td>
        </tr>
        <tr>
            <td>Remarks: </td>
            <td colspan="3">${ifNull(borrower.occupancy?.remarks, '-')}</td>
        </tr>
    </tbody>
</table>