<!-- 
Shivam Agarwal
shivamag@buffalo.edu
UBIT 50289132
 -->
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<html>

<script type="text/javascript">
function purchase(itemID){
	document.getElementById('itemSelected').value = itemID;
	document.getElementById('buyForm').submit();
}

function loginPage(itemID){
	document.getElementById('itemSelected').value = itemID;
	var frm = document.getElementById('buyForm');
	frm.action = "loginPage";
	frm.submit();
}

function redirectToSell(){
	document.getElementById('userDirection').value = "sell";
	document.getElementById('buyForm').action = "loginPage";
	document.getElementById('buyForm').submit();
}

function purchaseTokens() {
	
	if(document.getElementById('montcoin').value.trim() != ""){
		document.getElementById('tokens').value = 1;
		document.getElementById('buyForm').submit();
	}else{
		alert('please enter valid data');
	}
	
}

function listProducts(){
	var frm = document.getElementById('buyForm');
	frm.action = "listProducts";
	frm.submit();
}

function deleteAccount(){
	var frm = document.getElementById('buyForm');
	frm.action = "deleteAccount";
	frm.submit();
}
</script>

<body>
	<jsp:include page="/title.jsp"></jsp:include>
	<br>
		${message}
	<br>
	<form name="buyForm" id="buyForm" action="buy">
		<input type="hidden" id="itemSelected" name="itemSelected" value="0"/>
		<input type="hidden" id="userDirection" name="userDirection"/>
		<%-- <table width="50%">
			<tr>
				<input type="hidden" id="itemSelected" name="itemSelected" value="0"/>
				<input type="hidden" id="tokens" name="tokens" value="0"/>
			</tr>
			<tr>
				<td><h1>Buy Token</h1></td>
				<td>
					<select name="currency" id="currency">
						<option>USD</option>
						<option>CAD</option>
						<option>AUD</option>
						<option>INR</option>
						<option>EUR</option>
					</select>
				</td>
				<td>
					<input type="text" id="montcoin" name="montcoin">
				</td>
				<td><input type="button" id="buyTokens" value="Buy Montcoins" name="buyTokens" onclick="javascript:purchaseTokens();"></td>
			</tr>
			<tr>
				
			</tr> 
			<tr>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table> --%>
		<h1>Buy Products</h1>
		<table width="75%">
			<thead style="font-weight:bold">
				<td width="15%" >Product ID</td>
				<td width="35%">Product Description</td>
				<td width="15%">Price</td>
				<td width="15%">Seller ID</td>
			</thead>
			<tbody>
				<c:forEach var="item" items="${products}">
					<tr>
						<td width="15%">${item.itemId}</td>
						<td width="35%">${item.details}</td>
						<td width="15%">${item.price}</td>
						<td width="15%">${item.seller}</td>
						<td width="20%">
							<c:choose>
								<c:when test="${empty customer}">
									<input type="button" id="${item.itemId}"
										onclick="javascript:loginPage(this.id);" value="Buy Now" />
								</c:when>
								<c:otherwise>
									<input type="button" id="${item.itemId}"
										onclick="javascript:purchase(this.id);" value="Buy Now" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					<tr></tr>
					<tr></tr>
				</c:forEach>
				
				<tr></tr>
				<tr></tr>
				<tr></tr>
			</tbody>
		</table>
		<br>
		<br>
		<table>
			<tbody>
				<tr>
					<td><input type="button" value="Sell your product" onclick="javascript:redirectToSell();" /></td>
					<td>
						<c:if test="${not empty customer}">
							<input type="button" value="orders placed" onclick="javascript:listProducts();" />	
						</c:if>
					</td>
					<td>
						<c:if test="${not empty customer}">
							<input type="button" value="Delete Account" onclick="javascript:deleteAccount();" />
						</c:if>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>