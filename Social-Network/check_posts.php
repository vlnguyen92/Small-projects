<?php
    session_start();
    $login = $_SESSION['login'];
    include "connect.php";
    
    $query = "select uname, p_comment, DATE_FORMAT(p_time, '%a, %b %e, %Y %l:%i:%s %p') AS p_time from posts
    where uname = '$login' OR uname IN (select user2 from friends where user1 = '$login')";
    $result = mysql_query($query) or die(mysql_error());
    
    echo "<table border='2' cellpadding='3' bgcolor='white'>";
    
    while($row = mysql_fetch_array($result)){
        $uname = $row['uname'];
        $p_comment = $row['p_comment'];
        $p_time = $row['p_time'];
        
        $query2 = "SELECT pic, uname, fname, lname FROM users WHERE uname = '$uname'";
        $result2 = mysql_query($query2) or die(mysql_error());
        
        while($row2 = mysql_fetch_array($result2)){
            $uname = $row2['uname'];
            $fname = $row2['fname'];
            $lname = $row2['lname'];
            $pic = $row2['pic'];
            echo "<tr>
                    <td><img src='../Photos/$pic' height='50px' width='45px'></td>
                    <td>$uname</td>
                    <td>$fname $lname</td>
                    <td>$p_comment</td>
                    <td>$p_time</td>
                </tr>";
        }
    }
    echo "</table>";
?>