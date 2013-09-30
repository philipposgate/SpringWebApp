/**
 * Usage: jquery-select an empty <select> tag, like so...
 * 
 * $("select#timeSelector").timeOptions({start:"0900",end:"1800",increment:"15"});
 * 
 * OPTIONS:
 * start: HHmm
 * end: HHmm
 * increment: mins (integer)
 */
$.fn.timeOptions = function(opts) {
	app.addComponent("momentjs");
	var time = moment(opts.start, "HHmm");
	var end = moment(opts.end, "HHmm");
	var html = "";
	while (time.isBefore(end))
	{
		html += '<option value="' + time.format("HHmm") + '">' + time.format("h:mm a") + '</option>';
		time.add("minutes", opts.increment);
	}
	$(this).html(html);
}