<%@page import="java.util.ArrayList" %>
<% String email = String.valueOf(session.getAttribute("email")); 
    if (email == null){%>
<script>
    window.location.href = "logout";
</script>
<% } %>

<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Dashboard</title>
    
    <script>
      // Delete notification from server
      function delete_notification(id) {
          console.log("Deleting id");
          var xmlHttp = new XMLHttpRequest();
          
          xmlHttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
              // Update notifications
              var container = document.getElementById("notif_container");
              var notif_div = document.getElementById(id);
              container.removeChild(notif_div);
            }
          };

          xmlHttp.open( "POST", "delete_alert", true ); // false for synchronous request
          // send email to server  
          xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
          xmlHttp.send("id="+id.split("_")[1]);
      }
    </script>

    <script>
      // Delete all notifications from server
      function delete_all_notifications() {
        var container = document.getElementById("notif_container");
        for (var i = 0; i < container.childNodes.length; i++) {
          var notif_div = container.childNodes[i];
          var id = notif_div.id;
          delete_notification(id);
        }
      }
    </script>

<script>
  // Get the notifications from the server every seccond with javascript
  setInterval(function() {
    // Get data from server using post
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        var notifications = JSON.parse(this.responseText);
        // Update notifications
        var container = document.getElementById("notif_container");

        // Remove all children
       container.innerHTML = "";

        // Add new notifications
        var i = 0;
        while (notifications[i] != null) {
          var notif = notifications[i];
          var notif_div = document.createElement("div");
          var id = notif.split(";")[0];
          var title = notif.split(";")[1];
          var message = notif.split(";")[2];

          var color = "alert-info";
          if (title.includes("Image")){
            color = "alert-success";
          } else if (title.includes("attack")){
            color = "alert-danger";
          }

          notif_div.className = "alert "+color+" alert-dismissible fade show";
          notif_div.setAttribute("role", "alert");
          notif_div.setAttribute("id", "notif_"+id);
          notif_div.innerHTML = "<strong>" + title + "</strong> " + message+" Event timestamp: "+notif.split(";")[3];
          
          var button = document.createElement("button");
          button.setAttribute("type", "button");
          button.setAttribute("class", "close");
          // Align to the right of the notif_div
          button.setAttribute("style", "float:right");
          button.setAttribute("aria-label", "Close");
          button.innerHTML = "<span aria-hidden=\"true\">&times;</span>";
          button.onclick = function (){       
            delete_notification(this.parentNode.id);
          };

          notif_div.appendChild(button);
          container.appendChild(notif_div);
          i++;
        }
      }
    };
    xmlHttp.open("POST", "get_alerts", true ); // false for synchronous request
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttp.send("param=a");
  }, 1000);
</script>
</head>
<body>
  <div class="d-flex flex-column flex-5">
    <div class="d-flex flex-row">
      <header class="p-3 bg-primary text-white" id="header">
        <div class="container">
          <div class="d-grid align-items-center justify-content-center justify-content-lg-start">
            <div class="d-flex flex-column">
              <div class="d-grid">
                
                <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm"  width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">
              
                <a href="dashboard.jsp" class="rounded text-center my-2 text-secondary">Dashboard</a>
                <a href="gallery?device=web" class="rounded text-center my-2">Gallery</a>
                <a href="streaming.jsp" class="rounded text-center my-2">Streaming</a>
                <a href="contact.jsp" class="rounded text-center my-2">Contact us</a>
                <a href="about.jsp" class="rounded text-center my-2">About us</a>
                <% out.println("<a href=\"get_settings?email="+email+"&device=web\" class=\"rounded text-center my-2\">Settings</a>"); %>
        
                <div class="text-end mt-lg-5">
                  <%= email.split("@")[0] %>
                  <button type="button" id="logout" onclick="location.href='logout'" class="btn btn-outline-light me-2">Logout</button>
                  <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>
    
      <div>
        <div class="bg-primary text-white grid" id="title">
          <h1 class="text-center"> Welcome to SecurIA.com </h1>
        </div>

        <script>
          // Make DIV element screen wide
          var header = document.getElementById("header");
          header.style.height = window.innerHeight + "px";
    
          // Make DIV element screen tall
          var title = document.getElementById("title");
          title.style.height = window.innerHeight*0.15 + "px";
          title.style.width = (window.innerWidth - header.style.width) + "px";
        </script>

        <div >
          <!-- Header with title "Your notifications" -->
          <div class="align-content-center rounded-top d-grid mb-lg-3" style="border: 1px solid;">
            <div class="d-flex justify-content-center mb-3">
                <span class="h3 fw-bold mb-0 mt-2">Your notifications</span>
            </div>
          </div>
          <div class="d-flex justify-content-end container">
            <button type="button" id="clear" onclick="delete_all_notifications()" class="btn bg-primary btn-outline-light ml-2">Clear all</button>
          </div>
          <div class="container " id="notif_container">
              <!-- Red alert with title and description with a delete button 
              <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <strong>Possible attack danger!</strong> We strongly believe your camera is being covered.
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>-->
          </div>
        </div>

      </div>
      </div>
    </div>
    
    <footer class="bg-light pb-5 flex-1">
      <div class="container text-center">
        <p class="font-italic text-muted mb-0">&copy; Copyrights SecurIA.com All rights reserved.</p>
      </div>
    </footer>
  </div>
</body>
</html>