<style>
	#repeatModal input[type=radio] {margin-top:-3px;}
	#rruleSummary {font-weight:bold;}
</style>

<script type="text/javascript" src="/assets/scripts/underscore/underscore-min.js"></script>
<script type="text/javascript" src="/assets/scripts/rrule/rrule.js"></script>
<script type="text/javascript" src="/assets/scripts/rrule/nlp.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$(":input", "#repeatModal form#repeatForm").change(refreshSummary);
		$(":input", "#repeatModal form#repeatForm").keyup(refreshSummary);
	});
	
	function refreshSummary() {
		var rule = new RRule(RRule.parseString(getRRuleFromForm()));
	    $("#rruleSummary", "#repeatForm").html(rule.toText());
	}
	
	function showRepeatModal(rrule)
	{
		if (rrule)
		{
			var repeatForm = $("form#repeatForm", "#repeatModal");
			var rrOptions = RRule.parseString(rrule);
			var rule = new RRule(rrOptions);
			
			// set freq input
			$("select[name=freq]", repeatForm).val(rrOptions.freq);
			
			// set interval input
			$("select[name=interval]", repeatForm).val(rrOptions.interval);
			
			// set byweekday checkboxes
			if (rrOptions.byweekday)
			{
				for (var i=0; i < rrOptions.byweekday.length; i++)
				{
					$("input[name=byweekday][value=" + rrOptions.byweekday[i].weekday + "]", repeatForm).prop("checked", true);
				}
			}
			
			// set 'ends' radios & fields
			$("input[name=ends]", repeatForm).prop("checked", false);
			$("input[name=endsCount]", repeatForm).val("");
			if (rrOptions.count)
			{
				// set 'ends after N occurrences' radios & fields
				$("input[name=ends][value=count]", repeatForm).prop("checked", true);
				$("input[name=endsCount]", repeatForm).val(rrOptions.count);
			}
			else
			{
				// set 'ends never' radio
				$("input[name=ends][value=never]", repeatForm).prop("checked", true);
			}
			
			// set 'rule summary' text
			$("#rruleSummary", repeatForm).html(rule.toText());
		}
		
		// display 'repeat rule modal'
		$("#repeatModal").modal("show");
	}
	
	function saveRepeat()
	{
		$("#repeatModal").modal("hide");
	
		var rule = new RRule(RRule.parseString(getRRuleFromForm()));
	    $("input[name=rrule]", "#eventForm").val(rule.toString());
	    $("#rruleText", "#eventForm").html(rule.toText());
	    $("a#rruleEditBtn", "#eventForm").show();
	}
	
	function getRRuleFromForm()
	{
		var repeatForm = $("form#repeatForm", "#repeatModal");
		var rrOptions = new Object();
		
		// bind freq
		rrOptions.freq = $("select[name=freq]", repeatForm).val();
		
		// bind interval
		rrOptions.interval = $("select[name=interval]", repeatForm).val();
		
		// bind byweekday choices
		var byweekday = new Array();
		$("input[name=byweekday]:checked", repeatForm).each(function(){
			byweekday.push($(this).val());
		});
		rrOptions.byweekday = byweekday;

		// bind 'ends' radio-button choices
		var ends = $("input[name=ends]:checked", repeatForm).val();
		if ("count" == ends)
		{
			var endsCount = $("input[name=endsCount]", repeatForm).val();
			if (app.isNormalInteger(endsCount))
			{
				rrOptions.count = endsCount;
			}
		}

		console.log(rrOptions);

		var rrule = new RRule(rrOptions);
		return rrule.toString();
	}

	function cancelRepeat()
	{
		$("#repeatModal").modal("hide");
	}

</script>

<div id="repeatModal" class="modal hide fade">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3>Repeat</h3>
	</div>
	<div class="modal-body">
		<form id="repeatForm">
			<table class="table table-condensed">
				<tr>
					<td>Repeats:</td>
					<td>
						<select name="freq">
							<option value="3">Daily</option>
							<option value="2">Weekly</option>
							<option value="1">Monthly</option>
							<option value="0">Yearly</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Repeat every:</td>
					<td>
						<select name="interval">
							<option>1</option>
							<option>2</option>
							<option>3</option>
							<option>4</option>
							<option>5</option>
							<option>6</option>
							<option>7</option>
							<option>8</option>
							<option>9</option>
							<option>10</option>
							<option>11</option>
							<option>12</option>
							<option>13</option>
							<option>14</option>
							<option>15</option>
							<option>16</option>
							<option>17</option>
							<option>18</option>
							<option>19</option>
							<option>20</option>
							<option>21</option>
							<option>22</option>
							<option>23</option>
							<option>24</option>
							<option>25</option>
							<option>26</option>
							<option>27</option>
							<option>28</option>
							<option>29</option>
							<option>30</option>
						</select> weeks
					</td>
				</tr>
				<tr>
					<td>Repeat on:</td>
					<td>
						<input type="checkbox" name="byweekday" value="6"> S &nbsp;
						<input type="checkbox" name="byweekday" value="0"> M &nbsp;
						<input type="checkbox" name="byweekday" value="1"> T &nbsp;
						<input type="checkbox" name="byweekday" value="2"> W &nbsp;
						<input type="checkbox" name="byweekday" value="3"> T &nbsp;
						<input type="checkbox" name="byweekday" value="4"> F &nbsp;
						<input type="checkbox" name="byweekday" value="5"> S &nbsp;
					</td>
				</tr>
				<tr>
					<td>Starts on:</td>
					<td>
						<input type="text">
					</td>
				</tr>
				<tr>
					<td>Ends:</td>
					<td>
						<label><input type="radio" name="ends" value="never"> Never</label> <BR>
						<label><input type="radio" name="ends" value="count"> After <input type="text" name="endsCount"> occurrences</label> <BR>
						<label><input type="radio" name="ends" value="until"> On <input type="text"></label> <BR>
					</td>
				</tr>
				<tr>
					<td><B>Summary:</B></td>
					<td id="rruleSummary"></td>
				</tr>
			</table>
		</form>
	</div>
	<div class="modal-footer">
		<a href="#" class="btn" onclick="cancelRepeat()">Close</a> 
		<a href="#" class="btn btn-primary" onclick="saveRepeat()">Save changes</a>
	</div>
</div>