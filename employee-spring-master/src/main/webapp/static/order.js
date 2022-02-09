
//Inventory functions start here!!!
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function validate(){
	let x = document.forms["add-order-form"]["barcode"].value;
	if (x == "") {
		sweetAlert("Missing parameter", "Barcode must be filled out", "warning");
		return false;
	}
	else if(x.length>255){
		sweetAlert("Constraint Exception", "Length of barcode exceeded permitted length", "warning");
	}

	let y = document.forms["add-order-form"]["quantity"].value;
	if (y == "") {
		sweetAlert("Missing Parameter", "Quantity must be filled out", "warning");
		return false;
	}
	else if (y <= 0) {
		sweetAlert("Constraint Exception", "Quantity must be greater than or equal to 1 unit", "warning");
		return false;
	}

	let z = document.forms["add-order-form"]["selling_price"].value;
	if (z == "") {
		sweetAlert("Missing Parameter", "Selling Price must be filled out", "warning");
		return false;
	}
	else if (z < 0) {
		sweetAlert("Constraint Exception", "Selling price cannot be negative", "warning");
		return false;
	}

	AddItemToDB()
}

function validateUpdate(){
	let x = document.forms["order-edit-form"]["barcode"].value;
	if (x == "") {
		sweetAlert("Missing parameter", "Barcode must be filled out", "warning");
		return false;
	}
	else if(x.length>255){
		sweetAlert("Constraint Exception", "Length of barcode exceeded permitted length", "warning");
	}

	let y = document.forms["order-edit-form"]["quantity"].value;
	if (y == "") {
		sweetAlert("Missing Parameter", "Quantity must be filled out", "warning");
		return false;
	}
	else if (y <= 0) {
		sweetAlert("Constraint Exception", "Quantity must be greater than or equal to 1 unit", "warning");
		return false;
	}

	let z = document.forms["order-edit-form"]["selling_price"].value;
	if (z == "") {
		sweetAlert("Missing Parameter", "Quantity must be filled out", "warning");
		return false;
	}
	else if (z < 0) {
		sweetAlert("Constraint Exception", "Selling price cannot be negative", "warning");
		return false;
	}

	updateOrderItem()
}



//function to check valid barcode and quantity
function checkOrder(data,barcode,quantity){
	for(var i in data){
		var e = data[i];
		if (barcode==e.barcode){
					if(quantity<=e.quantity){
							return true
					}
					else{
						sweetAlert("Inventory Underflow", "Quantity exceeds amount in inventory", "warning");
						return false
					}
		}
	} 
	sweetAlert("Input Error", "Brand and category does not exist", "warning");
	return false
}

function checkOrder2(data,barcode,quantity,oldbarcode,row_barcode){
	var barcodearray = []
	for (var m = 0; m < row_barcode.length; m++) { 
		var bf = row_barcode[m]; 
		barcodearray.push(bf.innerHTML);
		} 
	for(var i in data){
		var e = data[i];
		if(barcode!=oldbarcode && barcodearray.includes(barcode)){
			sweetAlert("Input Error", "Barcode already exists in the given order. Please edit it if required.", "warning")
			return false;
		}
		if (barcode==e.barcode){
					if(quantity<=e.quantity){
							return true
					}
					else{
						sweetAlert("Inventory Underflow", "Quantity exceeds amount in inventory", "warning");
						return false
					}
		}
	} 
	sweetAlert("Input Error", "Brand and category does not exist", "warning");
	return false
}

function checkOrder3(data,barcode,quantity){
	var row_barcode = document.getElementsByName('barcode[]');
	var barcodearray = []
	for (var m = 0; m < row_barcode.length; m++) { 
		var bf = row_barcode[m]; 
		barcodearray.push(bf.innerHTML);
		} 
	for(var i in data){
		var e = data[i];
		if(barcodearray.includes(barcode)){
			sweetAlert("Input Error", "Barcode already exists in the given order. Please edit it if required.", "warning")
			return false;
		}
		if (barcode==e.barcode){
					if(quantity<=e.quantity){
							return true
					}
					else{
						sweetAlert("Inventory Underflow", "Quantity exceeds amount in inventory", "warning");
						return false
					}
		}
	} 
	sweetAlert("Input Error", "Brand and category does not exist", "warning");
	return false
}

