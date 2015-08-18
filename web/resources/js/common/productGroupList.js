/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var url = "./getProductGroups.do";
    var columns = [

        {"sTitle": "Name", "mData": "name", "bSortable": true},
        {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

            /* Condition to print appropriate icon based on status*/
            var html = '<a class="editTxt" href="./upsertProductGroup.do?productGroupId='+data.id+'"><img alt="Edit" title="Edit" src="'+contextPath+'/resources/images/edit.png">Edit</a>'
                +'&nbsp;<a class="deleteTxt" href="./deleteProductGroup.do?productGroupId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png">Delete</a>';

            return html;
        }
        }
    ];

    commonDataTableInit("#productGroupList", url, columns);

    $(".dataTables_filter").find("input").focus();
});



