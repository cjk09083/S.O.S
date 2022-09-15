<?

    $conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");

    $arr = array(array(), array(), array(), array(), array(), array(), array());

    if(isset($_GET['device_id']) && isset($_GET['year_time_start']) && isset($_GET['month_time_start']) && isset($_GET['day_time_start']) && 
           isset($_GET['year_time_end']) && isset($_GET['month_time_end']) && isset($_GET['day_time_end']))
    {
        $device_id = $_GET['device_id'];
        $year_time_start = $_GET['year_time_start'];
        $month_time_start = $_GET['month_time_start'];
        $day_time_start = $_GET['day_time_start'];
        $year_time_end = $_GET['year_time_end'];
        $month_time_end = $_GET['month_time_end'];
        $day_time_end = $_GET['day_time_end'];
        $day_time0 = '00:00:00';
        $day_time1 = '23:59:59';
        $time_start = $year_start.$year_time_start.'-'.$month_time_start.'-'.$day_time_start.' '.$day_time0;
        $time_end = $year_start.$year_time_end.'-'.$month_time_end.'-'.$day_time_end.' '.$day_time1;
        // echo $time_start. "<br>\n";
        // echo $time_end. "<br>\n";
        $index = 0;
        
        $sql = "SELECT MIN(temp) AS temp_min, MAX(temp) AS temp_max, AVG(temp) AS temp_avg, 
                       MIN(humid) AS humid_min, MAX(humid) AS humid_max, AVG(humid) AS humid_avg, date_format(created_at, '%Y%m%d') AS created_at 
                FROM temphumid_v01 where device_id = '$device_id' and created_at between '$time_start' and '$time_end' GROUP BY date_format(created_at, '%Y%m%d')";
            
        $result = mysqli_query($conn, $sql);
        


        while($data = mysqli_fetch_array($result, MYSQL_NUM))
        {
            $temp_min = round($data[0],2);
            $temp_max = round($data[1],2);
            $temp_avg = round($data[2],2);
            $humid_min = round($data[3],2);
            $humid_max = round($data[4],2);
            $humid_avg = round($data[5],2);
            $created_at = $data[6].'090000';
            
            
            $created_at = strtotime($created_at).'000';
            

            $arr[$index][0] = $created_at;
            $arr[$index][1] = $temp_min;
            $arr[$index][2] = $temp_max;
            $arr[$index][3] = $temp_avg;
            $arr[$index][4] = $humid_min;
            $arr[$index][5] = $humid_max;
            $arr[$index][6] = $humid_avg;

            
            // echo $created_at . " : " . $temp_avg . ", " . $temp_max . ", " . $temp_min. "<br>\n";
            // echo $created_at . " = " . $humid_avg . ", " . $humid_max . ", " . $humid_min. "<br>\n";

            $index++;
        }

    }