function deleteOrderItem(id,barcode,quantity){
	var url2 = getOrderUrl() + "item/" + id + "/" + barcode
	$.ajax({
	   url: url2,
	   type: 'GET',
	   success: function(data) {
		var url = getOrderUrl() + "item" + "/" + data;
		console.log(data+" delete id")
		console.log(quantity)
		$.ajax({
		   url: url,
		   type: 'DELETE',
		   success: function(data1) {
				   console.log("Order Item deleted");
				if(data1=="javax.persistence.NoResultException: No entity found for query"){
					sweetAlert("Data Missing error", "The record seems to not exist in our database. Please refresh and try again.", "error");
					return false
				}
				var baseUrl = $("meta[name=baseUrl]").attr("content");
				url3 = baseUrl + "/api/inventory";
				$.ajax({
					url: url3,
					type: 'GET',
					success: function(data) {	
							increaseInventory(barcode,quantity,data);
					},
					error: function(){
						sweetAlert("Data loading error", "An error has occurred in getting the order list", "error");
					}
				});
		   },
		   error: function(error){
			console.log(error)
				   sweetAlert("Unknown error", "An error has occurred", "error");
				   
		   }
		});
	   },
	   error: function(){
		sweetAlert("Data Loading error", "An error has occurred in getting the Brand list", "error");
	   }
	});
}

function deleteRow(elem){
	elem.parentNode.parentNode.remove();
}

let slno = 1;

function DisplayEditOrder(event){
	var id = event.parentNode.parentNode.getElementsByTagName("td")[0].innerHTML;
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = baseUrl +"/api/order_item_by_order_id/" + id;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
				console.log("Order items report data fetched");   	
				editModal(data,id);
		},
		error: function(){
				sweetAlert("Invoice Printing error", "An error has occurred in getting order items invoice", "error");
		}
	 });
}


function editModal(data,id){
	$('#edit-order-item-modal').modal('toggle');
	console.log(data);
	var $tbody = $('#editorderitem-table').find('tbody');
	$tbody.empty();
	slno = 1;
	$("#edit-item-row").remove();
	for(var i in data){
		var e = data[i];
		var barcode = e.barcode;
		var quantity = e.quantity;
		var selling_price = e.selling_price;
		var buttonHtml = '<button class="btn btn-primary btn-sm btn-open" onclick="deleteOrderItem(\'' + id  +'\',\'' + barcode + '\',\'' + quantity + '\');deleteRow(this);">DELETE</button>'
		buttonHtml += ' <button class="btn btn-primary btn-sm btn-open" onclick="displayEditOrderItem(\'' + id  +'\',\'' + barcode + '\',\'' + quantity + '\',\'' + selling_price + '\')">EDIT</button>'
		$(".data-table-form2 tbody").append("<tr id='edit-item-row' order_id='"+id+"'  data-barcode='"+barcode+"' data-quantity='"+quantity+"' data-selling_price='"+selling_price+"' data-buttonHtml='"+buttonHtml+"'><td name='barcode[]'>"+barcode+"</td><td name='quantity[]'>"+quantity+"</td><td name='selling_price[]'>"+selling_price+"</td><td name='buttonHtml'>"+buttonHtml+"</td></tr>");
		$("input[name='']").val("");
		slno++;
	}
}

function displayEditOrderItem(id,barcode,quantity,selling_price){
	$("#order-edit-form input[name=barcode]").val(barcode);	
	$("#order-edit-form input[name=quantity]").val(quantity);	
	$("#order-edit-form input[name=selling_price]").val(selling_price);
	$("#order-edit-form input[name=id]").val(id);
	$('#order-edit-form input[name=barcode]').attr("data-oldbarcode",barcode)
	$('#order-edit-form input[name=quantity]').attr("data-oldquantity",quantity)

	var url = getOrderUrl() + "item/" + id + "/" + barcode
	console.log(url)
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("OrderItem ID fetched");
			$("#order-edit-form input[name=order_item_id]").val(data);
	   },
	   error: function(){
		sweetAlert("Data Loading error", "An error has occurred in getting the Brand list", "error");
	   }
	});
	$('#edit-order-modal').modal('toggle');
}

