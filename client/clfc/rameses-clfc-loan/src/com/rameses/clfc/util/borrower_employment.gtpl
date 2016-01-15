<font class="bold">General Information</font>
<table>
    <tr>
        <td>Empoyer : </td>
        <td>$employment.employer</td>
    </tr>
    <tr>
        <td>Tel. No. : </td>
        <td>${ifNull(employment.contactno, '-')}</td>
    </tr>
    <tr>
        <td>Address : </td>
        <td>${ifNull(employment.address, '-')}</td>
    </tr>
    <tr>
        <td>Position : </td>
        <td>${ifNull(employment.position, '-')}</td>
    </tr>
    <tr>
        <td>Salary : </td>
        <td>${ifNull(employment.salary, '-')}</td>
    </tr>
    <tr>
        <td class="nowrap">Length of Service : </td>
        <td>${ifNull(employment.lengthofservice, '-')}</td>
    </tr>
    <tr>
        <td>Status : </td>
        <td>${ifNull(employment.status, '-')}</td>
    </tr>
    <tr>
        <td>Remarks : </td>
        <td><p>$employment.remarks</p></td>
    </tr>
</table>