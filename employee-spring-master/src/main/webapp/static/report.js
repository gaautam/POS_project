function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function changeDate(dt){
    date = dt.split("-");
    return date[2]+"-"+date[1]+"-"+date[0];
}

function startDate(event){
        console.log(document.getElementById("sdate"));
}


function orderReport(){
    var sdate = changeDate($('#sdate').val())
    var edate = changeDate($('#edate').val())
    var brand = changeDate($('#brand').val())
    var category = changeDate($('#category').val())

    //checking date values entered or not
	if (sdate == "undefined-undefined-") {
                sweetAlert("Missing parameter", "Starting date must be filled out", "warning");
		return false;
	}
        else if (edate == "undefined-undefined-") {
		sweetAlert("Missing parameter", "Ending date must be filled out", "warning");
		return false;
	}
        else if(edate<sdate){
                sweetAlert("Wrong parameter", "Ending date must be a date after the starting date!", "warning");
		return false;
        }
        if(brand=="undefined-undefined-" && category=="undefined-undefined-"){
                withoutBrandCategory(sdate,edate)
        }
        else if((brand != "undefined-undefined-" && category == "undefined-undefined-")||(brand == "undefined-undefined-" && category != "undefined-undefined-")){
                sweetAlert("Missing parameter", "Both brand and category must be filled out", "warning");
		return false;
        }
        if(sdate != "undefined-undefined-" && edate != "undefined-undefined-" && brand != "undefined-undefined-" && category != "undefined-undefined-"){
                withBrandCategory(sdate,edate,brand,category)
        }

   
}

function withBrandCategory(sdate,edate,brand,category){
        var url = getReportUrl() + "/api/order/report/"+sdate+"/"+edate+"/"+brand+"/"+category;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Order report data fetched");
	   		console.log(data);  
               $.ajax({
                url: getReportUrl() + "/api/order/report/orderurl",
                type: 'GET',
                success: function(download) {
                        console.log("Order report url fetched");
                        console.log(download);
                        window.open(download,'_blank');
                },
                error: function(){
                        sweetAlert("URL Loading Error", "An error has occurred in getting order url", "error");
                }
             });	 	
	   },
	   error: function(){
                sweetAlert("Data Loading Error", "An error has occurred in getting order report", "error");
	   }
	});
}

function withoutBrandCategory(sdate,edate){
        var url = getReportUrl() + "/api/order/report/"+sdate+"/"+edate;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Consolidated Order report data fetched");
	   		console.log(data);  
               $.ajax({
                url: getReportUrl() + "/api/order/report/consolidated_orderurl",
                type: 'GET',
                success: function(download) {
                        console.log("Consolidated Order report url fetched");
                        console.log(download);
                        window.open(download,'_blank');
                },
                error: function(){
                        sweetAlert("URL Loading Error", "An error has occurred in getting consolidated order url", "error");
                }
             });	 	
	   },
	   error: function(){
                sweetAlert("Data Loading Error", "An error has occurred in getting consolidated order report", "error");
	   }
	});
}

function brandReport(){
    var url = getReportUrl() + "/api/brand/report";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Brand report data fetched");
	   		console.log(data);
               $.ajax({
                url: url + "/brandurl",
                type: 'GET',
                success: function(download) {
                        console.log("Brand report url fetched");
                        console.log(download);
                        window.open(download,'_blank');
                },
                error: function(){
                        sweetAlert("URL Loading Error", "An error has occurred in getting brand url", "error");
                }
             });	
	   },
	   error: function(){
                sweetAlert("Data Loading Error", "An error has occurred in getting brand report", "error");
	   }
	});
}

function inventoryReport(){
    var url = getReportUrl() + "/api/inventory/report";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Inventory report data fetched");
	   		console.log(data);	
               $.ajax({
                url: url + "/inventoryurl",
                type: 'GET',
                success: function(download) {
                        console.log("Inventory report url fetched");
                        console.log(download);
                        window.open(download,'_blank');
                },
                error: function(){
                        sweetAlert("URL Loading Error", "An error has occurred in getting inventory url", "error");
                }
             });	
	   },
	   error: function(){
                sweetAlert("Data Loading Error", "An error has occurred in getting inventory report", "error");
	   }
	});
}

function init(){
	console.log("Initialising")
        $('#brand-report').click(brandReport);
    $('#inventory-report').click(inventoryReport);
    $('#order-report').click(orderReport);

}

$(document).ready(init);