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
        
        alphanumeric : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-alphanumeric-0.1.1/jquery.alphanumeric.pack.js'></script>",
            loaded : false
        },
        
        jqueryAutocomplete : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.autocomplete-1.0.2/jquery.autocomplete.pack.js'></script>",
            dependencies : ["jqueryAutocomplete_css"],
            loaded : false
        },
        
        jqueryAutocomplete_css : {
            tag : "<link type='text/css' rel='Stylesheet' href='/core/scripts/jquery/jquery.autocomplete-1.0.2/jquery.autocomplete.css' />",
            loaded : false
        },
        
        jqGrid : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.jqGrid-4.3.1/js/jquery.jqGrid.min.js'></script>",
            dependencies : ["jqGrid_css", "jqGrid_locale", "jqGrid_multiselect", "json", "OrbisGrid"],
            loaded : false
        },
        
        jqGrid_css : {
            tag : "<link type='text/css' rel='stylesheet' href='/core/scripts/jquery/jquery.jqGrid-4.3.1/css/ui.jqgrid.css' />",
            loaded : false
        },

        jqGrid_locale : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.jqGrid-4.3.1/js/i18n/grid.locale-en.js'></script>",
            loaded : false
        },
        
        jqGrid_multiselect : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.jqGrid-4.3.1/plugins/ui.multiselect.js'></script>",
            dependencies : ["multiselect_css"],
            loaded : false
        },
        
        multiselect_css : {
            tag : "<link type='text/css' rel='stylesheet' href='/core/scripts/jquery/jquery.jqGrid-4.3.1/plugins/ui.multiselect.css' />",
            loaded : false
        },
        
        json : {
            tag : "<script type='text/javascript' src='/core/scripts/json/json2.js'></script>",
            loaded : false
        },

        OrbisGrid : {
            tag : "<script type='text/javascript' src='/core/scripts/orbis/orbisGrid/OrbisGrid.js'></script>",
            loaded : false
        },
        
        blockUI : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.blockUI-2.31/jquery.blockUI.js'></script>",
            loaded : false
        },
        
        form : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.form-2.43/jquery.form.js'></script>",
            loaded : false
        },
        
        calendar : {
            tag : "<script type='text/javascript' src='/core/scripts/calendar/calendar-setup.js'></script>",
            dependencies : ["calendar_css", "calendar_js", "calendar_en"],
            loaded : false
        },
        
        calendar_js : {
            tag : "<script type='text/javascript' src='/core/scripts/calendar/calendar.js'></script>",
            loaded : false
        },

        calendar_css : {
            tag : "<link type='text/css' rel='stylesheet' href='/core/scripts/calendar/calendar-blue.css' />",
            loaded : false
        },
        
        calendar_en : {
            tag : "<script type='text/javascript' src='/core/scripts/calendar/lang/calendar-en.js'></script>",
            loaded : false
        },
        
        /**
         * Note: JQuery Validate must be imported before the additional methods.
         */
        jqueryValidate : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-validation-1.8.1/additional-methods.min.js'></script>",
            dependencies : ["jqueryValidate_css", "jqueryValidateJS", "jqueryValidateOrbisSettings", "jqueryValidate_locale"],
            loaded : false
        },
        
        jqueryValidateJS : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-validation-1.8.1/jquery.validate.min.js'></script>",
            loaded : false
        },
        
        jqueryValidateOrbisSettings : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-validation-1.8.1/orbisValidateSettings.js'></script>",
            loaded : false
        },      
        
        jqueryValidate_locale : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-validation-1.8.1/localization/messages_en.js'></script>",
            loaded : false
        },

        jqueryValidate_css : {
            tag : "<link type='text/css' rel='stylesheet' href='/core/css/jquery-validate.css' />",
            loaded : false
        },
        
        jqueryTooltip : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-tooltip-1.3/jquery.tooltip.min.js'></script>",
            dependencies : ["bgiframe", "delegate", "dimentions"],
            loaded : false
        },
        
        bgiframe : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-tooltip-1.3/lib/jquery.bgiframe.js'></script>",
            loaded : false
        },
        
        delegate : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-tooltip-1.3/lib/jquery.delegate.js'></script>",
            loaded : false
        },
        
        dimentions : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-tooltip-1.3/lib/jquery.dimensions.js'></script>",
            loaded : false
        },
        
        tooltip : {
            tag : "<script type='text/javascript' src='/core/scripts/tooltip/form-field-tooltip.js'></script>",
            loaded : false
        },
        
        tooltipCorners : {
            tag : "<script type='text/javascript' src='/core/scripts/tooltip/rounded-corners.js'></script>",
            loaded : false
        },
        
        tooltip_css : {
            tag : "<link type='text/css' rel='stylesheet' href='/core/css/tooltip/form-field-tooltip.css' />",
            loaded : false
        },
        
        md5 : {
            tag : "<script type='text/javascript' src='/core/scripts/md5.js'></script>",
            loaded : false
        },
        
        FusionCharts : {
            tag : "<script type='text/javascript' src='/core/scripts/FusionCharts.js'></script>",
            loaded : false
        },
        
        keepAlive : {
            tag : "<script type='text/javascript' src='/core/scripts/orbis/keepAlive.js'></script>",
            loaded : false
        },
        
        zrssfeed : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/zrssfeed-101/jquery.zrssfeed.min.js'></script>",
            loaded : false
        },  
        
        tree : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-wdTree/src/Plugins/jquery.tree.modified.js'></script>",
            dependencies : ["tree_css", "treeTools"],
            loaded : false
        },
        
        tree_css : {
            tag : "<link type='text/css' rel='stylesheet' href='/core/scripts/jquery/jquery-wdTree/css/tree.css' />",
            loaded : false
        },
        
        treeTools : {
            tag : "<script type='text/javascript' src='/core/scripts/orbis/treeTools/treeTools.js'></script>",
            loaded : false
        },
        
        dragscrollable : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.dragscrollable-1.0/dragscrollable.js'></script>",
            loaded : false 
        },
        
        myideaTools : {
            tag : "<script type='text/javascript' src='/core/scripts/orbis/myidea/myidea_tools.js'></script>",
            loaded : false
        },
        
        colorPicker : {
            tag : "<script type='text/javascript'src='/core/scripts/jquery/jquery-colorpicker-1.4/colorpicker.js'></script>",
            dependencies : ["colorPicker_css", "colorPicker_layoutCss"],
            loaded : false
        },
        
        colorPicker_css : {
            tag : "<link rel='stylesheet' href='/core/scripts/jquery/jquery-colorpicker-1.4/css/colorpicker.css' type='text/css' />",            
            loaded : false
        },
        
        colorPicker_layoutCss : {
            tag : "<link rel='stylesheet' media='screen' type='text/css' href='/core/scripts/jquery/jquery-colorpicker-1.4/css/layout.css' />",
            loaded : false
        },
        
        hoverIntent : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.hoverIntent-r5/jquery.hoverIntent.minified.js'></script>",
            loaded : false
        },
        
        lightbox : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery-lightbox-0.5/js/jquery.lightbox-0.5.pack.js"></script>',
            dependencies : ["lightbox_css"],
            loaded : false
        },
        
        lightbox_css : {
            tag : '<link rel="stylesheet" type="text/css" href="/core/scripts/jquery/jquery-lightbox-0.5/css/jquery.lightbox-0.5.css" media="screen" />',
            loaded : false
        },
        
        uploadify : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery.uploadify-2.0.3/jquery.uploadify.v2.0.3.ForOch.min.js"></script>',
            dependencies : ["uploadify_css", "swfobject"],
            loaded : false
        },
        
        uploadify_css : {
            tag : '<link href="/core/scripts/jquery/jquery.uploadify-2.0.3/css/style.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        swfobject : {
            tag : '<script type="text/javascript" src="/core/scripts/swfobject_2_2.js"></script>',
            loaded : false
        },
    
        youtubin : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery.youtubin-1.2/jquery.youtubin.js"></script>',
            dependencies : ["swfobject"],
            loaded : false
        },

        simpleviewer : {
            tag : '<script type="text/javascript" src="/core/scripts/simpleviewer_pro_210/js/simpleviewer.js"></script>',
            dependencies : ["swfobject"],
            loaded : false
        },
        
        orbisSlideshow : {
            tag : '<script type="text/javascript" src="/core/scripts/orbis/orbisSlideshow/orbisSlideshow-2.js"></script>',
            dependencies : ["orbisSlideshow_css"],
            loaded : false
        },
        
        orbisSlideshow_css : {
            tag : '<link href="/core/scripts/orbis/orbisSlideshow/orbisSlideshow.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        clockPick : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery-clockpick-1.2.5/jquery.clockpick.1.2.5.min.js"></script>',
            dependencies : ["bgiframe", "clockPick_css"],
            loaded : false
        },
        
        clockPick_css : {
            tag : '<link href="/core/scripts/jquery/jquery-clockpick-1.2.5/clockpick.1.2.5.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },

        orbisChat : {
            tag : '<script type="text/javascript" src="/core/scripts/orbis/orbisChat/orbisChat.js"></script>',
            dependencies : ["orbisChat_css"],
            loaded : false
        },
        
        orbisChat_css : {
            tag : '<link href="/core/scripts/orbis/orbisChat/orbisChat.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        orbisRatingWidget : {
            tag : '<script type="text/javascript" src="/core/scripts/orbis/orbisRating/ratingWidget.js"></script>',
            dependencies : ["orbisRatingWidget_css"],
            loaded : false
        },
        
        orbisRatingWidget_css : {
            tag : '<link href="/core/scripts/orbis/orbisRating/ratingWidget.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        scrollTo : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery.scrollTo-1.4.2/jquery.scrollTo-min.js"></script>',
            loaded : false
        },
        
        printElement : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery.printElement-1.2/jquery.printElement.min.js"></script>',
            loaded : false
        },
        
        orbisDropdownMenu : {
            tag : '<script type="text/javascript" src="/core/scripts/orbis/orbisDropdownMenu/orbisDropdownMenu.js"></script>',
            dependencies : ["orbisDropdownMenu_css"],
            loaded : false
        },
        
        orbisDropdownMenu_css : {
            tag : '<link href="/core/scripts/orbis/orbisDropdownMenu/orbisDropdownMenu.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        jqueryTools : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery-tools-1.2.5/jquery.tools.min.js"></script>',
            loaded : false
        },

        highCharts : {
            tag : '<script type="text/javascript" src="/core/scripts/jquery/jquery.highcharts-2.2.1/js/highcharts.js"></script>',
            loaded : false
        },
        
        orbisLimitMaxChar: {
            tag : '<script type="text/javascript" src="/core/scripts/orbis/orbisInput/orbisLimitMaxChar.js"></script>',
            loaded : false
        },
        
        orbisLimitMaxCharDefault: {
            tag : '<script type="text/javascript" src="/core/scripts/orbis/orbisInput/orbisLimitMaxCharDefault.js"></script>',
            loaded : false
        },
        
        jqueryCluetip : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-cluetip/jquery.cluetip.min.js'></script>",
            dependencies : ["bgiframe", "jqueryCluetip_css", "hoverIntent_r6"],
            loaded : false
        },
        
        jqueryCluetip_css : {
            tag : '<link href="/core/scripts/jquery/jquery-cluetip/jquery.cluetip.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        hoverIntent_r6 : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.hoverIntent-r6/jquery.hoverIntent.minified.js'></script>",
            loaded : false
        },
        
        userDetailPreferences : {
            tag : "<script type='text/javascript' src='/core/scripts/orbis/userDetailPreferences/userDetailPreferences.js'></script>",
            dependencies : ["userDetailPreferences_css"],
            loaded : false
        },
        
        userDetailPreferences_css : {
            tag : '<link href="/core/css/userDetailPreferences/userDetailPreferences.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        jqueryEasingGsgd : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery-easing-gsgd/jquery.easing.1.3.js'></script>",
            loaded : false
        },
        
        tweet : {
            tag : "<script type='text/javascript' src='/core/scripts/twitter/jquery.tweet.js'></script>",
            dependencies : ["tweet_css"],
            loaded : false
        },
        
        tweet_css : {
            tag : '<link href="/core/scripts/twitter/jquery.tweet.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        agilityjs : {
            tag : "<script type='text/javascript' src='/core/scripts/jquery/jquery.agility-0.1.2/agility.min.js'></script>",
            loaded : false
        },
        
        fullCalendar : {
            tag: "<script type='text/javascript' src='/core/scripts/jquery/jquery.fullCalendar-1.5.3/fullcalendar/fullcalendar_en.min.js'></script>",
            dependencies : ["fullCalendar_css"],
            loaded : false
        },
        
        fullCalendar_css : {
            tag : '<link href="/core/scripts/jquery/jquery.fullCalendar-1.5.3/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        objectWatch : {
            tag: "<script type='text/javascript' src='/core/scripts/orbis/objectWatch.js'></script>",
            loaded : false
        },
        
        noDoubleTapZoom : {
            tag: "<script type='text/javascript' src='/core/scripts/jquery/jquery.mobileTouchOptions/mobileOptions.js'></script>",
            loaded : false
        },
        
        jquery_hotkeys : {
            tag: "<script type='text/javascript' src='/core/scripts/jquery/jquery.hotkeys/jquery.hotkeys.js'></script>",
            loaded : false
        },
        
        jquery_timePickerAddon : {
            tag: "<script type='text/javascript' src='/core/scripts/jquery/jquery-Timepicker-Addon/jquery-ui-timepicker-addon.js'></script>",
            dependencies : ["jquery_timePickerAddon_css"],
            loaded : false
        },

        jquery_timePickerAddon_css : {
            tag : '<link href="/core/scripts/jquery/jquery-Timepicker-Addon/jquery-ui-timepicker-addon.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        bootstrap : {
            tag: "<script type='text/javascript' src='/core/bootstrap/js/bootstrap.js'></script>",
            dependencies : ["bootstrap_css", "bootstrap_css_mods"],
            loaded : false
        }, 

        bootstrap_css : {
            tag : '<link href="/core/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },

        bootstrap_css_mods : {
            tag : '<link href="/core/bootstrap/css/bootstrap.mods.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },

        date_format : {
            tag: "<script type='text/javascript' src='/core/scripts/dateFormat/date.format.js'></script>",
            loaded : false
        },
        
        markdown : {
            tag: "<script type='text/javascript' src='/core/scripts/markdown/markdown.js'></script>",
            loaded : false
        },
        
        jquery_datePicker_fr : {
            tag: "<script type='text/javascript' src='/core/scripts/jquery/jquery-ui-1.8.18.custom/js/jquery.ui.datepicker-fr.js'></script>",
            loaded : false
        },

        dropdownMultiselect : {
            tag: "<script type='text/javascript' src='/core/scripts/jquery/jquery.multiselect-1.13/src/jquery.multiselect.min.js'></script>",
            dependencies : ["dropdownMultiselect_filter", "dropdownMultiselect_css",],
            loaded : false
        }, 
        
        dropdownMultiselect_filter : {
            tag: "<script type='text/javascript' src='/core/scripts/jquery/jquery.multiselect-1.13/src/jquery.multiselect.filter.min.js'></script>",
            dependencies : ["dropdownMultiselect_filter_css"],
            loaded : false
        }, 

        dropdownMultiselect_css : {
            tag : '<link href="/core/scripts/jquery/jquery.multiselect-1.13/jquery.multiselect.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        dropdownMultiselect_filter_css : {
            tag : '<link href="/core/scripts/jquery/jquery.multiselect-1.13/jquery.multiselect.filter.css" rel="stylesheet" type="text/css" />',
            loaded : false
        },
        
        accounting : {
            tag: "<script type='text/javascript' src='/core/scripts/accounting/accounting.min.js'></script>",
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
