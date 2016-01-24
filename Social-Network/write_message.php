<?php
    session_start();
    $_SESSION['to_user'] = $_POST['to_user'];
    $to_user = $_POST['to_user'];
    echo <<<HTML
        <body bgcolor='darkgrey'>
        <script>
            function $(id){
            return document.getElementById(id);
            }
            function validate(){
            if($("msg").value == ""){
                alert("Please enter a message");
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
                                </form></td></tr></table></td>
                    </tr>
                </table></h1>
        <h2>WRITE YOUR MESSAGE HERE</h2>
        <form method="post" action="send_message.php" onsubmit="return validate()">
           <textarea id="msg" rows="5" cols="50" name="msg"></textarea><br/>
            <input type="submit" value="Send Message to $to_user">
            <input type="reset">
        </form>
        <script>
        $("msg").focus();
        </script>
        </body>
HTML;
?>