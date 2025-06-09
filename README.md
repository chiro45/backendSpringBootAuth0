# 🛡️ Backend Auth0 - Users and Roles

Este proyecto es una **API REST desarrollada en Spring Boot** que utiliza **Auth0** para la autenticación y autorización de usuarios, junto con una base de datos **MySQL** para persistencia.

---

## ⚙️ Configuración del entorno

Antes de ejecutar el proyecto, debés configurar las variables de entorno.  
Estas se encuentran normalmente en un archivo `.env` o directamente en el archivo `application.properties`.  
> 💡 Recomendado: usar `.env` para datos sensibles.

---

## 🔐 Variables de entorno requeridas

| Variable                                      | Descripción                                                                 |
|----------------------------------------------|-----------------------------------------------------------------------------|
| `spring.application.name`                    | Nombre de la aplicación. Por defecto: `auth0UsersAndRoles`.                |
| `spring.datasource.url`                      | URL de conexión a la base de datos MySQL. Ej: `jdbc:mysql://localhost:3306/mp`. |
| `spring.datasource.username`                 | Usuario de la base de datos MySQL.                                         |
| `spring.datasource.password`                 | Contraseña del usuario de la base de datos.                                |
| `spring.jpa.hibernate.ddl-auto`              | Estrategia de actualización del esquema. Usá `update` para desarrollo.     |
| `spring.jpa.database-platform`               | Dialecto de Hibernate. Para MySQL: `org.hibernate.dialect.MySQLDialect`.   |
| `server.port`                                | Puerto donde corre la API. Ej: `8081`.                                     |
| `spring.security.oauth2.resourceserver.jwt.issuer-uri` | URL del issuer de tu tenant de Auth0. Lo obtenés en el panel de Auth0.    |
| `auth0.audience`                             | Identificador de la API configurada en Auth0. Ej: `https://tuApiIdentifier`.|
| `logging.level.org.springframework.security` | Nivel de log para Spring Security. Ej: `INFO`, `DEBUG`.                    |
| `spring.websecurity.debug`                   | Activa/desactiva el debug de seguridad web (`true` o `false`).             |
| `web.cors.allowed-origins`                   | Orígenes permitidos por CORS. Ej: `http://localhost:5173` (si usás Vite).  |
| `auth0.domain`                               | Dominio de tu tenant en Auth0. Ej: `dev-xxxxxxx.us.auth0.com`.             |
| `auth0.clientId`                             | ID del cliente OAuth (Auth0).                                              |
| `auth0.clientSecret`                         | Secreto del cliente OAuth (**NO lo compartas públicamente**).             |

---

## 🧩 Inicialización: configuración de roles y usuario administrador

Al iniciar la aplicación **por primera vez**, es necesario crear **dos roles fundamentales** en Auth0:

- `Administrador`
- `Cliente`

---

### 🔁 Opciones para crear roles

Tenés dos formas de hacerlo:

---

### ✅ Opción 1: Crear roles usando Postman (modo manual)

Podés **deshabilitar temporalmente la seguridad** de la aplicación (comentando los filtros o permitiendo todos los endpoints en `SecurityConfig`)  
y luego enviar estas peticiones a la API:

---

#### 1. Crear Rol "Administrador"

**POST** `/api/admin/roles/createRole`  
**Body:**
```json
{
  "name": "Administrador",
  "description": "admin del local"
}
```
---

#### 2. Crear Rol "Cliente"

**POST** `/api/admin/roles/createRole`  
**Body:**
```json
{
  "name": "Cliente",
  "description": "cliente del sistema"
}
```
### 3. Crear usuario administrador en Auth0
Usando la API de Auth0 (requiere token de acceso válido), podés crear el usuario administrador con el siguiente body.
Este usuario debe estar vinculado al rol Administrador previamente creado:

POST https://<TU_DOMINIO_AUTH0>/api/v2/users
Body:
```json
{
  "email": "admin@buensabor.com",
  "name": "Administrador",
  "nickName": "admin total",
  "password": "Admin@admin",
  "connection": "Username-Password-Authentication",
  "roles": ["id_generado_de_auth0"]
}

```
⚠️ Importante:
Importante al tener la base de datos sincronizada con los usuarios y roles de auth0 es muy importante poder realizar esto


