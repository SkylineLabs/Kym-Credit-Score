<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ERROR | E_PARSE);


$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$username = $json_array["username"];
$month = $json_array["month"];
$year = $json_array["year"];
$account_number = $json_array["account_number"];


$month = (int)$month;
$year = (int)$year;

$m= new MongoClient("mongodb://127.0.0.1:27017");
$db = $m->r_db;
$coll = $username.'_summary';
$collection = $db->$coll;
$query = array("account_number"=>$account_number,"month"=>$month,"year"=>$year);
$cursor = $collection->find($query);
   // iterate cursor to display title of documents
	
	
	$result = array(
				"success" => 1,
				"msg" => "Sorry, I didn't get that query"
			);
			
   foreach ($cursor as $document) {
	   
			
	   if($document[balance])
		{
		   $result = array(
				
				"msg" => "Your balance is : ".$document[balance],
				"success" => 1
		   );
	   }
      
   }

echo json_encode($result);

?>