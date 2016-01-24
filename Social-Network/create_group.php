<?php
    session_start();
    include "connect.php";
    echo "<body bgcolor='darkgrey'>";
        echo "<link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>";
        echo "<h1 id='h1' class='headers'>
        <table>
            <tr>
                <td id='h1'><img src='angry.png'></td>
            </tr>
        </table></h1>";
        
    $login = $_SESSION['login'];
    $g_name = $_POST['g_name'];
    
    $query = "INSERT INTO groups(g_name) VALUES('$g_name')";
    mysql_query($query) or die (mysql_error());
    
    $query = "SELECT g_id FROM groups WHERE g_name = '$g_name'";
    $result = mysql_query($query) or die (mysql_error());
    $row = mysql_fetch_array($result);
    $g_id = $row['g_id'];
    
    $query = "INSERT INTO members VALUES('$login', '$g_id')";
    mysql_query($query) or die(mysql_error());
    
    echo <<<HTML
            <script>
                alert("Your group has been created");
                location.href = "tigerface.php";
            </script>
            </body>
HTML;
?>