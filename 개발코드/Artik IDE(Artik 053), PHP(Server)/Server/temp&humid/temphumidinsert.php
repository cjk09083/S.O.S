<?

if (isset($_POST['temp']) && isset($_POST['humid']) && isset($_POST['device_id']) && isset($_POST['temphumid_id']))
{ // check if both fields are set
	
	$temp = $_POST['temp'];
	$humid = $_POST['humid'];
	$device_id = $_POST['device_id'];
	$temphumid_id = $_POST['temphumid_id'];
	
	$conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");



	$hour_time = date('G');
	$daily_time = date('D');
	$year_time = date('y');
	$month_time = date('m');
	$day_time = date('j');
		

	$sql  ="INSERT INTO temphumid_v01
		(
			temphumid_id, 
			temp,
			humid,
			created_at, 
			hour_time, 
			daily_time, 
			year_time,
			month_time,
			day_time,
			device_id
		)
		
		VALUES
		(
			'$temphumid_id',
			'$temp',
			'$humid',
			now(),
			'$hour_time',
			'$daily_time',
			'$year_time',
			'$month_time',
			'$day_time',
			'$device_id'
		)";

	mysqli_query($conn, $sql);

}
?>