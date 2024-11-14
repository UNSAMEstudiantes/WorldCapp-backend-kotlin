# Ejemplo base para TP Algo3

[![Build](https://github.com/algo3-unsam/proyecto-base-tp/actions/workflows/build.yml/badge.svg)](https://github.com/algo3-unsam/tp-recetas-2020-gr-xx/actions/workflows/build.yml) ![Coverage](./.github/badges/jacoco.svg)

## ¿Quiénes somos?

Somos un grupo de cuatro estudiantes de la Universidad Nacional de San Martín, cursando una de las últimas materias de la carrera, e intentando dar lo mejor de nosotros en este proyecto. En concreto, somos:

Gomez Daniel

Sanchez Marcelo

Exarchos Alan

Mary Cairo

## El proyecto

está basado en _Maven_, y el archivo `build.gradle.kts` tiene dependencias a:

- Spring Boot
- JUnit
- JaCoCo (Java Code Coverage), para que agregues el % de cobertura en el README
- Swagger, para documentar tus endpoints
- la versión de Kotlin que estaremos usando
- además de estar basado en la JDK 17


El proyecto tiene un main, en la clase `ProyectoApplication`, que levantará el servidor web en el puerto 9000.

