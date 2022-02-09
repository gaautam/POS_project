
//Brand functions start here!!!
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

function checkUnique(data,brand,category){
	for(var i in data){
		var e = data[i];
		if (e.brand==brand.toLowerCase().trim() && e.category==category.toLowerCase().trim()){
			return false
		}
	}
	return true
}

function validate(){
	let x = document.forms["brand-form"]["brand"].value;
	if (x == "") {
		sweetAlert("Missing parameter", "Brand must be filled out", "warning");
		return false;
	}
	else if(x.length>255){
		sweetAlert("Constraint Exception", "Length of brand exceeded permitted length", "warning");
	}

	let y = document.forms["brand-form"]["category"].value;
	if (y == "") {
		sweetAlert("Missing parameter", "Category must be filled out", "warning");
		return false;
	}
	else if(y.length>255){
		sweetAlert("Constraint Exception", "Length of category exceeded permitted length", "warning");
	}

	addBrand()
}

function validateUpdate(){
	let x = document.forms["brand-edit-form"]["brand"].value;
	if (x == "") {
		sweetAlert("Missing parameter", "Brand must be filled out", "warning");
		return false;
	}
	else if(x.length>255){
		sweetAlert("Constraint Exception", "Length of brand exceeded permitted length", "warning");
	}

	let y = document.forms["brand-edit-form"]["category"].value;
	if (y == "") {
		sweetAlert("Missing parameter", "Category must be filled out", "warning");
		return false;
	}
	else if(y.length>255){
		sweetAlert("Constraint Exception", "Length of category exceeded permitted length", "warning");
	}

	updateBrand()
}

//BUTTON ACTIONS
async function addBrand(){
	// Set the values to update
	//$('#brand-form').validate().form()
	var brand=$("#brand-form input[name=brand").val();
	var category=$("#brand-form input[name=category").val();
	var js_form={"brand":brand,"category":category}
	console.log(js_form);
	var url = getBrandUrl();

	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/brand";
	// check brand and category combo is unique or not
	$.ajax({
		url: url2,
		type: 'GET',
		success: function(data) {	
			if(checkUnique(data,brand,category)){
				 fetch(url, {
					method: 'POST',
					headers: {
					  'Content-Type': 'application/json',
					},
					body: JSON.stringify(js_form),
				  })
				  .then(js_form => {
					console.log('Success:', js_form);
					getBrandList();
				  })
				  .catch((error) => {
					console.error('Error:', error);
				  });
			}
			else
			{	
				// alert("Brand and Category combination already Exists. Please try again");
				sweetAlert("Unique constraints exception", "Brand and Category combination already Exists. Please try again", "error");
				
			}
		},
		error: function(){
				sweetAlert("Data Loading error", "An error has occurred in getting the Brand list", "error");
				
		}
	 });
	console.log("adding brand");
	$("#modal-Form").css("display", "none");
	$(".fade").css("display","none");
	$('.form-group :input').val("");
	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();	
	var url = getBrandUrl() + "/" + id;

	//Set the values to update
	var brand = $("#brand-edit-form input[name=brand]").val();
	var category = $("#brand-edit-form input[name=category]").val();
	$form={"brand":brand,"category":category}

	var url2 = getBrandUrl();
	$.ajax({
		url: url2,
		type: 'GET',
		success: function(data) {	
			console.log(data);
			if(checkUnique(data,brand,category)){
				fetch(url, {
					method: 'PUT',
					headers: {
					  'Content-Type': 'application/json',
					},
					body: JSON.stringify($form),
				  })
				  .then(Response => {
					console.log('Success:', Response);
					getBrandList();
				  })
				  .catch((error) => {
					console.error('Error:', error);
				  });		 
			}
			else
			{
				sweetAlert("Unique constraints exception", "Brand and Category combination already Exists. Please try again", "error");
			}
		},
		error: function(){
			sweetAlert("Data Loading error", "An error has occurred in getting the Brand list", "error");
		}
	 });



	
	return false;
}


function getBrandList(){
	console.log("inside getBrandlist");
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Brand data fetched");
	   		console.log(data);	
	   		displayBrandList(data);
	   },
	   error: function(){
		sweetAlert("Data Loading error", "An error has occurred in getting the Brand list", "error");
	   }
	});
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
		
	   		console.log("Brand deleted");
			if(data=="javax.persistence.NoResultException: No entity found for query"){
				sweetAlert("Data Missing error", "The record seems to not exist in our database. Please refresh and try again.", "error");
			}
	   		getBrandList();     //...
	   },
	   error: function(error){
		console.log(error)
			   sweetAlert("Unknown error", "An error has occurred", "error");
			   
	   }
	});
}

