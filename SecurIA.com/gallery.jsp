<%@page import="java.util.ArrayList" %>
<% Object email = session.getAttribute("email");
   String email_str = String.valueOf(email); 
    if (email == null){%>
<script>
    window.location.href = "logout";
</script>
<% }

ArrayList<String> paths = (ArrayList<String>) session.getAttribute("paths");
ArrayList<String> labels = (ArrayList<String>) session.getAttribute("labels");
ArrayList<String> timestamps = (ArrayList<String>) session.getAttribute("timestamps");

  %>

<!DOCTYPE html>
<head>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <title>SECURIA - Gallery</title>
    <header class="p-3 bg-primary text-white">
        <div class="container">
          <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            
            <img class="img-fluid rounded-circle mb-3 img-thumbnail shadow-sm" width="60" role="img" aria-label="Bootstrap" src="logo.jpg" alt="SecurIA">

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="dashboard.jsp" class="nav-link px-2">Dashboard</a></li>
                <li><a href="gallery?device=web" class="nav-link px-2 text-secondary">Gallery</a></li>
                <li><a href="streaming.jsp" class="nav-link px-2">Streaming</a></li>
                <li><a href="contact.jsp" class="nav-link px-2">Contact us</a></li>
                <li><a href="about.jsp" class="nav-link px-2">About us</a></li>
                <% out.println("<li><a href=\"get_settings?email="+email+"&device=web\" class=\"nav-link px-2\">Settings</a></li>"); %>
            </ul>
    
            <div class="text-end">
              <%= email_str.split("@")[0] %>
              <button type="button" onclick="location.href='logout'" class="btn btn-outline-light me-2">Log out</button>
                            <!-- <button type="button" class="btn btn-warning">Sign-up</button> -->
            </div>
          </div>
        </div>

        <script>
          // Define a function to display image in the main label
          function displayImage(path, label, timestamp) {
            document.getElementById("mainImage").src = path;
            document.getElementById("label").innerHTML = label;
            document.getElementById("timestamp").innerHTML = timestamp;
          }
        </script>

        <script>
          function donwloadMainImg(){
            var path = document.getElementById("mainImage").src;
            var label = document.getElementById("label").innerHTML;
            if (label == "Undetected"){
              alert("No image to download");
            }else{
              var timestamp = document.getElementById("timestamp").innerHTML;
              var a = document.createElement("a");
              a.href = path;
              a.download = label + "_" + timestamp + ".jpg";
              a.click();
            }
          }
        </script>
  
        <script>
          // Define a function to delete all generated images
          function deleteAllImages() {
            // Put main image back to default
            document.getElementById("mainImage").src = "/securia/Avatar.jpg";
            document.getElementById("label").innerHTML = "Undetected";
            document.getElementById("timestamp").innerHTML = "18-11-2021 10:51:22";

            var images = document.getElementsByName("gen_img");
            while (images.length > 0) {
              images[0].parentNode.removeChild(images[0]);
            }
  
            var rows = document.getElementsByName("row");
            while (rows.length > 0){
              rows[0].parentNode.removeChild(rows[0]);
            }
  
            document.getElementById("image_holder").innerHTML = "";
          }

          function filterInput(){
            var paths = <%= castToJSArray(paths) %>;
            var labels = <%= castToJSArray(labels) %>;
            var timestamps = <%= castToJSArray(timestamps) %>;

            var result_paths = [];
            var result_labels = [];
            var result_timestamps = [];

            var filter = document.getElementById("filter").value;

            for (var i = 0; i < paths.length; i++){
              if (filter == '' || timestamps[i].split(" ")[0] == filter){
                result_paths.push(paths[i]);
                result_labels.push(labels[i]);
                result_timestamps.push(timestamps[i]);
              }
            }

            printImages(result_paths, result_labels, result_timestamps);
          }
  
          // Print all images that match the filter
          function printImages(paths, labels, timestamps) {
            deleteAllImages();
            var filter = document.getElementById("filter").value;

            var cols = 6;
            var rows = paths.length / cols;
            var image_pos = 0;
            var img_count = 0
  
            for (var j = 0; j < rows; j ++){
                var row = document.createElement("div");
                row.className = "d-flex alignt-items-start";
                row.id = "row"+j;
                row.name="row";
                document.getElementById("image_holder").appendChild(row);
                for (var i = 0; i < cols; i++) {
                  if (image_pos < paths.length) {
                    var image = document.createElement("input");
                    image.src = paths[image_pos];
                    image.type = "image";
                    image.name = "gen_img";
                    image.className = "border-card";
                    image.style.width = "200px";
                    image.style.height = "200px";
                    image.title = "Label: " + labels[image_pos] + "\nTimestamp: " + timestamps[image_pos]+"\nClick to display";
                    image.onclick = function() {
                      displayImage(this.src, this.title.split("\n")[0].split(":")[1], this.title.split("\n")[1].split(":")[1]);
                    };
                    document.getElementById("row"+j).appendChild(image);
                    img_count ++;
                  }
                  image_pos ++;
                }
            } 
            var img_count_div = document.getElementById("img_count");
            img_count_div.innerHTML= "Captured images: " + img_count;
          }
        </script>

        <script>
          function deleteImage(){
            var path = document.getElementById("mainImage").src;
            location.href="delete_image?path="+path.split("8080")[1];
          }
        </script>
      </header>
      
