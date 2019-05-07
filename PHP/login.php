<?php
//Constantes

$DB_DATABASE='Xgabad002_geochat'; #la base de datos a la que hay que conectarse# Se establece la conexión:
$DB_SERVER='localhost'; #la dirección del servidor
$DB_USER='Xgabad002'; #el usuario para esa base de datos
$DB_PASS='P2CqT1HDtW'; #la clave para ese usuario

//Datos de ususarios
$USER_NICK = $_POST['nick'];
$USER_PASSWORD = $_POST['password'];
$USER_TOKEN = $_POST['token'];
$USER_NAME = $_POST['name'];

//Caso de uso

$case = $_POST['case'];

$con = crearConexion($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($case == 0) { //Ver si el user existe y devuelve el nombre
    getUser($con, $USER_NICK, $USER_PASSWORD);
} elseif ($case == 1) { //Insertar un user
    insertUser($con, $USER_NICK, $USER_NAME, $USER_TOKEN, $USER_PASSWORD);
}

mysqli_close($con);

exit();

function insertUser($con, $USER_NICK, $USER_NAME, $USER_TOKEN, $USER_PASSWORD)
{
    $resultado= mysqli_query($con, "INSERT INTO user (nick, name, token, password) VALUES ('$USER_NICK', '$USER_NAME', '$USER_TOKEN', '$USER_PASSWORD')");

    $arrayresultados;
    if (!$resultado) {
        echo'Ha ocurrido algún error: '. mysqli_error($con);
        $arrayresultados = array('status' => '0');
    } else {
        $arrayresultados = array('status' => '1');
    }

    echo json_encode($arrayresultados);
}
function getUser($con, $USER_NICK, $USER_PASSWORD)
{
    $resultado= mysqli_query($con, "SELECT name FROM user WHERE nick = '$USER_NICK' and password = '$USER_PASSWORD'");

    if (!$resultado) {
        echo'Ha ocurrido algún error: '. mysqli_error($con);
    }
    $fila= mysqli_fetch_row($resultado);


    $arrayresultados = array(
  'name' => $fila[0],
);

    echo json_encode($arrayresultados);
}
function crearConexion($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE)
{
    $con= mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);#Comprobamos conexión
    if (mysqli_connect_errno($con)) {
        printf('Error de conexion: '. mysqli_connect_error());
        exit();
    } else {
        //printf('Conexion creada');
    }

    return $con;
}
