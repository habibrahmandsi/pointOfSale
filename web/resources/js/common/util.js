/**
 * Created by habib on 1/27/15.
 */

var globalDateFormat = 'dd-mm-yyyy';
var okTxt = 'Ok';
var cancelTxt = 'Cancel';
var confirmationTitle = 'Confirmation';
var editTitle = 'Are you sure to edit ?';
var confirmTitle = 'Are you sure ?';
var deleteTitle = 'Are you sure to delete ?';
var rowDisplayGlobal = 20;

function confirmDialog(title,bodyTitle,successBtnTxt,cancelBtnTxt,fn){
    bootbox.dialog({
        message: bodyTitle,
        title: title,
        buttons: {
            success: {
                label: successBtnTxt,
                className: "btn-success",
                callback: function() {
                    console.log("great success");
                    fn.call(this);
                }
            },
            danger: {
                label: cancelBtnTxt,
                className: "btn-danger",
                callback: function() {
                    console.log("uh oh, look out!");
                }
            }
        }
    });
}

oCache = {
    iCacheLower: -1
};

function fnSetKey( aoData, sKey, mValue )
{
    for ( var i=0, iLen=aoData.length ; i<iLen ; i++ )
    {
        if ( aoData[i].name == sKey )
        {
            aoData[i].value = mValue;
        }
    }
}

function fnGetKey( aoData, sKey )
{
    for ( var i=0, iLen=aoData.length ; i<iLen ; i++ )
    {
        if ( aoData[i].name == sKey )
        {
            return aoData[i].value;
        }
    }
    return null;
}

function fnDataTablesPipeline ( sSource, aoData, fnCallback ) {
    var iPipe = 3; /* Ajust the pipe size */

    var bNeedServer = false;
    var sEcho = fnGetKey(aoData, "sEcho");
    var iRequestStart = fnGetKey(aoData, "iDisplayStart");
    var iRequestLength = fnGetKey(aoData, "iDisplayLength");
    var iRequestEnd = iRequestStart + iRequestLength;
    oCache.iDisplayStart = iRequestStart;

    /* outside pipeline? */
    if ( oCache.iCacheLower < 0 || iRequestStart < oCache.iCacheLower || iRequestEnd > oCache.iCacheUpper )
    {
        bNeedServer = true;
    }

    /* sorting etc changed? */
    if ( oCache.lastRequest && !bNeedServer )
    {
        for( var i=0, iLen=aoData.length ; i<iLen ; i++ )
        {
            if ( aoData[i].name != "iDisplayStart" && aoData[i].name != "iDisplayLength" && aoData[i].name != "sEcho" )
            {
                if ( aoData[i].value != oCache.lastRequest[i].value )
                {
                    bNeedServer = true;
                    break;
                }
            }
        }
    }

    /* Store the request for checking next time around */
    oCache.lastRequest = aoData.slice();

    if ( bNeedServer )
    {
        if ( iRequestStart < oCache.iCacheLower )
        {
            iRequestStart = iRequestStart - (iRequestLength*(iPipe-1));
            if ( iRequestStart < 0 )
            {
                iRequestStart = 0;
            }
        }

        oCache.iCacheLower = iRequestStart;
        oCache.iCacheUpper = iRequestStart + (iRequestLength * iPipe);
        oCache.iDisplayLength = fnGetKey( aoData, "iDisplayLength" );
        fnSetKey( aoData, "iDisplayStart", iRequestStart );
        fnSetKey( aoData, "iDisplayLength", iRequestLength*iPipe );

        $.getJSON( sSource, aoData, function (json) {
            /* Callback processing */

            oCache.lastJson = jQuery.extend(true, {}, json);

            if ( oCache.iCacheLower != oCache.iDisplayStart )
            {
                json.aaData.splice( 0, oCache.iDisplayStart-oCache.iCacheLower );
            }
            json.aaData.splice( oCache.iDisplayLength, json.aaData.length );

            fnCallback(json)
        } );
    }
    else
    {
        json = jQuery.extend(true, {}, oCache.lastJson);
        json.sEcho = sEcho; /* Update the echo for each response */
        json.aaData.splice( 0, iRequestStart-oCache.iCacheLower );
        json.aaData.splice( iRequestLength, json.aaData.length );
        fnCallback(json);
        return;
    }
}

function commonDataTableInit(tableIdOrCss,url,columns){

    oTable = $(tableIdOrCss).dataTable({
        "aLengthMenu": [[5,10,20,60], [5,10,20,60]],
        "iDisplayLength": rowDisplayGlobal,
        "bProcessing": true,
        "bServerSide": true,
        "bJQueryUI": true,
        "bAutoWidth": true,
        "sPaginationType": "simple_numbers", // you can also give here 'simple','simple_numbers','full','full_numbers'
        "oLanguage": {
            "sSearch": "Search:"
        },
        "sAjaxSource": url,
        "fnServerData": fnDataTablesPipeline,
        "aoColumns":columns
    });
}

function makeAjaxUrlForTypeAhead(url, colIndex, txtToSearch,length){
    return url+"?iSortCOL="+colIndex+"&sSearch="+txtToSearch+"&iDisplayLength="+length;
}


function makeAutoComplete(inputIdOrClass,dataList, matchingColName, displayKey) {

    $(inputIdOrClass).typeahead({
            hint: false,
            highlight: true,
            limitTo:3,
            minLength: 1

        },
        {
            displayKey:  function(data){
                if("1" == displayKey){
                    //return data.productName+""+data.uomName+" "+data.typeName;
                    return data.productName;
                 }else{
                    return data[displayKey];
                 }
            },
            //source: substringMatcher(dataList)
            source:  function findMatches(q, cb) {
                var matches, substringRegex;

                // an array that will be populated with substring matches
                matches = [];

                // regex used to determine if a string contains the substring `q`
                substrRegex = new RegExp("^"+q, 'i');

                // iterate through the pool of strings and for any string that
                // contains the substring `q`, add it to the `matches` array
                $.each(dataList, function (i, str) {
                    if (substrRegex.test(str[matchingColName])) {
                        matches.push(str);
                    }
                });
                //console.log("SMNLOG:matches:"+matches);
                cb(matches);
            }
        });
}

/*
function calculateTotal(table, calculateFrom) {
    var total = 0;
    //$(".purchaseLineItemsDiv").find("table").find("tbody").find("tr").each(function(){
    $(table).each(function () {
        console.log("SMNLOG:row::"+$(this).find(calculateFrom).find("label").text());
        total += (+$(this).find(calculateFrom).find("label").text());
        console.log("SMNLOG:" + total);
    });
    return total;

}*/
function getDateForTableView(date){
    return  date.getDate() + "/" +(date.getMonth()+1) + "/" + date.getFullYear()
}