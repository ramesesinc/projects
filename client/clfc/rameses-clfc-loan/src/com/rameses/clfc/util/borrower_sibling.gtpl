<table>
    <tr>
        <td colspan="3"><font class="bold">General Information</font></td> 
    </tr>                                
    <tr>    
        <td width="5">&nbsp;</td>
        <td>Name : </td>
        <td>$sibling.name</td>
    </tr>
    <tr>
        <td width="5">&nbsp;</td>
        <td>Age : </td>
        <td>$sibling.age</td>
    </tr>
    <tr>
        <td width="5">&nbsp;</td>
        <td>Address : </td>
        <td>$sibling.address</td>
    </tr>
    <tr>
        <td colspan="3">
            <br><font class="bold">Employments</font>
        </td> 
    </tr>  
    ${getEmploymentList(sibling.employments)}
    <tr>
        <td colspan="3">
            <br><font class="bold">Other Sources of Income</font>
        </td> 
    </tr>  
    ${getOtherIncomeList(sibling.otherincomes)}
    </table>