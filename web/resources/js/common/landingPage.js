/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {

    console.log("SMNLOG:limitQty:"+limitQty);
    var url = "./getProducts.do?limitQty="+limitQty;
    var columns = [

        {"sTitle": "Name", "mData": null, "bSortable": true, "render": function (data) {
            var txt =  data.productName;
            if(data.uomName != null)
                txt += "-"+data.uomName;
            if(data.typeName != null)
                txt += "-"+data.typeName;

            return txt;
        }
        },
        {"sTitle": "Group Name", "mData": "groupName", "bSortable": true},
        {"sTitle": "Purchase Rate", "mData": "purchase_rate", "bSortable": true},
        {"sTitle": "Sale Rate", "mData": "sale_rate", "bSortable": true},
        {"sTitle": "Company Name", "mData": "companyName", "bSortable": true},
        {"sTitle": "Total Quantity", "mData": "total_quantity", "bSortable": true}
    ];

    commonDataTableInit("#limitedStockProductList", url, columns);
    //$(".dataTables_filter").find("input").focus();

});



