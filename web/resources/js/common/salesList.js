/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var url = "./getSales.do?opt="+opt;
    var subUrl = "";
    console.log("SMNLOG:opt--------:"+opt);

    var columns = [

        {"sTitle": "Invoice No", "mData": "sales_token_no", "bSortable": true},
        {"sTitle": "Sales Date", "mData": null, "bSortable": true, "render": function (data) {
            var date= new Date(data.sales_date);
            return  getDateForTableView(date)

        }
        },
        {"sTitle": "Total Amount", "mData": null, "bSortable": true, "render": function (data) {
            var date= new Date(data.sales_date);
            var totalAmount = +data.total_amount;
            var discount = +data.discount;

            return  (totalAmount+discount)

        }
        },
        {"sTitle": "Discount", "mData": "discount", "bSortable": true},
        {"sTitle": "Sold By", "mData": "userName", "bSortable": true},
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<b><a style="color: #28ce75" href="./upsertSales.do?salesId='+data.id+'">Details View</a><b>'

            return html;
        }
        },
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<b><a style="color: #c644ce" href="./upsertSalesReturn.do?salesId='+data.id+'">Sales Return</a></b>'

            return html;
        }
        },
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<a class="deleteTxt" href="./deleteSales.do?salesId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png">Delete</a>';

            return html;
        }
        }
    ];

    var colForSalesReturn =[

        {"sTitle": "Invoice No", "mData": "sales_token_no", "bSortable": true},
        {"sTitle": "Sales Date", "mData": null, "bSortable": true, "render": function (data) {
            var date= new Date(data.sales_date);
            return  getDateForTableView(date)

        }
        },
        {"sTitle": "Total Amount", "mData": "total_amount", "bSortable": true},
        {"sTitle": "Discount", "mData": "discount", "bSortable": true},
        {"sTitle": "Salesd By", "mData": "userName", "bSortable": true},
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<b><a style="color: #28ce75" href="./upsertSalesReturn.do?salesId='+data.id+'&update=1">Details View</a><b>'

            return html;
        }
        },
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<a class="deleteTxt" href="./deleteSales.do?opt=1&salesId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png">Delete</a>';

            return html;
        }
        }
    ];
    if("1".indexOf(opt) > -1){
        commonDataTableInit("#salesList", url, colForSalesReturn);
    }else{
        commonDataTableInit("#salesList", url, columns);
    }

    $(".dataTables_filter").find("input").focus();
});



