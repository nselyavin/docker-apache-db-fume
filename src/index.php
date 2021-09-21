<html lang="en">
<head>
<title>Hello world page</title>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
<h1>Хочу пивка</h1>

<?php
    $array = $_GET["arr"];

    foreach($array as $elem){
        echo($elem );
        echo(", ");
    }

    $len = count($array);
    for($i = 0; $i < $len; $i++){
        for($j = 1; $j < $len; $j++){
            $k = $j - 1;
            if($array[$j] < $array[$k]){
                list($array[$j], $array[$k]) = array($array[$k], $array[$j]);
            }
        }
    }
    echo("<br> ");
    foreach($array as $elem){
        echo($elem );
        echo(", ");
    }
?>

<?php
    phpinfo();
?>
</body>
</html>
