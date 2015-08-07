$(document).ready(function () {

    /* AutoComplete */
    $("#salesForm").find("input").attr("autocomplete","off");

    $(document).on("keyup", '.discount input', function () {
    console.log("SMNLOG:discount changed");
        reCalculateSalesTotal();
    });

    $(document).on("keyup", '.qunatityInput', function () {
    console.log("SMNLOG:qunatityInput changed");
        var qty = +$(this).val();
        var pRate = +$(this).closest("td").prev("td").prev("td").text();
        $(this).closest("td").next("td").find('label').text(pRate*qty);
        $(this).closest("td").next("td").find('input.totalPrice').val(pRate*qty);
        reCalculateSalesTotal();
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
        //discount = +$(".discount input").val();
        console.log("SMNLOG:discount:"+discount);
        //total = calculateTotal(, 'td.lineTotal');
        $(".salesLineItemsDiv table tbody tr").each(function () {
            console.log("SMNLOG:row 2::"+$(this).find('td.lineTotal').find("label").text());
            total += (+$(this).find('td.lineTotal').find("label").text());
            console.log("SMNLOG:" + total);
        });

        $(".salesLineItemsDiv table tfoot tr td.subTotal").text(total);

        //$(".salesLineItemsDiv table tfoot tr td.grandTotal").find("label").text(total-discount);
        //$(".salesLineItemsDiv table tfoot tr td.grandTotal").find("input").val(total-discount);// for binding
        $(".salesLineItemsDiv table tfoot tr td.grandTotal").find("label").text(total);
        $(".salesLineItemsDiv table tfoot tr td.grandTotal").find("input").val(total);// for binding
    }
})

