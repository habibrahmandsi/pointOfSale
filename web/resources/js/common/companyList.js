/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var url = "./getCompanys.do";
    var columns = [

        {"sTitle": "Name", "mData": "name", "bSortable": true},
        {"sTitle": "Agent Name", "mData": "agent_name", "bSortable": true},
        {"sTitle": "Agent Cell No", "mData": "agent_cell_no", "bSortable": true},
        {"sTitle": "Address", "mData": "permanent_address", "bSortable": true},
        {"sTitle": "Company Address", "mData": "company_address", "bSortable": true},
        {"sTitle": "Company Cell no", "mData": "company_cell_no", "bSortable": true},
        {"sTitle": "Company email", "mData": "company_email", "bSortable": true},
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<a class="editTxt" href="./upsertCompany.do?companyId='+data.id+'"><img alt="Edit" title="Edit" src="'+contextPath+'/resources/images/edit.png">Edit</a>'
                +'&nbsp;<a class="deleteTxt" href="./deleteCompany.do?companyId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png">Delete</a>';

            return html;
        }
        }
    ];

    commonDataTableInit("#companyList", url, columns);

    $(".dataTables_filter").find("input").focus();
});



