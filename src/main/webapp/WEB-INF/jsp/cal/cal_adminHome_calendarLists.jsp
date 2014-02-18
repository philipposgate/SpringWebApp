
<%-- JAVASCRIPT FOR CALENDAR-LIST UI --%>
<script type="text/javascript">
	app.addComponent("agilityjs");
	app.addComponent("qtip");
	
	var calLists = ${jsonCalLists};

	$(document).ready(function() {
		for (var i = 0; i < calLists.length; i++)
		{
			$$.document.append($$(calListProto, calLists[i]), "div#calLists");
		}
	});
	
	var calListProto = $$({
		  model: {},
		  view: {
		    format: '<div>' +	
		                '<div class="calListHeader">' +
		   			    	'<div class="calListTitle" data-bind="title"></div>' +
		   			    	'<div class="calListCtl"></div>' +
		   			    	'<div class="clrFloat"></div>' +
		   			    '</div>' +
		   			    '<div class="calendars"></div>' +
		   		 	'</div>',
		    style: '& .calListHeader {padding:5px;} ' +
	    	   	   '& .calListHeader:hover {background-color:#EEE;} ' + 
	    	   	   '& .calListTitle {float:left;font-weight:bold;} ' + 
	    	   	   '& .calListCtl {float:right;} ' + 
	    	   	   '& .clrFloat {clear:both;} ' + 
	    	   	   '& .calendars {margin-left:20px;} '
		  },
		  controller: {
			  'create' : function() {
				  var menuModel = {
				 	id: this.model.get("id"),
				  	menuClass: ".calListMenu"
				  };
				  this.append($$(calendarMenuProto, menuModel), this.view.$(".calListCtl"));
				  
				  var cals = this.model.get("calendars");
				  if (cals)
				  {
					  for (var i = 0; i < cals.length; i++)
					  {
						  var calModel = cals[i];
						  calModel.calendarListId = this.model.get("id");
						  this.append($$(calendarProto, calModel), ".calendars");
					  }
				  }
			  }
		  }
		});	

	var calendarProto = $$({
		  model: {},
		  view: {
		    format: '<div>' +	
					    '<div class="calToggle"></div>' + 
		   			    '<div class="calTitle" data-bind="title"></div>' + 
		   			    '<div class="calCtl"></div>' + 
		   			    '<div class="clrFloat"></div>' +
		   		 	'</div>',
		    style: '& {padding:5px;}' +
		    	   '&:hover {background-color:#EEE;}' +
	    	   	   '& .calToggle {float:left;margin-right:5px;} ' + 
	    	   	   '& .calTitle {float:left;} ' + 
	    	   	   '& .calCtl {float:right;} ' + 
		    	   '& .clrFloat {clear:both;}' 
		  },
		  controller: {
			  create: function() {
				  if (this.model.get("id"))
				  {
					  var tmodel = {
							  id: this.model.get("id"),
							  color: this.model.get("color"),
							  visible: this.model.get("visible")
						  };
					  this.append($$(calendarToggleProto, tmodel), ".calToggle");
					  
					  var menuModel = {
	 				 	 id: this.model.get("id"),
	 				 	 calendarListId: this.model.get("calendarListId"),
						 menuClass: ".calMenu"
					  };
					  this.append($$(calendarMenuProto, menuModel), ".calCtl");
				  }
			  }
		  }
	});

	var calendarToggleProto = $$({
		  model: {visible:true},
		  view: {
		    format: '<div>' +	
		   		 	'</div>',
		    style: '& {border:2px solid;width:20px;height:20px;background-color:inherit;} ' +
		    	   '&:hover {cursor:pointer;} '
		  },
		  controller: {
			  create: function() {
				  this.view.$().css("border-color", this.model.get("color"));
				  this.setViewState();
			  },
			  
			  'click &' : function() {
				  var visible = this.model.get("visible");
				  this.model.set({visible: !visible});
				  this.setViewState();
				  this.save();
			  },
			  
			  'persist:save:success': function() {
				  refreshCalendar();
			  }
		  },
		  setViewState: function() {
			  if (this.model.get("visible"))
			  {
				  this.view.$().css("background-color", this.model.get("color"));
			  }
			  else
			  {
				  this.view.$().css("background-color", "inherit");
			  }
		  }
	}).persist($$.adapter.restful, {baseUrl: '/rest/calendar/${domain.id}/', collection: 'updateCalendarViz'});
	
	var calendarMenuProto = $$({
		  model: {},
		  view: {
		    format: '<div class="dropdown">' + 
		            	'<div class="menuBtn dropdown-toggle" data-toggle="dropdown"><b class="caret"></b></div>' + 
		            '</div>',
		    style: '& .menuBtn {padding:3px;} ' +
		    	   '& .menuBtn:hover {cursor:pointer;} '
		  },
		  controller: {
			  create: function() {
				  var menuClass = this.model.get("menuClass");
				  $(menuClass, "#calListMenuTemplates").clone().appendTo(this.view.$());
			  },
			  
			  'click .calListMenuCreateCal': function() {
				  app.buildForm({action: "displayCalendarEdit", calendarListId: this.model.get("id")}).submit();
			  },
			  
			  'click .calMenuSettings': function() {
				  app.buildForm({action: "displayCalendarEdit", calendarListId: this.model.get("calendarListId"), calendarId: this.model.get("id")}).submit();
			  }
		  }
	});

</script>

<div id="calLists"></div>

<div id="calListMenuTemplates" style="display:none;">
	<ul class="calListMenu dropdown-menu" role="menu" aria-labelledby="dLabel">
		<li><a class="calListMenuCreateCal" role="menuitem" tabindex="-1" href="javascript:void(0)">Create New Calendar</a></li>
		<li><a class="calListMenuSettings" role="menuitem" tabindex="-1" href="javascript:void(0)">Settings</a></li>
	</ul>
	<ul class="calMenu dropdown-menu" role="menu" aria-labelledby="dLabel">
		<li><a class="calMenuSettings" role="menuitem" tabindex="-1" href="javascript:void(0)">Calendar Settings</a></li>
	</ul>
</div>
