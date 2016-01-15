<table>
    <tr>
        <td colspan="3"><font class="bold">General Information</font></td> 
    </tr>                
    <tr>
        <td width="5">&nbsp;</td>
        <td width="120px">Name: </td>
        <td>$child.name</td>
    </tr>
    <tr>
        <td width="5">&nbsp;</td>
        <td>Age: </td>
        <td>$child.age</td>
    </tr>
    <tr>
        <td width="5">&nbsp;</td>
        <td>Educational Attainment: </td>
        <td> <p>${ifNull(child.education, '-')}</p> </td>
    </tr>
    <tr>
        <td width="5">&nbsp;</td>
        <td>Remarks: </td>
        <td><p>${ifNull(child.remarks, '-')}</p></td>
    </tr>
    <tr>
        <td colspan="3">
            <br><font class="bold">Employments</font>
        </td> 
    </tr>
    ${getEmploymentList(child.employments)}
    <tr>
        <td colspan="3">
            <br><font class="bold">Other Sources of Income</font>
        </td> 
    </tr>
    ${getOtherIncomeList(child.otherincomes)}        
</table>