<?

    $conn = new mysqli("localhost", "minsanggyu2", "jgh3460!", "minsanggyu2");

    $arr = array(
      array(),  array(),  array(),  array(),  array(),  array(),
      array(),  array(),  array(),  array(),  array(),  array(),
      array(),  array(),  array(),  array(),  array(),  array(),
      array(),  array(),  array(),  array(),  array(),  array()
    );

    $sum_arr = array();


    $arr_hour = 0;
    $arr_pos = 0;
    $arr_pos_check = 0;
    if(isset($_GET['year_time']) && isset($_GET['month_time']) && isset($_GET['day_time']) && isset($_GET['device_id'])){

      $year_time = $_GET['year_time'];
      $month_time = $_GET['month_time'];
      $day_time = $_GET['day_time'];
      $device_id = $_GET['device_id'];

        $sql = "SELECT * FROM motion_v01
                WHERE year_time = '$year_time' and month_time = '$month_time'
                  and day_time = '$day_time' and device_id = '$device_id'
                ORDER BY motion_id";
        $result = mysqli_query($conn, $sql);
        while($data = mysqli_fetch_array($result, MYSQL_NUM))
        {
          if($data[1] != $arr_pos_check)
          {
            $arr_pos++;
          }
          $arr_hour = $data[3];
          $arr[$arr_hour][$arr_pos] = $arr[$arr_hour][$arr_pos] + 1/10;
          $arr_pos_check = $data[1];
          //echo "arr_pos".$arr_pos;
        }

        
        // 시간과 위치 초기화 후 60분 모두 더해주기
        $arr_hour = 0;
        $arr_pos = 1;
        while($arr_pos != 32)
        {
          while($arr_hour != 24)
          {
            if($arr[$arr_hour][$arr_pos] == 0)
            {
              $arr[$arr_hour][$arr_pos] = 0;  
            }
            $sum_arr[$arr_pos] = $sum_arr[$arr_pos] + $arr[$arr_hour][$arr_pos];
            
            $arr_hour++;
          }
          $arr_hour = 0;
          $arr_pos++;
        }
    }else{

      print("error");


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
                    text: 'Day Motion Data SUM'
                },
                subtitle: {
                    text: '0~23hour'
                },
                xAxis: {                    
                    categories: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23']
                },
                yAxis: {
                    title: {
                        text: 'Minute(m)'
                    }
                },
                plotOptions: {
                    line: {
                        dataLabels: {
                            enabled: true,
                            style: {
                                fontSize: '20px'
                            }
                        },
                        enableMouseTracking: true
                    }
                },
                series: [{
                    name: 'No.1',
                    data:  [<? echo $arr[0][1] ?>, <? echo $arr[1][1] ?>, <? echo $arr[2][1] ?>, <? echo $arr[3][1] ?>, <? echo $arr[4][1] ?>, <? echo $arr[5][1] ?>,
                            <? echo $arr[6][1] ?>, <? echo $arr[7][1] ?>, <? echo $arr[8][1] ?>, <? echo $arr[9][1] ?>, <? echo $arr[10][1] ?>, <? echo $arr[11][1] ?>,
                            <? echo $arr[12][1] ?>, <? echo $arr[13][1] ?>, <? echo $arr[14][1] ?>, <? echo $arr[15][1] ?>, <? echo $arr[16][1] ?>, <? echo $arr[17][1] ?>,
                            <? echo $arr[18][1] ?>, <? echo $arr[19][1] ?>, <? echo $arr[20][1] ?>, <? echo $arr[21][1] ?>, <? echo $arr[22][1] ?>, <? echo $arr[23][1] ?>]
                }, {
                    name: 'No.2',
                    data:  [<? echo $arr[0][2] ?>, <? echo $arr[1][2] ?>, <? echo $arr[2][2] ?>, <? echo $arr[3][2] ?>, <? echo $arr[4][2] ?>, <? echo $arr[5][2] ?>,
                            <? echo $arr[6][2] ?>, <? echo $arr[7][2] ?>, <? echo $arr[8][2] ?>, <? echo $arr[9][2] ?>, <? echo $arr[10][2] ?>, <? echo $arr[11][2] ?>,
                            <? echo $arr[12][2] ?>, <? echo $arr[13][2] ?>, <? echo $arr[14][2] ?>, <? echo $arr[15][2] ?>, <? echo $arr[16][2] ?>, <? echo $arr[17][2] ?>,
                            <? echo $arr[18][2] ?>, <? echo $arr[19][2] ?>, <? echo $arr[20][2] ?>, <? echo $arr[21][2] ?>, <? echo $arr[22][2] ?>, <? echo $arr[23][2] ?>]
                }, {
                    name: 'No.3',
                    data:  [<? echo $arr[0][3] ?>, <? echo $arr[1][3] ?>, <? echo $arr[2][3] ?>, <? echo $arr[3][3] ?>, <? echo $arr[4][3] ?>, <? echo $arr[5][3] ?>,
                            <? echo $arr[6][3] ?>, <? echo $arr[7][3] ?>, <? echo $arr[8][3] ?>, <? echo $arr[9][3] ?>, <? echo $arr[10][3] ?>, <? echo $arr[11][3] ?>,
                            <? echo $arr[12][3] ?>, <? echo $arr[13][3] ?>, <? echo $arr[14][3] ?>, <? echo $arr[15][3] ?>, <? echo $arr[16][3] ?>, <? echo $arr[17][3] ?>,
                            <? echo $arr[18][3] ?>, <? echo $arr[19][3] ?>, <? echo $arr[20][3] ?>, <? echo $arr[21][3] ?>, <? echo $arr[22][3] ?>, <? echo $arr[23][3] ?>]
                }, {
                    name: 'No.4',
                    data:  [<? echo $arr[0][4] ?>, <? echo $arr[1][4] ?>, <? echo $arr[2][4] ?>, <? echo $arr[3][4] ?>, <? echo $arr[4][4] ?>, <? echo $arr[5][4] ?>,
                            <? echo $arr[6][4] ?>, <? echo $arr[7][4] ?>, <? echo $arr[8][4] ?>, <? echo $arr[9][4] ?>, <? echo $arr[10][4] ?>, <? echo $arr[11][4] ?>,
                            <? echo $arr[12][4] ?>, <? echo $arr[13][4] ?>, <? echo $arr[14][4] ?>, <? echo $arr[15][4] ?>, <? echo $arr[16][4] ?>, <? echo $arr[17][4] ?>,
                            <? echo $arr[18][4] ?>, <? echo $arr[19][4] ?>, <? echo $arr[20][4] ?>, <? echo $arr[21][4] ?>, <? echo $arr[22][4] ?>, <? echo $arr[23][4] ?>]
                }, {
                    name: 'No.5',
                    data:  [<? echo $arr[0][5] ?>, <? echo $arr[1][5] ?>, <? echo $arr[2][5] ?>, <? echo $arr[3][5] ?>, <? echo $arr[4][5] ?>, <? echo $arr[5][5] ?>,
                            <? echo $arr[6][5] ?>, <? echo $arr[7][5] ?>, <? echo $arr[8][5] ?>, <? echo $arr[9][5] ?>, <? echo $arr[10][5] ?>, <? echo $arr[11][5] ?>,
                            <? echo $arr[12][5] ?>, <? echo $arr[13][5] ?>, <? echo $arr[14][5] ?>, <? echo $arr[15][5] ?>, <? echo $arr[16][5] ?>, <? echo $arr[17][5] ?>,
                            <? echo $arr[18][5] ?>, <? echo $arr[19][5] ?>, <? echo $arr[20][5] ?>, <? echo $arr[21][5] ?>, <? echo $arr[22][5] ?>, <? echo $arr[23][5] ?>]
                }, {
                    name: 'No.6',
                    data:  [<? echo $arr[0][6] ?>, <? echo $arr[1][6] ?>, <? echo $arr[2][6] ?>, <? echo $arr[3][6] ?>, <? echo $arr[4][6] ?>, <? echo $arr[5][6] ?>,
                            <? echo $arr[6][6] ?>, <? echo $arr[7][6] ?>, <? echo $arr[8][6] ?>, <? echo $arr[9][6] ?>, <? echo $arr[10][6] ?>, <? echo $arr[11][6] ?>,
                            <? echo $arr[12][6] ?>, <? echo $arr[13][6] ?>, <? echo $arr[14][6] ?>, <? echo $arr[15][6] ?>, <? echo $arr[16][6] ?>, <? echo $arr[17][6] ?>,
                            <? echo $arr[18][6] ?>, <? echo $arr[19][6] ?>, <? echo $arr[20][6] ?>, <? echo $arr[21][6] ?>, <? echo $arr[22][6] ?>, <? echo $arr[23][6] ?>]
                }]
            });



            $('#container2').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },
                title: {
                    text: 'Day Motion Data Percentage'
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black',
                                fontSize: '25px'
                            }
                        }
                    }
                },
                series: [{
                    name: 'Motion',
                    colorByPoint: true,
                    data: [{
                        name: 'No.1',
                        y: <? echo $sum_arr[1] ?>,
                        sliced: true,
                        selected: true
                    }, {
                        name: 'No.2',
                        y: <? echo $sum_arr[2] ?>
                    }, {
                        name: 'No.3',
                        y: <? echo $sum_arr[3] ?>
                    }, {
                        name: 'No.4',
                        y: <? echo $sum_arr[4] ?>
                    }, {
                        name: 'No.5',
                        y: <? echo $sum_arr[5] ?>
                    }, {
                        name: 'No.6',
                        y: <? echo $sum_arr[6] ?>
                    }]
                }]
            });
        });
    </script>
  </head>
  <body>
    <div id="container" style="min-width: 310px; height: 500px; margin: 0 auto"></div>
    <div id="container2" style="min-width: 310px; height: 1000px; max-width: 1000px; margin: 0 auto"></div>

    
  </body>
</html>