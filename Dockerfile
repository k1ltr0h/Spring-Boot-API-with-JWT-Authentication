# Usa una imagen de Maven para compilar
FROM maven:3.8.5-openjdk-17 AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia el código fuente del proyecto
COPY . .

# Ejecuta Maven para compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Usa una imagen más ligera para ejecutar el JAR
FROM openjdk:11-jre-slim

# Copia el archivo JAR generado desde la etapa de construcción
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar /app.jar

# Expone el puerto 8080
EXPOSE 8080

# Ejecuta el JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
