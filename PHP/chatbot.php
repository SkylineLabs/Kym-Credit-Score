<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);

error_reporting(E_ALL);

$json       = file_get_contents('php://input');
$json_array = json_decode($json, true);

$action = $json_array["action"];
$month = (int)$json_array["month"];
$year = (int)$json_array["year"];

if($json_array["action"]=="Summary"){
  getSummary($json_array["username"],$json_array["month"],$json_array["year"]);
}

function getSummary($username,$month,$year ){
	global $read_con,$write_con;
	$response = array();
	$m= new MongoClient("mongodb://localhost:27017");
	$db = $m->r_db;
	$collection = 'users';
	$cursor = $collection->find(array('name' => $username));
	if($result){
	$response["success"]=1;
	echo json_encode($response);
	}
	else{
	$response["success"]=0;
	echo json_encode($response);
	}
}

$response = array();
$response["success"]=1;
$response["message"]=$message;
echo json_encode($response);

?>