
package com.example.login_justo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login_justo.ui.theme.Login_JustoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val usuarioDao = database.usuarioDao()

        setContent {
            Login_JustoTheme {

                var pantalla by remember {
                    mutableStateOf("login")
                }

                when (pantalla) {

                    "login" -> {
                        LoginScreen(
                            usuarioDao = usuarioDao,
                            abrirRegistro = {
                                pantalla = "registro"
                            },
                            abrirDashboard = {
                                pantalla = "dashboard"
                            }
                        )
                    }

                    "registro" -> {
                        RegistroScreen(
                            usuarioDao = usuarioDao,
                            volverLogin = {
                                pantalla = "login"
                            }
                        )
                    }

                    "dashboard" -> {
                        DashboardScreen(
                            cerrarSesion = {
                                pantalla = "login"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    usuarioDao: UsuarioDao,
    abrirRegistro: () -> Unit,
    abrirDashboard: () -> Unit
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var contraseña by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var iniciarSesion by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(iniciarSesion) {

        if (iniciarSesion) {

            if (usuario.isBlank() || contraseña.isBlank()) {

                mensaje = "Complete todos los campos"
                iniciarSesion = false

            } else {

                val resultado = usuarioDao.validarUsuario(
                    usuario,
                    contraseña
                )

                if (resultado != null) {
                    mensaje = ""
                    abrirDashboard()
                } else {
                    mensaje = "Usuario o contraseña incorrectos"
                }

                iniciarSesion = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "INICIAR SESIÓN",
            color = Color.Red,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(35.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
            },
            label = {
                Text(
                    text = "Usuario",
                    color = Color.LightGray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedIndicatorColor = Color.Red,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = {
                contraseña = it
            },
            label = {
                Text(
                    text = "Contraseña",
                    color = Color.LightGray
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedIndicatorColor = Color.Red,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = {
                iniciarSesion = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "INGRESAR"
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                abrirRegistro()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "REGISTRARSE"
            )
        }
    }
}

@Composable
fun RegistroScreen(
    usuarioDao: UsuarioDao,
    volverLogin: () -> Unit
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var contraseña by remember {
        mutableStateOf("")
    }

    var confirmarContraseña by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var registrar by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(registrar) {

        if (registrar) {

            if (
                usuario.isBlank() ||
                contraseña.isBlank() ||
                confirmarContraseña.isBlank()
            ) {

                mensaje = "Complete todos los campos"

            } else if (contraseña != confirmarContraseña) {

                mensaje = "Las contraseñas no coinciden"

            } else {

                val usuarioExistente =
                    usuarioDao.buscarPorUsuario(usuario)

                if (usuarioExistente != null) {

                    mensaje = "El usuario ya existe"

                } else {

                    val nuevoUsuario = Usuario(
                        usuario = usuario,
                        contraseña = contraseña
                    )

                    usuarioDao.insertar(nuevoUsuario)

                    mensaje = "Usuario registrado correctamente"

                    usuario = ""
                    contraseña = ""
                    confirmarContraseña = ""
                }
            }

            registrar = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "REGISTRO",
            color = Color.Red,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(35.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
            },
            label = {
                Text(
                    text = "Usuario",
                    color = Color.LightGray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedIndicatorColor = Color.Red,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = {
                contraseña = it
            },
            label = {
                Text(
                    text = "Contraseña",
                    color = Color.LightGray
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedIndicatorColor = Color.Red,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = confirmarContraseña,
            onValueChange = {
                confirmarContraseña = it
            },
            label = {
                Text(
                    text = "Confirmar contraseña",
                    color = Color.LightGray
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedIndicatorColor = Color.Red,
                unfocusedIndicatorColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = {
                registrar = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "REGISTRARSE"
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                volverLogin()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "VOLVER"
            )
        }
    }
}

@Composable
fun DashboardScreen(
    cerrarSesion: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "DASHBOARD",
            color = Color.Red,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Bienvenido",
            color = Color.White,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                cerrarSesion()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "CERRAR SESIÓN"
            )
        }
    }
}

