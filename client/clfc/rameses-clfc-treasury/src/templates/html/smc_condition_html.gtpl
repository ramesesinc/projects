<%
import com.rameses.util.*;
%>
<html>
    <head>
        <style>
            .block {
                padding-left: 10px;
            }
        </style>
    </head>
    <body>
        <% conditions.eachWithIndex{ cond, i-> %>            
            <div class="block">
                <%
                    try {
                        out.print('<u>' + cond.title + '</u>&nbsp;&nbsp;');
                        if (mode != 'read') {
                            out.print('<a href="editCondition" objid="' + cond.objid + '">[Edit]</a>&nbsp;&nbsp;');
                            out.print('<a href="removeCondition" objid="' + cond.objid + '">[Remove]</a>&nbsp;&nbsp;');
                        }

                        out.print('<br>&nbsp;&nbsp;&nbsp;');
                        out.print('&nbsp;' + cond.operator?.caption + '&nbsp;');
                        def handler = cond.handler;
                        if (cond?.operator?.caption == 'default') {
                            switch (handler) {
                                case 'integer'  : out.print(cond.defaultvalue); break;
                                case 'decimal'  : out.print(cond.defaultvalue); break;
                                default         : out.print("'" + cond.defaultvalue + "'"); break;
                            }
                        } else {
                            switch (handler) {
                                case 'integer'  : out.print(cond.integervalue); break;
                                case 'decimal'  : out.print(cond.decimalvalue); break;
                                case 'date'     : out.print("'" + cond.datevalue + "'"); break;
                                default         : out.print("'" + cond.stringvalue + "'"); break;
                            }
                        }
                    } catch(e) {
                        out.print( e.message );
                    }
                %>
            </div>
        <% } %>
    </body>
</html>

