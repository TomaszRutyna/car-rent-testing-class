package pl.sda.carrent.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.sda.carrent.domain.model.Car
import pl.sda.carrent.domain.model.CarReservation
import pl.sda.carrent.domain.repository.CarRepository
import pl.sda.carrent.infrastructure.dto.CarDto
import pl.sda.carrent.infrastructure.exception.CarAlreadyExistsException
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarServiceSpec extends Specification {

    @Subject
    @Autowired
    private CarService carService

    @Autowired
    private CarRepository carRepository

    def setup() {
        carRepository.deleteAll()
    }

    def "should create new car"() {
        given:
        def newCar = new CarDto(null, "VW", "Passat", "123124112")
        when:
        carService.create(newCar)
        then:
        carRepository.count() == 1
        with (carRepository.findAll().first()) {
            brand == "VW"
            model == "Passat"
            vin == "123124112"
        }
    }

    def "should not create new car when vin already exists"() {
        given:
        carRepository.save(new Car(null, "Opel", "Insignia",
                "123124112", new HashSet<CarReservation>()))
        def newCar = new CarDto(null, "VW", "Passat", "123124112")
        when:
        carService.create(newCar)
        then:
        thrown(CarAlreadyExistsException)
    }

    def "should create,update and delete car"() {
        given:
        def newCar = new CarDto(null, "VW", "Passat", "123124112")

        when:
        carService.create(newCar)
        then:
        carRepository.count() == 1
        def createdCar = carRepository.findAll().first()

        with (createdCar) {
            brand == "VW"
            model == "Passat"
            vin == "123124112"
        }

        when:
        carService.update(new CarDto(createdCar.id, "VW", "Golf", "123124112"))
        then:
        carRepository.count() == 1

        with (carRepository.findAll().first()) {
            brand == "VW"
            model == "Golf"
            vin == "123124112"
        }

        when:
        carService.delete(createdCar.id)
        then:
        carRepository.count() == 0
    }
}
