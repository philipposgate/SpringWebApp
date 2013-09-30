<%@ include file="/WEB-INF/jsp/include.jsp"%>

<center>
	<h2>Appointment Booking Received</h2>
</center>

<div class="well">
	<div class="alert alert-info">
		<P><B>Thank you!</B> Your appointment booking has been received.</P>  
		<div style="font-size:1.3em;">
			<B>IMPORTANT!</B> 
			<br>You will receive a confirmation email shortly.  You must click the <b>I confirm my appointment</b> link in this email to complete the booking process.
		</div>
	</div>
	
	<div style="font-weight:bold;font-size:1.5em;margin:25px 0px 10px 0px;">Booking Details</div>
	<table class="table table-condensed table-striped">
		<col width="25%">
		<tr>
			<td>Confirmation Code</td>
			<td><span style="font-size:1.8em;"><B>${appt.confirmationCode}</B></span></td>
		</tr>
		<tr>
			<td>Customer Name</td>
			<td><B>${appt.customerName}</B></td>
		</tr>
		<tr>
			<td>Customer Phone</td>
			<td><B>${appt.customerPhone}</B></td>
		</tr>
		<tr>
			<td>Customer Email</td>
			<td><B>${appt.customerEmail}</B></td>
		</tr>
		<tr>
			<td>Appointment Date</td>
			<td><B><fmt:formatDate pattern="EEEE, MMMM dd, yyyy" value="${appt.apptStart}" /></B></td>
		</tr>
		<tr>
			<td>Appointment Time</td>
			<td><B><fmt:formatDate pattern="h:mm a" value="${appt.apptStart}" /> to ~<fmt:formatDate pattern="h:mm a" value="${appt.apptEnd}" /></B></td>
		</tr>
		<tr>
			<td>Number of Customers</td>
			<td><B>${appt.unitAmount}</B></td>
		</tr>
		<tr>
			<td>Appointment Location</td>
			<td>
				<c:if test="${appt.locationCode == 'defaultLoc'}">
					<B>Tantalize Tan Spa</B>
					<br>211 Martindale Cres
					<br>St Catherines, Ontario
					<br>(905) 688-6165
				</c:if>
				<c:if test="${appt.locationCode == 'otherLoc'}">
					<B>Customer's Location @</B>
					<br>${appt.locAddress}
					<br>${appt.locCity}
				</c:if>
			</td>
		</tr>
		<tr>
			<td>Appointment Fee</td>
			<td>
				<B>$$$$ (taxes included)</B>
			</td>
		</tr>
		<c:if test="${not empty appt.customerMessage}">
			<tr>
				<td>Special Requests and/or Instructions</td>
				<td>${fn:replace(appt.customerMessage, newLineChar,"<BR>")}</td>
			</tr>
		</c:if>
	</table>
		
</div>

<center>
	<a href="/" class="btn btn-large btn-primary">Return to Home Page</a>
</center>
