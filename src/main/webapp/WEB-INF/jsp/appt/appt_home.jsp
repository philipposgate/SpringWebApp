<%@ include file="/WEB-INF/jsp/include.jsp"%>

<script type="text/javascript">
	app.addComponent("jqueryValidate");
	app.addComponent("bootstrapDatepicker");
	app.addComponent("dateFormat");
	app.addComponent("timeOptions");
	app.addComponent("momentjs");
	var apptLength = ${apptLengthMinutes};

	$(document).ready(function() {
		initDatePicker();
		initTimePicker();
		initPersonsPicker();
		initLocationPicker();
		$("#apptForm").validate();
		$("input#name").focus();
	});
	
	function initTimePicker()
	{
		$("#apptTime").timeOptions({start:"0900",end:"1800",increment:"15"});
		$("#apptTime").change(calcApptFinish);
		$("#apptTime").change();
	}
	
	function initPersonsPicker()
	{
		$("#unitAmount").change(function(){
			var duration = "";
			var mins = apptLength * $(this).val();
			if (mins > 60)
			{
				var hrs = Math.floor(mins / 60);
				var min = Math.floor(mins - (hrs * 60));
				duration = hrs + (hrs == 1 ? " hour " : " hours ");
				if (min > 0)
				{
					duration += min + (min == 1 ? " minute" : " minutes");
				}
			}
			else
			{
				duration = mins + (mins == 1 ? " minute" : " minutes");
			}
			$("#timeEstimate").text("Approximately " + duration);
			calcApptFinish();
		});
		$("#unitAmount").change();
	}
	
	function calcApptFinish()
	{
		var time = moment($("#apptTime").val(), "HHmm");
		var unitAmount = $("#unitAmount").val();
		var length = apptLength * unitAmount;
		var fee;
		if (unitAmount < 5)
		{
			 fee = (unitAmount * 45 * 1.13).toFixed(2);
		}
		else
		{
			fee = (unitAmount * 45 * 0.8 * 1.13).toFixed(2);
		}
		time.add("minutes", length);
		
		$("#apptFinish").text("Around " + time.format("h:mm a"));
		$("#apptFee").text("$" + fee + " (taxes included)");
	}
	
	function initLocationPicker()
	{
		$("#locationCode").change(function(){
			if ($(this).val() == "otherLoc")
			{
				$("#locationAddress").show();
				$("#defaultAddress").hide();
				$("#locAddress").focus();
			}
			else
			{
				$("#locationAddress").hide();
				$("#defaultAddress").show();
			}
		});	
	}
	
	function initDatePicker () {
		var $apptDatePicker = $("#apptDatePicker"); 
		var $apptDateBtn = $("#apptDateBtn"); 
		
		$apptDatePicker.datepicker({
			todayBtn: "linked",
		    todayHighlight: true,
		    startDate: new Date()
		}).on("changeDate", closeApptDatePicker);
		
		$apptDatePicker.hide();
		$apptDateBtn.val($apptDatePicker.datepicker("getDate").format("fullDate"));
		
		$apptDateBtn.on("click", function(){
			$apptDatePicker.show();
			$apptDateBtn.hide();
		});
		
		$("html").on("click keyup", function(event) {
		    if (event.target.id != "apptDateBtn" && $(event.target).parents().index($apptDatePicker) == -1 && $apptDatePicker.is(":visible")) {
	        	closeApptDatePicker();
		    }        
		});
		
		$("input[name=apptDate]").val($apptDatePicker.datepicker("getDate").format("mm/dd/yyyy"));
	}
	
	function closeApptDatePicker () {
		var $apptDatePicker = $("#apptDatePicker"); 
		var $apptDateBtn = $("#apptDateBtn"); 
		$apptDatePicker.hide();
		$apptDateBtn.val($apptDatePicker.datepicker("getDate").format("fullDate"));
		$apptDateBtn.show();
		$("input[name=apptDate]").val($apptDatePicker.datepicker("getDate").format("mm/dd/yyyy"));
	}
