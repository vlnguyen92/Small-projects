<?php
    include "connect.php";
    
    $uname = $_GET['uname'];
    
    $query = "SELECT * FROM users WHERE uname = '$uname'";
    $result = mysql_query($query) or die(mysql_error());
    if(mysql_num_rows($result) > 0){
        echo "true";
    }else{
        echo "false";
    }
?>