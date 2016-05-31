/*
Init the page Javascript
*/
var currentStep = 0, currentId = "mac";
$(function() {

    // Init the Search Filter Button
    //
    if($.cookie("adult-search-filter") == "on") {
        $(".adult-search").removeClass("btn-info").addClass("active btn-success");
        $(".adult-search-text").html("Filter On");
        $(".search-bar-filter").attr("data-original-title", p.filteron);
    } else {
        $(".adult-search-text").html("Filter Off");
        $(".search-bar-filter").attr("data-original-title", p.filteroff);
    }
    
    $('.search-bar-filter').tooltip({html:true,placement:"bottom"});
    
    // clean up some IE8 bugs in bootstrap 3.0
    //
    if($.browser.msie && ($.browser.version == "8.0" || $.browser.version == "7.0" || $.browser.version == "6.0")) {

        if($(".search-btn")[0].innerHTML != "Search") {
            $(".search-btn").css("background-image", "url('/s/img/search-icon.png')").css("background-repeat", "no-repeat").css("background-position", "center center").html("");
        }
    }
    
    if(location.pathname.split("/")[1] == "search" && p.page == "0" && p.image == true) {
        ext();
    }

    if(location.pathname.split("/")[1] == "parked") {
        parkedSetup();
    }
    
    if(location.pathname.split("/")[1] == "image") {
        $('.image-results-list-item').tooltip({html:true});
    }
    
    if(location.pathname.split("/")[1] == "news") {

        FirstRow = $(".news-row")[0];
        LinkHref = $(FirstRow).find("a").attr("href");

        $.get("/news-image/?q=" + LinkHref, function(ImgUrl) {

            if (ImgUrl.length == 0) {
                return;
            }

            $(FirstRow).addClass("hidden");
        
            $(".top-row-img").attr("src", ImgUrl);
            $(".top-row-link").attr("href", LinkHref);

            $(".top-row-title").text($(FirstRow).find(".link-title").text());
            $(".top-row-text").text($(FirstRow).find(".link-text").text());
            $(".top-row-host").text($(FirstRow).find(".link-host").text());

            $(".top-row").removeClass("hidden");
        });
    }
    // Add Event Handler for Preference Screen
    //
    $("#pref-modal").on("show.bs.modal", function() {
        preference_init();
    });

    $('.help-location').popover({
        content: "Location blah blah",
        placement: "left",
        trigger: "hover",
        delay: {hide: 300}
    });

    $('.help-history').popover({
        content: "This Search Tool will store your Search History within your Web Browser.  This Data is not stored on our Servers and you may delete your Search History at any time.",
        placement: "bottom",
        trigger: "hover"
    });

    if($.cookie("search-history") == "on") {
        $(".enable-history").html("Disable History&nbsp;&nbsp;<span class=\"glyphicon glyphicon-remove-circle\"></span>");
        searchHistory();
    }

    $(".main-btn").click(function() {
        currentStep = 0;
        currentId = $(this)[0].id;
        drawOptOut();
    });

    $("#next-step").click(function() {
        if(currentStep >= (optoutData[currentId].length - 1)) {
            return;
        }
        currentStep++;
        drawOptOut();
    });

    $("#previous-step").click(function() {
        if(currentStep == 0) {
            return;
        }
        currentStep--;
        drawOptOut();
    });

    if(location.pathname.split("/")[1] == "optout") {
        console.log($(".main-btn"));
        $(".main-btn")[0].click();
    }
});

function parkedSetup() {

    $("#parked-no-btn").click(function() {
        console.log("Disabling Parked Page Service.");
        preference_save("typo2");
        typoRedirect();
    });

    $("#parked-disable-btn").click(function() {
        console.log("Disabling Parked Page Service.");
        preference_save("typo");
        typoRedirect();
    });
}

function typoRedirect() {
    window.location = query;
}

function drawOptOut() {

    $(".main-btn img").each(function() {
        $(this)[0].src = $(this)[0].src.replace("_hover.png", ".png");
    });

    var img = $("#" + currentId + " img")[0];
    img.src = img.src.replace(".png", "_hover.png");

    switch(currentId) {
        case "mac":
            $("#img-bar").css("margin-left", "0");
            break;
        case "win":
            $("#img-bar").css("margin-left", "27%");
            break;
        case "ios":
            $("#img-bar").css("margin-left", "53%");
            break;
        case "android":
            $("#img-bar").css("margin-left", "80%");
            break;
    }

    $("#step-count").html(optoutData[currentId].length);
    $(".current-step-number").html(currentStep + 1);
    $("#current-step-image").attr("src", optoutData[currentId][currentStep][0]);
    $("#step-text").html(optoutData[currentId][currentStep][1]);

    $(".step-image-list-images").css("width", (optoutData[currentId].length * 105) + "px");

    $("#step-image-list").empty();
    for (var i = 0; i < optoutData[currentId].length; i++) {

        var css = "";
        if(i == currentStep) {
            css = "current";
        }

        $("#step-image-list").append('<li step="' + i + '" class="' + css + '"><div class="text-center"><small>Step ' + (i + 1) + '</small></div><img src="' + optoutData[currentId][i][0] + '" alt="Step ' + (i + 1) + ' Image" class="step-img"/></li>');
    };

    $("#step-image-list li").click(function() {
        currentStep = parseInt($(this).attr("step"));
        drawOptOut();
    });
}

