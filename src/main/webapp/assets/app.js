$(document).ready(function() {
       app.hideFadeouts();
});  

/**
 * Used by the "htmlEditor" JQuery extension (see below)
 */
var htmlEditorPoller;

var app = {

    /**
     * "addComponent" function is called by the JSP to dynamically 
     * inject a component from "this.components" in the the <head>
     */
    addComponent : function(component) {
        var c = this.components[component];
        
        if (c)
        {
            if (c.loaded === false)
            {
                if(c.dependencies)
                {
                    for(var i = 0; i < c.dependencies.length; i++)
                    {
                        this.addComponent(c.dependencies[i]);               
                    }
                }           
    
                $("head").append(c.tag);            
                c.loaded = true;
            }
        }
        else
        {
            this.alertDialog("Error -> app.addComponent() -> Component '" + component + "' is undefined.", true, 300);
        }               
    }, 
        
    /**
     * This is the global map of all the available "components" that a JSP can choose from. 
     */
    components : {

        json : {
            tag : "<script type='text/javascript' src='/core/scripts/json/json2.js'></script>",
            loaded : false
        },
        
        jqueryValidate : {
            tag: "<script type='text/javascript' src='/assets/scripts/jquery-validation-1.11.1/additional-methods.min.js'></script>",
            dependencies : ["jqueryValidate_core"],
            loaded : false
        },
        
        jqueryValidate_core : {
            tag: "<script type='text/javascript' src='/assets/scripts/jquery-validation-1.11.1/jquery.validate.min.js'></script>",
            loaded : false
        }
        
    },
    
    
    hideFadeouts: function() {
        $(".successFadeout").addClass("alert");
        $(".successFadeout").addClass("alert-success");
        $(".successFadeout").prepend("<button type='button' class='close' data-dismiss='alert'>×</button>");
        $(".successFadeout:visible").stepDelay(5000, function() {$(this).fadeOut("slow");});

        $(".errorFadeout").addClass("alert");
        $(".errorFadeout").addClass("alert-error");
        $(".errorFadeout").prepend("<button type='button' class='close' data-dismiss='alert'>×</button>");
        $(".errorFadeout:visible").stepDelay(5000, function() {$(this).fadeOut("slow");});
    },    
   
    /**
     * A utility for turning any <div> into a standard dialog.  Once 
     * this method is called, the target div will be turned into a dialog
     * but you are still required to call $(divClassOrId).dialog("open");
     * and $(divClassOrId).dialog("close"); to open and close the dialog
     * respectively.
     * 
     * @param divClassOrId - a JQuery selector to your target div
     * @param buttons - a JQuery dialog button object (for custom buttons)
     * @param opts - a JQuery dialog options object (for custom dialog behaviour)
     */
    setUpRegularDialog : function (divClassOrId, buttons, opts)
    {
        if($(divClassOrId).is(':data(dialog)'))
        {
            $(divClassOrId).dialog("destroy");
        }

        this.resetDialog(divClassOrId, this.extendOptions(opts));               
        
        if(buttons && buttons != null)
        {
            $(divClassOrId).dialog('option', 'buttons', buttons);
        }   
    },

    /**
     * A "private" function use for supporting dialog functionality.
     */
    resetDialog : function (dialogDiv, options)
    {
        var dName;
        if (dialogDiv.substr(0, 1) == "#")
        {
            dName = $(dialogDiv).attr("id");
        }
        else if (dialogDiv.substr(0, 1) == ".")
        {
            dName = $(dialogDiv).attr("class");
        }
        else 
        {
            dName = dialogDiv;
        }
    
        var showTitle = $(dialogDiv).attr("title") != undefined && $(dialogDiv).attr("title") != null && $(dialogDiv).attr("title") != "";
    
        if (options)
        {
            $(dialogDiv).dialog(options);
        }
        else
        {
            $(dialogDiv).dialog(this.defaultDialogOpts);
        }   
        
        var thisDialog = $("div[aria-labelledby='ui-dialog-title-" + dName + "']");
    
        if (!showTitle)
        {       
            $(thisDialog).find(".ui-dialog-titlebar").css({"display" : "none"});
        }
        else
        {
            $(thisDialog).find(".ui-dialog-titlebar").css({"display" : "block"});
        }
    
        $(dialogDiv).dialog('option', 'open', function() { 
            $(this).css({'max-height': ($(window).height()-160), 'overflow-y':'auto'}); 
            $('.orbisDialog').css({position:"fixed", "top" : "40px", left : "40px"});
            if (options && options.open)
            {
                options.open();
            }
        });
        
        $(dialogDiv).dialog('option', 'buttons', {"Close": function(){ $(dialogDiv).dialog("close");}});        
        $(dialogDiv).dialog('option', 'closeOnEscape', true );          
    },

    /**
     * A "private" function use for supporting dialog functionality.
     */
    extendOptions : function (options)
    {
        var extendableOpts = new Object();
        $.extend(extendableOpts, this.defaultDialogOpts);
        if (options)
        {
            return $.extend(extendableOpts, options);
        }
        return extendableOpts;
    },

    /**
     * "orbisApp.checkAjaxResponse" is a utility method that can be used for 
     * checking an ajax response for error conditions.  If such a 
     * condition is detected then this method will perform the 
     * appropriate UI behaviour, and also return FALSE which the 
     * caller can use to stop normal application flow.
     *
     * @param xmlHttpRequest<XmlHttpRequest> - the ajax-object used to perform the ajax request.
     * @return <boolean> - FALSE if there was a problem, otherwise TRUE
     */
    checkAjaxResponse : function (xmlHttpRequest)
    {
        var happy = true;
        
        if (this.isEmpty(xmlHttpRequest))
        {
            happy = false;
            this.alertDialog("There has been a communication error with the server(1).  Please try again.", true, 300);         
        }
        else
        {
            if (!this.isEmpty(xmlHttpRequest.getResponseHeader("notLoggedIn")))
            {
                happy = false;
                window.parent.location = "/notLoggedIn.htm";
            }
            else if (!this.isEmpty(xmlHttpRequest.getResponseHeader("portalError")))
            {
                happy = false;
                window.parent.location = "/portalError.htm";
            }
            else if (this.isEmpty(xmlHttpRequest.status))
            {
                if(!unloadedProperly)
                {
                    happy = false;
                    this.alertDialog("There has been a communication error with the server(2).  Please try again.", true, 300);
                }
            }
            else if (xmlHttpRequest.status != 200)
            {
                happy = false;
                this.alertDialog("There has been a communication error with the server(3).  Please try again.", true, 300);         
            }
        }
        
        return happy;
    },
    
    /**
     * A utility for showing a standard "alert dialog"
     * 
     * @param message<string> - the text to be displayed in the dialog
     * @param error<boolean> - when TRUE, the dialog will be styled as an "error alert".
     * @param width<number>[optional] - the pixel-width of the dialog.
     */
    alertDialog : function (message, error, width, callback)
    {   
        var dName = this.createDialogDiv();
        $("#" + dName).attr("title", "Alert");
        var opts = {"close": function() {if($("#" + dName).is(':data(dialog)')){$("#" + dName).dialog("destroy");}}};   
        this.resetDialog("#" + dName, this.extendOptions(opts));
        var thisDialog = $("div[aria-labelledby='ui-dialog-title-" + dName + "']");
        
        if (width)
        {
            width = width + "px";
        }
        else
        {
            width = '';
        }
                    
        $("#" + dName).dialog('option', 'width', width);
        $("#" + dName).dialog('option', 'buttons', '');
        $("#" + dName).dialog('option', 'buttons', {
                                    "OK": function(){ 
                                        $("#" + dName).dialog("close");
                                        if(callback)
                                        {
                                            callback();
                                        }
                                    }                                                           
                                    });
        $("#" + dName).html("<div id='centerThis'>" + message + "</div>");      
        $("#" + dName).dialog("open");
        
        if(error)
        {
            $("#" + dName).addClass("ui-state-error");
            $(thisDialog).find(".ui-dialog-title").css({"color":"#FFFFFF"});        
            $(thisDialog).find(".ui-dialog-titlebar").css({"background":"#B90101"});
        }
                                    
        $(thisDialog).find(".ui-dialog-title").css({"width":"100%", "text-align":"center"});
        $(thisDialog).find(".ui-dialog-title").css({"display" : "block", "padding-left":"0px", "padding-right":"0px"});//display title bar  
        $(thisDialog).find(".ui-dialog-buttonpane button").css({"float":"none"}); //these 2 lines will hack the default UI settings and make the buttons center
        $(thisDialog).find(".ui-dialog-buttonpane").css({"text-align":"center", "padding":"0px"});
        
        this.centerDiv("#" + dName, "#centerThis");
        
        return "#" + dName;     
    },
    
    /**
     * Returns TRUE if obj is "empty", otherwise returns FALSE
     */
    isEmpty : function (obj)
    {
        var empty = false;
        
        if (typeof obj == "undefined" || obj == null || obj == "")
        {
            empty = true;
        }
        
        return empty;
    },
    
    /**
     * Returns the specified "str" without the left or right
     * "chars".
     */
    trim : function (str, chars) {
        return this.ltrim(this.rtrim(str, chars), chars);
    },
     
    /**
     * Returns the specified "str" without the left "chars".
     */
    ltrim : function (str, chars) {
        chars = chars || "\\s";
        return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
    },
     
    /**
     * Returns the specified "str" without the right "chars".
     */
    rtrim : function (str, chars) {
        chars = chars || "\\s";
        return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
    },
    
    /*********** DIALOG STUFF **************/
    
    numOfDialogs : 0,
    
    defaultDialogOpts : {
        resizable: false,
        draggable: true,
        modal: true,
        autoOpen: false,
        dialogClass: "orbisDialog",
        width: 600
    },

    buildForm : function(parameters, action, method) {
        app.addComponent("json");
        
        var theForm = $(document.createElement("form"));
        
        if (action)
        {
            theForm.attr("action", action);
        }
        
        if (method)
        {
            theForm.attr("method", method);
        }
        else
        {
            theForm.attr("method", "POST");
        }
        
        $.each(parameters, function(name, value){
            if(Object.prototype.toString.call(value) === '[object Array]')
            {
                $.each(value, function(arrayIndex, arrayValue){
                    theForm.append($(document.createElement("input")).attr({
                        type : "checkbox",
                        name : name,
                        value : arrayValue,
                        checked : "checked"
                    }).css("display", "none"));
                });
            }
            else if(Object.prototype.toString.call(value) === '[object Object]')
            {
                theForm.append($(document.createElement("input")).attr({
                    type : "hidden",
                    name : name,
                    value : JSON.stringify(value)
                }));
            }
            else
            {
                theForm.append($(document.createElement("input")).attr({
                    type : "hidden",
                    name : name,
                    value : value
                }));
            }
        });
        
        theForm.append($(document.createElement("input")).attr({
            type : "hidden",
            name : "rand",
            value : Math.floor(Math.random() * 100000)
        }));
        
        $(theForm).appendTo("body");
        return theForm;
    },
    
    /**
     * Poll each ckeditor in the current page.  
     * For each editor using our customer 'maxChars' feature, display the "characters remaining" to the user.
     */
    pollHtmlEditors: function()
    {
        for (var editorId in CKEDITOR.instances)
        {
            var editor = CKEDITOR.instances[editorId];
            
            if (editor.maxChars)
            {
                var data = editor.getData();

                // Update the counter display...
                var counterId = "#charCount_" + editorId;
                $(counterId).html(editor.maxChars - data.length);

                if (editor.maxChars < data.length)
                {
                    // Color counter red...
                    $(counterId).css({"color":"red"});
                    
                    // Truncate content to "maxChars - 20"...
                    editor.setData(data.substr(0, editor.maxChars - 20), function(){
                        // Calls to setData() will cause loss of focus on the editor.
                        // Therefore, re-establish focus...
                        this.focus();

                        // However, calls to focus() will only put the cursor at the beginning of the editor
                        // Therefore, reposition the cursor to the "end of content"...
                        var s = this.getSelection(); 
                        var selected_ranges = s.getRanges(); 
                        var node = selected_ranges[0].startContainer; 
                        var parents = node.getParents(true);
                        node = parents[parents.length - 2].getFirst();
                        while (true) {
                            var x = node.getNext();
                            if (x == null) {
                                break;
                            }
                            node = x;
                        }
                        s.selectElement(node);
                        selected_ranges = s.getRanges();
                        selected_ranges[0].collapse(false);  
                        s.selectRanges(selected_ranges); 
                    });
                }
                else
                {
                    // Color counter green...
                    $(counterId).css({"color":"green"});
                }
            }   
        }   
    }
}; 

