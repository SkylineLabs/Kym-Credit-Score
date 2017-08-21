<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ERROR | E_PARSE);


$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$type = $json_array["type"];
$amount = (int)$json_array["amount"];
$account_number = $json_array["account_number"];
$p_d = (int)$json_array["p_d"];


$m= new MongoClient("mongodb://localhost:27017");
$db = $m->r_db;
$name = $json_array["username"];
$additional = '_payments';
$name = $name.$additional;
$collection = $db->$name;

$item = $collection->findOne(array('account_number' => $account_number, 'type' => $type, 'amount' => $amount));
if($item)
{
	$value1 = $item['timely_payment'];
	$value2 = $item['default_payment'];
	if($p_d) {
	$value1 = $value1 +1;
	}
	else {
		$value2 = $value2 +1;

	}
	$var = $collection->update(array("account_number"=>$account_number, "type" => $type, "amount" => $amount),array('$set'=>array("timely_payment"=>$value1 , "default_payment" =>$value2)),array('$upsert'=>true));

}

else
{
	$value1 = 0;
	$value2 = 0;
	if($p_d) {
	$value1 = $value1 +1;
	}
	else {
		$value2 = $value2 +1;

	}
	
	$document = array( 
      "account_number" => $account_number, 
      "amount" => $amount, 
      "timely_payment" => $value1,
	  "default_payment" => $value2,
      "type" => $type, 

   );
	
$collection->insert($document);
	
}


$response = array();

$message = 'Message';

$response["success"]=1;
$response["message"]=$message;
echo json_encode($response);

?>