package pl.sda.carrent

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import pl.sda.carrent.domain.model.Car
import pl.sda.carrent.domain.model.CarReservation
import pl.sda.carrent.domain.repository.CarRepository
import pl.sda.carrent.infrastructure.dto.CarDto
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarEndpointSpec extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private CarRepository carRepository

    def setup() {
        carRepository.deleteAll()
    }

    def "should create car"() {
        given:
        def newCar = new CarDto(null, "VW", "Passat", "123124112")
        def entity = new HttpEntity(newCar)
        when:
        def rsp = testRestTemplate.exchange("/car", HttpMethod.POST, entity, Object.class)
        then:
        rsp.statusCodeValue == 201

        carRepository.count() == 1
        with (carRepository.findAll().first()) {
            brand == "VW"
            model == "Passat"
            vin == "123124112"
        }
    }

    def "should not create car when vin already exists"() {
        given:
        def existingCar = new Car(null, "Opel", "Insignia",
                "123124112", new HashSet<CarReservation>())
        carRepository.save(existingCar)
        and:
        def newCar = new CarDto(null, "VW", "Passat", "123124112")
        def entity = new HttpEntity(newCar)
        when:
        def rsp = testRestTemplate.exchange("/car", HttpMethod.POST, entity, String.class)
        then:
        rsp.statusCodeValue == 409

        rsp.body == "Car already exist"
    }

    def "should delete car"() {
        given:
        def existingCar = new Car(1, "Opel", "Insignia",
                "123124112", new HashSet<CarReservation>())
        carRepository.save(existingCar)
        when:
        testRestTemplate.delete("/car/1")
        then:
        carRepository.count() == 0
    }
}
