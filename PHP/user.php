<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$username = $json_array["username"];
$twitter = $json_array["twitter_handle"];

$m= new MongoClient("mongodb://localhost:27017");
$db = $m->r_db;
$name = 'user_info';
$collection = $db->$name;
$document = array( 
      "name" => $username, 
      "twitter_handle" => $twitter, 
	  "wallet_balance" =>0,
	  "credit_balance" =>0,
	  "credit_limit" =>500
   );
	
$collection->insert($document);

$response = array();
$response["success"]=1;
echo json_encode($response);

?>