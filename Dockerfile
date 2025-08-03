FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy all project files
COPY . .

# Make wrapper executable
RUN chmod +x mvnw

# Build the app
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["java", "-jar", "target/Cinema_Reservation_System-0.0.1-SNAPSHOT.jar"]