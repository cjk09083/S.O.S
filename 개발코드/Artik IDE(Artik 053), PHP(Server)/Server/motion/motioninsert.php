<?

if (isset($_POST['motion']) && isset($_POST['device_id']))
{ // check if both fields are set
	
	$motion_id = $_POST['motion'];
	$device_id = $_POST['device_id'];

	$conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");

// /* check connection */
// if ($conn->connect_errno) {
//     printf("Connect failed: %s\n", $conn->connect_error);
//     exit();
// }
	
	
	$hour_time = date('G');
	$daily_time = date('D');
	$year_time = date('y');
	$month_time = date('m');
	$day_time = date('j');
		
	$sql  ="INSERT INTO motion_v01
		(
			motion_id,
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
			'$motion_id',
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