<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ERROR | E_PARSE);


$m= new MongoClient("mongodb://localhost:27017");
$db = $m->r_db;
$name = 'try';
$type = 'EMI';
$amount = "200";
$account_number = '123';
$p_d = 0;
$additional = '_payments';
$name = $name.$additional;
$collection = $db->$name;

$item = $collection->findOne(array('account_number' => $account_number, 'type' => $type, 'amount' => $amount));
if($item)
{
	$value1 = $item['timely_payment'];
	$value2 = $item['default_payment'];
	echo $value1;echo $value2;
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




?>