<?php
    session_start();
    $_SESSION['g_id'] = $_GET['g_id'];
    $g_id = $_GET['g_id'];
    include "connect.php";
    
    $query = "SELECT g_name FROM groups WHERE g_id = '$g_id'";
    $result = mysql_query($query) or die (mysql_error());
    while ($row = mysql_fetch_array($result)){
        $g_name = $row['g_name'];
    }
    
    echo <<<HTML
        <body bgcolor='darkgrey'>
        <script>
    function $(id){
            return document.getElementById(id);
        }
        function validate(){
            if($("comment").value == ""){
                alert("Please enter a comment");
                return false;
            }
            return true;
        }
    </script>
            <link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>
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
        <h2>WRITE YOUR COMMENT HERE</h2>
        <form method="post" action="post_group_comment.php" onsubmit="return validate()">
           <textarea id="comment" rows="5" cols="50" name="g_post_comment"></textarea><br/>
            <input type="submit" value="Post Comment to $g_name">
            <input type="reset">
        </form>
        <script>
        $("comment").focus();
        </script>
        </body>
HTML;
    
    
?>