<?php
    session_start();
    $login = $_SESSION['login'];
    
    include "connect.php";
    
     $query = "SELECT to_user, pic, uname, fname, lname, msg, DATE_FORMAT(msg_time, '%a, %b %e, %Y %l:%i:%s %p') AS msg_time , was_read
              FROM users JOIN msgs ON from_user = '$login' OR to_user = '$login'
              WHERE from_user = uname ORDER BY msg_time ASC";
    $result = mysql_query($query) or die(mysql_error());
    
    

    
    
    echo "
            <table border='2' cellpadding='3' bgcolor='white'>
        ";
    
    while($row = mysql_fetch_array($result)){
        $pic = $row['pic'];
        $uname = $row['uname'];
        $fname = $row['fname'];
        $lname = $row['lname'];
        $msg = $row['msg'];
        $msg_time = $row['msg_time'];
        $to_user = $row['to_user'];
        if($row['was_read'] == 0){
            $was_read = 'unread';
        }else{
            $was_read = 'read';
        }
        
        if($to_user == $login && $was_read == 'unread'){
            echo "<tr>
                    <td><img src='../Photos/$pic' height='50px' width='45px'></td>
                    <td><b>$uname</b></td>
                    <td><b>$fname $lname</b></td>
                    <td><b>$msg</b></td>
                    <td><b>$msg_time</b></td>
                    <td><b>$was_read</b></td>
              </tr>";
        }else{
        
        echo "<tr>
                    <td><img src='../Photos/$pic' height='50px' width='45px'></td>
                    <td>$uname</td>
                    <td>$fname $lname</td>
                    <td>$msg</td>
                    <td>$msg_time</td>
                    <td>$was_read</td>
              </tr>";
        }
        
    }
    echo "</table>";
    
    $query = "UPDATE msgs SET was_read = TRUE WHERE to_user = '$login'";
    mysql_query($query) or die (mysql_error());
?>
