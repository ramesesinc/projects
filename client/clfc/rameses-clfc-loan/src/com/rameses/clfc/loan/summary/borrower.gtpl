<font class="bold">Principal Information</font>
<table>
    <tr>
        <td width="100">Name: </td>
        <td>$borrower.lastname, $borrower.firstname ${ifnull(borrower.middlename, '')}</td>
    </tr>
    <tr>
        <td>Address: </td>
        <td>${ifnull(borrower.address, '-')}</td>
    </tr>
    <tr>
        <td>Birth Date: </td>
        <td>${ifnull(borrower.birthdate, '-')}</td>
    </tr>
    <tr>
        <td>Marital Status: </td>
        <td>${ifnull(borrower.civilstatus, '-')}</td>
    </tr>
    <tr>
        <td>Citizenship: </td>
        <td>${ifnull(borrower.citizenship, '-')}</td>
    </tr>
    <tr>
        <td>Contact No.: </td>
        <td>${ifnull(borrower.contactno, '-')}</td>
    </tr>
    <tr>
        <td colspan="4" class="bold">Residency</td>
        <td colspan="4" class="bold">Lot Occupancy</td>

    </tr>
    <tr>
        <td width="100">Type: </td>
        <td width="150">${ifnull(borrower.residency?.type, '-')}</td>
        <td width="100">Since: </td>
        <td width="150">${ifnull(borrower.residency?.since, '-')}</td>
        <td width="100">Type: </td>
        <td width="150">${ifnull(borrower.occupancy?.type, '-')}</td>
        <td width="100">Since: </td>
        <td width="150">${ifnull(borrower.occupancy?.since, '-')}</td>
    </tr>
    <tr>
        <td>Rent Type: </td>
        <td>${ifnull(borrower.residency?.renttype, '-')}</td>
        <td>Rent Amount: </td>
        <td>${ifnull(borrower.residency?.rentamount, '-')}</td>
        <td>Rent Type: </td>
        <td>${ifnull(borrower.occupancy?.renttype, '-')}</td>
        <td>Rent Amount: </td>
        <td>${ifnull(borrower.occupancy?.rentamount, '-')}</td>
    </tr>
    <tr>
        <td>Remarks: </td>
        <td colspan="3">${ifnull(borrower.residency?.remarks, '-')}</td>
        <td>Remarks: </td>
        <td colspan="3">${ifnull(borrower.occupancy?.remarks, '-')}</td>
    </tr>
</table><br/>