<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ERROR | E_PARSE);


$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$username = $json_array["username"];
$amount = $json_array["amount"];

$amount = (int)$amount;
$m= new MongoClient("mongodb://127.0.0.1:27017");
$db = $m->r_db;
$coll = 'user_info';
$collection = $db->$coll;
$query = array("name"=>$username);
$cursor = $collection->find($query);
   
   foreach($cursor as $doc){
	   
	$current_balance = $doc[wallet_balance];
	$current_balance = (int)$current_balance;
	$new_balance = $current_balance - $amount;
	$var = $collection->update(array("name"=>$username),array('$set'=>array("wallet_balance"=>$new_balance)));

	$result = array(
				"success" => 1
			);
			
   }
echo json_encode($result);

?>