// Document Click Event Handler
//
$(document).click(function(e) {
    
    var el = $(e.target);
    
    if($(e.target).parent().prop('tagName') == 'A') {
        el = $(e.target).parent();
    }
    
    if($(el).prop('tagName') == 'A' && (el.attr('s') == "true" || el.attr('s') == "false")) {
        
        e.preventDefault();
        
        if(el.attr('s') == "true") {
            var c0 = "ah", c1 = "aa", c2 = "as";
        } else {
            var c0 = "wh", c1 = "wa", c2 = "ws";
        }
        
        var link = "http://s.srchdeliv.com/" + p.c + "/?" + c0 + "=" + el.attr('h') + "&" + c1 + "=" + p.search + "&" + c2 + "=" + p.type;
        
        $(document).append($("<img>").attr({'src':link}));
        
        setTimeout(function(){
            this.location.href = el.attr('href');
        }, 1500);
        
        return false;
    }
    
    return true;
});

// History Side Panel Event Handler
//
$(".panel-history .panel-heading").click(function() {

    if($(".panel-history .panel-body").hasClass("hidden")) {
        $(".panel-history .panel-body").removeClass("hidden");
    } else {
        $(".panel-history .panel-body").addClass("hidden");
    }

});

// Location Side Panel Event Handler
//
$(".panel-location .panel-heading").click(function() {

    if($(".panel-location .panel-body").hasClass("hidden")) {
        $(".panel-location .panel-body").removeClass("hidden");
    } else {
        $(".panel-location .panel-body").addClass("hidden");
    }
    
});

// Adult Search Filter Event Handler
//
$( ".adult-search" ).click(function() {
    
    if($(".adult-search").hasClass("active")) {
        
        $(".adult-search").removeClass("active btn-success").addClass("btn-info");
        $(".adult-search-text").html("Filter Off");
        $.cookie("adult-search-filter", "off", { expires : 365 });
        $(".search-bar-filter").attr("data-original-title", p.filteroff);
    } else {
        
        $(".adult-search").removeClass("btn-info").addClass("active btn-success")
        $(".adult-search-text").html("Filter On");
        $.cookie("adult-search-filter", "on", { expires : 365 });
        $(".search-bar-filter").attr("data-original-title", p.filteron);
    }
    
});

// Preference Event Handlers
//

// toggle off
$(".btn-prefs-on").click(function() {
    $(".btn-prefs-on").addClass("active btn-success");
    $(".btn-prefs-off").removeClass("active btn-danger");
});

// toggle on
$(".btn-prefs-off").click(function() {
    $(".btn-prefs-off").addClass("active btn-danger");
    $(".btn-prefs-on").removeClass("active btn-success");
});

// preference save
$(".preference-save").click(function() {
    preference_save();
});

function preference_init(t) {
    
    $(".preference-settings").addClass("hidden");
    $(".preference-init").removeClass("hidden");
    $(".preference-footer").addClass("hidden");
    $(".preference-notice").addClass("hidden");
    
    var c = $.ajax({
        type:   "GET",
        url:    "/preferences/",
        headers:{ "X-SG-TYPE": t },
        cache:  false
    });
    
    // normal completion call
    c.done(function(DATA){
        
        //console.log(DATA);
        
        if (DATA.Success == false || DATA.Success == "false") {
            preference_fail(p.prefsfail, 0);
            return 0;
        }
        
        if (DATA.Setting == "on") {
            $(".btn-prefs-on").addClass("active btn-success");
            $(".btn-prefs-off").removeClass("active btn-danger");
        } else {
            $(".btn-prefs-off").addClass("active btn-danger");
            $(".btn-prefs-on").removeClass("active btn-success");
        }
        
        $(".preference-settings").removeClass("hidden");
        $(".preference-init").addClass("hidden");
        $(".preference-footer").removeClass("hidden");
        
    });
    
    // failure call
    c.fail(function() {
        preference_fail(p.prefsfail, 0);
    });
}

function preference_notice(text) {
    
    $(".preference-notice").addClass("alert-success").removeClass("alert-danger hidden").html(text);
    
}

function preference_fail(text, type) {
    
    if (type == 0) {
        $(".preference-settings").addClass("hidden");
        $(".preference-init").addClass("hidden");
        $(".preference-footer").addClass("hidden");
    }
    
    $(".preference-notice").addClass("alert-danger").removeClass("alert-success hidden").html(text);
    
}

