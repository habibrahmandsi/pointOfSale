/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function () {
    $('#dp1-1').datepicker();
    $('#dp1-2').datepicker();
    $('.crossBtn').click(function () {
        console.log("crossBtn is clickrd !!");
        $(this).closest("div").find("input").val("");
    });

    url = "./getStockReport.do?1=1";


    if (companyId > 0) {
        url += "&companyId=" + companyId;
    }

    if (productId > 0) {
        url += "&productId=" + productId;
    }

    function initializeDataTable(tableIdOrClass, url) {
        console.log("SMNLOG:table initialization is going on.URL:" + url);
        var columnNameToShowDetails = "";
        var prop = "";

        oTable = $(tableIdOrClass).dataTable({
            "aLengthMenu": [[5, 10, 15, -1], [5, 10, 15, "All"]],
            "iDisplayLength": 10,
            "bProcessing": true,
            "bServerSide": true,
            "bJQueryUI": true,
            "bAutoWidth": true,
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


                {"sTitle": "Product Name", "mData": "productName", "bSortable": true},
                {"sTitle": "Company Name", "mData": "companyName", "bSortable": true},
                {"sTitle": "Purchase Qty", "mData": "purchaseItemQty", "bSortable": true},
                {"sTitle": "Purchase Rate", "mData": null, "bSortable": true, "render": function (data) {
                    return getRoundNDigits(data.pItemPRate, globalRoundDigits);

                }
                },
                {"sTitle": "Sale Rate", "mData": null, "bSortable": true, "render": function (data) {
                    return getRoundNDigits(data.pItemSaleRate, globalRoundDigits);

                }
                },
                {"sTitle": "Rest Qty", "mData": "rest_quantity", "bSortable": true},
                {"sTitle": "TP Rate", "mData": null, "bSortable": true, "render": function (data) {
                    return getRoundNDigits(data.tpRate, globalRoundDigits);

                }
                },
                {"sTitle": "MRP Rate", "mData": null, "bSortable": true, "render": function (data) {
                    return getRoundNDigits(data.mrpRate, globalRoundDigits);

                }
                }
            ]
        });
    }

    initializeDataTable("#stockReportList", url)
});



