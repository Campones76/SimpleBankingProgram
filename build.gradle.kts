plugins {
    id("java")
}

group = "io.github.campones76"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.commons:commons-csv:1.10.0")
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    implementation("org.xerial:sqlite-jdbc:3.45.0.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("com.github.javafaker:javafaker:1.0.2")


}

tasks.test {
    useJUnitPlatform()
}