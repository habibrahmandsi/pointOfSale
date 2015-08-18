/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    $('#dp1-1').datepicker();
    $('#dp1-2').datepicker();

    url = "./getUnpostedSales.do";
    console.log("SMNLOG:------------------userIdFromQparam :"+userIdFromQparam);
    if(userIdFromQparam > 0){
        $("#userId").attr("disabled",'disabled');
        $("#userId").val(userIdFromQparam);
        url +="?userId="+userIdFromQparam;
    }


    var fromDate = $("#fromDate").val();
    var toDate = $("#toDate").val();
    var userId = $("#userId").val();
    console.log("SMNLOG:initial fromDate:"+fromDate+" toDate:"+toDate+" userId:"+userId);
    $(".unpostedSearch").click(function(){
    console.log("SMNLOG:Searching.....");
        fromDate = $("#fromDate").val();
        toDate = $("#toDate").val();
        userId = $("#userId").val();
        console.log("SMNLOG:update fromDate:"+fromDate+" toDate:"+toDate+" userId:"+userId);

     url =  "./getUnpostedSales.do"
                +"?fromDate="+$("#fromDate").val()
                +"&toDate="+$("#toDate").val()
                +"&userId="+$("#userId").val();
        initializeDataTable("#unpostedSalesList", url);
        //$(document).find("#unpostedSalesList").find("thead").find("th:nth-child(0)").click();
        $(document).find("#unpostedSalesList").find("thead").find("th:first-child").click()
    });


    function initializeDataTable(tableIdOrClass, url){
        console.log("SMNLOG:table initialization is going on.URL:"+url);
        var columnNameToShowDetails = "";
        var prop = "";

        oTable = $(tableIdOrClass).dataTable( {
            "aLengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]],
            "iDisplayLength": 10,
            "bProcessing": true,
            "bServerSide": true,
            "bJQueryUI": true,
            "bAutoWidth":true,
            "bDestroy": true,
            //"bRetrieve": true,
            "sPaginationType": "simple_numbers", // you can also give here 'simple','simple_numbers','full','full_numbers'
            "oLanguage": {
                "sSearch": "Search:"
            },
            "sAjaxSource": url
             ,
            "fnServerData": fnDataTablesPipeline,
            "aoColumns": [


                {"sTitle": "Invoice No", "mData": "sales_token_no", "bSortable": true},
                {"sTitle": "Sales Date", "mData": null, "bSortable": true, "render": function (data) {
                    var date= new Date(data.sales_date);
                    return  getDateForTableView(date)

                }
                },
                {"sTitle": "Total Amount", "mData": "total_amount", "bSortable": true},
                {"sTitle": "Discount", "mData": "discount", "bSortable": true},
                {"sTitle": "Sold By", "mData": "userName", "bSortable": true},
                {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

                    /* Condition to print appropriate icon based on status*/
                    var html = '<b><a style="color: #28ce75" href="./upsertSales.do?salesId='+data.id+'">Sale Now</a><b>'

                    return html;
                }
                },
                {"sTitle": "", "mData": null,"bSortable": false, "render": function (data) {

                    /* Condition to print appropriate icon based on status*/
                    var html = '<a class="deleteTxt" href="./deleteSales.do?opt=2&salesId='+data.id+'"><img alt="Delete" title="Delete" src="'+contextPath+'/resources/images/delete2.png">Delete</a>';

                    return html;
                }
                }
            ]
        } );
    }
    initializeDataTable("#unpostedSalesList", url)
    if(userIdFromQparam > 0){
        $(".dataTables_filter").find("input").parent().remove();
    }else
    $(".dataTables_filter").find("input").focus();

    var sData = oTable.fnGetData();
    console.log('The cell clicked on had the value of '+JSON.stringify(sData));

});



