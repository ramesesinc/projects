<font class="bold">General Information</font>
<table>
<tr>
    <td width="120px">Kind of Loan: </td>
    <td>${ifNull(otherlending.kind, '-')}</td>
</tr>
<tr>
    <td>Name of Lending Inst.: </td>
    <td>$otherlending.name</td>
</tr>
<tr>
    <td>Address: </td>
    <td>${ifNull(otherlending.address, '-')}</td>
</tr>
<tr>
    <td>Loan Amount: </td>
    <td>$otherlending.amount</td>
</tr>
<tr>
    <td>Date Granted: </td>
    <td>${ifNull(otherlending.dtgranted, '-')}</td>
</tr>
<tr>
    <td>Maturity Date: </td>
    <td>${ifNull(otherlending.dtmatured, '-')}</td>
</tr>
<tr>
    <td>Term: </td>
    <td>${ifNull(otherlending.term, '-')}</td>
</tr>
<tr>
    <td>Interest Rate: </td>
    <td>${ifNull(otherlending.interest, '-')}</td>
</tr>
<tr>
    <td>Mode of Payment: </td>
    <td>${ifNull(otherlending.paymentmode, '-')}</td>
</tr>
<tr>
    <td>Payment: </td>
    <td>$otherlending.paymentamount</td>
</tr>
<tr>
    <td>Collateral(s): </td>
    <td> <p>${ifNull(otherlending.collateral, '-')}</p> </td>
</tr>
<tr>
    <td>Remarks: </td>
    <td> <p>${ifNull(otherlending.remarks, '-')}</p> </td>
</tr>
</table>