<font class="bold">Loan Details</font>
<table>
    <tr>
        <td width="100">App. Type: </td>
        <td width="150">$summary.apptype</td>
        <td width="100">Date Filed: </td>
        <td width="150">$summary.dtcreated</td>
    </tr>
    <tr>
        <td>Client Type: </td>
        <td>$summary.clienttype</td>
        <td>Interviewed By: </td>
        <td>${ifnull(summary.marketedby, '-')}</td>
    </tr>
    <tr>
        <td>Product Type: </td>
        <td>$summary.schedule.name</td>
        <td>Term: </td>
        <td>$summary.schedule.term days</td>
    </tr>
    <tr>
        <td>Amount Applied: </td>
        <td>$summary.loanamount</td>
        <td>Loan Count: </td>
        <td></td>
    </tr>
    <tr>
        <td>Purpose of Loan: </td>
        <td colspan="3">
            <p>$summary.purpose</p>
        </td>
    </tr>
</table><br/>