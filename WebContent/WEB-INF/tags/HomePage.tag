<%-- 
    Document   : HomePageTop
    Created on : Mar 1, 2016, 10:48:20 PM
    Author     : sagarpatel
--%>

<%@tag description="Top part of the HTML DOC over here!!" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html lang="en"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search</title>
    <link href="./css/bootstrap.min.css" rel="stylesheet">
    <link href="./css/searchguide.css" rel="stylesheet">
    
</head>

<body class="no-entry">
    <div class="container">
        
        <br/><br/><br/><br/><br/><br/><br/><br/><br/>
        <div class="middle">
            <img src="./images/logo.png">
        </div>
    
        
        <br/><br/>
        <div class="search-bar row">
        
            <div class="middle">
                <form method="get">
                    <div class="input-group">
                        <input class="form-control" name="query" id="query" type="search" placeholder="Enter Your Search Here" x-webkit-speech="">
                        <div class="input-group-btn">
                            <button class="btn btn-primary btn-lg search-btn" type="submit" onclick="form.action='SearchResult.jsp';"> Go! </button>
                        </div>                        
                    </div>
                    <br />
                    <button class="btn btn-primary" type="submit" onclick="form.action='LuckyResult';"> Test Your Luck! </button>
                </form>
            </div>
        
        </div>
        
    </div>
    
    <script src="./js/jquery.min.js" type="text/javascript"></script>
    <script src="./js/bootstrap.min.js" type="text/javascript"></script>
    <script src="./js/searchguide.js" type="text/javascript"></script>
    <script src="./js/jquery.cookie.js" type="text/javascript"></script>

</body></html>