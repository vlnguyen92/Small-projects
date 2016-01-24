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
    $s_name = $_POST['s_name'];
    
    $query = "SELECT CONCAT(fname, ' ', lname), uname FROM users WHERE uname <> '$login' AND (fname LIKE '%$s_name%' OR lname LIKE '%$s_name%') AND
            (uname NOT IN (SELECT user2 FROM friends WHERE user1 = '$login'))";
    $result = mysql_query($query) or die (mysql_error());
    
    echo <<<HTML
            <form method="post" action="send_friend_request.php">
                <select name="to_user">
HTML;

    while($row = mysql_fetch_array($result)){
        $name = $row["CONCAT(fname, ' ', lname)"];
        $uname = $row['uname'];
        
        echo "<option value='$uname'>$name</option>";
    }
    
    echo <<<HTML
            </select>
            <input type="submit" value="Send Friend Request">
            </form></body>
HTML;
    
    
?>