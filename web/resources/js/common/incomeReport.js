/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var colorArr = ["#FF0000", "#0000CC", "#00FF00", "#00CCFF", "#CC00FF", "#CCCC00", "#eae200", "#ffca2c", "#f7951e", "#a1a1a1"];
    var i = 0;
    var totalSale = 0;
    var totalPurchase = 0;
    var totalDiscount = 0;
    var total = 0;

   /* $(".incomeTotalByUser tbody tr").each(function(){
        $(this).find("td:last").find("div.legentRect").css("background-color",colorArr[i++]);
        totalPurchase += +$(this).find("td").eq(2).html();
        totalSale += +$(this).find("td").eq(3).html();
        totalDiscount += +$(this).find("td").eq(4).html();
        total += +$(this).find("td").eq(5).html();
    });
    totalPurchase = getRoundNDigits(totalPurchase,globalRoundDigits);
    totalSale = getRoundNDigits(totalSale,globalRoundDigits);
    totalDiscount = getRoundNDigits(totalDiscount,globalRoundDigits);
    total = getRoundNDigits(total,globalRoundDigits);

    $(".incomeTotalByUser tbody").append('<tr>'
    + '<td></td>'
    + '<td class="italicFont">Total: </td>'
    + '<td class="italicFont grandTotal"><label>'+totalPurchase+'</label></td>'
    + '<td class="italicFont grandTotal"><label>'+totalSale+'</label></td>'
    + '<td class="italicFont grandTotal"><label>'+totalDiscount+'</label></td>'
    + '<td class="italicFont grandTotal"><label>'+total+'</label></td><td></td></tr>');

    $(".incomeTotalByUser tbody").append('<tr>'
    + '<td></td>'
    + '<td class="italicFont">Total Income: </td>'
    + '<td colspan="4" class="italicFont grandTotal"><label>'+totalSale+' - ('+totalPurchase+' + '+totalDiscount+') = '
    + total+'</label></td></tr>');
    console.log("total:"+total);*/

    $('#dp1-1').datepicker();
    $('#dp1-2').datepicker();

    url =  "./getSaleReport.do"
    +"?fromDate="+$("#fromDate").val()
    +"&toDate="+$("#toDate").val()
    +"&userId="+$("#userId").val()
    +"&opt="+opt;

    console.log("SMNLOG:------------------userIdFromQparam :"+userIdFromQparam);
    if(typeof userIdFromQparam !='undefined' && userIdFromQparam > 0){
        $("#userId").attr("disabled",'disabled');
        $("#userId").val(userIdFromQparam);
        url +="?userId="+userIdFromQparam;
    }


    var fromDate = $("#fromDate").val();
    var toDate = $("#toDate").val();
    var userId = $("#userId").val();
    console.log("SMNLOG:initial fromDate:"+fromDate+" toDate:"+toDate+" userId:"+userId);

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
                {"sTitle": "Total", "mData": "sItemTotalPrice", "bSortable": true},
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



