# Ejemplo base para TP Algo3

[![Build](https://github.com/algo3-unsam/proyecto-base-tp/actions/workflows/build.yml/badge.svg)](https://github.com/algo3-unsam/tp-recetas-2020-gr-xx/actions/workflows/build.yml) ![Coverage](./.github/badges/jacoco.svg)


## El proyecto

está basado en _Maven_, y el archivo `build.gradle.kts` tiene dependencias a

- Spring Boot
- JUnit
- JaCoCo (Java Code Coverage), para que agregues el % de cobertura en el README
- Swagger, para documentar tus endpoints
- la versión de Kotlin que estaremos usando
- además de estar basado en la JDK 17


El proyecto tiene un main, en la clase `ProyectoApplication`, que levantará el servidor web en el puerto 9000.

