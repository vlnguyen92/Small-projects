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
    $uname = $_POST['uname'];
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $sex = $_POST['sex'];
    $month = $_POST['month'];
    $day = $_POST['day'];
    $year = $_POST['year'];
    $pic = $_POST['pic'];
    $bday = $year . "-" . $month . "-" . $day;
    $pswd = crypt($_POST['pswd'], "tigerface");
    
    
    include "connect.php";
    
    $query = "DELETE FROM users WHERE uname = '$login'";
    mysql_query($query);
    
    $query = "DELETE FROM passwords WHERE uname = '$login'";
    mysql_query($query);
    
    $query = "INSERT INTO users VALUES ('$fname', '$lname', '$uname', '$sex', '$bday', TRUE, '$pic')";
    mysql_query($query) or die(mysql_error());
    
    session_destroy();
    session_start(); 
   
    $query = "INSERT INTO passwords VALUE('$uname', '$pswd')";
    mysql_query($query) or die(mysql_error());
    $_SESSION['login'] = $uname;
    $_SESSION['pswd'] = $pswd;

    
    
    
    echo <<<HTML
        <script>
                alert("Your profile was successfully changed");
                location.href = "tigerface.php";
            </script></body>
HTML;
    ?>