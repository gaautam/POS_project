//Product functions start here

//CRUD operations
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getProductList(){
	console.log("inside getProductlist");
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Product data fetched");
	   		console.log(data);	
	   		displayProductList(data);
	   },
	   error: function(){
			   sweetAlert("Data loading error", "An error has occurred in getting the Product list", "error");
	   }
	});
}

function checkBrandCategory(data,brand,category,barcode){

	var url = getProductUrl();
	$.ajax({
		url: url,
		type: 'GET',
		async:false,
		success: function(data1) {	
			for(var i in data1){
				var e = data1[i];
				if (barcode==e.barcode.toLowerCase().trim()){
					alert("The barcode entered already exists in Inventory. Edit it if required.");
					//sweetAlert("Input Data Error","The barcode entered already exists in Inventory. Edit it if required.","warning")
					return false;
					
					
				}
			} 
		},
		error: function(){
			sweetAlert("Data loading error", "An error has occurred in getting the inventory list", "error");
		}
	 });

	for(var i in data){
		var e = data[i];
		if (brand.toLowerCase().trim()==e.brand && category.toLowerCase().trim()==e.category){
			return true
		}
	} 
	return false
}

var flag=0;

function checkFileBrandCategory(data,brand,category,barcode){

	var url = getProductUrl();
	flag=0;
	$.ajax({
		url: url,
		type: 'GET',
		async:false,
		success: function(data1) {	
			for(var i in data1){
				var e = data1[i];
				if (barcode==e.barcode.toLowerCase().trim()){
					//sweetAlert("Input Data Error","The barcode entered already exists in Inventory. Edit it if required.","warning")
					flag=1;
					
					
				}
			} 
		},
		error: function(){
			sweetAlert("Data loading error", "An error has occurred in getting the inventory list", "error");
		}
	 });
	 if(flag==1){
		 return false;
	 }

	for(var i in data){
		var e = data[i];
		if (brand.toLowerCase().trim()==e.brand && category.toLowerCase().trim()==e.category){
			return true
		}
	} 
	return false
}

function checkUpdatedBarcode(data,barcode,id){
	for(var i in data){
		var e = data[i];
		if (barcode!=e.barcode.toLowerCase().trim()){
			continue;
		}
		else{
			if(e.id==id){
				return true;
			}
			return false;
		}
	} 
	return true;
}

function checkBarcode(data,barcode){
	for(var i in data){
		var e = data[i];
		if (barcode!=e.barcode.toLowerCase().trim()){
			continue;
		}
		else if(barcode==e.barcode.toLowerCase().trim()) {
			return false;
		}
	} 
	return true;
}


function validate(){
	let x = document.forms["product-form"]["barcode"].value;
	if (x == "") {
		sweetAlert("Missing Parameter", "Barcode must be filled out", "warning");
		return false;
	}
	else if(x.length>255){
		sweetAlert("Constraint Exception", "Length of barcode exceeded permitted length", "warning");
	}

	let y = document.forms["product-form"]["brand"].value;
	if (y == "") {
		sweetAlert("Missing Parameter", "Brand must be filled out", "warning");
		return false;
	}

	let b = document.forms["product-form"]["category"].value;
	if (b == "") {
		sweetAlert("Missing Parameter", "Category must be filled out", "warning");
		return false;
	}

	let z = document.forms["product-form"]["name"].value;
	if (z == "") {
		sweetAlert("Missing Parameter", "Name must be filled out", "warning");
		return false;
	}
	else if(z.length>255){
		sweetAlert("Constraint Exception", "Length of name exceeded permitted length", "warning");
	}

	let a = document.forms["product-form"]["mrp"].value;
	if (a == "") {
		sweetAlert("Missing Parameter", "MRP must be filled out", "warning");
		return false;
	}
	else if (a < 0) {
		sweetAlert("Constraint Exception", "MRP cannot be negative", "warning");
		return false;
	}

	addProduct();
}

