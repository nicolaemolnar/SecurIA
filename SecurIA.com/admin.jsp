<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% String email = String.valueOf(session.getAttribute("email")); 
if (!email.equals("admin@securia.com")) { %>
    <script>
        window.location.href = "logout";
    </script>
    <% } %>

<!DOCTYPE html>

<head>
    <title>Admin Page</title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />

    <script>
        function launchQuery(){
            var query = document.getElementById("sql").value;
            // Send query to server via POST method and get response
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("result").innerHTML = this.responseText;
                }else{
                    document.getElementById("result").innerHTML = "Loading...";
                }
            };

            xmlhttp.open("POST", "admin_sql", true);
            xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            xmlhttp.send("query=" + query);
        }
    </script>

    <script>
        function prepared_query(type){
            var query;
            // Update query text
            if (type == "alert"){
                query = "SELECT * FROM alert ORDER BY id DESC";
            } else if (type == "contact"){
                query = "SELECT * FROM contact_request ORDER BY request_id DESC";
            } else if (type == "del_contact"){
                query = "DELETE FROM contact_request WHERE request_id='?'";
            }else if (type == "del_images"){
                query = "DELETE FROM image WHERE image_id IN (SELECT image_id FROM public.image NATURAL JOIN public.system WHERE email=?)";
            }

            document.getElementById("sql").value = query;
        }
    </script>
</head>

<body>
    <!-- Screen-wide centered title "Admin Page" -->
    <div class="container w-100">
        <!-- Header -->
        <div class="row bg-primary text-white">
            <div class="col-md-12">
                <h1 class="text-center">Admin Page</h1>
            </div>
        </div>

        <!-- Navigation bar -->
        <div class="row">
            <nav class="navbar d-flex bg-opacity-75 bg-primary text-white">
                <p class="navbar-brand flex-5">Welcome: <u> <%= email%> </u></p>
                <!-- "log out" button at the end of navbar -->
                <button onclick="location.href='logout'" class="flex-1 btn btn-primary btn-outline-info">Log out</button>
            </nav>
        </div>

        <!-- Admin panel -->
        <div class="row border-primary border-card">
            <!-- Left column -->
            <div class="col-md-2 border-right border-card">
                <div class="row">
                    <div class="col-md-12">
                        <h3 class="text-center">Prepared SQL</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <ul class="list-group">
                            <!-- List of SQL buttons -->
                            <li class="list-group-item">
                                <button type="button" class="btn btn-info btn-block" name="select_alerts" onclick="prepared_query('alert')">
                                    <h5>Select all alerts</h5>
                                </button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" class="btn btn-info btn-block" name="select_alerts" onclick="prepared_query('contact')">
                                    <h5>Select all contacts</h5>
                                </button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" class="btn btn-info btn-block" name="select_alerts" onclick="prepared_query('del_contact')">
                                    <h5>Delete contact</h5>
                                </button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" class="btn btn-info btn-block" name="select_alerts" onclick="prepared_query('del_images')">
                                    <h5>Delete images</h5>
                                </button>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- Right column -->
            <div class="col-md-10">
                <div class="row">
                    <div class="col-md-12">
                        <h3 class="text-center">SQL Console</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <!-- SQL Console -->
                        <label for="sql">SQL:</label>
                        <textarea id="sql" class="form-control" rows="2" cols="50"></textarea>
                        <label for="result">Result:</label>
                        <textarea id="result" class="form-control my-lg-2" rows="10" cols="50" readonly></textarea>
                        <!-- Button at the end of the div -->
                        <div class="d-grid mb-1">
                            <button onclick="launchQuery()" class="btn btn-primary btn-outline-light">Execute</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
</body>
</html>