function updateOrderItem(){
	var oldbarcode = $('#order-edit-form input[name=barcode]').attr("data-oldbarcode");
	var oldquantity = $('#order-edit-form input[name=quantity]').attr("data-oldquantity");
	var order_item_id = $("#order-edit-form input[name=order_item_id]").val();
	var id = $("#order-edit-form input[name=id]").val();
	var barcode = $("#order-edit-form input[name=barcode]").val();
	var quantity = $("#order-edit-form input[name=quantity]").val();
	var selling_price = $("#order-edit-form input[name=selling_price]").val();
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	url2 = baseUrl + "/api/inventory";
	var flag = true;

	$('#edit-order-modal').modal('toggle');
	$('#edit-order-item-modal').modal('toggle');

	if(oldbarcode==barcode){
		updated_quantity = quantity-oldquantity;	
		$.ajax({
			url: url2,
			type: 'GET',
			async:false,
			success: function(data) {	
					if(checkOrder(data,barcode,updated_quantity)){
					reduceInventory(barcode,updated_quantity,data);
					}
			},
			error: function(){
				sweetAlert("Data loading error", "An error has occurred in getting the order list", "error");
			}
		});
	}
	else{
		$.ajax({
			url: url2,
			type: 'GET',
			async:false,
			success: function(data) {	
				var row_barcode = document.getElementsByName('barcode[]'); 
					flag=checkOrder2(data,barcode,quantity,oldbarcode,row_barcode);
					if(flag===true){
					reduceInventory(barcode,quantity,data);
					increaseInventory(oldbarcode,oldquantity,data);
					}

			},
			error: function(){
				sweetAlert("Data loading error", "An error has occurred in getting the order list", "error");
			}
		});
	}
	
	var url = getOrderUrl() +"item"+ "/" + order_item_id + "/";
	$form={"barcode":barcode,"order_id":id,"quantity":quantity,"selling_price":selling_price}

	console.log("flag in put is "+flag);

	if(flag===true){

	fetch(url, {
		method: 'PUT',
		headers: {
		  'Content-Type': 'application/json',
		},
		body: JSON.stringify($form),
	  })
	  .then(Response => {
		console.log('Success:', Response);
		sweetAlert("Order Item Updated Successfully","","success")
	  })
	  .catch((error) => {
		console.error('Error:', error);
	  });
	}
}

function AddItem(event){
	$('#add-order-modal').modal('toggle');
	var id = event.parentNode.parentNode.getElementsByTagName("tr")[1].attributes.order_id.value;
	$("#add-order-form input[name=add_order_id]").val(id);	
}

function AddItemToDB(event){
	var id = $("#add-order-form input[name=add_order_id]").val();
	$('#add-order-modal').modal('toggle');
	$('#edit-order-item-modal').modal('toggle');
	var barcode = $("#add-order-form input[name=barcode]").val();
	var quantity = $("#add-order-form input[name=quantity]").val();
	var selling_price = $("#add-order-form input[name=selling_price]").val();
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	url = baseUrl + "/api/inventory";

	$.ajax({
		url: url,
		type: 'GET',
		async:false,
		success: function(data) {	
				if(checkOrder3(data,barcode,quantity)){
					reduceInventory(barcode,quantity,data);
					var x =[{"barcode":barcode,"order_id":id,"quantity":quantity,"selling_price":selling_price}]
					var object2_orderitems = JSON.stringify(x)
								url2 = getOrderUrl()+"item";
									fetch(url2, {
										method: 'POST',
										headers: {
										'Content-Type': 'application/json',
										},
										body: object2_orderitems,
									})
									.then(response => {
												console.log('Success:', response);
												sweetAlert("Order Item Added Successfully","","success")
									})
									.catch((error) => {
										console.error('Error:', error);
									});
								}
						},
						error: function(){
							sweetAlert("Data loading error", "An error has occurred in getting the order list", "error");
						}
					});
					$("#add-order-form input[name=barcode]").val("");
	$("#add-order-form input[name=quantity]").val("");
	$("#add-order-form input[name=selling_price]").val("");
	
}


