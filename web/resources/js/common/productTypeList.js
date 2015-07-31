/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var url = "./getProductTypes.do";

    oTable = $("#productTypeList").dataTable({
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

            {"sTitle": "Name", "mData": "name", "bSortable": true},
            {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

                /* Condition to print appropriate icon based on status*/
                var html = '<a href="./upsertProductType.do?productTypeId='+data.id+'"><img alt="Edit" title="Edit" src="'+contextPath+'/resources/images/edit.png"></a>'
                          +'&nbsp;<a href="./deleteProductType.do?productTypeId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png"></a>';

                return html;
            }
            }
        ]
    });
    $(".dataTables_filter").find("input").focus();
});


