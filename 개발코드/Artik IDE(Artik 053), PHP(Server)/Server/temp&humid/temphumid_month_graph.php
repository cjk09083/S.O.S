<?

    $conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");

    $arr = array(array(), array());



    if(isset($_GET['device_id']) && isset($_GET['year_time'])){

        $device_id = $_GET['device_id'];
        $year_time = $_GET['year_time'];

        $month = 1;
        
        while($month != 13)
        {
            $sql = "SELECT AVG(temp) AS temp, AVG(humid) AS humid FROM temphumid_v01
                    WHERE month_time = $month and device_id = '$device_id' and year_time = '$year_time'";
                
            $result = mysqli_query($conn, $sql);
            $row = mysqli_fetch_array($result);
            
            $temp = (float)$row['temp'];
            $temp = round($temp,2);
            
            $humid = (float)$row['humid'];
            $humid = round($humid,2);
            
            $arr[$month][1] = $temp;
            $arr[$month][2] = $humid;

            // echo $month . " = ". $temp . "<br>\n";
            
            $month++;
        }
    }
?>




<html>
  <head>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="http://code.highcharts.com/highcharts.js"></script>
    
    <script>
        $(function () {
            $('#container').highcharts({
                chart: {
                      type: 'line'
                },
                title: {
                    text: 'Temp & Humid AVG Value',
                    style: {
                        fontSize: '40px'
                    }
                },
                subtitle: {
                    text: 'January ~ December',
                    style: {
                        fontSize: '24px'
                    }
                },
                xAxis: {                    
                    categories: ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May', 'Jun.', 'Jul.', 'Aug.', 'Sep.', 'Oct.', 'Nov.', 'Dec.'],
                    labels: {
                        style: {
                            fontSize: '20px'
                        }
                    }
                },
                yAxis: {
                    title: {
                        text: 'Temperature(`C)  &  Humidity(%)',
                        style: {
                            fontSize: '16px'
                        }                       
                    },
                    labels: {
                        style: {
                            fontSize: '20px'
                        }
                    }
                },
                plotOptions: {
                    line: {
                        dataLabels: {
                            enabled: true,
                            style: {
                                fontSize: '12px'
                            }
                        },
                        enableMouseTracking: true
                    }
                },
                series: [{
                    name: 'Temp',
                    data:  [<? echo $arr[1][1] ?>, <? echo $arr[2][1] ?>, <? echo $arr[3][1] ?>, <? echo $arr[4][1] ?>, <? echo $arr[5][1] ?>, <? echo $arr[6][1] ?>,
                            <? echo $arr[7][1] ?>, <? echo $arr[8][1] ?>, <? echo $arr[9][1] ?>, <? echo $arr[10][1] ?>, <? echo $arr[11][1] ?>, <? echo $arr[12][1] ?>]
                }, {
                    name: 'Humid',
                    data:  [<? echo $arr[1][2] ?>, <? echo $arr[2][2] ?>, <? echo $arr[3][2] ?>, <? echo $arr[4][2] ?>, <? echo $arr[5][2] ?>, <? echo $arr[6][2] ?>,
                            <? echo $arr[7][2] ?>, <? echo $arr[8][2] ?>, <? echo $arr[9][2] ?>, <? echo $arr[10][2] ?>, <? echo $arr[11][2] ?>, <? echo $arr[12][2] ?>]
                }]
            });
        });
    </script>
  </head>
  <body>
    <div id="container" style="min-width: 310px; height: 500px; margin: 0 auto"></div>
    
  </body>
</html>