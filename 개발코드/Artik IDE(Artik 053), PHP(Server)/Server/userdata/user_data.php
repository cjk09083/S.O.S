<?

if(isset($_POST['input']))
{
	
	$conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");


	$input = $_POST['input'];
	if($input == "userinsert")
	{
		if(isset($_POST['userID']) && isset($_POST['userPw']) && isset($_POST['userName']) && isset($_POST['userAdd']) && isset($_POST['userNum']) && isset($_POST['userRef']))
		{
			$userID = $_POST['userID'];
			$userPw = $_POST['userPw'];
			$userName = $_POST['userName'];
			$userAdd = $_POST['userAdd'];
			$userNum = $_POST['userNum'];
			$userRef = $_POST['userRef'];
		
			
			$sql  ="INSERT INTO userdata_v01
				(
					created_at, 
					user_ID,
					user_PW,
					user_NAME,
					user_ADD,
					user_NUM,
					user_REF
				)
				
				VALUES
				(
					now(),
					'$userID',
					'$userPw',
					'$userName',
					'$userAdd',
					'$userNum',
					'$userRef'
				)";

			mysqli_query($conn, $sql);

		}	
	}
	else if($input == "seninsert")
	{
		if(isset($_POST['userID']) && isset($_POST['userPw']) && isset($_POST['userRef']) && isset($_POST['senID']) && isset($_POST['seninfo']))
		{
			$userID = $_POST['userID'];
			$userPw = $_POST['userPw'];
			$userRef = $_POST['userRef'];
			$senID = $_POST['senID'];
			$seninfo = $_POST['seninfo'];
		
			
			$sql  ="INSERT INTO userdata_v01
				(
					created_at, 
					user_ID,
					user_PW,
					user_REF,
					sen_ID,
					sen_INFO
				)
				
				VALUES
				(
					now(),
					'$userID',
					'$userPw',
					'$userRef',
					'$senID',
					'$seninfo'
				)";

			mysqli_query($conn, $sql);
		}
	}
	else if($input == "update")
	{
		if(isset($_POST['userID']) && isset($_POST['userPw']) && isset($_POST['senID']) && isset($_POST['seninfo']))
		{
			$userID = $_POST['userID'];
			$userPw = $_POST['userPw'];
			$senID = $_POST['senID'];
			$seninfo = $_POST['seninfo'];

        	$sql = "UPDATE userdata_v01
        		    SET sen_INFO = '$seninfo'
  					WHERE user_ID = '$userID' and user_PW = '$userPw' and sen_ID = '$senID'";

			mysqli_query($conn, $sql);
		}
	}
	else if($input == "delete")
	{
		if(isset($_POST['userID']) && isset($_POST['userPw']) && isset($_POST['userRef']) && isset($_POST['senID']))
		{
			$userID = $_POST['userID'];
			$userPw = $_POST['userPw'];
			$userRef = $_POST['userRef'];
			$senID = $_POST['senID'];
			$seninfo = $_POST['seninfo'];
			$sql = "DELETE FROM userdata_v01
        		    WHERE user_ID = '$userID' and user_PW = '$userPw' and user_REF = '$userRef' and sen_ID = '$senID'";

			mysqli_query($conn, $sql);
		}
	}
}

else
{
	echo "fail";
}
?>
