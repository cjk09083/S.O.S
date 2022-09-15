<?
    $conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");



    if(isset($_GET['device_id']))
    {
    	$device_id = $_GET['device_id'];

    	if(isset($_GET['toilet_id']))
    	{
    		$toilet_id = $_GET['toilet_id'];
    		
    		if(isset($_GET['toilet_times']))
    		{

    			$time_end = date('Y-m-d 23:59:59', strtotime("-1 day"));
			    //echo $time_end . "<br>\n";
			    $time_start = date('Y-m-d 00:00:00', strtotime("-1 day"));
			    //echo $time_start . "<br>\n";

			    $toilet_time_start = 0;
			    $toilet_time_end = 0;
			    $toilet_flag = 0;
			    $toilet_longtime_flag = 0;

    			$sql = "SELECT * FROM motion_v01 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d %H%i%s')";



				$result = mysqli_query($conn, $sql);
		        while($data = mysqli_fetch_array($result, MYSQL_NUM))
		        {
		        	if($toilet_id == $data[1])
		        	{
		        		if($toilet_flag == 0)
		        		{
		        			$toilet_time_start = $data[2];
			        		//시작 시간 저장 완료
			        		$toilet_flag = 1;
		        		}
		        		else if($toilet_flag == 1)
		        		{
		        			$toilet_time_end = $data[2];
		        		}
		        	}
		        	if($toilet_id != $data[1] && $toilet_flag == 1)
		        	{
		        		if($toilet_time_end != 0)
		        		{
		        			
		        			//echo $toilet_time_end . "_" . $toilet_time_start . "<br>\n";
			        		$toilet_time_calc = strtotime($toilet_time_end) - strtotime($toilet_time_start);
			        		//echo $toilet_time_calc . "<br>\n";
			        		if($toilet_time_calc >= 300)
			        		{
			        			$toilet_longtime_flag++;
			        		}
		        		}
		        		
		        		$toilet_time_start = 0;
			    		$toilet_time_end = 0;
		        		
		        		$toilet_flag = 0;
		        		
		        	}
		        	//echo $data[2]."<br>\n";

		        }	
		        echo $toilet_longtime_flag;
    		}



    		// 0이면 화장실에 오래있음, 1이면 화장실에 오래있지 않음
	    	$toilet_long = 0;
	    	// 0이면 화장실에 안감, 1이면 화장실에 감
	    	$toilet_go_motion = 0;

	    	$time_end = date('Y-m-d H:i:s');
		    // echo $time_end . "<br>\n";
		    $time_start = date('Y-m-d H:i:s', strtotime("-20 minutes"));
		    // echo $time_start . "<br>\n";
	    	
	    	$sql = "SELECT * FROM motion_v01 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d %H%i%s')";
			$result = mysqli_query($conn, $sql);
	        while($data = mysqli_fetch_array($result, MYSQL_NUM))
	        {
	        	if($toilet_id == $data[1])
	        	{
	        		$toilet_go_motion = 1;
	        	}
	        	if($toilet_id != $data[1])
	        	{
	        		$toilet_long = 1;
	        	}
	        	//echo $data[1] . "<br>\n";
	        	// echo $data[2] . "<br>\n";
	        }
	        //화장실에 갔고 오래 있으면 오류
	        if($toilet_go_motion == 1 && $toilet_long == 0)
	        {
	        	echo 0;
	        }
	        //화장실에 간 데이터가 없으면
	        else if($toilet_go_motion == 0)
	        {
	        	echo 2;
	        }
	        // good!
	        else
	        {
	        	echo 1;
	        }
    	}

    	if(isset($_GET['time_data']))
    	{
    		
    		$time_data = 0;

    		$no_data = 0;
    		
    		// 8시간 측정
    		$time_step = 1;
    		$end_condition = 0;

    		$time_end = date('Y-m-d H:i:s');
		    // $time_end = '2018-09-06 00:00:00';
		    $time_start = date('Y-m-d H:i:s', strtotime("-8 hours"));
		    // $time_start = '2018-09-05 21:00:00';
		    	   
		   	while($end_condition == 0)
		   	{
		   		$sql = "SELECT * FROM motion_v01 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d %H%i%s')";
				$result = mysqli_query($conn, $sql);
		        while($data = mysqli_fetch_array($result, MYSQL_NUM))
		        {
		        	if($data[0] > 0)
		        	{
		        		$no_data = 1;
		        	}
		        	// echo $data[0] . "<br>\n";
		        	// echo $data[2] . "<br>\n";
		        }
		        if($no_data == 1)
		        {
		        	$end_condition = 1;
		        }
		        else if($no_data == 0)
		        {
		       		if($time_step == 1)
		       		{
		       			//12시간으로 변경
		       			$time_step = 2;
					    $time_start = date('Y-m-d H:i:s', strtotime("-12 hours"));
		       		}
		       		else if($time_step == 2)
		       		{
		       			//24시간으로 변경
		       			$time_step = 3;
					    $time_start = date('Y-m-d H:i:s', strtotime("-24 hours"));
		       		}
		       		else if($time_step == 3)
		       		{
		       			$end_condition = 1;
		       		}
		        }
		        
		   	}

		   	if($time_step == 1)
		   	{
		   		// 정상
		   		$time_data = 1;
		   	}
		   	else if($time_step == 2)
		   	{
		   		// 8시간 비정상
		   		$time_data = 2;
		   	}
		   	else if($time_step == 3 && $no_data == 1)
		   	{
		   		// 12시간 비정상
		   		$time_data = 3;
		   	}
		   	else if($time_step == 3 && $no_data == 0)
		   	{
		   		// 24시간 비정상
		   		$time_data = 4;
		   	}

		   	echo $time_data;
		}


		if(isset($_GET['one_place']))
    	{
    		$time_end = date('Y-m-d H:i:s');
		    // $time_end = '2018-09-04 11:51:49';
		    $time_start = date('Y-m-d H:i:s', strtotime("-8 hours"));
		    // $time_start = '2018-09-04 10:40:00';
		   	
		   	$init_cnt = 0;
		  	$one_place = 0;
		  	$place_step = 1;
		  	$place_data = 0;

		  	while($end_condition == 0)
		  	{
		  		$sql = "SELECT * FROM motion_v01 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d %H%i%s')";
				$result = mysqli_query($conn, $sql);
		        while($data = mysqli_fetch_array($result, MYSQL_NUM))
		        {
		        	
		        	if($init_cnt == 1)
		        	{
		        		if($motion_id != $data[1])
			        	{
			        		$one_place = 1;
			        	}
		        	}
		        	$motion_id = $data[1];
		        	$init_cnt = 1;
		        	//echo $data[1] . "<br>\n";
		        }

		        if($one_place == 1)
		        {
		        	$end_condition = 1;
		        }
		        if($one_place == 0)
		        {
		        	if($place_step == 1)
		        	{
		        		$place_step = 2;
					    $time_start = date('Y-m-d H:i:s', strtotime("-12 hours"));
		        	}
		        	else if($place_step == 2)
		        	{
		        		$place_step = 3;
		        		$time_start = date('Y-m-d H:i:s', strtotime("-24 hours"));
		        	}
		        	else if($place_step == 3)
		        	{
		        		$end_condition = 1;
		        	}
		        }
		  	}


		  	if($place_step == 1)
		   	{
		   		// 정상
		   		$place_data = 1;
		   	}
		   	else if($place_step == 2)
		   	{
		   		// 8시간 비정상
		   		$place_data = 2;
		   	}
		   	else if($place_step == 3 && $one_place == 1)
		   	{
		   		// 12시간 비정상
		   		$place_data = 3;
		   	}
		   	else if($place_step == 3 && $one_place == 0)
		   	{
		   		// 24시간 비정상
		   		$place_data = 4;
		   	}

	        echo $place_data;
    	}


	}
	    	
?>