<?php
    session_start();
    echo "<body bgcolor='darkgrey'>";
        echo "<link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>";
        echo "<h1 id='h1' class='headers'>
        <table>
            <tr>
                <td id='h1'><img src='angry.png'></td>
            </tr>
        </table></h1>";
        
    $login = $_SESSION['login'];
    $p_comment = addslashes($_POST['p_comment']);
    
    include "connect.php";
    
    $query = "INSERT INTO posts(uname, p_comment) VALUES('$login', '$p_comment')";
    mysql_query($query) or die (mysql_error());
    
    echo <<<HTML
        <script>
            alert("Your comment has been posted");
            location.href = "tigerface.php";
        </script>
        </body>
HTML;
?>