function preference_save(t) {
    
    var setting = "off";
    if($(".btn-prefs-on").hasClass("active")) {
        setting = "on";
    }
    
    var c = $.ajax({
        type:   "GET",
        url:    "/preferences_update/" + setting,
        headers:{ "X-SG-TYPE": t },
        async: false,
        cache:  false
    });
    
    // normal completion call
    c.done(function(DATA){
        
        //console.log(DATA)
        
        if (DATA.Success == false || DATA.Success == "false") {
            preference_fail(p.prefsupdatefail, 1);
            return false;
        }
        
        preference_notice(p.prefsupdate);
        return false;
        
    });
    
    // failure call
    c.fail(function() {
        preference_fail(p.prefsupdatefail, 1);
        return false;
    });
}

$(".btn-determine-location").click(function() {

    if(navigator.geolocation) {

        navigator.geolocation.getCurrentPosition(function(location) {
            //console.log(location);
        }, function () {
            //console.log("Error getting location");
        });
    }

});

// Search History
//

$(".enable-history").click(function() {

    if($.cookie("search-history") == "on") {
        
        $.cookie("search-history", "off");
        $.localStorage("searches", null);
        $(".search-history-list").html("");
        $(".enable-history").html("Enable History&nbsp;&nbsp;<span class=\"glyphicon glyphicon-ok-circle\"></span>");
    } else {

        $.cookie("search-history", "on");
        searchHistory();
        $(".enable-history").html("Disable History&nbsp;&nbsp;<span class=\"glyphicon glyphicon-remove-circle\"></span>");
    }

});

function searchHistory() {

    searches = $.localStorage("searches");

    if(jQuery.type(searches) == "null") {

        searches = [p.kw];
    } else {

        if($.inArray(p.kw, searches) == -1) {
            searches.push(p.kw);
        }
    }

    $.localStorage("searches", searches);
    drawSearchHistory();
}

function drawSearchHistory() {

    $(".search-count").html(searches.length);

    $(".search-history-list").html("");

    for(var x = 0; x < searches.length; x++) {

        $("<div><span term=\"" + searches[x] + "\" class=\"glyphicon glyphicon-trash search-delete\"></span>&nbsp;&nbsp;" + searches[x] + "</div>").appendTo(".search-history-list");
    }

    $(".search-delete").click(function() {

        k = $.inArray($(this).attr("term"), searches);

        if(k > -1) {
            searches.splice(k, 1);
        }

        $.localStorage("searches", searches);

        searches = $.localStorage("searches");

        drawSearchHistory();

    });
}

// Search Extensions
//
function ext() {
    // do the extended search for new, images, video
    //
    $.getJSON( "/e-image/?q=" + p.search + "&t=" + p.type + "&p=" + p.page, function( data ) {

        if (data.bossresponse.images.count == 0) {
            return;
        }
        
        // Images
        var images = [];

        for(var i = 0; i < data.bossresponse.images.results.length && i < 12; i++) {

            Image = data.bossresponse.images.results[i];
            
            vec = "width";
            if(parseInt(Image.width) > parseInt(Image.height)) {
                vec = "height";
            }
            
            images.push( "<li class='dyn-image' data-toggle='tooltip' title='" + $('<div/>').text(Image.title).html() + "'><a href='" + Image.clickurl + "'><img " + vec + "='75px' src='" + Image.url + "' onerror='$(this).remove();'/></a></li>" );
        }
        
        if($(".images-right-list").is(":visible")) {
            $(".images-right h5").removeClass("hidden");
            $(".images-right-list").append(images.join( "" ));
        } else if($(".images-center-list").is(":visible")) {
            $(".images-center h5").removeClass("hidden");
            $(".images-center h4").removeClass("hidden");
            $(".images-center-list").append(images.join( "" ));
        }
    });

    $.getJSON( "/e-news/?q=" + p.search + "&t=" + p.type + "&p=" + p.page, function( data ) {
        
        // News
        var news = [];

        if (data.bossresponse.news.count == 0) {
            return;
        }

        for(var i = 0; i < data.bossresponse.news.results.length && i < 6; i++) {

            News = data.bossresponse.news.results[i];
            
            var d = "";
            if(i == 0) {
                d = "<div class='link-text'>" + News.abstract + "</div>";
            }
            
            news.push( "<div class='link'><a href='" + News.url + "'><div class='news-title'>" + News.title + "</div>" + d + "<div class='link-host'><img height='15px' width='15px' src='http://" + News.url.split("/")[2] + "/favicon.ico' onerror='$(this).remove()'/> " + News.source + "</div></a></div>" );

        }
        
        $(".news-inline h4").removeClass("hidden");
        $(".news-inline").append(news.join(""));
        
        $('.dyn-image').tooltip({html:true})

    });

}