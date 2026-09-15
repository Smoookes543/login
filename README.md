

Características

- Registro de usuarios con validación de campos
- Inicio de sesión con verificación contra base de datos local
- Persistencia de datos con Room (SQLite)
- Validaciones:
  - Campos vacíos
  - Contraseñas coincidentes
  - Usuario duplicado
  - Credenciales incorrectas
- Dashboard,ost-login con opción de cerrar sesión
- UI oscura moderna con Jetpack Compose y Material 3
- Navegación entre pantallas con estado (Login / Registro / Dashboard)

---

Tecnologías utilizadas

| Tecnología | Uso |
|------------|-----|
| Kotlin  Lenguaje principal |
| Jetpack Compose | UI declarativa |
| Material 3 | Componentes de diseño |
| Room Database | Persistencia local (SQLite) |
| Coroutines / LaunchedEffect | Manejo asíncrono |
| Gradle Kotlin DSL | Sistema de build |


Base de datos

Entidad: `Usuario`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Int (PK) | Autogenerado |
| `usuario` | String | Nombre de usuario |
| `contraseña` | String | Contraseña |

Consultas del DAO:
- `insertar(usuario)` → Registrar nuevo usuario
- `validarUsuario(usuario, contraseña)` → Login
- `buscarPorUsuario(usuario)` → Verificar existencia

---

Ejecutar el proyecto

 Requisitos previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK API 24+ (Android 7.0)



repositorio
  
  https://github.com/tu-usuario/login-justo.git
