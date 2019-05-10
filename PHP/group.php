<?php
//Constantes

$DB_DATABASE='Xgabad002_geochat'; #la base de datos a la que hay que conectarse# Se establece la conexión:
$DB_SERVER='localhost'; #la dirección del servidor
$DB_USER='Xgabad002'; #el usuario para esa base de datos
$DB_PASS='P2CqT1HDtW'; #la clave para ese usuario

//Datos de ususarios
$USER_NICK = $_POST['nick'];
$GROUP_ID = $_POST['groupId'];
$GROUP_NAME = $_POST['groupName'];

//Caso de uso

$case = $_POST['case'];

$con = crearConexion($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);


if ($case == 0) { //Añadir un usuario al grupo, y devolver los ususarios de ese grupo
    anadirUsuarioAGrupo($con, $USER_NICK, $GROUP_ID);
} elseif ($case == 1) { //Conseguir los usuarios de un grupo
    getUsuariosDeGrupo($con, $GROUP_ID);
} elseif ($case == 2) {//Crear un grupo
    crearGrupo($con, $GROUP_NAME);
}

mysqli_close($con);

exit();


function anadirUsuarioAGrupo($con, $USER_NICK, $GROUP_ID)
{
    $resultado= mysqli_query($con, "INSERT INTO userGroup (userNick, groupoId) VALUES ('$USER_NICK', '$GROUP_ID')");

    $arrayresultados;
    if (!$resultado) {
        echo'Ha ocurrido algún error: '. mysqli_error($con);
        $arrayresultados = array('status' => '0');
    } else {
        $arrayresultados = array('status' => '1');
    }

    echo json_encode($arrayresultados);
}

function getUsuariosDeGrupo($con, $GROUP_ID)
{
    $resultado= mysqli_query($con, "SELECT userNick FROM userGroup WHERE groupoId = '$GROUP_ID'");

    if (!$resultado) {
        echo'Ha ocurrido algún error: '. mysqli_error($con);
    }
    $fila= mysqli_fetch_row($resultado);


    $arrayresultados = array(
    'nick' => $fila[0],
);

    echo json_encode($arrayresultados);
}

function crearGrupo($con, $GROUP_NAME)
{
    $resultado= mysqli_query($con, "INSERT INTO grupo (name) VALUES ('$GROUP_NAME')");

    $arrayresultados;
    if (!$resultado) {
        echo'Ha ocurrido algún error: '. mysqli_error($con);
        $arrayresultados = array('status' => '0');
    } else {
        $arrayresultados = array('status' => '1');
    }

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
