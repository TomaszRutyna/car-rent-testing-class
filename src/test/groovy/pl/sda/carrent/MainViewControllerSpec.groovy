package pl.sda.carrent

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import pl.sda.carrent.infrastructure.MainViewController
import spock.lang.Specification

import static org.hamcrest.Matchers.containsString
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(MainViewController.class)
class MainViewControllerSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    def "should display main page"() {
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/main?city=Lublin"))
                .andDo(MockMvcResultHandlers.print())
        then:
        result.andExpect(model().attribute("city", "Lublin"))
                .andExpect(content()
                    .string(containsString("Witaj w wypozyczalni aut w <span>Lublin</span>")))
                .andExpect(status().isOk())
    }

    def "should not display main page when city param not present"() {
        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/main"))
                .andDo(MockMvcResultHandlers.print())
        then:
        result.andExpect(status().isBadRequest())
    }

}