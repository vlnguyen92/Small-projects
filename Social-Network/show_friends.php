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
    $login = $_SESSION['login'];
    
    include "connect.php";
    
    echo "<h1>Friend Requests</h1>";
    
    $query_fr = "SELECT from_user, DATE_FORMAT(r_time, '%a, %b %e, %Y %l:%i:%s %p') AS r_time  FROM requests WHERE to_user = '$login'";
    $result_fr = mysql_query($query_fr) or die (mysql_error());
    
    if(mysql_num_rows($result_fr) > 0){
        echo "<table border='2' cellpadding='3' bgcolor='white'>";
        while($row = mysql_fetch_array($result_fr)){
            $from_user = $row['from_user'];
            $r_time = $row['r_time'];
            
            $query_user = "SELECT fname, lname, pic FROM users WHERE uname = '$from_user'";
            $reuslt_user = mysql_query($query_user) or die (mysql_error());
            $row_user = mysql_fetch_array($reuslt_user);
            $fname = $row_user['fname'];
            $lname = $row_user['lname'];
            $pic = $row_user['pic'];
            
            
            echo <<<HTML
                    <tr>
                        <td><img src='../Photos/$pic' height='50px' width='45px'></td>
                        <td>$from_user</td>
                        <td>$fname $lname</td>
                        <td>$r_time</td>
                        <td>
                            <form method="post" action="req_responses.php">
                                <input type="hidden" name="from_user" value="$from_user">
                                <input type="radio" name="resp" value="yes">Accept
                                <input type="radio" name="resp" value="no">Reject<br/>
                                <input type="radio" name="resp" value="later">Postpone
                                <input type="submit" value="Respond">
                            </form>
                        </td>
                    </tr>
                                
HTML;


        }
        echo "</table>";
    }else{
        echo"<h3>You have no friend requests.</h3>";
    }
    
   
    
    echo <<<HTML
            <h1>Find a Friend</h1>
            <form method="post" action="search_friends.php">
                <input type="text" name="s_name">
                <input type="submit" value="Search Friend">
            </form>
HTML;

    echo <<<HTML
            <h2>Delete a Friend</h2>
            <form method="post" action="drop_friend.php">
            <select name="uname">
HTML;
    
    $query_delete = "SELECT fname, lname, uname
              FROM users NATURAL JOIN friends
              WHERE user2 = uname AND user1 = '$login'";
    $result_delete = mysql_query($query_delete) or die (mysql_error());
    while($row = mysql_fetch_array($result_delete)){
        $fname = $row['fname'];
        $lname = $row['lname'];
        $uname = $row['uname'];
        echo "<option value='$uname'>$fname $lname</option>";
    }
    echo <<<HTML
            </select>
            <input type="submit" value="Delete Friend">
            </form>
HTML;
    
    
    
    
    $query = "SELECT fname, lname, uname, sex, DATE_FORMAT(bday, '%a, %b %e, %Y %l:%i:%s %p') AS bday, online, pic
              FROM users NATURAL JOIN friends
              WHERE user2 = uname AND user1 = '$login'";
    $result = mysql_query($query) or die(mysql_error());
     
    echo "
            <table border='2' cellpadding='3' bgcolor='white'>
                <h1>My Friends</h1>
        ";
        
        while($row = mysql_fetch_array($result)){
            $fname = $row['fname'];
            $lname = $row['lname'];
            $uname = $row['uname'];
            $sex = $row['sex'];
            $bday = $row['bday'];
            if ($row['online'] == 0){
                $online = 'offline';
            }else{
                $online = 'online';
            }
            $pic = $row['pic'];
                            
            echo "<tr>
                    <td><img src='../Photos/$pic' height='50px' width='45px'></td>
                    <td>$uname</td>
                    <td>$fname $lname</td>
                    <td>$sex</td>
                    <td>$bday</td>
                    <td>$online</td>
                </tr>";
            }
        
        echo "</table> </body>";
?>