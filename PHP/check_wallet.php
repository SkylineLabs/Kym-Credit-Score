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
   // iterate cursor to display title of documents
	foreach($cursor as $document){
		
	$current_balance = $document[wallet_balance];
	$current_balance = (int)$current_balance;
	$credit_limit = $document[credit_limit];
	$credit_limit = (int)$credit_limit;
	$credit_balance = $document[credit_balance];	
	$credit_balance = (int)$credit_balance;
	
	if($amount<=$current_balance){
		
	$result = array(
				"amount" => $amount,
				"credit_limit" => $credit_limit,
				"credit_balance" => $credit_balance,
				"output" => 1
			);
	}
	
	elseif((($current_balance>0)&&($amount<=($current_balance+($credit_limit-$credit_balance))))||(($current_balance<0)&&($amount<=($credit_limit-$credit_balance)))){
	if($current_balance>0){
	$credit_balance = $credit_balance + ($amount-$current_balance);
	
	}
	else{
		$credit_balance = $credit_balance + $amount;
	}
	$current_balance = $current_balance - $amount;
	$var = $collection->update(array("name"=>$username),array('$set'=>array("credit_balance"=>$credit_balance)));

	$var2 = $collection->update(array("name"=>$username),array('$set'=>array("wallet_balance"=>$current_balance)));
	
	$result = array(
				"amount" => $amount,
				"credit_limit" => $credit_limit,
				"credit_balance" => $credit_balance,
				"output" => 2
			);
	}
	
	else{
		$add = $amount-$credit_balance;
	$result = array(
				"amount" => $amount,
				"credit_limit" => $credit_limit,
				"credit_balance" => $credit_balance,
				"add_money" => $add,
				"output" => 3
			);
	}
	
	
			
	}
echo json_encode($result);

?>

