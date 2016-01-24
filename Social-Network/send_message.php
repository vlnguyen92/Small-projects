<?php
    session_start();
    $login = $_SESSION['login'];
    $to_user = $_SESSION['to_user'];
    $msg = $_POST['msg'];
    
    include "connect.php";
    $query = "INSERT INTO msgs(from_user, to_user, msg) VALUES('$login', '$to_user', '$msg')";
    mysql_query($query) or die (mysql_error());
    
    echo <<<HTML
            <body bgcolor='darkgrey'>
            <link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>
            <h1 id='h1' class='headers'>
                <table>
                    <tr>
                        <td id='h1'><img src='angry.png'></td>
                    </tr>
                </table></h1>
            <script>
                alert('Your message was sent');
                location.href = "tigerface.php";
            </script></body>
HTML;
?>