</script>

<h2>Book Your Appointment</h2>

<div class="well">
	<div class="alert alert-info">
		<B>Fill out the following form to book your appointment.</B>
	</div>

	<form id="apptForm" class="form-horizontal" action="/appts/bookAppt/" method="POST">
	
		<div class="darkWell">
			<div class="control-group">
				<label class="control-label" for="customerName"><B>Name</B></label>
				<div class="controls">
					<input type="text" id="customerName" name="customerName" class="required" style="width:300px;" maxlength="255">
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label" for="customerPhone"><B>Phone Number</B></label>
				<div class="controls">
					<input type="text" id="customerPhone" name="customerPhone" class="required" style="width:300px;" maxlength="255">
				</div>
			</div>
	
			<div class="control-group">
				<label class="control-label" for="customerEmail"><B>Email Address</B></label>
				<div class="controls">
					<input type="text" id="customerEmail" name="customerEmail" class="required email" style="width:300px;" maxlength="255">
				</div>
			</div>
		</div>
	
		<div class="darkWell">
			<div class="control-group">
				<label class="control-label"><B>Appointment Date</B></label>
				<div class="controls">
					<input type="hidden" name="apptDate" value="">
					<input type="button" id="apptDateBtn" class="btn btn-large">
					<div id="apptDatePicker"></div>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="apptTime"><B>Appointment Time</B></label>
				<div class="controls">
					<select id="apptTime" name="apptTime" class="required" style="width:auto;"></select>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="unitAmount"><B>Number of Customers</B></label>
				<div class="controls">
					<select id="unitAmount" name="unitAmount" style="width:auto;">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						<option value="6">6</option>
						<option value="7">7</option>
						<option value="8">8</option>
						<option value="9">9</option>
						<option value="10">10</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="locationCode"><B>Appointment Location</B></label>
				<div class="controls">
					<select id="locationCode" name="locationCode" class="required" style="width:auto;">
						<option value="defaultLoc">at Tantalize Tan Spa </option>
						<option value="otherLoc">at another location </option>
					</select>
					<div id="defaultAddress" style="margin-top:25px;">
						
						<B>Tantalize Tan Spa</B>
						<br>211 Martindale Cres
						<br>St Catherines, Ontario
						<br>(905) 688-6165
					
					</div>
					<div id="locationAddress" style="display:none;margin-top:25px;">
						
						<P><B>Please Provide Your Location</B></P>
					
						<table cellpadding="5">
							<tr>
								<td><label for="locAddress">Street Address</label></td>
								<td><input type="text" id="locAddress" name="locAddress" class="required" style="width:300px;"></td>
							</tr>
							<tr>
								<td><label for="locCity">City</label></td>
								<td><input type="text" id="locCity" name="locCity" class="required"></td>
							</tr>
						</table>				
					</div>
				</div>
			</div>
		</div>

		<div class="darkWell">
			<div class="control-group">
				<label class="control-label"><B>Appointment Duration</B></label>
				<div class="controls" style="padding-top:5px;">
					<span id="timeEstimate"></span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label"><B>Appointment Finish</B></label>
				<div class="controls" style="padding-top:5px;">
					<span id="apptFinish"></span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label"><B>Appointment Fee</B></label>
				<div class="controls" style="padding-top:5px;">
					<span id="apptFee"></span>
				</div>
			</div>
		</div>

		<div class="control-group darkWell">
			<label class="control-label" for="customerMessage"><B>Special Requests and/or Instructions</B></label>
			<div class="controls">
				<textarea id="customerMessage" name="customerMessage" style="width:470px; height:180px;"></textarea>
			</div>
		</div>

		<div class="darkWell" style="text-align:center;"><input class="btn btn-primary btn-large" type="submit" value="Book Appointment"></div>	
	
	</form>
		
</div>
