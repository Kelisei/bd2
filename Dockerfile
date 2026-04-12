# Etapa 1: Compilación (Build)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copiamos el pom y el código fuente
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copiamos el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de la aplicación (por defecto Spring Boot usa 8080)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]