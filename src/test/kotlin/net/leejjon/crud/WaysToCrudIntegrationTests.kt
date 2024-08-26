package net.leejjon.crud

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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [WaysToCrudApplication::class])
class WaysToCrudIntegrationTests {
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
	fun `Verify that the GET request on the v1 persons endpoint that uses the Jdbc Rest template returns Messi and Ronaldo`() {
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

	companion object {
		val MESSI_NAME = "Lionel Messi"
		val MESSI_HEIGHT = 1.70
		val MESSI_DATE_OF_BIRTH = LocalDate.of(1987, 6, 24)
		val RONALDO_NAME = "Christiano Ronaldo"
		val RONALDO_HEIGHT = 1.87
		val RONALDO_DATE_OF_BIRTH = LocalDate.of(1985, 2, 5)
	}
}
