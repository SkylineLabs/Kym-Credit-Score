<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ERROR | E_PARSE);


$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$username = $json_array["username"];



$month = (int)$month;
$year = (int)$year;

$m= new MongoClient("mongodb://127.0.0.1:27017");
$db = $m->r_db;
$coll = 'user_info';
$collection = $db->$coll;
$query = array("name"=>$username);
$cursor = $collection->find($query);
   // iterate cursor to display title of documents
	
	
	$result = array(
				"success" => 1,
				
			);
			
   foreach ($cursor as $document) {
	   
		   $result = array(
				
				"credit_balance" => $document[credit_balance],
				"credit_limit" => $document[credit_limit],
				"wallet_balance" => $document[wallet_balance],
				"success" => 1
		   );
	   
      
   }

echo json_encode($result);

?>

