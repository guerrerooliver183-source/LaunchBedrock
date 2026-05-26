# Diseño de Arquitectura y Estructura del Proyecto Bedrock Launcher

## 1. Introducción

Este documento detalla la arquitectura y la estructura propuesta para el desarrollo de la aplicación Android "Bedrock Launcher", basándose en los requisitos proporcionados por el usuario. El objetivo es crear una aplicación robusta, escalable y fácil de mantener, siguiendo las mejores prácticas de desarrollo Android y el diseño Material You.

## 2. Requisitos Clave

Los requisitos principales del usuario incluyen:

*   **Diseño Visual:** Estilo Material You moderno, adaptación automática a los colores del sistema, modo claro/oscuro, esquinas redondeadas, espacios bien distribuidos.
*   **Pantallas:**
    *   Pantalla de carga con logo de Minecraft, secuencia de textos cambiantes y barra de progreso.
    *   Sistema de notificaciones emergentes.
    *   Pantalla de información de actualizaciones con detalles y botón de instalación.
    *   Pantalla de Ajustes con secciones General y Gameplay.
*   **Funcionalidades:**
    *   Nombre de la aplicación "Minecraft" en el menú del teléfono.
    *   Detección de Minecraft Trial y redirección a descarga si no está instalado.
    *   Aviso inicial para ocultar la aplicación original.
    *   Tiempos específicos para la pantalla de carga y cambio a orientación horizontal.
    *   Sistema seguro para ocultar/mostrar la aplicación original usando permisos de administrador.
    *   Guardado y aplicación instantánea de ajustes.
    *   Sistema completo de actualizaciones.
    *   Apertura integrada de la aplicación de Minecraft al finalizar el proceso.

## 3. Arquitectura Propuesta

Se propone una arquitectura basada en **MVVM (Model-View-ViewModel)** con **Jetpack Compose** para la interfaz de usuario. Esta arquitectura facilita la separación de responsabilidades, mejora la testabilidad y la mantenibilidad del código.

### 3.1 Componentes Principales

*   **Actividades (Activities):** Puntos de entrada para las diferentes pantallas de la aplicación. Se utilizará una `MainActivity` principal que orquestará la navegación entre las diferentes pantallas compuestas.
*   **Composables (UI):** Elementos de la interfaz de usuario construidos con Jetpack Compose. Cada pantalla (carga, notificaciones, actualizaciones, ajustes) será un composable separado, lo que permitirá una UI declarativa y reactiva.
*   **ViewModels:** Clases que exponen el estado de la UI y manejan la lógica de negocio. Interactuarán con los repositorios para obtener datos y actualizar el estado de la UI.
*   **Repositorios (Repositories):** Abstraen la fuente de datos (API, base de datos local, preferencias compartidas, etc.). Serán responsables de la lógica para obtener, almacenar y manipular datos.
*   **Modelos (Models):** Clases de datos que representan la información utilizada en la aplicación.

### 3.2 Flujo de Datos

El flujo de datos seguirá el patrón unidireccional de MVVM:

1.  **Eventos de Usuario:** Las interacciones del usuario en la UI (Composables) generan eventos.
2.  **ViewModel:** Los eventos son manejados por el ViewModel, que puede actualizar su estado o interactuar con los Repositorios.
3.  **Repositorio:** El Repositorio obtiene o guarda datos, y notifica al ViewModel sobre los cambios.
4.  **ViewModel:** El ViewModel actualiza el estado de la UI basándose en los datos del Repositorio.
5.  **Composables (UI):** La UI se recompone automáticamente para reflejar el nuevo estado del ViewModel.

## 4. Estructura de Carpetas y Archivos

La estructura del proyecto se organizará de manera modular y clara, siguiendo las convenciones de Android y Kotlin. Se propone la siguiente estructura dentro del paquete `com.minecraft.bedrocklauncher`:

```
com.minecraft.bedrocklauncher
├── MainActivity.kt
├── ui
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── screens
│   │   ├── SplashScreen.kt
│   │   ├── NotificationScreen.kt
│   │   ├── UpdateInfoScreen.kt
│   │   └── SettingsScreen.kt
│   └── components
│       ├── LoadingBar.kt
│       └── NotificationBanner.kt
├── data
│   ├── models
│   │   └── AppUpdate.kt
│   ├── repositories
│   │   ├── AppRepository.kt
│   │   └── SettingsRepository.kt
│   └── preferences
│       └── AppPreferences.kt
├── domain
│   └── usecases
│       ├── CheckMinecraftTrialUseCase.kt
│       ├── HideAppUseCase.kt
│       └── UpdateAppUseCase.kt
├── utils
│   ├── DeviceAdminUtil.kt
│   └── AppDetector.kt
└── viewmodels
    ├── SplashViewModel.kt
    ├── UpdateViewModel.kt
    └── SettingsViewModel.kt
```

## 5. Tecnologías Clave

*   **Kotlin:** Lenguaje de programación principal.
*   **Jetpack Compose:** Toolkit moderno para construir la interfaz de usuario nativa de Android.
*   **Material You:** Guía de diseño para la interfaz de usuario, permitiendo la adaptación dinámica de colores.
*   **Android Jetpack:** Colección de bibliotecas para ayudar a los desarrolladores a seguir las mejores prácticas, reducir el código repetitivo y escribir código que funcione de manera consistente en todas las versiones y dispositivos de Android.
    *   **ViewModel:** Para la gestión del estado de la UI.
    *   **Navigation Compose:** Para la navegación entre pantallas.
    *   **DataStore/SharedPreferences:** Para el almacenamiento local de preferencias y ajustes.
*   **Retrofit (opcional):** Para la comunicación con APIs externas (si el sistema de actualizaciones lo requiere).

## 6. Consideraciones Especiales

*   **Permisos de Administrador:** La funcionalidad de ocultar/mostrar la aplicación requerirá permisos de administrador del dispositivo. Se implementará un `DeviceAdminReceiver` para gestionar esto de forma segura.
*   **Detección de Aplicaciones:** Se implementará lógica para detectar la instalación de Minecraft Trial y otras aplicaciones relevantes.
*   **Internacionalización:** Se utilizarán recursos de `strings.xml` para facilitar futuras traducciones.
*   **Accesibilidad:** Se prestará atención a la accesibilidad en el diseño de la UI.

## 7. Próximos Pasos

Una vez aprobado este diseño, se procederá con la implementación de las diferentes pantallas y funcionalidades, siguiendo la estructura y arquitectura aquí definidas.
