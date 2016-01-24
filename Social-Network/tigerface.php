<?php
    session_start();
    echo <<<HTML
              <head>
                <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js">
                </script>
                <script>
                    $(document).ready(function(){
                        $("#check_posts").click(function(){
                            $("#posts_table").load("check_posts.php");
                        });
                        $("#check_g_posts").click(function(){
                            $("#g_posts_table").load("check_g_posts.php");
                        });
                    });
                </script>
            </head>
HTML;

    echo "<body bgcolor='darkgrey'>";
    echo "<link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>";
    echo "<h1 id='h1' class='headers'>
        <table>
            <tr>
                <td id='h1'><img src='angry.png'></td>";
    
    
    
    include "connect.php";
    
    if(isset($_SESSION['login'])){
        $login = $_SESSION['login'];
        $pswd = $_SESSION['pswd'];
    }else{
        $login = $_POST['uname'];
        $pswd = $_POST['pswd'];
        $pswd = crypt($pswd, "tigerface");
        $_SESSION['login'] = $login;
        $_SESSION['pswd'] = $pswd;
        
        
    }

    $query = "SELECT * FROM passwords WHERE uname = '$login' AND pswd = '$pswd'";
    $result = mysql_query($query) or die(mysql_error());
    $row = mysql_fetch_array($result);
    if(mysql_num_rows($result) == 0){
           echo "
            </h1>
            <h2>Your username or password is incorrect. Please enter the correct username and password.</h2>
            </tr></table></h1>
            <form method='post' action='tigerface.html'>
                <input type='submit' value='Return to login'>
            </form></body>";
            session_unset();
            
    }
    if(mysql_num_rows($result) > 0){
    $query = "UPDATE users SET online = TRUE WHERE uname = '$login'";
    mysql_query($query);
    $query = "SELECT fname, lname, pic FROM users WHERE uname = '$login'";
    $result = mysql_query($query) or die(mysql_error());
    $row = mysql_fetch_array($result);
    
    $fname = $row['fname'];
    $lname = $row['lname'];
    $pic = $row['pic'];
    echo <<<HTML
            <td width='70%' align='right'>
            <table>
            <tr>
            <td>
            <img style='margin-right:10px' src='../Photos/$pic' height='52'" . " width='47'/>
            <font size='5px' color='white'>$fname $lname</font><br/>
            </td></tr></table>
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
          </td>
          </tr></table></h1>
HTML;
          
    echo "<h3><img style='margin-right:10px' src='../Photos/$pic' height='72'"
    . " width='54'/>Welcome, $fname $lname !</h3>";
    
    echo "<form method='get' action='show_friends.php'>
            <input type='submit' value='My Friends'>";
            
    $query = "SELECT * FROM requests WHERE to_user = '$login'";
    $result4 = mysql_query($query) or die (mysql_error());
    
    if(mysql_num_rows($result4) > 0){
        echo "<font size='4px' color='red'>NEW FRIEND REQUEST</font>";
    }
    
    echo "</form>";
    echo "<form method='post' action='show_messages.php'>
            <input type='submit' value='Messages'>";
            
    $query = "SELECT * FROM msgs WHERE to_user = '$login' AND was_read = FALSE";
    $result6 = mysql_query($query) or die (mysql_error());

    if(mysql_num_rows($result6) > 0){
        echo "<font size='4px' color='red'>NEW MESSAGE</font>";
    }
    
            
    echo "</form>";
        
    $query = "SELECT g_id, g_name FROM members NATURAL JOIN groups WHERE uname = '$login'";
    $result1 = mysql_query($query) or die(mysql_error());
    $result2 = mysql_query($query) or die(mysql_error());
    
    echo <<<HTML
            <h2>Group Posts</h2>
            <form method="post" action="create_group.html">
                <input type="submit" value="Create Group">
            </form>
            <form method="post" action="update_member.php">
                <input type="submit" value="Update Membership">
            </form>
            <form method="get" action="write_group_comment.php">
                <select name="g_id">            
HTML;
    
    while($row1 = mysql_fetch_array($result1)){
        $g_name = $row1['g_name'];
        $g_id = $row1['g_id'];
        echo "<option value='$g_id'>$g_name</option>";
    }
    
    echo <<<HTML
        </select>
        <input type="submit" value="Post Comment to Group">
        </form>
HTML;
    
    
    echo"<span id='g_posts_table'>";
    while($row = mysql_fetch_array($result2)){
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
        echo "</table></span>
                <button id='check_g_posts'>Refresh Group Posts</button>";
        
    }
    
    $query = "select uname, p_comment, DATE_FORMAT(p_time, '%a, %b %e, %Y %l:%i:%s %p') AS p_time from posts
    where uname = '$login' OR uname IN (select user2 from friends where user1 = '$login')";
    $result = mysql_query($query) or die(mysql_error());
    
    echo "
            <h2>POSTS</h2>
            <form method='post' action='write_comment.html'>
                <input type='submit' value='Post a Comment'>
            </form>
            <span id='posts_table'>
            <table border='2' cellpadding='3' bgcolor='white'>
        ";
    
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
    echo "</table></span>
    <button id='check_posts'>Refresh Posts</button>
    </body>";
    }
    
?>