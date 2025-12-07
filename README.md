# 📚 Gestión de Empleados (TCSW-Practica02)

Proyecto de aplicación de escritorio en **Java** que implementa una solución de **Gestión de Empleados (CRUD)**. Utiliza la librería **Swing** para la interfaz gráfica y **PostgreSQL** para la persistencia de datos.

## ✨ Características Técnicas

| Tecnología | Rol en el Proyecto |
| :--- | :--- |
| **Java** | Lenguaje de programación. |
| **Swing** | Desarrollo de la Interfaz Gráfica de Usuario (GUI). |
| **PostgreSQL** | Sistema de Gestión de Bases de Datos (SGBD). |
| **JDBC** | Driver de conectividad a la base de datos. |

---

## 🏗️ Arquitectura y Patrones

El diseño del proyecto se basa en patrones de POO para asegurar la modularidad y la seguridad:

* **DAO (Data Access Object) Genérico:** Define el contrato CRUD (`IDAOGeneral`).
* **Singleton:** Gestiona una única instancia de la conexión a la base de datos (`ConexionDB`).
* **Command/Strategy:** Implementado en `TransactionDB` para encapsular operaciones SQL y garantizar el uso de **Prepared Statements** (seguridad contra inyección SQL).
* **POJO / DTO:** La clase `PojoEmpleado` actúa como objeto de transferencia de datos.

### Componentes Clave

| Clase/Interfaz | Tipo | Función |
| :--- | :--- | :--- |
| `Principal.java` | JFrame | Contenedor principal de la aplicación (MDI). |
| `EmpleadoGUI.java` | JInternalFrame | Interfaz de formulario para operaciones CRUD. |
| `DAOEmpleado.java` | Clase | Lógica de persistencia (Guardar, Modificar, Eliminar) de Empleados. |
| `ConexionDB.java` | Singleton | Gestiona la conexión y asegura la existencia de la DB y la tabla. |

---

## 🚀 Instalación y Ejecución

### Requisitos

* **JDK** (Java Development Kit) 8+ (Configurado en el `pom.xml`).
* **PostgreSQL Server** (Activo en puerto 5432).
* **Driver JDBC** de PostgreSQL (como dependencia en Maven).

### Configuración de Conexión

La configuración de la base de datos se encuentra en `ConexionDB.java`.

| Parámetro | Valor por defecto |
| :--- | :--- |
| **URL** | `jdbc:postgresql://localhost:5432/ejemplo` |
| **Usuario** | `postgres` |
| **Contraseña** | `pastel` |

### Uso

1.  Ejecuta la aplicación (clase `Principal.java`).
2.  Accede al menú **OPCIONES** ➡️ **EMPLEADO** para abrir la interfaz de gestión.
3.  Utiliza los botones **Guardar**, **Modificar**, **Eliminar** y **Listar todos** para interactuar con la base de datos.

---

## 🤝 Contribución

¡Cualquier sugerencia o pull request para mejorar el código es bienvenida!
