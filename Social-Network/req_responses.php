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
    $from_user = $_POST['from_user'];
    $resp = $_POST['resp'];
    
    if($resp == 'yes'){
        $query = "INSERT INTO friends(user1, user2) VALUES('$login', '$from_user')";
        mysql_query($query) or die (mysql_error());
        $query = "INSERT INTO friends(user1, user2) VALUES('$from_user', '$login')";
        mysql_query($query) or die (mysql_error());
        $query = "DELETE FROM requests WHERE from_user = '$from_user' AND to_user = '$login'";
        mysql_query($query) or die (mysql_error());
        echo <<<HTML
            <script>
                location.href = "show_friends.php";
            </script>
            </body>
HTML;
        
    }elseif($resp == 'no'){
        $query = "DELETE FROM requests WHERE from_user = '$from_user' AND to_user = '$login'";
        mysql_query($query) or die (mysql_error());
        echo <<<HTML
            <script>
                location.href = "show_friends.php";
            </script>
            </body>
HTML;
    }
    echo <<<HTML
            <script>
                location.href = "show_friends.php";
            </script>
            </body>
HTML;
    
            
?>