<% Object email = session.getAttribute("email");
   String email_str = String.valueOf(email); 
    if (email == null){%>
<script>
    window.location.href = "logout";
</script>
<% } %>


<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Settings</title>
    <header class="p-3 bg-primary text-white">
        <div class="container">
          <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            
            <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">
                
            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="dashboard.jsp" class="nav-link px-2">Dashboard</a></li>
                <li><a href="gallery?device=web" class="nav-link px-2">Gallery</a></li>
                <li><a href="streaming.jsp" class="nav-link px-2">Streaming</a></li>
                <li><a href="contact.jsp" class="nav-link px-2">Contact us</a></li>
                <li><a href="about.jsp" class="nav-link px-2">About us</a></li>
                <% out.println("<li><a href=\"get_settings?email="+email+"&device=web\" class=\"nav-link px-2 text-secondary\">Settings</a></li>"); %>
            </ul>

            <script type="text/javascript" language="javaScript">
              function confirmDeletion(){
                if(confirm("Are you sure you want to delete this user?")){
                  document.getElementById("deleteForm").submit();
                }
              }
            </script>

            <div class="text-end">
              <form action="delete_account" method="post" id="deleteForm">
                <% String username = email_str.split("@")[0];
                  out.println("<input type='hidden' name='email' value='" + session.getAttribute("email") + "'>");
                  out.println("<button type='button' onclick='confirmDeletion()' class='btn btn-outline-light bg-danger me-2'>Delete "+username+"</button>");
                %>
                <button type="button" onclick="location.href='logout'" class="btn btn-outline-light me-2">Log out</button>
              </form>
                            <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
            </div>
          </div>
        </div>
      </header>
</head>
<body>
  <div id="form-message-warning mt-4">
    <% if (session.getAttribute("error") != null){
       out.println("<div class='alert alert-danger' role='alert'>"+session.getAttribute("error")+"</div>");
       session.removeAttribute("error");
     } %>
 </div>
  <div class="container rounded-3 py-2 mt-5" style="border: 1px solid;">
    <div class="content">
        <div class="container">
            <div class="row align-items-stretch no-gutters">
                <div class="form h-100">
                    <span class="h3 ">User Settings</span>
                    <form class="mb-5" method="post" action="set_settings" id="settingsForm" name="settingsForm">
                      <div class="bg-light rounded-3">
                        <div class="row mt-3 mx-3">
                            <div class="form-group mb-3">
                                <label for="" class="col-form-label ">Email </label>
                                <% out.println("<input type='email' required class='form-control' name='email' value='" + session.getAttribute("email") + "' placeholder='example@email.com' readonly>"); %>
                            </div>
                        </div>
                        <div class="row mx-3">
                            <div class="col-md-6 form-group mb-3">
                                <label for="" class="col-form-label ">Password</label>
                                <% out.println("<input type='password' required class='form-control' name='password' value='"+session.getAttribute("password")+"' placeholder='Your password'>"); %>
                            </div>
                            <div class="col-md-6 form-group mb-3">
                                <label for="" class="col-form-label ">Confirm password</label>
                                <% out.println("<input type='password' required class='form-control' name='password_conf' value='"+session.getAttribute("password")+"' placeholder='Confirm your password'>"); %>
                            </div>
                        </div>
                        <div class="row mx-3">
                          <div class="col-md-6 form-group mb-3">
                              <label for="firstname" class="col-form-label ">First Name</label>
                              <% out.println("<input type='text' required class='form-control' name='firstname' value='"+session.getAttribute("firstname")+"' placeholder='Your first name'>"); %>
                          </div>
                          <div class="col-md-6 form-group mb-3">
                              <label for="surname" class="col-form-label ">Surname</label>
                              <% out.println("<input type='text' required class='form-control' name='surname' value='"+session.getAttribute("surname")+"' placeholder='Your surname'>"); %>
                          </div>
                      </div>
                      <div class="row mx-3 ">
                        <div class="col-md-6 form-group mb-3">
                            <label for="phone" class="col-form-label ">Phone number</label>
                            <% out.println("<input type='`phone' required class='form-control' name='phone' value='"+session.getAttribute("phone")+"' placeholder='(+34) 000 000 000'>"); %>
                        </div>
                        <div class="col-md-6 form-group mb-3">
                            <label for="birthdate" class="col-form-label ">Birth date</label>
                            <% out.println("<input type='date' class='form-control' name='birthdate' value='"+session.getAttribute("birthdate")+"' placeholder='dd/mm/yyyy'>"); %>
                        </div>
                    </div>
                  </div>
                  <div class="mt-3">
                    <span class="h3">System Settings</span>
                  </div>
                    <div class="bg-light mt-4 rounded-3">
                        <div class="row mx-3">
                          <div class="col-md-5 form-group mb-3">
                            <label for="getPhotos" class="">Capture photos</label>
                            <% out.println("<input type='checkbox' name='getPhotos' value='true' "+ ((String.valueOf(session.getAttribute("getPhotos")) == "true")? "checked":"") +">"); %>
                          </div>
                          <div class="col-md-5 form-group mb-3">
                            <label for="sendNotifications" class="">Send notifications</label>
                            <% out.println("<input type='checkbox' name='sendNotifications' value='true' "+ ((String.valueOf(session.getAttribute("sendNotifications"))=="true")? "checked":"") +">"); %>
                          </div>
                          <div class="col-md-2 form-group mb-3">
                            <label for="canStream" class="">Live Streaming</label>
                            <% out.println("<input type='checkbox' name='canStream' value='true' "+ ((String.valueOf(session.getAttribute("canStream"))=="true")? "checked":"") +">"); %>
                          </div>
                        </div>
                      </div>  
                        <div class="row">
                            <div class="col-md-12 mt-4 form-group">
                              <input type="submit" value="Save Changes" class="btn btn-primary rounded-1 py-2 px-2" style="width: 100%;">
                              <span class="submitting"></span>
                            </div>
                        </div>
                    </form>
                </div>
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