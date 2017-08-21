<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ERROR | E_PARSE);


$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$username = $json_array["username"];
$month = $json_array["month"];
$year = $json_array["year"];


$month = (int)$month;
$year = (int)$year;

$m= new MongoClient("mongodb://127.0.0.1:27017");
$db = $m->r_db;

$collection = $db->users;
$query = array("name"=>$username,"month"=>$month,"year"=>$year);
 $cursor = $collection->find($query);
   // iterate cursor to display title of documents
	
   foreach ($cursor as $document) {
	   
	   $result = array(
			"msg" => "Your number of credit transcations-".$document["number_transactions_credit"]."\n"." Average amount for credit transactions-".$document["averageamount_transactions_credit"]."\n"." Total amount for credit transactions-".$document["totalamount_transactions_credit"]."\n"." Number of debit transactions-".$document["number_transactions_debit"]."\n"." Average amount for debit transactions-".$document["averageamount_transactions_debit"]."\n"." Total amount for debit transactions-".$document["totalamount_transactions_debit"]."\n"." Number of accounts-".$document["no_of_accounts"]."\n"." Total account Balance-".$document["total_bank_balance"],
			"success" => 1
	   );
      
   }




echo json_encode($result);

?>