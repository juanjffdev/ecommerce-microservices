FROM eclipse-temurin:21-jdk
WORKDIR /app

#Copiamos solo el wrapper de Maven y los archivos pom.xml
COPY auth-service/.mvn/ .mvn
COPY auth-service/mvnw auth-service/pom.xml ./
COPY commons/pom.xml /commons/pom.xml

#Copiamos el código de commons, lo compilamos e instalamos en el repositorio local
COPY commons/src /commons/src
RUN ./mvnw -B -f /commons/pom.xml install -DskipTests

#Descargamos las dependencias en caché (se vuelve a descargar solo si tocamos pom.xml)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B



#Copiamos el código de auth-service (si cambiamos algo aquí, todo lo anterior ya está listo)
COPY auth-service/src ./src

EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]
