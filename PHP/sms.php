<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ALL);

$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$bankname = $json_array["bank_name"];
$amount = (int)$json_array["amount"];
$balance = (int)$json_array["balance"];
$account_number = $json_array["account_number"];
$type = $json_array["type"];
$month = (int)$json_array["month"];
$year = (int)$json_array["year"];
$vendor = $json_array["vendor"];

$m= new MongoClient("mongodb://localhost:27017");
$db = $m->r_db;
$name = $json_array["username"];
$collection = $db->$name;


$document = array( 
      "bank_name" => $bankname, 
      "amount" => $amount, 
      "balance" => $balance,
	  "account_number" => $account_number,
      "type" => $type, 
      "month" => $month, 
      "year" => $year, 
	  "vendor" => $vendor,
   );
	
$collection->insert($document);

$response = array();

$message = 'Message';

$response["success"]=1;
$response["message"]=$message;
echo json_encode($response);

?>