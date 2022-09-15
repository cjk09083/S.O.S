<?
	$conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");



  	if(isset($_GET['userID']) && isset($_GET['userPw']) && isset($_GET['userRef']))
	{
		$userID = $_GET['userID'];
		$userPw = $_GET['userPw'];
		$userRef = $_GET['userRef'];

		if($userRef == 1)
		{
			$sql = "SELECT * FROM userdata_v01 where user_ID = '$userID' and user_PW = '$userPw' and user_REF = '$userRef'";
			$result = mysqli_query($conn, $sql);
		    while($data = mysqli_fetch_array($result, MYSQL_NUM))
		    {
		    	echo $data[8] . $data[9]."\n";
		    	//echo $data[9] . "<br>\n";
		    }
		}
		
	}
?>