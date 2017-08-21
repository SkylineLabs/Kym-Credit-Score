<?php
   // connect to mongodb
   //$m = new MongoClient();
	$m= new MongoClient("mongodb://localhost:27017");
   echo "Connection to database successfully";
   // select a database
   $db = $m->db1;
	
	$name = $_GET["name"];
	$type = $_GET["type"];
	$amount = $_GET["amount"];
	$balance = $_GET["balance"];
	$bank_name = $_GET["bank_name"];
	$timestamp = $_GET["timestamp"];
	
	$collection = $db->$name;
	
	
	$document = array( 
      "type" => $type, 
      "amount" => $amount, 
      "balance" => $balance,
      "bank_name" => $bank_name,
      "timestamp" => $timestamp
   );
	
   $collection->insert($document);
   echo "Document inserted successfully";
 ?>