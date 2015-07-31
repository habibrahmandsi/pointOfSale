/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {


    var url = "./getProducts.do";

    oTable = $("#productList").dataTable({
        "aLengthMenu": [[3, 5, 9, 15,20, -1], [3, 5, 9, 15,20, "All"]],
        "iDisplayLength": rowDisplayGlobal,
        "bProcessing": true,
        "bServerSide": true,
        "bJQueryUI": true,
        "bAutoWidth": true,
        "sPaginationType": "simple_numbers", // you can also give here 'simple','simple_numbers','full','full_numbers'
        "oLanguage": {
            "sSearch": "Search:"
        },
        "sAjaxSource": url,
        "fnServerData": fnDataTablesPipeline,
        "aoColumns": [

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
            {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

                /* Condition to print appropriate icon based on status*/
                var html = '<a href="./upsertProduct.do?productId='+data.productId+'"><img alt="Edit" title="Edit" src="'+contextPath+'/resources/images/edit.png"></a>'
                          +'&nbsp;<a href="./deleteProduct.do?productId='+data.productId+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png"></a>';

                return html;
            }
            }
        ]
    });

    $(".dataTables_filter").find("input").focus();
});



