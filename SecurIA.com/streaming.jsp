<% Object email = session.getAttribute("email");
   String email_str = String.valueOf(email); 
    if (email == null){%>
<script>
    window.location.href = "logout";
</script>
<% } 
%>

<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Streaming</title>
    <header class="p-3 bg-primary text-white">
        <div class="container">
          <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
    
            <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">
                        
            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="dashboard.jsp" class="nav-link px-2">Dashboard</a></li>
                <li><a href="gallery?device=web" class="nav-link px-2">Gallery</a></li>
                <li><a href="streaming.jsp" class="nav-link px-2 text-secondary">Streaming</a></li>
                <li><a href="contact.jsp" class="nav-link px-2">Contact us</a></li>
                <li><a href="about.jsp" class="nav-link px-2">About us</a></li>
                <% out.println("<li><a href=\"get_settings?email="+email_str+"&device=web\" class=\"nav-link px-2\">Settings</a></li>"); %>
            </ul>
    
            <div class="text-end">
                <%= email_str.split("@")[0] %>
                <button type="button" onclick="location.href='logout'" class="btn btn-outline-light me-2">Log out</button>
                <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
            </div>
          </div>
        </div>
      </header>

      <script>
          function httpGet(url) {
                var xmlHttp = new XMLHttpRequest();
                xmlHttp.open( "GET", url, false ); // false for synchronous request
                xmlHttp.send( null );
                return xmlHttp.responseText;
          }
        </script>

        <script>
          // Update date and time every second
            setInterval(function() {
                var date = new Date();
                var time = date.toLocaleTimeString();
               
                document.getElementById("date").innerHTML = "Date: " + date.toLocaleDateString();
                document.getElementById("time").innerHTML = "Time: " +time;
            }, 1000);
        </script>
        <script>
            // Update label and frame every 100 miliseconds
            setInterval(function(){
                var email = "<%= email_str %>";
                // Get json data from server
                var json = JSON.parse(httpGet("/securia/streaming?email="+email));

                // Update frame
                var frame = json.stream;

                if (frame != ""){
                    // Update frame element
                    document.getElementsByTagName("iframe")[0].contentDocument.getElementById("frame").style.backgroundImage = "url(" + "data:image/png;base64," + frame + ")";
                } else{
                    // Update frame element
                    document.getElementsByTagName("iframe")[0].contentDocument.getElementById("frame").style.backgroundImage = "url(Images/no_stream.png)";
                }
            }, 100)
        </script>
</head>
<body>
    <div class="container rounded-3 py-2 h-100">
        <div class="align-content-center rounded-top d-grid" style="border: 1px solid;">
            <div class="d-flex justify-content-center md-5 mb-3 pb-1">
                <span class="h3 fw-bold mb-0 mt-2">WebCam Image Streaming</span>
            </div>
            <div class="d-flex row mb-3 justify-content-center">
                <div class="d-flex col justify-content-center">
                    <span id="date" class="h5 fw-bold mb-0 mt-1">Date: 00/00/0000</span>
                </div>
                <div class="d-flex col justify-content-center">
                    <span id="time" class="h5 fw-bold mb-0 mt-1">Time: 00:00:00</span>
                </div>
            </div>
        </div>
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="border-primary border-2">
                <iframe class="rounded-bottom" id="iframe" style="height: 600px;width: 1115px;" src="stream_img.jsp"></iframe>
            </div>
        </div>
    </div>

    <footer class="bg-light pb-5">
        <div class="container text-center">
          <p class="font-italic text-muted mb-0">&copy; Copyrights SecurIA.com All rights reserved.</p>
        </div>
      </footer>
</body>
</html>