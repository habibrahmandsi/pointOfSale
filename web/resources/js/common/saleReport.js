/**
 * Created by habib on 1/27/15.
 */

$(document).ready(function() {
    var colorArr = ["#FF0000", "#0000CC", "#00FF00", "#00CCFF", "#CC00FF", "#CCCC00", "#eae200", "#ffca2c", "#f7951e", "#a1a1a1"];
    var i = 0;
    var total = 0;
    $(".salesTotalByUser tbody tr").each(function(){
       $(this).find("td:last").find("div.legentRect").css("background-color",colorArr[i++]);
        //total += +$(this).find("td").eq(2).html();
    });

   /* $(".salesTotalByUser tbody").append('<tr><td></td><td class="italicFont">Total: </td><td class="italicFont grandTotal"><label>'
    +getRoundNDigits(total,globalRoundDigits)+'</label></td><td></td></tr>')
*/
    console.log("total:"+total);

    $('#dp1-1').datepicker();
    $('#dp1-2').datepicker();

    url =  "./getSaleReport.do"
    +"?fromDate="+$("#fromDate").val()
    +"&toDate="+$("#toDate").val()
    +"&opt="+opt;

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
    $(".dataTables_filter").find("input").focus();


    (function(d3) {
        'use strict';
        var dataset = donutChartData;

        var width = 160;
        var height = 160;
        //var radius = Math.min(width, height) / 2;
        var radius = Math.min(width, height) / 2;
        var donutWidth = 40;
        var legendRectSize = 10;                                  // NEW
        var legendSpacing = 2;                                    // NEW
        //var color = d3.scale.category20b();
        var color = d3.scale.ordinal().range(colorArr);
        var svg = d3.select('#chartByUserTotalSales')
            .append('svg')
            .attr('width', width)
            .attr('height', height)
            .append('g')
            .attr('transform', 'translate(' + (width / 2) +
            ',' + (height / 2) + ')');
        var arc = d3.svg.arc()
            .innerRadius(radius - donutWidth)
            .outerRadius(radius);
        var pie = d3.layout.pie()
            .value(function(d) { return d.count; })
            .sort(null);
        var path = svg.selectAll('path')
            .data(pie(dataset))
            .enter()
            .append('path')
            .attr('d', arc)
            .attr('fill', function(d, i) {
                return color(d.data.label);
            });
        /*var legend = svg.selectAll('.legend')                     // NEW
            .data(color.domain())                                   // NEW
            .enter()                                                // NEW
            .append('g')                                            // NEW
            .attr('class', 'legend')                                // NEW
            .attr('transform', function(d, i) {                     // NEW
                var height = legendRectSize + legendSpacing;          // NEW
                var offset =  height * color.domain().length / 2;     // NEW
                var horz = -2 * legendRectSize;                       // NEW
                var vert = i * height - offset;                       // NEW
                return 'translate(' + horz + ',' + vert + ')';        // NEW
            });                                                     // NEW
        legend.append('rect')                                     // NEW
            .attr('width', legendRectSize)                          // NEW
            .attr('height', legendRectSize)                         // NEW
            .style('fill', color)                                   // NEW
            .style('stroke', color);                                // NEW

        legend.append('text')                                     // NEW
            .attr('x', legendRectSize + legendSpacing)              // NEW
            .attr('y', legendRectSize - legendSpacing)              // NEW
            .text(function(d,i) {
                return d; });  */                     // NEW
    })(window.d3);
});



