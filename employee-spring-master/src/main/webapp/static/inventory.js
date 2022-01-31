
//Inventory functions start here!!!
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function checkBarcode(data,barcode){
	for(var i in data){
		var e = data[i];
		if (barcode==e.barcode.toLowerCase().trim()){
			return true
		}
	} 
	return false
}

function validate(){
	let x = document.forms["inventory-form"]["barcode"].value;
	if (x == "") {
		sweetAlert("Missing parameter", "Barcode must be filled out", "warning");
		return false;
	}
	else if(x.length>255){
		sweetAlert("Constraint Exception", "Length of barcode exceeded permitted length", "warning");
	}

	let y = document.forms["inventory-form"]["quantity"].value;
	if (y == "") {
		sweetAlert("Missing Parameter", "Quantity must be filled out", "warning");
		return false;
	}
	else if (y <= 0) {
		sweetAlert("Constraint Exception", "Quantity must be greater than or equal to 1 unit", "warning");
		return false;
	}

	addInventory()
}


//BUTTON ACTIONS
function addInventory(event){
	// Set the values to update
	var barcode=$("#inventory-form input[name=barcode").val();
	var quantity=$("#inventory-form input[name=quantity").val();

	var js_form={"barcode":barcode,"quantity":quantity}
	var url = getInventoryUrl();

	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/product";

	//checking barcode
	$.ajax({
		url: url2,
		type: 'GET',
		success: function(data) {	
			if(checkBarcode(data,barcode)){
				 fetch(url, {
					method: 'POST',
					headers: {
					  'Content-Type': 'application/json',
					},
					body: JSON.stringify(js_form),
				  })
				  .then(js_form => {
					console.log('Success:', js_form);
					getInventoryList();
				  })
				  .catch((error) => {
					console.error('Error:', error);
				  });
			}
			else
			{
				//alert("The entered Barcode does not exist in the database, please try again !")
				sweetAlert("Input Data Error", "The entered Barcode does not exist in the database, please try again !", "error");
			}
		},
		error: function(){
			sweetAlert("Data loading error", "An error has occurred in getting the inventory list", "error");
		}
	 });
	 //end of check

	console.log("adding Inventory details");
	$("#modal-Form").css("display", "none");
	$(".fade").css("display","none");
	$('.form-group :input').val("");
	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	//id is the id
	var id = $("#inventory-edit-form input[name=id]").val();	
	var url = getInventoryUrl() + "/" + id;
	
	//Set the values to update
	var barcode = $("#inventory-edit-form input[name=barcode]").val();
	var quantity = $("#inventory-edit-form input[name=quantity]").val();
	$form={"barcode":barcode,"quantity":quantity}

	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/product";

	//checking barcode
	$.ajax({
		url: url2,
		type: 'GET',
		success: function(data) {	
			if(checkBarcode(data,barcode)){
				 fetch(url, {
					method: 'PUT',
					headers: {
					  'Content-Type': 'application/json',
					},
					body: JSON.stringify($form),
				  })
				  .then($form => {
					console.log('Success:', $form);
					getInventoryList();
				  })
				  .catch((error) => {
					console.error('Error:', error);
				  });
			}
			else
			{
				sweetAlert("Input Data Error", "The entered Barcode does not exist in the database, please try again !", "error");
			}
		},
		error: function(){
			sweetAlert("Data loading error", "An error has occurred in getting the inventory list", "error");
		}
	 });

	return false;
}


function getInventoryList(){
	console.log("inside getInventorylist");
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Inventory data fetched");
	   		console.log(data);	
	   		displayInventoryList(data);
	   },
	   error: function(){
		sweetAlert("Data loading error", "An error has occurred in getting the inventory list", "error");
	   }
	});
}

function deleteInventory(id){
	var url = getInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
		if(data=="javax.persistence.NoResultException: No entity found for query"){
			sweetAlert("Data Missing error", "The record seems to not exist in our database. Please refresh and try again.", "error");
		}
	   		console.log("Inventory deleted");
	   		getInventoryList();     //...
	   },
	   error: function(){
			   sweetAlert("Unknown error", "An error has occurred", "error");
	   }
	});
}

//File upload methods
function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}

function processData(event){
	var file = $('#process-inventory-data')[0].files[0];
	if(file==undefined){
		sweetAlert("File Error", "Please select a file first", "error");
		return false
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	console.log(fileData);
	if(fileData.length>5000){
		sweetAlert("Rows Exceeded","Number of rows in the file Exceeded 5000","warning")
		return false;
	}
	for(var i in fileData){
		uploadRows(fileData[i]);
	}
}

function uploadRows(row){
	var url = getInventoryUrl();
	console.log(url);
	var barcode = row.barcode
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/product";

	//checking barcode
	$.ajax({
		url: url2,
		type: 'GET',
		success: function(data) {	
			if(checkBarcode(data,barcode)){
				 fetch(url, {
					method: 'POST',
					headers: {
					  'Content-Type': 'application/json',
					},
					body: JSON.stringify(row),
				  })
				  .then(Response => {
					console.log('Success:', Response);
					var $file = $('#process-inventory-data');
	  				$file.val('');
					getInventoryList();
				  })
				  .catch((error) => {
					console.error('Error:', error);
					var $file = $('#process-inventory-data');
					$file.val('');
				  });
			}
			else
			{
				sweetAlert("Input Data Error", "The entered Barcode does not exist in the database, please try again !", "error");
			}
		},
		error: function(){
			sweetAlert("Data loading error", "An error has occurred in getting the inventory list", "error");
		}
	 });
	return false;
}

//UI DISPLAY METHODS

function displayInventoryList(data){
	console.log('Printing Inventory data');
	var $tbody = $('#inventory-table').find('tbody');
	$('#inventory-table').dataTable().fnDestroy();
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-primary btn-md" onclick="deleteInventory(' + e.id + ')">DELETE</button>'
		buttonHtml += ' <button class="btn btn-primary btn-md btn-open" onclick="displayEditInventory(' + e.id + ')">EDIT</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	createTable();
}

function createTable(){
	$('#inventory-table').DataTable({
		info:false,
		bDestroy: true,
		pageLength : 5,
		lengthMenu: [[5, 10, 20], [5, 10, 20]]
	});
	
}


function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Inventory data fetched");
	   		console.log(data);	
	   		displayInventory(data);     //...
	   },
	   error: function(){
		sweetAlert("Unknown error", "An error has occurred", "error");
	   }
	});	
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);	
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);	
	$('#edit-inventory-modal').modal('toggle');
}
//Inventory functions end here!!!


// INITIALIZATION CODE
function init(){
	console.log("Initialising")
	$('#add-inventory').click(validate);
	$('#update-inventory').click(updateInventory);
	$('#refresh-inventory-data').click(getInventoryList);
	$('#process-inventory-file').click(processData);
}

$(document).ready(init);
$(document).ready(getInventoryList);


