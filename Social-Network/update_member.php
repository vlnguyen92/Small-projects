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
    
    $query = "SELECT * FROM groups";
    $result = mysql_query($query) or die (mysql_error());
    
    echo <<<HTML
                <table border='2' cellpadding='3' bgcolor='white'>
                    <tr align="left">
                        <th>Group</th>
                        <th>Join/Quit</th>
                    </tr>
HTML;

    while($row = mysql_fetch_array($result)){
        $g_id = $row['g_id'];
        $g_name = $row['g_name'];
        
        echo <<<HTML
                    <tr>
                        <td>$g_name</td>
                        <td><form method="post" action="update_group.php">
                        <input type="hidden" name="g_id" value="$g_id">
HTML;

        $query_member = "SELECT * FROM members WHERE uname = '$login' AND g_id = '$g_id'";
        $result_member = mysql_query($query_member) or die (mysql_error());
        
        if(mysql_num_rows($result_member) == 0){
            echo <<<HTML
                        <input type="radio" name="update" value="join">JOIN
                        <input type="radio" name="update" value="quit" disabled="disabled">QUIT<br/>
HTML;
        }else{
            echo <<<HTML
                        <input type="radio" name="update" value="join" disabled="disabled">JOIN
                        <input type="radio" name="update" value="quit">QUIT<br/>
HTML;
        }
        
        echo <<<HTML
                    <input type="submit">
                    </form>
HTML;
    }
    echo "</body>";

    
?>