function validateUpdate(){
	let x = document.forms["product-edit-form"]["barcode"].value;
	if (x == "") {
		sweetAlert("Missing Parameter", "Barcode must be filled out", "warning");
		return false;
	}
	else if(x.length>255){
		sweetAlert("Constraint Exception", "Length of barcode exceeded permitted length", "warning");
	}

	let y = document.forms["product-edit-form"]["brand"].value;
	if (y == "") {
		sweetAlert("Missing Parameter", "Brand must be filled out", "warning");
		return false;
	}

	let b = document.forms["product-edit-form"]["category"].value;
	if (b == "") {
		sweetAlert("Missing Parameter", "Category must be filled out", "warning");
		return false;
	}

	let z = document.forms["product-edit-form"]["name"].value;
	if (z == "") {
		sweetAlert("Missing Parameter", "Name must be filled out", "warning");
		return false;
	}
	else if(z.length>255){
		sweetAlert("Constraint Exception", "Length of name exceeded permitted length", "warning");
	}

	let a = document.forms["product-edit-form"]["mrp"].value;
	if (a == "") {
		sweetAlert("Missing Parameter", "MRP must be filled out", "warning");
		return false;
	}
	else if (a < 0) {
		sweetAlert("Constraint Exception", "MRP cannot be negative", "warning");
		return false;
	}

	updateProduct();
}

function addProduct(event){
	// Set the values to update
	var barcode=$("#product-form input[name=barcode").val();
	var brand=$("#product-form input[name=brand").val();
	var category=$("#product-form input[name=category").val();
	var name=$("#product-form input[name=name").val();
	var mrp=$("#product-form input[name=mrp").val();
	var js_form={"barcode":barcode,"brand":brand,"category":category,"name":name,"mrp":mrp}
	var url = getProductUrl();

	//checking brand category
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/brand";
	var url3 = baseUrl + "/api/product";

				$.ajax({
					url: url2,
					type: 'GET',
					success: function(data) {
						if(checkBrandCategory(data,brand,category,barcode)){
								fetch(url, {
									method: 'POST',
									headers: {
									'Content-Type': 'application/json',
									},
									body: JSON.stringify(js_form),
								})
								.then(js_form => {
									console.log('Success:', js_form);
									getProductList();
								})
								.catch((error) => {
									console.error('Error:', error);
								});
						}
						else
						{
							alert("The entered Brand and Category combination does not exist in the database, please try again !");
						//sweetAlert("Input Data Error", "The entered Brand and Category combination does not exist in the database, please try again !", "error");
						}
					},
					error: function(){
					sweetAlert("Data loading error", "An error has occurred in getting the Product list", "error");
					}
				});
	
	//end of check

	console.log("adding Product");
	$("#modal-Form").css("display", "none");
	$(".fade").css("display","none");
	$('.form-group :input').val("");
	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();	
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var barcode=$("#product-edit-form input[name=barcode").val();
	var brand=$("#product-edit-form input[name=brand").val();
	var category=$("#product-edit-form input[name=category").val();
	var name=$("#product-edit-form input[name=name").val();
	var mrp=$("#product-edit-form input[name=mrp").val();
	$form={"barcode":barcode,"brand":brand,"category":category,"name":name,"mrp":mrp}
	
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/brand";
	var url3 = baseUrl + "/api/product";

	$.ajax({
		url: url3,
		type: 'GET',
		success: function(data1) {	
			if(checkUpdatedBarcode(data1,barcode,id)){
				$.ajax({
					url: url2,
					type: 'GET',
					success: function(data) {	
						if(checkBrandCategory(data,brand,category)){
							 fetch(url, {
								method: 'PUT',
								headers: {
								  'Content-Type': 'application/json',
								},
								body: JSON.stringify($form),
							  })
							  .then($form => {
								console.log('Success:', $form);
								getProductList();
							  })
							  .catch((error) => {
								console.error('Error:', error);
							  });
						}
						else
						{
							sweetAlert("Input Data Error", "The entered Brand and Category combination does not exist in the database, please try again !", "error");
						}
					},
					error: function(){
						sweetAlert("Data loading error", "An error has occurred in getting the Product list", "error");
					}
				 });
			}
			else
			{
				sweetAlert("Input Data Error", "The entered Barcode exists in the database, please try again !", "error");
			}
		},
		error: function(){
			sweetAlert("Data loading error", "An error has occurred in getting the inventory list", "error");
		}
	 });
	
	return false;
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
		if(data=="javax.persistence.NoResultException: No entity found for query"){
			sweetAlert("Data Missing error", "The record seems to not exist in our database. Please refresh and try again.", "error");
		}
	   		console.log("Product deleted");
	   		getProductList();     //...
	   },
	   error: function(){
			   sweetAlert("Unknown error", "An error has occurred", "error");
	   }
	});
}

