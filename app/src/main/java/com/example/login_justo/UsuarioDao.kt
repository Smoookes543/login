package com.example.login_justo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario LIMIT 1")
    suspend fun buscarPorUsuario(usuario: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE usuario = :usuario AND contraseña = :contraseña LIMIT 1")
    suspend fun validarUsuario(
        usuario: String,
        contraseña: String
    ): Usuario?
}