$.fn.stepDelay = function(time, callback){
    $.fx.step.delay = function(){};
    return this.animate({delay:1}, time, callback); //the callback is the function that will wait for the delay to run before it executes
};

$.fn.htmlEditor = function(useFinder, toolbar, height, width, locale, maxChars) {

    var config = {};

    if (navigator.userAgent.indexOf("Firefox") == -1)
    {
        // All browsers, besides FF, need this config to correctly copy & paste from MSWord
        config.forcePasteAsPlainText = true;
    }

    if (toolbar)
    {
        config.toolbar = toolbar;
    }
    if (height)
    {
        config.height = height;
    }
    if (width)
    {
        config.width = width;
    }
    if (locale)
    {
        config.language = locale;
    }

    return $(this).htmlEditorByConfig(config, useFinder, maxChars);
};

$.fn.htmlEditorByConfig = function(config, useFinder, maxChars) {
    $(this).ckeditor(config);
    var editor = $(this).ckeditorGet();
    
    if (useFinder)
    {
        CKFinder.setupCKEditor(editor, "/assets/ckfinder/");
    }
    
    // maxChars behaviour...
    if ($.isNumeric(maxChars))
    {
        editor.maxChars = maxChars;
        
        var counterId = "charCount_" + $(this).prop("id");
        var counter = "<div style='font-size:x-small;'>MAX CHARS: <b>" + maxChars + "</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; CHARS REMAINING: <span id='" + counterId + "' style='font-weight:bold;'></span></div>";
        $(counter).insertAfter(this);
        
        if (!htmlEditorPoller)
        {
            htmlEditorPoller = setInterval("app.pollHtmlEditors()", 1000);
        }
    }
    
    return editor;
};
