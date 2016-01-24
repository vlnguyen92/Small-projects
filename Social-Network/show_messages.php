<?php
    session_start();
    echo <<<HTML
            <head>
                <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js">
                </script>
                <script>
                    $(document).ready(function(){
                        $("#check_msgs").click(function(){
                            $("#msgs_table").load("check_msgs.php");
                        });
                    });
                </script>
            </head>
HTML;
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
    
    include "connect.php";
    
    echo <<<HTML
                <h1>MY MESSAGES</h1>
                <form method="post" action="write_message.php">
                    <select name="to_user">
HTML;

    $query = "SELECT fname, lname, uname FROM users NATURAL JOIN friends
              WHERE user2 = uname AND user1 = '$login'";
    $result = mysql_query($query) or die(mysql_error());
    
    While($row = mysql_fetch_array($result)){
        $fname = $row['fname'];
        $lname = $row['lname'];
        $uname = $row['uname'];
        
        echo "<option value='$uname'>$fname $lname</option>";
    }
    
    echo <<<HTML
            </select>
            <input type="submit" value="Write Message">
            </form>
HTML;
    
    $query = "SELECT to_user, pic, uname, fname, lname, msg, DATE_FORMAT(msg_time, '%a, %b %e, %Y %l:%i:%s %p') AS msg_time , was_read
              FROM users JOIN msgs ON from_user = '$login' OR to_user = '$login'
              WHERE from_user = uname ORDER BY msg_time ASC";
    $result = mysql_query($query) or die(mysql_error());
    
    

    
    
    echo "
            <span id='msgs_table'>
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
    echo "</table></span>
     <button id='check_msgs'>Refresh</button>
     </body>";
    
    $query = "UPDATE msgs SET was_read = TRUE WHERE to_user = '$login'";
    mysql_query($query) or die (mysql_error());
?>