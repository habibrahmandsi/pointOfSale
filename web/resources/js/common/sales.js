$(document).ready(function () {

    /*if (typeof salesId != "undefined" && salesId > 0) {
        $("#salesForm").find("div.leftPanelDiv").remove();
        $("#salesForm").find("div.salesLineItemsDiv").removeClass("col-lg-8").addClass("col-lg-12");
        $("#salesForm").find("input").attr("disabled", true);
        $("#salesForm").find("img").remove();
        $("#salesForm").find("button").remove();
        $(".unpostedSaleLnk").remove();
    } else {*/
        $("#salesForm").find("input").attr("autocomplete", "off");
        $(".productName").focus();
        makeTabularAutoComplete(".productName", './getProductsForAutoComplete.do', function (data) {
            console.log("SMNLOG:this is call back:" + JSON.stringify(data));
            productObject = data
            console.log("SMNLOG:product id:" + productObject.productId);
            $(".salesRateInput").val(productObject.saleRate);
        });

        $(document).on("keyup", '.discount input', function () {
            console.log("SMNLOG:discount changed");
            reCalculateSalesTotal();
            var sId = +$("#saleId").val();
            var discount = +$(this).val();
            var grandTotal = +$(".salesLineItemsDiv table tfoot tr td.grandTotal").find("label").text();
            var salesReturn = 0;
            if("true".indexOf($("#salesReturn").val()) > -1){
                salesReturn = 1;
            }

            $.ajax({
                url: './updateSalesDiscounts.do?'
                + 'sId=' + sId + '&totalPrice=' + grandTotal + '&discount=' + discount + '&salesReturn=' + salesReturn,
                type: 'get',
                data: {},
                success: function (result) {
                    console.log('result:' + result);
                    if ("true".indexOf(result) > -1) {
                        deleteRowOrFullTable(that);
                        indexingSales();
                    } else {
                        console.log("SMNLOG:Failed update Sales Discounts:");
                    }
                }
            });


        });

        $(document).on("keyup", '.qunatityInput', function () {
            console.log("SMNLOG: ***************** qunatityInput changed ***************");
            var quantity = +$(this).val();
            var salesRate = +$(this).closest("td").prev("td").text();
            var lineTotal = quantity * salesRate;
            $(this).closest("td").next("td").find('label').text(lineTotal);
            $(this).closest("td").next("td").find('input.totalPrice').val(lineTotal);
            reCalculateSalesTotal();

            console.log("SMNLOG:quantity:" + quantity + " salesRate:" + salesRate + " lineTotal:" + lineTotal);

            var salesReturn = 0;
            if("true".indexOf($("#salesReturn").val()) > -1){
                salesReturn = 1;
            }
            var serialNo = +$(".salesLineItemsDiv table tbody tr").length + 1;
            var discount = +$(".salesLineItemsDiv table tfoot tr td.discount").find("input").val();
            var subTotal = findTotal();

            if (isNaN(parseFloat(subTotal))) {
                subTotal = 0;
            }
            if (subTotal == 0)
                subTotal = lineTotal;
            var grandTotal = subTotal - discount;

            console.log("SMNLOG:---------subTotal:" + subTotal + " grandTotal:-------" + grandTotal + " discount:----->" + discount);
            var sId = +$("#saleId").val();
            var pId = +$(this).closest('tr').find("input.productId").val();
            var pRate = +$(this).closest('tr').find("input.purchaseRate").val();
            console.log("SMNLOG:---------sId:" + sId + " pId:-------" + pId + " pRate:----->" + pRate);

            var sItemId = "";
            if ($(".salesLineItemsDiv").find("#" + pId).length > 0) {
                sItemId = +$(this).closest('tr').find("input.itemId").val();
            }
            if (isNaN(parseFloat(sItemId))) {
                sItemId = 0;
            }
            if (isNaN(parseFloat(discount))) {
                discount = 0;
            }
            var onUpdateTxt = 0;

            var grandTotal = +$(".salesLineItemsDiv table tfoot tr td.grandTotal").find("label").text();
            var discount = +$(".salesLineItemsDiv table tfoot tr td.discount").find("input").val();
            if (grandTotal == 0)
                grandTotal = lineTotal;

            console.log("SMNLOG:------>grandTotal:" + grandTotal + " discount:" + discount);

            var url = './addSalesItems.do?sId=' + sId + '&pId=' + pId + '&qty=' + quantity + '&pRate=' + pRate
                + '&sRate=' + salesRate + '&totalPrice=' + lineTotal + '&sItemId=' + sItemId + "&onUpdateTxt="
                + onUpdateTxt + "&grandTotal=" + grandTotal + "&discount=" + discount+"&salesReturn="+salesReturn;


            ajaxCallToSaveLineItems(url, sId, function (data) {
                if (!"false".indexOf(data) > -1) {
                    console.log("SMNLOG:Update success");

                }
            });
        });

        $(document).on('click', ".salesSave", function (event) {
            console.log("SMNLOG:Save clicked");
            $("#salesForm").submit();
        });

        function indexingSales() {
            $(".salesLineItemsDiv table tbody tr").each(function () {
                var index = $(this).index();
                var sales = $(this).index();
                console.log("-------Indexing start---------" + index);
                $(this).find('td').eq(0).find("input.productId").attr('name', "salesItemList[" + index + "].product.id");
                $(this).find('td').eq(0).find("input.itemId").attr('name', "salesItemList[" + index + "].id");
                $(this).find('td').eq(0).find("input.salesId").attr('name', "salesItemList[" + index + "].sales.id");
                $(this).find('td').eq(0).find("input.prevQuantity").attr('name', "salesItemList[" + index + "].prevQuantity");
                $(this).find('td').eq(0).find("input.purchaseRate").attr('name', "salesItemList[" + index + "].purchaseRate");
                $(this).find('td').eq(3).find("input.salesRate").attr('name', "salesItemList[" + index + "].salesRate");
                $(this).find('td').eq(4).find("input.qunatityInput").attr('name', "salesItemList[" + index + "].quantity");
                $(this).find('td').eq(5).find("input.totalPrice").attr('name', "salesItemList[" + index + "].totalPrice");
            });

        }

        function findTotal() {
            var total = 0;
            $(".salesLineItemsDiv table tbody tr").each(function () {
                console.log("SMNLOG:row 1::" + $(this).find('td.lineTotal').find("label").html());
                console.log("SMNLOG:row 2::" + $(this).find('td.lineTotal').find("label").text());
                total += (+$(this).find('td.lineTotal').find("label").text());
                console.log(":: findTotal:: " + total);
            });
            return total;
        }

        function reCalculateSalesTotal() {
            /* Calculate the grand total*/
            var total = 0;
            var discount = 0;
            discount = +$(".discount input").val();
            console.log("SMNLOG:discount:" + discount);
            //total = calculateTotal(, 'td.lineTotal');
            /* $(".salesLineItemsDiv table tbody tr").each(function () {
             console.log("SMNLOG:row 1::" + $(this).find('td.lineTotal').find("label").html());
             console.log("SMNLOG:row 2::" + $(this).find('td.lineTotal').find("label").text());
             total += (+$(this).find('td.lineTotal').find("label").text());
             console.log("SMNLOG:" + total);
             });*/
            total = findTotal();

            $(".salesLineItemsDiv table tfoot tr td.subTotal").text(total);

            if (discount > total) {
                $(".salesLineItemsDiv table tfoot tr td.discount").next("td").find("img").show();
            } else {
                $(".salesLineItemsDiv table tfoot tr td.discount").next("td").find("img").hide();
            }
            $(".salesLineItemsDiv table tfoot tr td.grandTotal").find("label").text(total - discount);
            $(".salesLineItemsDiv table tfoot tr td.grandTotal").find("input").val(total - discount);// for binding
        }

        $(".addLineItem").click(function (e) {

            if (typeof productObject.totalQuantity != 'undefined' && productObject.totalQuantity <= 0) {
                alert("Quantity is empty.Please purchase first !");
                event.preventDefault();
                $(".productName").val("");
                $(".productName").focus();
            } else {
                var salesReturn = 0;
                if("true".indexOf($("#salesReturn").val()) > -1){
                    salesReturn = 1;
                }

                var table = '<table class="table table-striped">'
                    + '<thead>'
                    + '<th  style="width:5%">Sl #</th>'
                    + '<th>Product name</th>'
                    + '<th>Company Name</th>'
                    + '<th>Sales rate</th>'
                    + '<th>Qty</th>'
                    + '<th>Item total</th>'
                    + '<th></th>'
                    + '</thead>'
                    + '<tbody>'
                    + '</body>'
                    + '<tfoot>'
                    + '<tr><td></td><td></td><td></td><td></td><td class="italicFont">Sub Total: </td><td class="italicFont subTotal"></td><td></td></tr>'
                    + '<tr><td></td><td><td></td><td></td><td class="italicFont">Discount: </td><td class="italicFont discount"><input type="text" name="discount" placeholder="0.0" class="italicFont" style="max-width: 50px;padding-right: 5px;" autocomplete="off"></td><td style="text-align: right"><img class="hidden" alt="Invalid" title="Invalid" style="width: 20px;"  src="' + contextPath + '/resources/images/crossIcon.jpeg"></td></tr>'
                    + '<tr><td></td><td><td></td><td></td><td class="italicFont">Total: </td><td class="italicFont grandTotal"><label></label><input type="text" name="totalAmount" class="productId hidden"></td><td></td></tr>'
                    + '</tfoot>'
                    + '</table>'
                    + '</br>'
                    + '<div style="text-align: right;"><button class="btn btn-danger" type="reset">Cancel</button>&nbsp;';

                if(salesReturn == 0)
                    table +=  '<button class="btn btn-success salesSave" type="button">Sales</button></div>';
                else
                    table += '<button class="btn btn-success salesSave" type="button">Sales Return</button></div>';


                if ($(".salesLineItemsDiv").find("table").length > 0) {
                    console.log("SMNLOG:Table EXIST");
                } else {
                    console.log("SMNLOG:Table NOT EXIST");
                    $($(".salesLineItemsDiv")).append(table);
                }




                var salesRate = +$(".salesRateInput").val();
                var quantity = +$(".qty").val();
                var lineTotal = quantity * salesRate;
                console.log("SMNLOG:quantity:" + quantity + " salesRate:" + salesRate + " lineTotal:" + lineTotal);

                var serialNo = +$(".salesLineItemsDiv table tbody tr").length + 1;
                var discount = +$(".salesLineItemsDiv table tfoot tr td.discount").find("input").val();
                var subTotal = findTotal();

                if (isNaN(parseFloat(subTotal))) {
                    subTotal = 0;
                }

                if (subTotal > 0) {
                    subTotal = subTotal + lineTotal;
                    grandTotal = subTotal - discount;
                } else
                    subTotal = lineTotal;

                var grandTotal = subTotal - discount;

                console.log("SMNLOG:---------subTotal:" + subTotal + " grandTotal:-------" + grandTotal + " discount:----->" + discount);
                var sId = +$("#saleId").val();
                var pId = +productObject.productId;
                var pRate = typeof productObject.purchaseRate != "undefined" ? productObject.purchaseRate : 0;

                var sItemId = "";
                if ($(".salesLineItemsDiv").find("#" + pId).length > 0) {
                    sItemId = +$(".salesLineItemsDiv").find("#" + pId).find("input.itemId").val();
                }
                if (isNaN(parseFloat(sItemId))) {
                    sItemId = 0;
                }
                if (isNaN(parseFloat(discount))) {
                    discount = 0;
                }
                var onUpdateTxt = 0;

                if (sItemId > 0) {
                    console.log("SMNLOG:------------- ROW EXIST --------------");
                    onUpdateTxt = 1;
                    var newQty = +$(".salesLineItemsDiv").find("#" + pId).find("input.qunatityInput").val();
                    $(".salesLineItemsDiv").find("#" + pId).find("input.qunatityInput").val(quantity + newQty).keyup();
                } else {
                    console.log("SMNLOG:------------- ROW NOT EXIST SO AJAX CALLING--------------");
                    var url = './addSalesItems.do?sId=' + sId + '&pId=' + pId + '&qty=' + quantity + '&pRate=' + pRate
                        + '&sRate=' + salesRate + '&totalPrice=' + lineTotal + '&sItemId=' + sItemId + "&onUpdateTxt="
                        + onUpdateTxt + "&grandTotal=" + grandTotal + "&discount=" + discount+"&salesReturn="+salesReturn;

                    ajaxCallToSaveLineItems(url, sId, function (data, sId) {
                        if (!"false".indexOf(data) > -1) {
                            console.log("SMNLOG:Save success full");

                            var row = '<tr id="' + pId + '">'
                                + '<td><label>' + serialNo + '</label>'
                                + '<input type="text" name="salesItemList[].product.id" class="productId hidden" value="' + pId + '">'
                                + '<input type="text" name="salesItemList[].id" class="itemId hidden" value="' + data + '">'
                                + '<input type="text" name="salesItemList[].sales.id" class="salesId hidden" value="' + sId + '">'
                                + '<input type="text" name="salesItemList[].prevQuantity" class="prevQuantity hidden" value="0">'
                                + '<input type="text" name="salesItemList[].purchaseRate" class="purchaseRate hidden" value="' + pRate + '">'
                                + '</td>'
                                + '<td>' + productObject.productName + '</td>'
                                + '<td>' + productObject.companyName + '</td>'
                                + '<td>' + salesRate + '<input type="text" name="salesItemList[].salesRate" class="salesRate hidden" value="' + salesRate + '"></td>'
                                + '<td><input type="text" name="salesItemList[].quantity" class="qunatityInput" value="' + quantity + '" style="max-width: 80px;padding-right: 5px;padding-left:5px;"></td>'
                                + '<td class="lineTotal italicFont"><label>' + lineTotal + '</label><input type="text" name="salesItemList[].totalPrice" class="totalPrice hidden" value="' + lineTotal + '"></td>'
                                + '<td style="text-align: right"><img alt="Saved" title="Saved" class="iconInsideTable"  src="' + contextPath + '/resources/images/crossIcon.jpeg"></td>'
                                + '</tr>';

                            $(".salesLineItemsDiv").find("table").find("tbody").append(row);
                            reCalculateSalesTotal();
                            $(".productName").val("");
                            $(".qty").val("");
                            $(".salesRateInput").val("");
                            $(".productName").focus();
                            /* for indexing */
                            indexingSales();
                        }
                    })
                }
            }
            e.preventDefault();
        });

        $(".qty").keypress(function (event) {
            //console.log("SMNLOG:writting in qty");
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                $(".addLineItem").click();
            }
        });

        function deleteRowOrFullTable(that) {
            if ($(".salesLineItemsDiv").find("table").find("tbody").find("tr").length <= 1) {
                $(".salesLineItemsDiv").find("table").remove();
                $(".salesLineItemsDiv").find("div").remove();
            } else {
                $(that).closest("tr").remove();
                reCalculateSalesTotal();
            }
        }

        $(document).on("click", '.iconInsideTable', function () {
            var itemId = +$(this).closest("tr").find("input.itemId").val();
            var totalPrice = $(this).closest("tr").find("input.totalPrice").val();
            var sId = $("#saleId").val();
            var salesReturn = $("#salesReturn").val();
            var that = $(this);
            if (itemId > 0) {
                console.log("SMNLOG:To Delete from db:itemId:" + itemId);
                $.ajax({
                    //url: './deleteAnyObject.do?'
                    url: './deleteSalesItem.do?'
                    + 'sId=' + sId + '&sItemId=' + itemId + '&totalPrice=' + totalPrice + '&salesReturn=' + salesReturn,
                    type: 'get',
                    data: {},
                    success: function (result) {
                        console.log('result:' + result);
                        if ("true".indexOf(result) > -1) {
                            deleteRowOrFullTable(that);
                            indexingSales();
                        } else {
                            console.log("SMNLOG:Failed to delete:");
                        }
                    }
                });
            } else {
                deleteRowOrFullTable(that);
                indexingSales();
            }


        });

    //}


    /*    var previousCompanyValue = "";
     $(".companyName").keyup(function(){
     var cName = $(this).val();

     console.log("SMNLOG:changed detected:"+cName+" previousCompanyValue:"+previousCompanyValue);
     if(cName.length > previousCompanyValue.length){
     console.log("SMNLOG:Typing...");
     if(cName.length ==1){
     console.log("SMNLOG:Calling service...");
     $.ajax({
     url: makeAjaxUrlForTypeAhead('./getCompanys.do',0,cName),
     type: 'get',
     data: {},
     success: function (data) {
     var dataList = data.aaData;
     console.log('dataList:'+JSON.stringify(dataList));
     makeAutoComplete('.companyName',dataList,"name",'name');
     $(".companyName").focus();

     }
     });
     }
     }else{
     console.log("SMNLOG:Deleting...");
     }
     previousCompanyValue = cName;
     });*/

    function ajaxCallToSaveLineItems(url, sId, callBack) {
        console.log("SMNLOG:url:" + url);
        $.ajax({
            url: url,
            type: 'get',
            data: {},
            success: function (data) {
                if (typeof callBack === 'function') {
                    callBack(data, sId);
                }

            }
        });

    }
})

