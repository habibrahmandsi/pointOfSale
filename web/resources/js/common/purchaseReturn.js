$(document).ready(function () {

    /* AutoComplete */
    $("#purchaseForm").find("input").attr("autocomplete","off");

    $(document).on("keyup", '.discount input', function () {
    console.log("SMNLOG:discount changed");
        reCalculatePurchaseTotal();
    });

    $(document).on("keyup", '.qunatityInput', function () {
    console.log("SMNLOG:qunatityInput changed");
        var qty = +$(this).val();
        var pRate = +$(this).closest("td").prev("td").prev("td").text();
        $(this).closest("td").next("td").find('label').text(pRate*qty);
        $(this).closest("td").next("td").find('input.totalPrice').val(pRate*qty);
        reCalculatePurchaseTotal();
    });

    function indexingPurchase(){
        $(".purchaseLineItemsDiv table tbody tr").each(function () {
            var index = $(this).index();
            var purchase = $(this).index();
            console.log("-------Indexing start---------"+index);
            $(this).find('td').eq(0).find("input.productId").attr('name',"purchaseItemList["+index+"].product.id");
            $(this).find('td').eq(0).find("input.itemId").attr('name',"purchaseItemList["+index+"].id");
            $(this).find('td').eq(0).find("input.purchaseId").attr('name',"purchaseItemList["+index+"].purchase.id");
            $(this).find('td').eq(0).find("input.prevQuantity").attr('name',"purchaseItemList["+index+"].prevQuantity");
            $(this).find('td').eq(3).find("input.purchaseRate").attr('name',"purchaseItemList["+index+"].purchaseRate");
            $(this).find('td').eq(4).find("input.qunatityInput").attr('name',"purchaseItemList["+index+"].quantity");
            $(this).find('td').eq(5).find("input.totalPrice").attr('name',"purchaseItemList["+index+"].totalPrice");
        });

    }

    function reCalculatePurchaseTotal(){
        /* Calculate the grand total*/
        var total = 0;
        var discount = 0;
        //discount = +$(".discount input").val();
        console.log("SMNLOG:discount:"+discount);
        //total = calculateTotal(, 'td.lineTotal');
        $(".purchaseLineItemsDiv table tbody tr").each(function () {
            console.log("SMNLOG:row 2::"+$(this).find('td.lineTotal').find("label").text());
            total += (+$(this).find('td.lineTotal').find("label").text());
            console.log("SMNLOG:" + total);
        });

        $(".purchaseLineItemsDiv table tfoot tr td.subTotal").text(total);

        //$(".purchaseLineItemsDiv table tfoot tr td.grandTotal").find("label").text(total-discount);
        //$(".purchaseLineItemsDiv table tfoot tr td.grandTotal").find("input").val(total-discount);// for binding
        $(".purchaseLineItemsDiv table tfoot tr td.grandTotal").find("label").text(total);
        $(".purchaseLineItemsDiv table tfoot tr td.grandTotal").find("input").val(total);// for binding
    }
})

