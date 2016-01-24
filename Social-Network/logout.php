<?php
    session_start();
    echo "<body bgcolor='darkgrey'>";
    echo "<link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>";
    echo "<h1 id='h1' class='headers'>
        <table>
            <tr>
                <td id='h1'><img src='angry.png'></td>
            </tr>
        </table></h1>";
    $login = $_SESSION['login'];
    include "connect.php";
    $query = "UPDATE users SET online = FALSE WHERE uname = '$login'";
    mysql_query($query);
    session_destroy();
    echo "<script>location.href = 'tigerface.html';</script>";
    
?>