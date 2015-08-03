/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var url = "./getPurchases.do?opt="+opt;
    var subUrl = "";
    console.log("SMNLOG:opt--------:"+opt);

    var columns = [

        {"sTitle": "Invoice No", "mData": "purchase_token_no", "bSortable": true},
        {"sTitle": "Purchase Date", "mData": null, "bSortable": true, "render": function (data) {
            var date= new Date(data.purchase_date);
            return  getDateForTableView(date)

        }
        },
        {"sTitle": "Total Amount", "mData": "total_amount", "bSortable": true},
        {"sTitle": "Discount", "mData": "discount", "bSortable": true},
        {"sTitle": "Purchased By", "mData": "userName", "bSortable": true},
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<b><a style="color: #28ce75" href="./upsertPurchase.do?purchaseId='+data.id+'">Details View</a><b>'

            return html;
        }
        },
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<b><a style="color: #c644ce" href="./upsertPurchaseReturn.do?purchaseId='+data.id+'">Purchase Return</a></b>'

            return html;
        }
        },
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<a href="./deletePurchase.do?purchaseId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png"></a>';

            return html;
        }
        }
    ];

    var colForPurchaseReturn =[

        {"sTitle": "Invoice No", "mData": "purchase_token_no", "bSortable": true},
        {"sTitle": "Purchase Date", "mData": null, "bSortable": true, "render": function (data) {
            var date= new Date(data.purchase_date);
            return  getDateForTableView(date)

        }
        },
        {"sTitle": "Total Amount", "mData": "total_amount", "bSortable": true},
        {"sTitle": "Discount", "mData": "discount", "bSortable": true},
        {"sTitle": "Purchased By", "mData": "userName", "bSortable": true},
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<b><a style="color: #28ce75" href="./upsertPurchaseReturn.do?purchaseId='+data.id+'&update=1">Details View</a><b>'

            return html;
        }
        },
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<a href="./deletePurchase.do?opt=1&purchaseId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png"></a>';

            return html;
        }
        }
    ];
    if("1".indexOf(opt) > -1){
        commonDataTableInit("#purchaseList", url, colForPurchaseReturn);
    }else{
        commonDataTableInit("#purchaseList", url, columns);
    }

    $(".dataTables_filter").find("input").focus();
});



