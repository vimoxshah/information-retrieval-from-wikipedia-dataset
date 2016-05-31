<%-- 
    Document   : ResultPageTop
    Created on : Mar 1, 2016, 11:10:55 PM
    Author     : sagarpatel
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%@attribute name="TimeTaken"%>
<%@attribute name="SearchString"%>

<!DOCTYPE html>
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search</title>
    
    
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/searchguide.css" rel="stylesheet">
</head>

<body>
    <div class="header container-fluid">
        
        <div class="search-bar row">
            
            <div class="col-xs-12 col-sm-1 col-md-1 col-lg-1 search-bar-logo">
                <img class="logo img-responsive" src="./images/logo.png" />
            </div>
            
            <div class="col-xs-12 col-sm-6 col-md-5 col-lg-5 search-bar-bar">
                <form action="SearchResult.jsp" method="get">
                    <div class="input-group">
                        <input class="form-control" name="query" id="query" type="search" placeholder="" x-webkit-speech="">
                        <div class="input-group-btn">
                            <button class="btn btn-primary btn-lg search-btn" type="submit">Go!</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        
        
        <div class="row search-options-row">
            <div class="col-xs-12 col-sm-12 col-md-10 col-md-offset-1 col-lg-10 col-lg-offset-1">
                <ul class="list-inline search-options">
                    <li class="search-option-selected">Web</li>
                </ul>
            </div>
        </div>
        
    </div>
    