//adding each order item
$('form').submit(function(e){
	e.preventDefault();
	var barcode1=$(this).find("input[name='barcode']").val();
	var quantity=$(this).find("input[name='quantity']").val();
	var selling_price=$(this).find("input[name='selling_price']").val();
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	url = baseUrl + "/api/inventory" 
	var buttonHtml = '<button onclick="deleteRow(this)">DELETE</button>'

	var cond= document.getElementById("item-row") || false
	if(cond!=false){
		var row_barcode = document.getElementsByName('barcode[]'); 
		console.log(row_barcode)
		for (var m = 0; m < row_barcode.length; m++) { 
			var bf = row_barcode[m]; 
			if (bf.innerHTML == barcode1){
				sweetAlert("Input Data Error","This barcode has been entered already. Delete and add again if any changes must be made in it","error")
				return false;
			} 
		}
	}

	//checks whether the barcode entered is valid and whether quantity entered is correct
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {	
			if(checkOrder(data,barcode1,quantity)){
				$(".data-table-form tbody").append("<tr id='item-row' data-slno='"+slno+"' data-barcode1='"+barcode1+"' data-quantity='"+quantity+"' data-selling_price='"+selling_price+"' data-buttonHtml='"+buttonHtml+"'><td>"+slno+"</td><td name='barcode[]'>"+barcode1+"</td><td name='quantity[]'>"+quantity+"</td><td name='selling_price[]'>"+selling_price+"</td><td name='buttonHtml'>"+buttonHtml+"</td></tr>");
				$("input[name='']").val("");
				slno++;
			}
			else
			{
				sweetAlert("Input Data Error", "The entered Barcode does not exist in the database, please try again !", "error");
			}
		},
		error: function(){
			sweetAlert("Data loading error", "An error has occurred in getting the order list", "error");
		}
	 });

	
  });

function increaseInventory(barcode,quantity,data){
	for(var i in data){
		var e = data[i];
		if (barcode==e.barcode){
						var baseUrl = $("meta[name=baseUrl]").attr("content")
						url5 = baseUrl + "/api/order_update/"+barcode;
					
						var x = +e.quantity + +quantity;
						console.log(x+" is x");
						$form = {"barcode":barcode,"quantity":x}
						fetch(url5, {
							method: 'PUT',
							headers: {
							'Content-Type': 'application/json',
							},
							body: JSON.stringify($form),
						})
						.then(response => {
							console.log('Success:', response);
						})
						.catch((error) => {
							console.error('Error:', error);
						});
				}
			}
  }


function reduceInventory(barcode,quantity,data){
			console.log("Inside reduce inventory")
			for(var i in data){
				var e = data[i];
				if (barcode==e.barcode){
							if(quantity<=e.quantity){
								var baseUrl = $("meta[name=baseUrl]").attr("content")
								url5 = baseUrl + "/api/order_update/"+barcode;
							
								var x = e.quantity-quantity;
								console.log("x is"+x)
								$form = {"barcode":barcode,"quantity":x}
								fetch(url5, {
									method: 'PUT',
									headers: {
									'Content-Type': 'application/json',
									},
									body: JSON.stringify($form),
								})
								.then(response => {
									console.log('Success:', response);
									getOrderList();
								})
								.catch((error) => {
									console.error('Error:', error);
								});
	
							}
						}}}
						

//BUTTON ACTIONS
function addSubmitOrder(event){
	var cond= document.getElementById("item-row") || false
	console.log(cond);
	if(cond!=false){
			slno=1;
			// Set the values to update
			var barcode = document.getElementsByName('barcode[]'); 
			var quantity = document.getElementsByName('quantity[]'); 
			var selling_price = document.getElementsByName('selling_price[]');
			var currentdate = new Date(); 
			var datetime =  String(currentdate.getDate()).padStart(2, '0') + "-"
							+ String((currentdate.getMonth()+1)).padStart(2, '0')  + "-" 
							+ currentdate.getFullYear() + " @ "  
							+ currentdate.getHours() + ":"  
							+ currentdate.getMinutes() + ":" 
							+ currentdate.getSeconds();
			
			console.log(barcode)
			console.log(quantity)
			console.log(selling_price)
			var object1_order = JSON.stringify({"createDate": datetime});

			var url = getOrderUrl();

			fetch(url, {
				method: 'POST',
				headers: {
				'Content-Type': 'application/json',
				},
				body: object1_order,
			})
			.then((response) => {
				console.log('Success:',response);
				response.text().then((data)=>{
					var tcombined = [];
				for (var m = 0; m < barcode.length; m++) { /* Getting element */ 
					var bf = barcode[m]; 
					var df = quantity[m];
					var cf = selling_price[m]; 
					var combb=bf.innerHTML; 
					var combd=df.innerHTML; 
					var combc=cf.innerHTML;
					var x ={"barcode":combb,"order_id":data,"quantity":combd,"selling_price":combc}
					tcombined.push(x);
				}
					//getting the ID of the order
				var object2_orderitems = JSON.stringify(tcombined)
				url2 = url+"item";
					fetch(url2, {
						method: 'POST',
						headers: {
						'Content-Type': 'application/json',
						},
						body: object2_orderitems,
					})
					.then(response => {
								console.log('Success:', response);
								var baseUrl = $("meta[name=baseUrl]").attr("content")
								url = baseUrl + "/api/inventory"
								$.ajax({
									url: url,
									type: 'GET',
									success: function(data) {	
										tcombined.forEach((a) => {
											console.log(a);
											reduceInventory(a.barcode,a.quantity,data);
										})
										clear_table();
									},
									error: function(){
										sweetAlert("Data loading error", "An error has occurred in getting the order list", "error");
									}
								});
						
						getOrderList();
					})
					.catch((error) => {
						console.error('Error:', error);
					});

				})
				getOrderList();
			})
			.catch((error) => {
				console.error('Error:', error);
			});
		}
	else if(cond==false){
		sweetAlert("Input Data Error","Enter Order Items before submitting","warning")
	}


	$("#modal-Form").css("display", "none");
	$(".fade").css("display","none");
	$('.form-group :input').val("");
}