//File upload methods

var errorBrandData = [];

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
	console.log("inside file")
	var file = $('#process-brand-data')[0].files[0];
	if(file==undefined){
		sweetAlert("File Error", "Please select a file first", "error");
		return false
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
		sweetAlert("Rows Exceeded","Number of rows in the file Exceeded 5000","warning")
		return false;
	}
	else if(fileData.length==0){
		sweetAlert("Input Data Error","The uploaded file seems to be empty or wrong with respect to the template provided. Please download the sample template and try again !!","warning")
		return false;
	}
	checkFile(fileData)
	if(errorBrandData.length==0){
		for(var i in fileData){
			uploadRows(fileData[i]);
		}
		sweetAlert("Upload Successful","The contents of the file has been uploaded successfully","success")
	}
	else{
		$('#file-error-brand-modal').modal('toggle');
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
					console.log(errorBrandData);
					if(typeof row.brand === 'undefined' || typeof row.category === 'undefined' || row.brand==="" || row.category===""){
						row.Error_Message = "Brand or Category value missing in the file"
						errorBrandData.push(row)
					}
					else if(checkUnique(data,row.brand,row.category)){
						console.log(fileData[i]+" this is a row")
					}
					else
					{
						row.Error_Message = "Brand and Category combination already Exists."
						errorBrandData.push(row)
					}
				},
				error: function(){
					sweetAlert("Data Loading error", "An error has occurred in getting the Brand list", "error");
				}
			 });
	}
}

function downloadErrors(){
	writeFileData(errorBrandData);
	var $file = $('#process-brand-data');
	$file.val('');
	$('#file-error-brand-modal').modal('toggle');
	errorBrandData = [];
}

function uploadRows(row){
	var url = getBrandUrl();
	console.log(url);

	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url2 = baseUrl + "/api/brand";
	var brand = row.brand;
	var category = row.category;

	$.ajax({
		url: url2,
		type: 'GET',
		success: function(data) {	
			console.log(data);
				fetch(url, {
					method: 'POST',
					headers: {
					  'Content-Type': 'application/json',
					},
					body: JSON.stringify(row),
				  })
				  .then(Response => {
					console.log('Success:', Response);
					console.log(Response)
					var $file = $('#process-brand-data');
					$file.val('');
					getBrandList();
				  })
				  .catch((error) => {
					console.error('Error:', error);
					var $file = $('#process-brand-data');
					$file.val('');
				  });
		},
		error: function(){
			sweetAlert("Data Loading error", "An error has occurred in getting the Brand list", "error");
		}
	 });

	
	console.log("adding brand");
	return false;
}

//UI DISPLAY METHODS

function displayBrandList(data){
	console.log('Printing Brand data');
	var $tbody = $('#brand-table').find('tbody');
	$('#brand-table').dataTable().fnDestroy();
	$tbody.empty();
	//$("#brand-table tbody").empty();
	//$("#brand-table  tbody").remove();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-primary btn-md" onclick="deleteBrand(' + e.id + ')">DELETE</button>'
		buttonHtml += ' <button class="btn btn-primary btn-md btn-open" onclick="displayEditBrand(' + e.id + ')">EDIT</button>'
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	createTable();
}

function createTable(){
	$('#brand-table').DataTable({
		info:false,
		bDestroy: true,
		pageLength : 5,
		lengthMenu: [[5, 10, 20], [5, 10, 20]],
	});
	
}

function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Brand data fetched");
	   		console.log(data);	
	   		displayBrand(data);    
	   },
	   error: function(){
			   sweetAlert("Unkown error", "An error has occurred", "error");
			   }
	});	
}






function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);	
	$("#brand-edit-form input[name=category]").val(data.category);	
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}
//Brand functions end here!!!


// INITIALIZATION CODE
function init(){
	console.log("Initialising")
	$('#add-brand').click(validate);
	$('#update-brand').click(validateUpdate);
	$('#refresh-brand-data').click(getBrandList);
	$('#process-brand-file').click(processData);
	$('#download-brand-errors').click(downloadErrors);

}

$(document).ready(init);
$(document).ready(getBrandList);
// $(document).ready(function() {
// 	var datatable=$('#brand-table').DataTable({
// 		info:false,
// 		searching:false
// 	});
// });
