/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var url = "./getUsers.do";

    oTable = $("#userList").dataTable({
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
            {"sTitle": "Father Name", "mData": "father_name", "bSortable": true},
            {"sTitle": "Mother Name", "mData": "mother_name", "bSortable": true},
            {"sTitle": "Sex", "mData": "sex", "bSortable": true},
            {"sTitle": "Age", "mData": "age", "bSortable": true},
            {"sTitle": "Date Of Birth", "mData": null, "bSortable": true, "render": function (data) {
                var date= new Date(data.date_of_birth);
                return  date.getDate() + "/" +(date.getMonth()+1) + "/" + date.getFullYear()

            }
            },
            {"sTitle": "Max Discount Percent", "mData": "max_discount_percent", "bSortable": true},
            {"sTitle": "National Id", "mData": "national_id_no", "bSortable": true},
            {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

                /* Condition to print appropriate icon based on status*/
                var html = '<a href="./upsertUser.do?userId='+data.id+'"><img alt="Edit" title="Edit" src="'+contextPath+'/resources/images/edit.png"></a>'
                          +'&nbsp;<a href="./deleteUser.do?userId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png"></a>';

                return html;
            }
            }
        ]
    });
    $(".dataTables_filter").find("input").focus();
});



