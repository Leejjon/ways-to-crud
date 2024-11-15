package net.leejjon.crud

import io.github.oshai.kotlinlogging.KotlinLogging
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import org.junit.jupiter.api.Test
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import net.leejjon.crud.model.Person
import net.leejjon.crud.model.Persons
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [WaysToCrudApplication::class])
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class WaysToCrudIntegrationTests {
    private val logger = KotlinLogging.logger {}

    private lateinit var requestSpecification: RequestSpecification

    @LocalServerPort
    private val localPort: Int = 0

    @BeforeEach
    fun setup() {
        requestSpecification =
            Given {
                baseUri("http://localhost")
                port(localPort)
                log().all()
                response().log().all()
                contentType(ContentType.JSON)
                accept(ContentType.JSON)
            }
    }


    @Test
    fun `Verify that the GET request on the v1 persons endpoint returns Messi and Ronaldo`() {
        val response = Given {
            spec(requestSpecification)
        } When {
            get("/v1/persons")
        } Then {
            statusCode(200)
        } Extract {
            body().`as`(Persons::class.java)
        }

        assertThat(response.persons).hasSize(2)
        assertMessi(response.persons.first())
        assertRonaldo(response.persons.last())

    }

    @Test
    fun `Verify that the GET request on the v1 single person returns Messi`() {
        val response = Given {
            spec(requestSpecification)
        } When {
            get("/v1/persons/0")
        } Then {
            statusCode(200)
        } Extract {
            body().`as`(Person::class.java)
        }

        assertMessi(response)
    }

    @Test
    fun `Verify that the POST request on the v1 persons endpoint stores the user and returns 200`() {
        val response = Given {
            spec(requestSpecification)
        } When {
            body(
                """{
                  "name": "$NEYMAR_NAME",
                  "dateOfBirth": "$NEYMAR_DATE_OF_BIRTH",
                  "heightInMeters": $NEYMAR_HEIGHT
                }"""
            )
            post("/v1/persons")
        } Then {
            statusCode(200)
        } Extract {
            body().`as`(Person::class.java)
        }

        assertNeymar(response)
    }

    @Test
    fun `Verify that the DELETE request on the v1 persons endpoint deletes the user and returns 200`() {
        Given {
            spec(requestSpecification)
        } When {
            delete("/v1/persons/1")
        } Then {
            statusCode(200)
        }

        // Verify if there is one less person
        val response = Given {
            spec(requestSpecification)
        } When {
            get("/v1/persons")
        } Then {
            statusCode(200)
        } Extract {
            body().`as`(Persons::class.java)
        }

        assertThat(response.persons).hasSize(1)
        assertMessi(response.persons.first())
    }



    private fun assertMessi(messi: Person) {
        assertThat(messi.name).isEqualTo(MESSI_NAME)
        assertThat(messi.heightInMeters).isEqualTo(MESSI_HEIGHT)
        assertThat(messi.dateOfBirth).isEqualTo(MESSI_DATE_OF_BIRTH)
    }

    private fun assertRonaldo(ronaldo: Person) {
        assertThat(ronaldo.name).isEqualTo(RONALDO_NAME)
        assertThat(ronaldo.heightInMeters).isEqualTo(RONALDO_HEIGHT)
        assertThat(ronaldo.dateOfBirth).isEqualTo(RONALDO_DATE_OF_BIRTH)
    }

    private fun assertNeymar(neymar: Person) {
        assertThat(neymar.name).isEqualTo(NEYMAR_NAME)
        assertThat(neymar.heightInMeters).isEqualTo(NEYMAR_HEIGHT)
        assertThat(neymar.dateOfBirth).isEqualTo(NEYMAR_DATE_OF_BIRTH)
    }

    companion object {
        val MESSI_NAME = "Lionel Messi"
        val MESSI_HEIGHT = 1.70
        val MESSI_DATE_OF_BIRTH = LocalDate.of(1987, 6, 24)

        val RONALDO_NAME = "Christiano Ronaldo"
        val RONALDO_HEIGHT = 1.87
        val RONALDO_DATE_OF_BIRTH = LocalDate.of(1985, 2, 5)

        val NEYMAR_NAME = "Neymar Júnior"
        val NEYMAR_HEIGHT = 1.75
        val NEYMAR_DATE_OF_BIRTH = LocalDate.of(1992, 2, 5)
    }
}
