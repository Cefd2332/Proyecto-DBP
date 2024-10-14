Link deployment: https://proyecto-dbp-1.onrender.com

# CS 20242 Desarrollo Basado en Plataformas

**Integrado por:**
- Carlos Flores Delgado
- Mateo Herrera Morales
- Carlos Alcazar Alvarez
- Leonardo De Casanova Paz
- Harold Inca Tenorio

## ÍNDICE

1. [INTRODUCCIÓN](#introducción)
2. [IDENTIFICACIÓN DEL PROBLEMA O NECESIDAD](#identificación-del-problema-o-necesidad)
3. [DESCRIPCIÓN DE LA SOLUCIÓN](#descripción-de-la-solución)
4. [MODELO DE ENTIDADES](#modelo-de-entidades)
5. [TESTING Y MANEJO DE ERRORES](#testing-y-manejo-de-errores)
6. [MEDIDAS DE SEGURIDAD IMPLEMENTADAS](#medidas-de-seguridad-implementadas)
7. [EVENTOS Y ASINCRONÍA](#eventos-y-asincronía)
8. [GITHUB](#github)
9. [CONCLUSIÓN](#conclusión)
10. [APÉNDICES](#apéndices)

## INTRODUCCIÓN

### Contexto:
En el mundo de la adopción de mascotas, tanto los colectivos que ayudan en las adopciones como las personas que adoptan se enfrentan a dificultades. Tras la adopción, el control de la salud y las necesidades de cuidados del animal carece de un sistema único para una mejor gestión. El proyecto pretende agilizar y simplificar estos procesos.

### Objetivo:
Desarrollar una plataforma integral que facilite la adopción de mascotas, ofreciendo herramientas avanzadas para el seguimiento post-adopción. Buscamos ofrecer una interfaz sencilla con funcionalidades como registro de salud, citas veterinarias, sistema de avisos de vacunas y geolocalización para asegurar el bienestar continuo de las mascotas.

## IDENTIFICACIÓN DEL PROBLEMA O NECESIDAD

### Problema:
La informalidad en el proceso de adopción de mascotas hace necesario un sistema que permita controlar su salud, ya que a menudo no hay conocimiento sobre su historial médico, vacunaciones o necesidades veterinarias, poniendo en riesgo su bienestar.

### Justificación:
Abordar este problema es crucial para garantizar el bienestar de las mascotas adoptadas. Un sistema integral de seguimiento permitirá a los usuarios y organizaciones gestionar mejor la salud y cuidado de los animales, mejorando la transparencia y reduciendo riesgos de abandono o negligencia.

## DESCRIPCIÓN DE LA SOLUCIÓN

### Funcionalidades Implementadas:

1. **Registro de adopción**: Captura la información del usuario y de la mascota adoptante, detalles de la adopción y estado de la mascota.
2. **Registro de citas veterinarias**: Programación y registro de citas para el seguimiento de la salud del animal.
3. **Notificaciones automáticas**: Uso de Firebase para enviar notificaciones sobre citas, vacunas y estado de salud.
4. **Gestión de usuario**: Registro, actualización y eliminación de usuarios, con control de permisos.
5. **Ubicación**: Monitoreo de la ubicación del animal mediante coordenadas GPS.
6. **Registro de Salud**: Registro y actualización del estado de salud del animal, con un historial médico completo accesible tanto para adoptantes como para veterinarios.

### Tecnologías Usadas:
- **Base de datos**: Sistema de persistencia basado en JPA.
- **Asincronía**: Uso de la anotación `@Async` para tareas asíncronas como notificaciones.
- **Firebase**: Autenticación y manejo de notificaciones push.
- **Seguridad**: Spring Security para protección de APIs y control de acceso por roles.

## MODELO DE ENTIDADES

- **Vacuna**: Información sobre las vacunas aplicadas y futuras.
- **Usuario**: Registro de la información del usuario que adopta un animal.
- **Adopción**: Gestión del proceso de adopción.
- **Animal**: Detalles de cada mascota adoptada, su estado de salud, y citas veterinarias.
- **CitaVeterinaria**: Gestión de citas veterinarias, fechas y consultas.
- **UbicacionAnimal**: Seguimiento geolocalizado de la mascota.
- **Registro de Salud**: Historial médico completo, incluyendo diagnósticos, tratamientos y exámenes.

### Relaciones entre entidades:

- **One-to-One**: Entre Animal y Ubicación.
- **One-to-Many**: Entre Usuario y Adopción.
- **Many-to-Many**: Entre Animal y Vacuna.

## TESTING Y MANEJO DE ERRORES

### Testing: Uso de Mockito

1. **Mocking de Dependencias**: Simulación de dependencias como repositorios de datos.
2. **Verificación de Comportamientos**: Verificación de llamadas a métodos.
3. **Pruebas de Casos de Excepción**: Validación del sistema ante excepciones como `NotFoundException`.
4. **Simulación de Retornos**: Simulación de respuestas específicas.
5. **Cobertura de Pruebas**: Cobertura significativa en funciones críticas como gestión de citas veterinarias y notificaciones.

### Exceptions:

- **BadRequestException**: Datos incorrectos o faltantes.
- **ConflictException**: Conflictos en la creación de recursos, como usuarios duplicados.
- **ForbiddenException**: Acceso denegado a usuarios sin permisos.
- **InternalServerErrorException**: Error interno del servidor.
- **NotFoundException**: Recurso solicitado no encontrado.
- **UnauthorizedException**: Falta de autenticación.

## MEDIDAS DE SEGURIDAD IMPLEMENTADAS

Se implementó seguridad de datos con tokens JWT. Cada usuario recibe un token único al registrarse o iniciar sesión, lo que garantiza la autenticación en futuras solicitudes. Los tokens tienen fecha de expiración para evitar reutilización.

## EVENTOS Y ASINCRONÍA

Implementación de eventos asíncronos para notificaciones, utilizando Firebase para correos y notificaciones push. Las tareas como recordatorios de citas se procesan en segundo plano sin afectar el rendimiento general.

## GITHUB

Utilizamos GitHub Projects para la gestión de tareas. Se asignaron issues con etiquetas como 'Por Hacer', 'En Progreso', y 'Completado', con deadlines para cada issue. Esto permitió un seguimiento eficiente del proyecto.

## CONCLUSIÓN

Este proyecto proporciona una solución integral para la adopción y cuidado post-adopción de mascotas. La plataforma mejora la transparencia y responsabilidad de las adopciones mediante el registro de adopciones, citas veterinarias, notificaciones automáticas y seguimiento de la ubicación, beneficiando tanto a adoptantes como a organizaciones.

