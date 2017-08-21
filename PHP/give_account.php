<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ERROR | E_PARSE);


$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$username = $json_array["username"];





$m= new MongoClient("mongodb://127.0.0.1:27017");
$db = $m->r_db;
$coll = $username.'_summary';

$collection = $db->$coll;
$query = array("type"=>"account");
 $cursor = $collection->find($query);
   // iterate cursor to display title of documents
	$result1 = array();
	
   foreach ($cursor as $document) {
	  // echo $document[account_number];
	   array_push($result1, $document[account_number]);
      
   }
   $accnt = $result1[0]."\n";
   $i=1;
	while($result1[$i]){
		$accnt = $accnt.$result1[$i]."\n";
		$i++;
	}

		$result = array(
		"msg" => "Your accounts are as follows :".$accnt."Enter appropriate account number" ,
		"success"=>1
	);
	
	

echo json_encode($result);

?>