</head>
<body>

  <div class="align-content-center rounded-top d-grid mb-lg-3" style="border: 1px solid;">
    <div class="d-flex justify-content-center md-5 mb-3 pb-1">
        <span class="h3 fw-bold mb-0 mt-2">WebCam Image Gallery</span>
    </div>
  </div>

  <div class="d-grid">
    <div class="d-flex justify-content-center">
      <div class="d-grid">
        <img id="mainImage" src="Avatar.jpg" class="border-card justify-content-center" style="width: 450px;height: 450px;">
      </div>
      <div class="grid p-md-4">
        <div>
          <h4>Image properties</h4>
          <hr class="line-blue" width="100%" style="height: 3px;">
          <p><b>Label:</b> <i id="label">Undetected</i>
          <p><b>Timestamp:</b> <i id="timestamp">18-11-2021 10:51:22</i></p>
        </div>
        <div class="d-grid mt-xl-5 align-items-end" >
          <button class="btn btn-outline-light btn-primary" onclick="donwloadMainImg()" style="height: fit-content;">Download</button>
          <button class="btn btn-outline-light btn-primary" onclick="deleteImage()" style="height: fit-content;">Delete</button>
        </div>
      </div>
    </div>
  </div>

  <div class="mx-lg-4 my-lg-4">
    <div class="d-flex">
      <h3 class="flex-5" id="img_count">Captured Images</h3>
      <div class="d-flex">
        <div class="d-grid me-lg-4">
          <label for="filter">Image filter</label>
          <input type="date" id="filter">
        </div>
        <div class="d-grid align-items-end ">

          <%!
            public String castToJSArray(ArrayList<String> array){
              // Parse paths arraylist to javascript array
              String array_str = "[";
              for (int i = 0; i < array.size(); i++) {
                  array_str += "'"+ array.get(i) + "',";
              }
              array_str = array_str.substring(0, array_str.length() - 1);
              array_str += "]";    
              return array_str;
            }
            %>

          <%
            
            String paths_str = castToJSArray(paths);
            String labels_str = castToJSArray(labels);
            String timestamps_str = castToJSArray(timestamps);

            out.println("<button class=\"btn btn-outline-light btn-primary\" onclick=\"filterInput()\" style=\"height: fit-content;\">Filter</button>");
          %>
          
        </div>
      </div>
    </div>
    <div class="d-grid my-lg-4 justify-content-center" id="image_holder">
      <script>
        filterInput();
      </script>

      <!-- Image row example
      <div class="d-flex"> Fila 
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;"> Columna 0 
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
        <input type="image" src="Avatar.jpg" class="border-card" onclick="" style="width: 200px;height: 200px;">
      </div>
      -->
    </div>
  </div>

  <footer class="bg-light pb-5">
    <div class="container text-center">
      <p class="font-italic text-muted mb-0">&copy; Copyrights SecurIA.com All rights reserved.</p>
    </div>
  </footer>
</body>
</html>