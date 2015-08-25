var dashArraySpace = "10,10";
var circleRadius = 4;
var campaignLegendCircleRadius = 3;
var addCampaignBtnName = "Add";

$(document).ready(function () {
    console.log("SMNLOG:Doc is ready......");
    if(typeof lineChartData[0].values != 'undefined' && lineChartData[0].values.length > 1){
        console.log("SMNLOG:ineChartData.values.length:"+lineChartData[0]);
        renderAnnualYieldsChart(lineChartData);
    }

})

function renderAnnualYieldsChart(chartData) {
    console.log("SMNLOG:Data 11:"+JSON.stringify(chartData));
    var campaigns = [];

    var margin = {top: 50, right: 50, bottom: 30, left: 40},
        canvasHeight = 370,
        width = 830,
        height = 380 - margin.top - margin.bottom;

    var x = d3.scale.ordinal().rangeRoundBands([0, width], 0),
        y = d3.scale.linear().range([height, 0]),
        xAxis = d3.svg.axis().scale(x).orient("bottom").tickFormat(""),
        yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(function (d) {
            return d;
        });

    /* Lines */
    var line = d3.svg.line()
//      .interpolate("basis")
        .interpolate("linear")
//      .interpolate("step-after")
        .x(function (d) {
            return x(d.date);
        })
        .y(function (d) {
            return y(d.totalCounts);
        });

    /* Canvas */
    var svg = d3.select("#annualYieldsChart").append("svg")
        .attr("width", width + margin.left + margin.right + 50)// 50 px for right side long legend bars
        .attr("height", canvasHeight)
        .attr({"margin-top": margin.top, "margin-left": 0})
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    /* Set background */
    svg.append("rect").attr("class", "annual-yield-grid-background").attr({
        "height": 300,
        "width": 805,
        "x": 1,
        "y": 0
    });

    /* Legend: Term controls */
    var colorLegend = d3.scale.ordinal().range(["#FF0000", "#0000CC", "#00FF00", "#00CCFF", "#CC00FF", "#CCCC00", "#eae200", "#ffca2c", "#f7951e", "#a1a1a1"]),
//        var colorLegend = d3.scale.ordinal().range(["RED", "BLACK", "GREEN"]),

        legendData = ["1"];//["1", "2", "3", "4", "5", "6", "All"],
        defaultPageLoad = true;

       var legend = svg.selectAll(".legend")
        .data(colorLegend.domain().slice()).enter().append("g").attr("class", "legend-grade")
        .attr("id", function (d, i) {
            return "grade-" + d;
        })
        .attr("transform", function (d, i) {
            var grades = legendData.length;
            return "translate(-" + (((grades - i) * 31) - 29 + 24) + ", -43)";
        }
    );
    legend.append("rect")
        .attr("x", width - 52).attr({"width": 26, "height": 26})
        .attr("class", function (d) {
            return "rect-grade-" + d;
        })
        .style({"fill": colorLegend, "cursor": "pointer"});
    legend.append("text").attr("x", width - 44)
        .attr("y", 14).attr("dy", ".25em")
        .attr("campaignId", (function (d, i) {
            return d;
        }))
        .text(function (d, i) {
            return d;
        })
        .style({"fill": "#fff", "cursor": "pointer"})
        .attr("font-weight", function (d, i) {
            if (d === "All") {
                return ("normal");
            } else {
                return ("bold");
            }
        })
        .attr("text-transform", function (d, i) {
            if (d === "All") {
                return ("none");
            } else {
                return ("uppercase");
            }
        });

    /* Static UI elements */
    /* Using for loop to draw multiple vertical lines */
    var drawVerticalGridLines = function (gridWidth) {
        for (var j = gridWidth; j <= width; j = j + gridWidth) {
            svg.append("svg:line")
                .attr("class", "vertical-grid-lines")
                .attr("x1", j - gridWidth)
                .attr("y1", 0)
                .attr("x2", j - gridWidth)
                .attr("y2", height)
                .style("stroke", function () {
                    return j % 3 === 0 ? "#e4e4e4" : "#f8f8f8";
                })
                .style("stroke-width", 1.5);
        }
        ;
    };


    /* Draw chart */

    var drawData = function (chartData) {
        console.log("SMNLOG:Drawing starts with data::" + JSON.stringify(chartData));
        var colorVintage = [];

        colorVintage = colorLegend;

        var colorVintageDomainVal = [];
        var jsonForClickedChart = [];
        var jsonForViewedChart = [];
        var xDomainData = [];
        var vintageMinTotalCounts = 100000000;
        var vintageMaxTotalCounts = 0;
        var resetVintageMin = 0;
        var date_sort_asc = function (a, b) {
            // This is a comparison function that will result in dates being sorted in
            // ASCENDING order. As you can see, JavaScript's native comparison operators
            // can be used to compare dates. This was news to me.
            /* if (date1 > date2) return 1;
             if (date1 < date2) return -1;
             return 0;*/
            return new Date(a.date) - new Date(b.date);
        };
        console.log("SMNLOG:chartData.values--------------:"+JSON.stringify(chartData[0].values));
            for (totalItemObjectKey in chartData[0].values) {
                var item = chartData[0].values[totalItemObjectKey];
                console.log("SMNLOG:item--------------:"+JSON.stringify(item));
                                if (xDomainData.indexOf(item) == -1) { // for unique array
                                    xDomainData.push(item);
                                }

                                // for min totalCounts
                                if (vintageMinTotalCounts >= item.totalCounts)
                                    vintageMinTotalCounts = item.totalCounts;

                                // for max totalCounts
                                if (vintageMaxTotalCounts <= item.totalCounts)
                                    vintageMaxTotalCounts = item.totalCounts;

            }

        jsonForClickedChart = lineChartData;
        console.log("lineChartData:" + JSON.stringify(lineChartData));


        colorVintage.domain = ["1"];
        console.log("SMNLOG:colorVintage.domain():" + JSON.stringify(colorVintage.domain));

        //console.log("x.domain(): PRE:" + JSON.stringify(xDomainData));

        xDomainData.sort(date_sort_asc);

        console.log("x.domain():After:" + JSON.stringify(xDomainData));

        x.domain(xDomainData.map(function (d, i) {
            console.log("SMNLOG:-----------_____>" + new Date(d.date));
            return d.date;
        }));

        svg.append("g")
            .attr("class", "xAxis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis).append("text");

        resetVintageMin = vintageMinTotalCounts > 0 ? 0 : -5;


        /* -5 is the default hard coded negative totalCounts */

        if (vintageMaxTotalCounts % 5 === 0) {
            vintageMaxTotalCounts = vintageMaxTotalCounts + 5;
        } else {
            vintageMaxTotalCounts = vintageMaxTotalCounts + 5 - (vintageMaxTotalCounts % 5);
        }
        y.domain([resetVintageMin, vintageMaxTotalCounts]);

        console.log("SMNLOG:vintageMinTotalCounts:" + vintageMinTotalCounts);
        console.log("SMNLOG:vintageMaxTotalCounts:" + vintageMaxTotalCounts);
        console.log("SMNLOG:resetVintageMin:" + resetVintageMin);

        svg.append("g")
            .attr("class", "y axis yAxis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)");

        /* Draw line for each vintage year - Data
         * Solid line for clicked data
         */

        var vintage = svg.selectAll(".vintage")
            .data(jsonForClickedChart).enter()
            .append("g")
            .attr("class", "vintage");

        vintage.append("path")
            .attr("class", "line")
            .attr("d", function (d) {
                return line(d.values);
            })
            .style("stroke", function (d) {
                console.log("SMNLOG:this.parentNode.__data__.id 11:" + d.id + " Color:" + colorVintage(d.id));
                return colorVintage(d.id);
            })
//                    .style("stroke", "rgb(120, 74, 28)")
            .style("fill-opacity", 0)
            .attr("id", function (d) {
                return d.id;
            })

        /* .on("mouseout", function() { tooltip.style("display", "none"); })
         .on("mouseover", function(d) {
         var xPosition = d3.mouse(this)[0],
         yPosition = d3.mouse(this)[1];
         tooltip.attr("transform", "translate(" + xPosition + "," + yPosition + ")")
         .style({"display": "block"})
         .attr("fill",function() { return colorVintage(d.id); });
         tooltip.select("text").text(function(){ return d.id; });
         });*/

        /* Start circle point drawing */
        vintage.append("g").selectAll("circle")
            .data(function (d) {
                return d.values
            })
            .enter()
            .append("circle")
            .attr('class', 'data-point')
            .attr("r", circleRadius)
            .style("stroke", function (d) {
                return colorVintage(this.parentNode.__data__.id)
            })
            .attr("cx", function (dd) {
                return x(dd.date)
            })
            .attr("cy", function (dd) {
                return y(dd.totalCounts)
            });


/*        *//* For dashed line for viewed data *//*

        var vintageViewed = svg.selectAll(".vintageViewed")
            .data(jsonForViewedChart).enter()
            .append("g")
            .attr("class", "vintageViewed");

        vintageViewed.append("path")
            .attr("class", "line")
            .attr("stroke-dashoffset", 0)
            .attr("stroke-dasharray", dashArraySpace)
            .attr("d", function (d) {
                return line(d.values);
            })
            .style("stroke", function (d) {
                return colorVintage(this.parentNode.__data__.id);
            })
            .style("fill-opacity", 0)
            .attr("id", function (d) {
                return d.id;
            })


        *//* Start circle point drawing *//*
        vintageViewed.append("g").selectAll("circle")
            .data(function (d) {
                return d.values
            })
            .enter()
            .append("circle")
            .attr('class', 'data-point')
            .attr("r", circleRadius)
            .style("stroke", function (d) {
                return colorVintage(this.parentNode.__data__.id)
            })
            .attr("cx", function (dd) {
                return x(dd.date)
            })
            .attr("cy", function (dd) {
                return y(dd.totalCounts)
            })
            .on("click", function (d) {
                getTrackDetails(this, d);
            });*/

        $('svg circle.data-point').tipsy({
            gravity: 'w',
            html: true,
            title: function () {
                var d = this.__data__;
                var pDate = d.date;
//                  return 'Date: ' + pDate.getDate() + " " + monthNames[pDate.getMonth()] + " " + pDate.getFullYear() + '<br>Value: ' + d.totalCounts;
                return 'Date: ' + d.date + '<br>Value: ' + d.totalCounts;
            }
        });


        /* Animation */
        /* Note: Firefox will crash after a number of animation loops, put a lazy check */
//            if (window.navigator.userAgent.toLowerCase().indexOf('firefox') < 0) {
        d3.select("#annualYieldsChart").selectAll("path.line").each(function (d, i) {
            var eachPath = d3.select(this),
                totalLength = eachPath.node().getTotalLength();
            eachPath.attr("stroke-dasharray", totalLength + " " + totalLength)
                .attr("stroke-dashoffset", totalLength)
                .transition()
                .duration(1800)
                .ease("linear")
                .each("end", function () {
                    console.log("SMNLOG:ENDING.........." + d3.select(this.parentNode).attr('class'));
                    if ('vintageViewed' == d3.select(this.parentNode).attr('class')) {
                        d3.select(this).attr("stroke-dasharray", dashArraySpace)
                    }
                })
                .attr("stroke-dashoffset", 0);
        });


//            }


        /* Grid horizontal lines */
        svg.insert("g", ".vintage")
            .attr("class", "y axis")
            .attr("transform", "translate(0,0)")
            .call(d3.svg.axis().scale(y)
                .orient("left")
                .tickSize(-805, 0, 0)
                .tickFormat("")
        );

        /* Month ticks for Grid vertical lines */
        svg.insert("g", ".vintage")
            .attr("class", "x axis")
            .attr("transform", "translate(-10,0)")
            .call(d3.svg.axis().scale(x).orient("bottom").tickSize(height, height, 0));

        d3.select("#annualYieldsChart").select(".x").selectAll(".tick line").style({
            "opacity": "0",
            "stroke-width": "0"
        });

        /* Tooltip, initial display is hidden */
        var tooltip = svg.append("g")
            .attr("class", "tooltip")
            .style("display", "none");

        tooltip.append("rect")
            .attr("width", 50)
            .attr("height", 20)
            .attr("rx", "5").attr("ry", "5")
            .style("opacity", 1);

        tooltip.append("text")
            .attr("x", 25).attr("dy", "1.2em")
            .style("text-anchor", "middle")
            .attr({"font-size": "12px", "fill": "#fff"});

    };

    /* Load chart data */
    var loadChartData = function (data) {
        console.log("SMNLOG:Data is loading.....");
        drawData(data);
    };

    var formatMonthTicks = function (tickDivider) {
        d3.select("#annualYieldsChart").select(".x").selectAll(".tick text").each(function (d, i) {
            i++;
            if (i % tickDivider !== 0) {
                d3.select(this).style("opacity", 0);
            }
        });
    }


    /* Default selection ALL terms, ALL grades */
    if (defaultPageLoad) {
        console.log(":: defaultPageLoad ::"+defaultPageLoad);
        /*  On page load, the default Data is "30" term for "All" grade
         * Term "36" : selected
         * Grade "All" : selected
         * Default vertical grid width: width/months = 23*/

        var defaultVerticalGridWidth = 23, defaultTickDivider = 3, defaultTerm = "36";

        drawVerticalGridLines(defaultVerticalGridWidth);
        loadChartData(lineChartData);
        defaultPageLoad = false;
    }

   }