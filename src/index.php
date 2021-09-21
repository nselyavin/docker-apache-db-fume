<html lang="en">
<head>
<title>Hello world page</title>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
<h1>Хочу пивка</h1>
<?php
$value = 0;
try{
    $value = (int)$_GET["data"];
}catch(Exception $e){
    echo("No data variables");
}
printf("%1$32b", $value);

$im = imagecreatetruecolor(255, 255);

// Create a colour.
$white = imagecolorallocate($im, 255, 255, 255);

// Set the line thickness to 4.
imagesetthickness($im, 4);

// Set the center point of the circle.
$centerX = 128;
$centerY = 128;

// Set the radius of the circle.
$radius = 100;

// The angle what will be incremented for each loop.
$theta = 0;

while ($theta <= 360) {
  // Calculate the new x,y coordinates.
  $x = $centerX + $radius * cos($theta);
  $y = $centerY + $radius * sin($theta);

  // Increment theta.
  $theta += 1;

  // Draw a line from the centrer point to the x,y coordinates.
  imageline($im, $centerX, $centerY, $x, $y, $white);
}

// Save the image to a file.
imagepng($im, 'image.png');

// Destroy the image handler.
imagedestroy($im);

$img="image.png";
        echo "<img src='$img' width=\"300\"/>"
?>
</body>

<?php
    phpinfo();
?>
</html>
