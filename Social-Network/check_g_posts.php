<?php
    session_start();
    $login = $_SESSION['login'];
    include "connect.php";
    
    $query = "SELECT g_id, g_name FROM members NATURAL JOIN groups WHERE uname = '$login'";
    $result = mysql_query($query) or die(mysql_error());
    
    
    while($row = mysql_fetch_array($result)){
        $g_name = $row['g_name'];
        $g_id = $row['g_id'];
        
        echo "<h3>$g_name</h3>";
        
        $query2 = "SELECT pic, uname, fname, lname, g_post_comment, DATE_FORMAT(g_post_time, '%a, %b %e, %Y %l:%i:%s %p') AS g_post_time
                    FROM group_posts NATURAL JOIN users WHERE g_id = '$g_id'";
        $result3 = mysql_query($query2) or die(mysql_error());
        
        echo "
            <table border='2' cellpadding='3' bgcolor='white'>
        ";
        
        while($row2 = mysql_fetch_array($result3)){
            $pic = $row2['pic'];
            $fname = $row2['fname'];
            $lname = $row2['lname'];
            $uname = $row2['uname'];
            $g_post = $row2['g_post_comment'];
            $g_time = $row2['g_post_time'];
            
            echo "<tr>
                    <td><img src='../Photos/$pic' height='50px' width='45px'></td>
                    <td>$uname</td>
                    <td>$fname $lname</td>
                    <td>$g_post</td>
                    <td>$g_time</td>
                </tr>";
        }
        echo "</table>";}
?>