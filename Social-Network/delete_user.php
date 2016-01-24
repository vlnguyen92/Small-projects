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
    
    $query = "DELETE FROM users WHERE uname = '$login'";
    mysql_query($query);
    
    $query = "DELETE FROM passwords WHERE uname = '$login'";
    mysql_query($query);
    
    $query = "DELETE FROM friends WHERE user1 = '$login' OR user2 = '$login'";
    mysql_query($query);
    
    $query = "DELETE FROM posts WHERE uname = '$login'";
    mysql_query($query);
    
    $query = "DELETE FROM members WHERE uname = '$login'";
    mysql_query($query);
    
    $query = "DELETE FROM group_posts WHERE uname = '$login'";
    mysql_query($query);
    
    $query = "DELETE FROM msgs WHERE to_user = '$login' OR from_user = '$login'";
    mysql_query($query);
    session_destroy();
    
    echo "<form method='post' action='tigerface.html'>
                <input type='submit' value='Continue'>
          </form>";
?>