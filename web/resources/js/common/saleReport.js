/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    $('#dp1-1').datepicker();
    $('#dp1-2').datepicker();

    url = "./getSaleReport.do";
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

     url =  "./getSaleReport.do"
                +"?fromDate="+$("#fromDate").val()
                +"&toDate="+$("#toDate").val()
                +"&userId="+$("#userId").val();
        initializeDataTable("#salesReportList", url);
        $(document).find("#salesReportList").find("thead").find("th:first-child").click()
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
                {"sTitle": "Product Name", "mData": "productName", "bSortable": true},
                {"sTitle": "Company Name", "mData": "companyName", "bSortable": true},
                {"sTitle": "PRate", "mData": "sItemPRate", "bSortable": true},
                {"sTitle": "SRate", "mData": "sItemSaleRate", "bSortable": true},
                {"sTitle": "Qty", "mData": "saleItemQty", "bSortable": true},
                {"sTitle": "Total", "mData": "total_amount", "bSortable": true},
                {"sTitle": "Sold By", "mData": "userName", "bSortable": true},
            ]
        } );
    }
    initializeDataTable("#salesReportList", url)
    if(userIdFromQparam > 0){
        $(".dataTables_filter").find("input").parent().remove();
    }else
    $(".dataTables_filter").find("input").focus();
});



