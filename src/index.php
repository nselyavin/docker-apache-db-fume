<html lang="en">
<head>
<title>Hello world page</title>
    <link rel="stylesheet" href="style.css" type="text/css"/>
</head>
<body>
<h1>Хочу пивка</h1>
<?php
include("constants.php");

$value = 0;
try{
    $value = (int)$_GET["data"];
}catch(Exception $e){
    echo("No data variables");
}

$Sh = $value >> 29;
$x =  (($value &      0b0011111110000000000000000000000) >> 22);
if($x > 100){
    $x = 100;
}
$y =  (($value &      0b0000000001111111000000000000000) >> 15);
if($y > 100){
    $y = 100;
}
$Cd = (($value&      0b0000000000000000111000000000000) >> 12);
$F =  (($value &      0b0000000000000000000100000000000) >> 11);
$iScale = (($value & 0b0000000000000000000011110000000) >> 6);
$fScale = (($value & 0b0000000000000000000000001111111));
if($fScale > 100){
    $fScale = 100;
}
$Scale = (float)$iScale + (float)$fScale / 100.0;

printf("value = %1$32b (%1$1d)", $value, $value);
echo("<br>");
printf("sh = %1$32b (%1$1d)", $Sh, $Sh);
echo("<br>");
printf("x = %1$32b (%1$1d %%)", $x, $x);
echo("<br>");
printf("y = %1$32b (%1$1d %%)", $y, $y);
echo("<br>");
printf("Cd = %1$32b (%1$1d)", $Cd, $Cd);
echo("<br>");
printf("F = %1$32b (%1$1d)", $F, $F);
echo("<br>");
printf("iScale = %1$32b (%1$1d)", $iScale, $iScale);
echo("<br>");
printf("fScale = %1$32b (%1$1d)", $fScale, $fScale);
echo("<br>");
printf("Scale = %1$32b (%1$1f)", $Scale, $Scale);

$imgWidth = 400;
$imgHeight = 400;

$im = imagecreatetruecolor($imgWidth, $imgHeight);

// Create a colour.
$color = imagecolorallocate($im, $color2[$Cd][0], $color2[$Cd][1], $color2[$Cd][2]);

// Set the line thickness to 4.
imagesetthickness($im, 4);

// Set the center point of the circle.
$centerX = $imgWidth * $x / 100;
$centerY = $imgHeight * $y / 100;

if($Sh == 0){
// Draw CIRCLE
    $radius = 100 * $Scale;
    if($F == 1){
        imagefilledellipse($im, $centerX, $centerY, $radius, $radius, $color);
    }else{
        imageellipse($im, $centerX, $centerY, $radius, $radius, $color);
    }
}else if($Sh == 1){
// Draw RECT
    $radius = 100 * $Scale;
    if($F == 1){
        imagefilledrectangle($im, $centerX, $centerY, $radius, $radius, $color);
    }else{
        imagerectangle($im, $centerX, $centerY, $radius, $radius, $color);
    }
}
else if($Sh == 2){
// Draw TRIANGLE
    $points = array(
        $centerX, $centerY,
        $centerX - 45 * $Scale, $centerY - 50 * $Scale,
        $centerX + 50 * $Scale, $centerY - 50 * $Scale,
        $centerX, $centerY
    );
    $radius = 100 * $Scale;
    if($F == 1){
        imagefilledpolygon($im, $points, 4, $color);
    }else{
        imageopenpolygon($im, $points, 4, $color);
    }
}
else if($Sh == 3){
// Draw TRIANGLE
    $points = array(
        $centerX, $centerY,
        $centerX - 50 * $Scale, $centerY + 60 * $Scale,
        $centerX + 60 * $Scale, $centerY + 20 * $Scale,
        $centerX - 60 * $Scale, $centerY + 20 * $Scale,
        $centerX + 50 * $Scale, $centerY + 60 * $Scale,
        $centerX, $centerY
    );

    $radius = 100 * $Scale;
    if($F == 1){
        imagefilledpolygon($im, $points, 6, $color);
    }else{
        imageopenpolygon($im, $points, 6, $color);
    }
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
