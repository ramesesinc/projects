<% def dmf = new java.text.SimpleDateFormat('MMM'); def YMD = new java.text.SimpleDateFormat('yyyy-MM-dd'); %>

        ${o.acctname}
	${o.address}
          ${o.acctno}             ${o.blockseqno}           ${o.classification}


                                   ${o.readingdate ? YMD.format(o.readingdate) : ""}
                                   ${o.reading}
                                   ${o.prevreadingdate ? YMD.format(o.prevreadingdate) : ""}
                                   ${o.prevreading}
                                   ${o.volume}
				   ${o.amount}
                                   ${o.readingdate ? dmf.format(o.readingdate) : ""}

				   ${(o.arrears==0 && o.discdate) ? YMD.format(o.discdate) : "-"}
                                   ${(o.arrears==0) ? o.amount*(1-0.05) : "-"}

                                   ${o.arrears+o.surcharge+o.interest+o.otherfees}
                                   ${o.amount+o.arrears+o.surcharge+o.interest+o.otherfees}

                                   ${o.amount * 0.14}
                                   ${o.duedate ? YMD.format(o.duedate) : ""}
                                   ${o.amount*1.14}
                                   ${(o.amount*1.14)+o.arrears+o.surcharge+o.interest+o.otherfees}

                                   ${o.refbillno}