//File upload methods
var errorProductData = []

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
	var file = $('#process-product-data')[0].files[0];
	console.log(file);
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
	checkFile(fileData)
	if(errorProductData.length==0){
		for(var i in fileData){
			uploadRows(fileData[i]);
		}
	}
	else{
		$('#file-error-product-modal').modal('toggle');
	}
}

function checkFile(fileData){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/brand";
	for(var i in fileData){
		var row = fileData[i];
			$.ajax({
				url: url2,
				type: 'GET',
				async: false,
				success: function(data) {	
					if(checkFileBrandCategory(data,row.brand,row.category,row.barcode)){
						console.log(row+" this is a row")
					}
					else
					{
						if(flag==1){
						row.Error_Message = "The barcode entered already exists in Inventory. Edit it if required.";
						}
						else{
							row.Error_Message = "The entered Brand and Category combination does not exist in the database !";
						}
						errorProductData.push(row)
					}
				},
				error: function(){
					sweetAlert("Data Loading error", "An error has occurred in getting the Inventory list", "error");
				}
			 });
	}
}

function downloadErrors(){
	writeFileData(errorProductData);
	var $file = $('#process-product-data');
	$file.val('');
	$('#file-error-product-modal').modal('toggle');
	errorProductData = [];
}

function uploadRows(row){
	var url = getProductUrl();
	var brand = row.brand;
	var category = row.category;
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/brand";
	$.ajax({
	   url: url2,
	   type: 'GET',
	   success: function(data) {	
					fetch(url, {
						method: 'POST',
						headers: {
						'Content-Type': 'application/json',
						},
						body: JSON.stringify(row),
					})
					.then(Response => {
						console.log('Success:', Response);
						var $file = $('#process-product-data');
	  					$file.val('');
						getProductList();
					})
					.catch((error) => {
						console.error('Error:', error);
						var $file = $('#process-product-data');
						$file.val('');
					});
	   },
	   error: function(){
		sweetAlert("Data loading error", "An error has occurred in getting the Product list", "error");
	   }
	});
	return false;
}

//Display Operations
function displayProductList(data){
	console.log('Printing Product data');
	var $tbody = $('#product-table').find('tbody');
	$('#product-table').dataTable().fnDestroy();
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-primary btn-md" onclick="deleteProduct(' + e.id + ')">DELETE</button>'
		buttonHtml += ' <button class="btn btn-primary btn-md btn-open" onclick="displayEditProduct(' + e.id + ')">EDIT</button>'
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	createTable();

}

function createTable(){
	$('#product-table').DataTable({
		info:false,
		bDestroy: true,
		pageLength : 5,
		lengthMenu: [[5, 10, 20], [5, 10, 20]]
	});
	
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Product data fetched");
	   		console.log(data);	
	   		displayProduct(data);     //...
	   },
	   error: function(){
			   sweetAlert("Unknown error", "An error has occurred", "error");
	   }
	});	
}

function displayProduct(data){	
	$("#product-edit-form input[name=barcode").val(data.barcode);
	$("#product-edit-form input[name=brand").val(data.brand);
	$("#product-edit-form input[name=category").val(data.category);
	$("#product-edit-form input[name=name").val(data.name);
	$("#product-edit-form input[name=mrp").val(data.mrp);
	$("#product-edit-form input[name=id]").val(data.id);	
	$('#edit-product-modal').modal('toggle');
}

// INITIALIZATION CODE
function init(){
	console.log("Initialising")
	$('#add-product').click(validate);
	$('#update-product').click(validateUpdate);
	$('#refresh-product-data').click(getProductList);
	$('#process-product-file').click(processData);
	$('#download-product-errors').click(downloadErrors);
}

$(document).ready(init);
$(document).ready(getProductList);
//$(document).change(window.location.reload());

