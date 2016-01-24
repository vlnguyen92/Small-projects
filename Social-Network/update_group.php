<?php
    session_start();
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
        
        include "connect.php";
        $login = $_SESSION['login'];
        $g_id = $_POST['g_id'];
        $update = $_POST['update'];
        
        if($update == 'join'){
            $query = "INSERT INTO members VALUES('$login', '$g_id')";
            mysql_query($query) or die (mysql_error());
            echo "<h2>Your membership has been updated!</h2>";            
        }elseif($update == 'quit'){
            $query = "DELETE FROM members WHERE uname = '$login' AND g_id = '$g_id'";
            mysql_query($query) or die (mysql_error());
            echo "<h2>Your membership has been updated!</h2>"; 
        }else{
            echo "<h2>You did not select whether you want to join or quit the group</h2>";
        }
        
        echo <<<HTML
                    <form method="post" action="update_member.php">
                        <input type="submit" value="Update Memberships">
                    </form></body>
HTML;
    
    $query = "SELECT * FROM members WHERE g_id = '$g_id'";
    $result = mysql_query($query);
    
    if(mysql_num_rows($result) == 0){
        $query = "DELETE FROM groups WHERE g_id = '$g_id'";
        mysql_query($query);
    }
?>