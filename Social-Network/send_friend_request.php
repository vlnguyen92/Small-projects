<?php
    session_start();
    include "connect.php";
    
    echo "<body bgcolor='darkgrey'>";
    echo "<link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>";
    echo <<<HTML
    <h1 id='h1' class='headers'>
        <table>
            <tr>
                <td id='h1'><img src='angry.png'></td>
                <td width='70%' align='right'>
                    <table>
                        <tr>
                            <td>
                                <form method='post' action='tigerface.php'>
                                    <input type='submit' value='Home'>
                                </form></td>
                            <td>
                                <form method='post' action='logout.php'>
                                    <input type='submit' value='Logout'>
                                </form></td>
                            <td>
                                <form method='post' action='edit.php'>
                                    <input type='submit' value='Edit Profile'>
                                </form></td>
                            <td>
                                <form method='post' action='delete_user.php' onsubmit="return confirm('Are you sure you want to remove your profile?')">
                                    <input type='submit' value='Delete Profile'>
                                </form></td></tr></table>
                        </tr>
        </table></h1>
HTML;
        
    $login = $_SESSION['login'];
    $to_user = $_POST['to_user'];
    
    $query = "select * from requests where from_user = '$login' AND to_user='$to_user'";
    $result = mysql_query($query) or die (mysql_error());
    
    if(mysql_num_rows($result) > 0){
        $query_update = "UPDATE requests SET r_time = CURRENT_TIMESTAMP WHERE from_user = '$login' AND to_user = '$to_user'";
        mysql_query($query_update) or die (mysql_error());
    }else{
        $query_insert = "INSERT INTO requests(from_user, to_user) VALUES ('$login', '$to_user')";
        mysql_query($query_insert) or die (mysql_error());
    }
    
    echo <<<HTML
           <script>
                alert("Your friend request was sent");
                location.href = "tigerface.php";
            </script></body>
HTML;
?>