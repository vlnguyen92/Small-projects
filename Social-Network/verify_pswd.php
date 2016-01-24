<?php
    include "connect.php";
    $login = $_GET['login'];
    $pswd = $_GET['pswd'];
    
    $query = "SELECT * FROM passwords WHERE uname = '$login' AND pswd = '$pswd'";
    $result = mysql_query($query) or die(mysql_error());
    if(mysql_num_rows($result) == 0){
        echo "true";
    }else{
        echo"false";
    }
?>