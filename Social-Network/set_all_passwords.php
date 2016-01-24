<?php

mysql_connect("localhost", "root", "slatehill")
	or die("Failed to connect to MySQL: " . mysql_error());

mysql_select_db("tigerface3")
	or die("Failed to open tigerface: " . mysql_error());

$query = "INSERT INTO passwords VALUES ('timber', '" . crypt('timber', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('bimbo', '" . crypt('bimbo', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('crazycharlie', '" . crypt('crazycharlie', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('007', '" . crypt('007', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('elong', '" . crypt('elong', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('jlo', '" . crypt('jlo', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('justjess', '" . crypt('justjess', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('twin2', '" . crypt('twin2', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('twin1', '" . crypt('twin1', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('jess', '" . crypt('jess', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('psy', '" . crypt('psy', 'tigerface') . "')";
mysql_query($query);

$query = "INSERT INTO passwords VALUES ('rkoether', '" . crypt('mathguy', 'tigerface') . "')";
mysql_query($query);

?>