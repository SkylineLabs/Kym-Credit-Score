<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ALL);

$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);


$amount = (int)$json_array["amount"];
$type = $json_array["type"];


$m= new MongoClient("mongodb://localhost:27017");
$db = $m->r_db;
$name = $json_array["username"];
$collection = $db->$name;


$document = array( 
      
      "amount" => $amount, 
      "type" => $type, 
   );
	
$collection->insert($document);

$response = array();

$message = 'Message';

$response["success"]=1;
$response["message"]=$message;
echo json_encode($response);

?>