<?php

        session_start();
	
        echo <<<HTML
	<body bgcolor='darkgrey'>
		<script>
		function $(id){
            return document.getElementById(id);
        }
        function validate(){
            if($("fname").value == ""){
                alert("Please enter you first name");
                return false;
            }
            if($("lname").value == ""){
                alert("Please enter your last name");
                return false;
            }
            if($("uname_n").value == ""){
                alert("Please enter your username");
                return false;
            }
            if($("pswd1").value == ""){
                alert("Please enter your password");
                return false;
            }
            if($("pswd2").value == ""){
                alert("Please verify your password");
                return false;
            }
            if($("pswd2").value != $("pswd1").value){
                alert("Please verify with the same password");
                return false;
            }
            return true;
        }
	</script>
HTML;
        echo "<link rel='stylesheet' type='text/css' href='tigerfacestyles.css'/>";
        echo "<h1 id='h1' class='headers'>
        <table>
            <tr>
                <td id='h1'><img src='angry.png'></td>
            </tr>
        </table></h1>";
        
        $login = $_SESSION['login'];
        $pswd = $_SESSION['pswd'];
        include "connect.php";
        $query = "SELECT * FROM users WHERE uname = '$login'";
        $result = mysql_query($query) or die(mysql_error());
        $row = mysql_fetch_array($result);
        
        $fname = $row['fname'];
        $lname = $row['lname'];
        $uname = $row['uname'];
        $bday = $row['bday'];
        $sex = $row['sex'];
        $pic = $row['pic'];
        
        echo <<<HTML
        <form method="post" action="update_profile.php" onsubmit="return validate()">
                    First Name:
                    <input type="text" id="fname" name="fname" value="$fname"><br/>
                    Last Name:
                    <input type="text" id="lname" name="lname" value="$lname"><br/>
                    User Name:
                    <input type="text" id="uname" name="uname" value="$uname"><br/>
                    Password:
                    <input type="password" id="pswd1" name="pswd"><br/>
		    Re-enter password:
		    <input type="password" id="pswd2" name="pswd"><br/>
                    Sex:
HTML;
            
        if($sex == 'M'){
                echo <<<HTML
                <input type="radio" value="M" name="sex" checked="true">Male
                <input type="radio" value="F" name="sex">Female<br/>
HTML;
        }else{
                echo<<<HTML
                <input type="radio" value="M" name="sex" >Male
                <input type="radio" value="F" name="sex" checked="true">Female<br/>
                Birthday:
HTML;
        }
        
        $year = substr($bday, 0, 4);
        $month = substr($bday, 5, 2);
        $day = substr($bday, 8, 2);
        
        $month_a = array("1" => "January", "2" => "February", "3" => "March", "4" => "April",
	"5" => "May", "6" => "June", "7" => "July", "8" => "August", "9" => "September",
	"10" => "October", "11" => "November", "12" => "December");
        
        echo "<select name='month'>";
        for ($i = 1; $i <= 12; $i++){
        if ($i == $month)
		echo "<option value='$i' selected='selected'>$month_a[$i]</option>";
	else
		echo "<option value='$i'>$month_a[$i]</option>";
        }
        
        echo "</select><select name='day'>";
        for ($i = 1; $i < 31; $i++){
        if ($i == $day)
		echo "<option value='$i' selected='selected'>$i</option>";
	else
		echo "<option value='$i'>$i</option>";
        }
                
        echo "</select><select name='year'>";
        for ($i = 2013; $i >= 1900; $i--){
	if ($i == $year)
		echo "<option value='$i' selected='selected'>$i</option>";
	else
		echo "<option value='$i'>$i</option>";
        }
        echo "</select><br/>";

        
                echo <<<HTML
                    </select><br/>
                    Profile Pic:
                    <input type="text" name="pic" value="$pic"><br/>
                    <input type="submit" value="Save Profile">
                </form>
HTML;
?>