# Usar uma imagem base compatível com ARM e x86
FROM eclipse-temurin:21-jre-alpine

# Criar o diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven/Gradle
COPY target/equaly-app-messenger-1.0.12.jar equaly-app-messenger-1.0.12.jar

# Expor a porta da aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "equaly-app-messenger-1.0.12.jar"]