?>
<html>
   <head>
      <title>Temp & Humid select graph</title>
      <script src = "https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js">
      </script>
      <script src = "https://code.highcharts.com/highcharts.js"></script>  
      <script src = "https://code.highcharts.com/highcharts-more.js"></script>  
   </head>
   
   <body>
      <script language = "JavaScript">
         $(document).ready(function() {  
            var ranges =   [
                [<? echo $arr[0][0] ?>, <? echo $arr[0][1] ?>, <? echo $arr[0][2] ?>],
                [<? echo $arr[1][0] ?>, <? echo $arr[1][1] ?>, <? echo $arr[1][2] ?>],
                [<? echo $arr[2][0] ?>, <? echo $arr[2][1] ?>, <? echo $arr[2][2] ?>],
                [<? echo $arr[3][0] ?>, <? echo $arr[3][1] ?>, <? echo $arr[3][2] ?>],
                [<? echo $arr[4][0] ?>, <? echo $arr[4][1] ?>, <? echo $arr[4][2] ?>],
                [<? echo $arr[5][0] ?>, <? echo $arr[5][1] ?>, <? echo $arr[5][2] ?>],
                [<? echo $arr[6][0] ?>, <? echo $arr[6][1] ?>, <? echo $arr[6][2] ?>],
                [<? echo $arr[7][0] ?>, <? echo $arr[7][1] ?>, <? echo $arr[7][2] ?>],
                [<? echo $arr[8][0] ?>, <? echo $arr[8][1] ?>, <? echo $arr[8][2] ?>],
                [<? echo $arr[9][0] ?>, <? echo $arr[9][1] ?>, <? echo $arr[9][2] ?>]
            ];
            var averages = [
                [<? echo $arr[0][0] ?>, <? echo $arr[0][3] ?>],
                [<? echo $arr[1][0] ?>, <? echo $arr[1][3] ?>],
                [<? echo $arr[2][0] ?>, <? echo $arr[2][3] ?>],
                [<? echo $arr[3][0] ?>, <? echo $arr[3][3] ?>],
                [<? echo $arr[4][0] ?>, <? echo $arr[4][3] ?>],
                [<? echo $arr[5][0] ?>, <? echo $arr[5][3] ?>],
                [<? echo $arr[6][0] ?>, <? echo $arr[6][3] ?>],
                [<? echo $arr[7][0] ?>, <? echo $arr[7][3] ?>],
                [<? echo $arr[8][0] ?>, <? echo $arr[8][3] ?>],
                [<? echo $arr[9][0] ?>, <? echo $arr[9][3] ?>]
            ];

            var title = {
               text: 'Temperatures'   
            };    
            var xAxis = {
               type: 'datetime'
                
                
            };
            var yAxis = {
               title: {
                  text: null
               }      
            };
            var tooltip = {
               shared: true,
               crosshairs: true,
               valueSuffix: '\xB0C'
            };
            var legend = {      
            }    
            var series = [
               {
                  name: 'Temperature avg',
                  data: averages,
                  pointStart: <? echo $arr[0][0] ?>,
                  zIndex: 1,
                  marker: {
                     fillColor: 'white',
                     lineWidth: 2,
                     lineColor: Highcharts.getOptions().colors['#ff0000']
                  }
               }, 
               {
                  name: 'Range',
                  data: ranges,
                  pointStart: <? echo $arr[0][0] ?>,
                  type: 'arearange',
                  lineWidth: 0,
                  linkedTo: ':previous',
                  color: Highcharts.getOptions().colors['#ff0000'],
                  fillOpacity: 0.3,
                  zIndex: 0
               }
            ];  
            
            var json = {};   
            json.title = title;    
            json.xAxis = xAxis;
            json.yAxis = yAxis;
            json.tooltip = tooltip;
            json.legend = legend;     
            json.series = series;
            $('#container').highcharts(json); 
         });





         $(document).ready(function() {  
            var ranges1 =   [
                [<? echo $arr[0][0] ?>, <? echo $arr[0][4] ?>, <? echo $arr[0][5] ?>],
                [<? echo $arr[1][0] ?>, <? echo $arr[1][4] ?>, <? echo $arr[1][5] ?>],
                [<? echo $arr[2][0] ?>, <? echo $arr[2][4] ?>, <? echo $arr[2][5] ?>],
                [<? echo $arr[3][0] ?>, <? echo $arr[3][4] ?>, <? echo $arr[3][5] ?>],
                [<? echo $arr[4][0] ?>, <? echo $arr[4][4] ?>, <? echo $arr[4][5] ?>],
                [<? echo $arr[5][0] ?>, <? echo $arr[5][4] ?>, <? echo $arr[5][5] ?>],
                [<? echo $arr[6][0] ?>, <? echo $arr[6][4] ?>, <? echo $arr[6][5] ?>],
                [<? echo $arr[7][0] ?>, <? echo $arr[7][4] ?>, <? echo $arr[7][5] ?>],
                [<? echo $arr[8][0] ?>, <? echo $arr[8][4] ?>, <? echo $arr[8][5] ?>],
                [<? echo $arr[9][0] ?>, <? echo $arr[9][4] ?>, <? echo $arr[9][5] ?>]
            ];
            var averages1 = [
                [<? echo $arr[0][0] ?>, <? echo $arr[0][6] ?>],
                [<? echo $arr[1][0] ?>, <? echo $arr[1][6] ?>],
                [<? echo $arr[2][0] ?>, <? echo $arr[2][6] ?>],
                [<? echo $arr[3][0] ?>, <? echo $arr[3][6] ?>],
                [<? echo $arr[4][0] ?>, <? echo $arr[4][6] ?>],
                [<? echo $arr[5][0] ?>, <? echo $arr[5][6] ?>],
                [<? echo $arr[6][0] ?>, <? echo $arr[6][6] ?>],
                [<? echo $arr[7][0] ?>, <? echo $arr[7][6] ?>],
                [<? echo $arr[8][0] ?>, <? echo $arr[8][6] ?>],
                [<? echo $arr[9][0] ?>, <? echo $arr[9][6] ?>]
            ];

            var title = {
               text: 'Humidity'   
            };    
            var xAxis = {
               type: 'datetime'
                
                
            };
            var yAxis = {
               title: {
                  text: null
               }      
            };
            var tooltip = {
               shared: true,
               crosshairs: true,
               valueSuffix: '%'
            };
            var legend = {      
            }    
            var series = [
               {
                  name: 'Humidity avg',
                  data: averages1,
                  pointStart: <? echo $arr[0][0] ?>,
                  zIndex: 1,
                  marker: {
                     fillColor: 'white',
                     lineWidth: 2,
                     lineColor: Highcharts.getOptions().colors[0]
                  }
               }, 
               {
                  name: 'Range',
                  data: ranges1,
                  pointStart: <? echo $arr[0][0] ?>,
                  type: 'arearange',
                  lineWidth: 0,
                  linkedTo: ':previous',
                  color: Highcharts.getOptions().colors[0],
                  fillOpacity: 0.3,
                  zIndex: 0
               }
            ];  
            
            var json = {};   
            json.title = title;    
            json.xAxis = xAxis;
            json.yAxis = yAxis;
            json.tooltip = tooltip;
            json.legend = legend;     
            json.series = series;
            $('#container2').highcharts(json); 
         });
      </script>

      <div id = "container" style = "width: 1550px; height: 400px; margin: 0 auto"></div>
      <div id = "container2" style = "width: 1550px; height: 400px; margin: 0 auto"></div>
   </body>
   
</html>