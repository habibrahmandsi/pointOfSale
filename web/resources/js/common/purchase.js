$(document).ready(function () {

    if(typeof purchaseId != "undefined" && purchaseId > 0){
        $("#purchaseForm").find("div.leftPanelDiv").remove();
        $("#purchaseForm").find("div.purchaseLineItemsDiv").removeClass("col-lg-9").addClass("col-lg-12");
        $("#purchaseForm").find("input").attr("disabled",true);
        $("#purchaseForm").find("img").remove();
        $("#purchaseForm").find("button").remove();
    }else {
        $("#purchaseForm").find("input").attr("autocomplete", "off");

        /* AutoComplete */
        $('.productName').on('typeahead:selected', function (evt, itemObject) {
            console.log('i am in updater' + JSON.stringify(itemObject));
            productObject = itemObject
            console.log("SMNLOG:product id:" + productObject.id);

            $(".purchaseRateInput").val(itemObject.purchase_rate);
            $(".saleRateInput").val(itemObject.sale_rate);
            $(".qty").focus();
        })

        /*Auto complete for company name*/
        $.ajax({
            url: makeAjaxUrlForTypeAhead('./getProducts.do', 0, "", -1),
            type: 'get',
            data: {},
            success: function (data) {
                var dataList = data.aaData;
                console.log('dataList:' + JSON.stringify(dataList));
                makeAutoComplete('.productName', dataList, "productName", "1");
                $(".productName").focus();
            }
        });

        $(document).on("keyup", '.discount input', function () {
            console.log("SMNLOG:discount changed");
            reCalculatePurchaseTotal();
        });

        $(document).on("keyup", '.qunatityInput', function () {
            console.log("SMNLOG:qunatityInput changed");
            var qty = +$(this).val();
            var pRate = +$(this).closest("td").prev("td").text();
            $(this).closest("td").next("td").find('label').text(getRoundNDigits(pRate * qty,globalRoundDigits));
            $(this).closest("td").next("td").find('input.totalPrice').val(getRoundNDigits(pRate * qty,globalRoundDigits));
            reCalculatePurchaseTotal();
        });


        $(document).on("keyup", '.vatPercentage', function () {
            console.log("SMNLOG:vatPercentage changed");
            var vatPercentage = +$(this).val();
            var subTotal = +$(".subTotal").html();
            var totalVat = (vatPercentage/100)*subTotal;
            console.log("SMNLOG:totalVat"+totalVat);

            $(this).closest("td").next("td").find("input.vatToPay").val(getRoundNDigits(totalVat,globalRoundDigits));
            reCalculatePurchaseTotal();
        });

        $(".qty").keypress(function (event) {
            //console.log("SMNLOG:writting in qty");
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                $(".addLineItem").click();
            }
        });

        $(document).on('click', ".purchaseSave", function (event) {
            console.log("SMNLOG:Save clicked");
            $("#purchaseForm").submit();
        });

        function indexingPurchase() {
            $(".purchaseLineItemsDiv table tbody tr").each(function () {
                var index = $(this).index();
                var purchase = $(this).index();
                console.log("-------Indexing start---------" + index);
                $(this).find('td').eq(0).find("input.productId").attr('name', "purchaseItemList[" + index + "].product.id");
                $(this).find('td').eq(0).find("input.itemId").attr('name', "purchaseItemList[" + index + "].id");
                $(this).find('td').eq(0).find("input.purchaseId").attr('name', "purchaseItemList[" + index + "].purchase.id");
                $(this).find('td').eq(0).find("input.prevQuantity").attr('name', "purchaseItemList[" + index + "].prevQuantity");
                $(this).find('td').eq(3).find("input.purchaseRate").attr('name', "purchaseItemList[" + index + "].purchaseRate");
                $(this).find('td').eq(4).find("input.qunatityInput").attr('name', "purchaseItemList[" + index + "].quantity");
                $(this).find('td').eq(5).find("input.totalPrice").attr('name', "purchaseItemList[" + index + "].totalPrice");
            });

        }

        function reCalculatePurchaseTotal() {
            /* Calculate the grand total*/
            var total = 0;
            var discount = 0;
            discount = +$(".discount input").val();
            var vat = +$(".vatToPay").val();
            console.log("SMNLOG:discount:" + discount);
            //total = calculateTotal(, 'td.lineTotal');
            $(".purchaseLineItemsDiv table tbody tr").each(function () {
                console.log("SMNLOG:row 1::" + $(this).find('td.lineTotal').find("label").html());
                console.log("SMNLOG:row 2::" + $(this).find('td.lineTotal').find("label").text());
                total += (+$(this).find('td.lineTotal').find("label").text());
                console.log("SMNLOG:" + total);
            });

            $(".purchaseLineItemsDiv table tfoot tr td.subTotal").text(total);

            if (discount > total) {
                $(".purchaseLineItemsDiv table tfoot tr td.discount").next("td").find("img").show();
            } else {
                $(".purchaseLineItemsDiv table tfoot tr td.discount").next("td").find("img").hide();
            }
            total = getRoundNDigits(total - discount+ vat, globalRoundDigits);
            $(".purchaseLineItemsDiv table tfoot tr td.grandTotal").find("label").text(total);
            $(".purchaseLineItemsDiv table tfoot tr td.grandTotal").find("input").val(total);// for binding
        }

        $(".addLineItem").click(function (e) {
            var table = '<table class="table table-striped">'
                + '<thead>'
                + '<th  style="width:5%">Sl #</th>'
                + '<th>Product name</th>'
                + '<th>Company Name</th>'
                + '<th>Purchase rate</th>'
                + '<th>Qty</th>'
                + '<th>Item total</th>'
                + '<th></th>'
                + '</thead>'
                + '<tbody>'
                + '</body>'
                + '<tfoot>'
                + '<tr><td></td><td></td><td></td><td></td><td class="italicFont">Sub Total: </td><td class="italicFont subTotal"></td><td></td></tr>'
                + '<tr><td></td><td><td></td><td class="italicFont">Vat: </td><td class="italicFont vat"><input type="text" placeholder="0.0" class="italicFont vatPercentage" style="max-width: 50px;padding-right: 5px;">%</td><td class="italicFont vat"><input name="vat" style="max-width: 50px;padding-right: 5px;" type="text" class="italicFont vatToPay"></td><td style="text-align: right"><img class="hidden" alt="Invalid" title="Invalid" style="width: 20px;"  src="' + contextPath + '/resources/images/crossIcon.jpeg"></td></tr>'
                + '<tr><td></td><td><td></td><td></td><td class="italicFont">Discount: </td><td class="italicFont discount"><input type="text" name="discount" placeholder="0.0" class="italicFont" style="max-width: 50px;padding-right: 5px;"></td><td style="text-align: right"><img class="hidden" alt="Invalid" title="Invalid" style="width: 20px;"  src="' + contextPath + '/resources/images/crossIcon.jpeg"></td></tr>'
                + '<tr><td></td><td><td></td><td></td><td class="italicFont">Total: </td><td class="italicFont grandTotal"><label></label><input type="text" name="totalAmount" class="productId hidden"></td><td></td></tr>'
                + '</tfoot>'
                + '</table>'
                + '</br>'
                + '<div style="text-align: right;"><button class="btn btn-danger" type="reset">Cancel</button>&nbsp;'
                + '<button class="btn btn-success purchaseSave" type="button">Purchase</button></div>';


            if ($(".purchaseLineItemsDiv").find("table").length > 0) {
                console.log("SMNLOG:Table EXIST");
            } else {
                console.log("SMNLOG:Table NOT EXIST");
                $($(".purchaseLineItemsDiv")).append(table);
            }

            var serialNo = +$(".purchaseLineItemsDiv table tbody tr").length + 1;
            var purchaseRate = +$(".purchaseRateInput").val();
            var quantity = +$(".qty").val();
            var lineTotal = quantity * purchaseRate;
            console.log("SMNLOG:quantity:" + quantity + " purchaseRate:" + purchaseRate + " lineTotal:" + lineTotal);
            var row = '<tr id="' + productObject.productId + '">'
                + '<td><label>' + serialNo + '</label>'
                + '<input type="text" name="purchaseItemList[].product.id" class="productId hidden" value="' + productObject.productId + '">'
                + '<input type="text" name="purchaseItemList[].prevQuantity" class="prevQuantity hidden" value="0">'
                + '</td>'
                + '<td>' + productObject.productName + '</td>'
                + '<td>' + productObject.companyName + '</td>'
                + '<td>' + purchaseRate + '<input type="text" name="purchaseItemList[].purchaseRate" class="purchaseRate hidden" value="' + purchaseRate + '"></td>'
                + '<td><input type="text" name="purchaseItemList[].quantity" class="qunatityInput" value="' + quantity + '" style="max-width: 80px;padding-right: 5px;padding-left:5px;"></td>'
                + '<td class="lineTotal italicFont"><label>' + lineTotal + '</label><input type="text" name="purchaseItemList[].totalPrice" class="totalPrice hidden" value="' + lineTotal + '"></td>'
                + '<td style="text-align: right"><img alt="Saved" title="Saved" class="iconInsideTable"  src="' + contextPath + '/resources/images/crossIcon.jpeg"></td>'
                + '</tr>';

            console.log("SMNLOG:productObject.id" + productObject.productId);

            //$(".purchaseLineItemsDiv").find("table").find("tbody").find("tr").each(function () {
            if ($("#" + productObject.productId).length > 0) { // same product row is already exist
                console.log("SMNLOG:FOUND......");
                var newQty = +$("#" + productObject.productId).find("input.qunatityInput").val();
                $("#" + productObject.productId).find("input.qunatityInput").val(quantity + newQty).keyup();
            } else {
                $(".purchaseLineItemsDiv").find("table").find("tbody").append(row);
            }
            //});


            reCalculatePurchaseTotal();

            $(".productName").val("");
            $(".productName").focus();

            /* for indexing */
            indexingPurchase();
            e.preventDefault();
        });

        function deleteRowOrFullTable(that) {
            if ($(".purchaseLineItemsDiv").find("table").find("tbody").find("tr").length <= 1) {
                $(".purchaseLineItemsDiv").find("table").remove();
                $(".purchaseLineItemsDiv").find("div").remove();
            } else {
                $(that).closest("tr").remove();
                reCalculatePurchaseTotal();
            }
        }

        $(document).on("click", '.iconInsideTable', function () {
            var itemId = $(this).closest("tr").find("input.itemId").val();
            var that = $(this);
            if (itemId > 0) {
                console.log("SMNLOG:To Delete from db:itemId:" + itemId);
                $.ajax({
                    url: './deleteAnyObject.do?'
                    + 'tableName=purchase_item&colName=id&colValue=' + itemId,
                    type: 'get',
                    data: {},
                    success: function (result) {
                        console.log('result:' + result);
                        if ("true".indexOf(result) > -1) {
                            deleteRowOrFullTable(that);
                            indexingPurchase();
                        } else {
                            console.log("SMNLOG:Failed to delete:");
                        }
                    }
                });
            } else {
                deleteRowOrFullTable(that);
                indexingPurchase();
            }


        });
    }

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

})

