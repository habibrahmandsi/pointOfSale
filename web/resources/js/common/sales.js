$(document).ready(function () {
    $("#salesForm").find("input").attr("autocomplete","off");

    /* AutoComplete */

/*

    $('.companyName').on('typeahead:selected', function (evt, itemObject) {
        console.log('i am in updater' + JSON.stringify(itemObject));
        companyIdFromTypeahead = itemObject.id
        console.log("SMNLOG:company id:" + companyIdFromTypeahead);
        $(".productName").focus();
    })

    */
/*Auto complete for company name*//*

    $.ajax({
        url: makeAjaxUrlForTypeAhead('./getCompanys.do', 0, "", -1),
        type: 'get',
        data: {},
        success: function (data) {
            var dataList = data.aaData;
            console.log('dataList:' + JSON.stringify(dataList));
            makeAutoComplete('.companyName', dataList, "name", 'name');


        }
    });
*/

    /* AutoComplete */

    $('.productName').on('typeahead:selected', function (evt, itemObject) {
        console.log('i am in updater' + JSON.stringify(itemObject));
        productObject = itemObject
        console.log("SMNLOG:product id:" + productObject.id);

        $(".salesRateInput").val(itemObject.sales_rate);
        $(".salesRateInput").val(itemObject.sale_rate);
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
            makeAutoComplete('.productName', dataList, "productName", "2");
            $(".typeahead-filter-button").remove();
            $(".productName").focus();
        }
    });

    $(document).on("keyup", '.discount input', function () {
    console.log("SMNLOG:discount changed");
        reCalculateSalesTotal();
    });

    $(document).on("keyup", '.qunatityInput', function () {
    console.log("SMNLOG:qunatityInput changed");
        var qty = +$(this).val();
        var pRate = +$(this).closest("td").prev("td").text();
        $(this).closest("td").next("td").find('label').text(pRate*qty);
        $(this).closest("td").next("td").find('input.totalPrice').val(pRate*qty);
        reCalculateSalesTotal();
    });

    $(".qty").keypress(function (event) {
    //console.log("SMNLOG:writting in qty");
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
          $(".addLineItem").click();
        }
    });

    $(document).on('click',".salesSave",function (event) {
    console.log("SMNLOG:Save clicked");
    $("#salesForm").submit();
    });

    function indexingSales(){
        $(".salesLineItemsDiv table tbody tr").each(function () {
            var index = $(this).index();
            var sales = $(this).index();
            console.log("-------Indexing start---------"+index);
            $(this).find('td').eq(0).find("input.productId").attr('name',"salesItemList["+index+"].product.id");
            $(this).find('td').eq(0).find("input.itemId").attr('name',"salesItemList["+index+"].id");
            $(this).find('td').eq(0).find("input.salesId").attr('name',"salesItemList["+index+"].sales.id");
            $(this).find('td').eq(0).find("input.prevQuantity").attr('name',"salesItemList["+index+"].prevQuantity");
            $(this).find('td').eq(3).find("input.salesRate").attr('name',"salesItemList["+index+"].salesRate");
            $(this).find('td').eq(4).find("input.qunatityInput").attr('name',"salesItemList["+index+"].quantity");
            $(this).find('td').eq(5).find("input.totalPrice").attr('name',"salesItemList["+index+"].totalPrice");
        });

    }

    function reCalculateSalesTotal(){
        /* Calculate the grand total*/
        var total = 0;
        var discount = 0;
        discount = +$(".discount input").val();
        console.log("SMNLOG:discount:"+discount);
        //total = calculateTotal(, 'td.lineTotal');
        $(".salesLineItemsDiv table tbody tr").each(function () {
            console.log("SMNLOG:row 1::"+$(this).find('td.lineTotal').find("label").html());
            console.log("SMNLOG:row 2::"+$(this).find('td.lineTotal').find("label").text());
            total += (+$(this).find('td.lineTotal').find("label").text());
            console.log("SMNLOG:" + total);
        });

        $(".salesLineItemsDiv table tfoot tr td.subTotal").text(total);

        if(discount > total){
            $(".salesLineItemsDiv table tfoot tr td.discount").next("td").find("img").show();
        }else{
            $(".salesLineItemsDiv table tfoot tr td.discount").next("td").find("img").hide();
        }
        $(".salesLineItemsDiv table tfoot tr td.grandTotal").find("label").text(total-discount);
        $(".salesLineItemsDiv table tfoot tr td.grandTotal").find("input").val(total-discount);// for binding
    }

    $(".addLineItem").click(function (e) {
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
            + '<tr><td></td><td><td></td><td></td><td class="italicFont">Discount: </td><td class="italicFont discount"><input type="text" name="discount" placeholder="0.0" class="italicFont" style="max-width: 50px;padding-right: 5px;"></td><td style="text-align: right"><img class="hidden" alt="Invalid" title="Invalid" style="width: 20px;"  src="' + contextPath + '/resources/images/crossIcon.jpeg"></td></tr>'
            + '<tr><td></td><td><td></td><td></td><td class="italicFont">Total: </td><td class="italicFont grandTotal"><label></label><input type="text" name="totalAmount" class="productId hidden"></td><td></td></tr>'
            + '</tfoot>'
            + '</table>'
            + '</br>'
            + '<div style="text-align: right;"><button class="btn btn-danger" type="reset">Cancel</button>&nbsp;'
            + '<button class="btn btn-success salesSave" type="button">Sales</button></div>';


        if ($(".salesLineItemsDiv").find("table").length > 0) {
            console.log("SMNLOG:Table EXIST");
        } else {
            console.log("SMNLOG:Table NOT EXIST");
            $($(".salesLineItemsDiv")).append(table);
        }

        var serialNo = +$(".salesLineItemsDiv table tbody tr").length + 1;
        var salesRate = +$(".salesRateInput").val();
        var quantity = +$(".qty").val();
        var lineTotal = quantity * salesRate;
        console.log("SMNLOG:quantity:"+quantity+" salesRate:"+salesRate+" lineTotal:"+lineTotal);
        var row = '<tr id="'+productObject.productId+'">'
            + '<td><label>' + serialNo + '</label>'
                     + '<input type="text" name="salesItemList[].product.id" class="productId hidden" value="'+productObject.productId+'">'
                     + '<input type="text" name="salesItemList[].prevQuantity" class="prevQuantity hidden" value="0">'
                     + '</td>'
            + '<td>' + productObject.productName + '</td>'
            + '<td>' + productObject.companyName + '</td>'
            + '<td>' + salesRate + '<input type="text" name="salesItemList[].salesRate" class="salesRate hidden" value="'+salesRate+'"></td>'
            + '<td><input type="text" name="salesItemList[].quantity" class="qunatityInput" value="'+quantity +'" style="max-width: 80px;padding-right: 5px;padding-left:5px;"></td>'
            + '<td class="lineTotal italicFont"><label>' + lineTotal + '</label><input type="text" name="salesItemList[].totalPrice" class="totalPrice hidden" value="'+lineTotal+'"></td>'
            + '<td style="text-align: right"><img alt="Saved" title="Saved" class="iconInsideTable"  src="' + contextPath + '/resources/images/crossIcon.jpeg"></td>'
            + '</tr>';

        console.log("SMNLOG:productObject.id"+productObject.productId);

        //$(".salesLineItemsDiv").find("table").find("tbody").find("tr").each(function () {
            if($("#"+productObject.productId).length > 0){ // same product row is already exist
                console.log("SMNLOG:FOUND......");
                var newQty = +$("#"+productObject.productId).find("input.qunatityInput").val();
                $("#"+productObject.productId).find("input.qunatityInput").val(quantity+newQty).keyup();
            }else{
                $(".salesLineItemsDiv").find("table").find("tbody").append(row);
            }
        //});



        reCalculateSalesTotal();

        $(".productName").val("");
        $(".productName").focus();

        /* for indexing */
        indexingSales();
        e.preventDefault();
    });

    function deleteRowOrFullTable(that){
        if ($(".salesLineItemsDiv").find("table").find("tbody").find("tr").length <= 1) {
            $(".salesLineItemsDiv").find("table").remove();
            $(".salesLineItemsDiv").find("div").remove();
        }else{
            $(that).closest("tr").remove();
            reCalculateSalesTotal();
        }
    }

    $(document).on("click", '.iconInsideTable', function () {
        var itemId = $(this).closest("tr").find("input.itemId").val();
        var that = $(this);
        if( itemId > 0){
            console.log("SMNLOG:To Delete from db:itemId:"+itemId);
            $.ajax({
                url: './deleteAnyObject.do?'
                +'tableName=sales_item&colName=id&colValue='+itemId,
                type: 'get',
                data: {},
                success: function (result) {
                    console.log('result:' + result);
                    if("true".indexOf(result) > -1){
                        deleteRowOrFullTable(that);
                        indexingSales();
                    }else{
                        console.log("SMNLOG:Failed to delete:");
                    }
                }
            });
        }else{
            deleteRowOrFullTable(that);
            indexingSales();
        }



    });

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

