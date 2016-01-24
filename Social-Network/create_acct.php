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
    
    $fname = $_POST['fname'];
    $lname = $_POST['lname'];
    $uname = $_POST['uname'];
    $pswd = $_POST['pswd2'];
    $sex = $_POST['sex'];
    $month = $_POST['month'];
    $day = $_POST['day'];
    $year = $_POST['year'];
    $pic = $_POST['pic'];
    $bday = $year . "-" . $month . "-" . $day;
    $crypt_pswd = crypt($pswd, "tigerface");
    
    include "connect.php"
    
    $query = "INSERT INTO users VALUES ('$fname', '$lname', '$uname', '$sex', '$bday', TRUE, '$pic')";
    mysql_query($query) or die("Insertion into 'users' failed: " . mysql_error());
    $query = "INSERT INTO passwords VALUE('$uname', '$crypt_pswd')";
    mysql_query($query) or die("Insertion into 'passwords' failed: " . mysql_error());
    $_SESSION['login'] = $uname;
    $_SESSION['pswd'] = $crypt_pswd;
	
    echo "<script>
                alert('Your account has been created!');
		location.href = 'tigerface.php';
                </script>
            </body>";
                


    }
    
    
    
?>