function getOrderList(){
	console.log("inside getOrderlist");
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Order table data fetched");
	   		console.log(data);	
	   		displayOrderList(data);
	   },
	   error: function(){
		sweetAlert("Data loading error", "An error has occurred in getting the order list", "error");
	   }
	});
}

//UI DISPLAY METHODS

function displayOrderList(data){
	console.log('Printing Order table data');
	var $tbody = $('#order-table').find('tbody');
	$('#order-table').dataTable().fnDestroy();
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button type="button" class="btn btn-primary btn-md btn-open" data-toggle="modal" data-target="#row-open" onclick="itemDisplay(this)">VIEW</button>'
		buttonHtml += ' <button type="button" class="btn btn-primary btn-md btn-open" data-toggle="modal" onclick="DisplayEditOrder(this)">EDIT</button>'
		buttonHtml += ' <button type="button" class="btn btn-primary btn-md btn-open" onclick="invoiceGenerator(this)">GENERATE INVOICE</button>'
		
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.createDate + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	createTable();
}

function createTable(){
	$('#order-table').DataTable({
		info:false,
		bDestroy: true,
		pageLength : 5,
		lengthMenu: [[5, 10, 20], [5, 10, 20]]
	});
	
}





function invoiceGenerator(event){
	var id = event.parentNode.parentNode.getElementsByTagName("td")[0].innerHTML;
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = baseUrl + "/api/order/report/" + id;

	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
				console.log("Order items report data fetched");
				console.log(data);  
				$.ajax({
				 url: getOrderUrl() + "/report/orderitemurl",
				 type: 'GET',
				 success: function(download) {
						 console.log("Order items report url fetched");
						 console.log(download);
						 window.open(download,'_blank');
				 },
				 error: function(){
						 sweetAlert("URL loading error", "An error has occurred in getting the order items url", "error");
				 }
			  });	 	
		},
		error: function(){
				sweetAlert("Invoice Printing error", "An error has occurred in getting order items invoice", "error");
		}
	 });
}

function itemDisplay(event){
	$('#del-row').remove();
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	var id = event.parentNode.parentNode.getElementsByTagName("td")[0].innerHTML
	console.log(id)
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	var url = baseUrl + "/api/order_item_by_order_id/" + id;

	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {	
		   console.log(data)
		   for (var i=0;i<data.length;i++){
			   console.log(i)
			var e = data[i];
			var row = '<tr id="del-row">'
			+ '<td>' + e.barcode + '</td>'
			+ '<td>' + e.quantity + '</td>'
			+ '<td>' + e.selling_price + '</td>'
			+ '</tr>';
			$('#data-items').append(row);
		   }
		},
	   error: function(){
			sweetAlert("Displaying order items error", "An error has occurred in displaying Order items", "error");
	   }
	});
}


//Inventory functions end here!!!


// INITIALIZATION CODE
function init(){
	console.log("Initialising")
	$('#refresh-order-data').click(getOrderList);
	$('#add-order').click(addSubmitOrder);
	$('#update-order-item').click(validateUpdate);
	$('#add-order-item').click(validate);
	
}

$(document).ready(init);
$(document).ready(getOrderList);