package pl.sda.carrent.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainViewMVCIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldDisplayMainView() throws Exception {
        //when
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/main?city=Lublin"))
                        .andDo(print());
        //then
        result.andExpect(model().attribute("city", "Lublin"))
                .andExpect(content().string(
                    containsString("Witaj w wypozyczalni aut w <span>Lublin</span>")))
                .andExpect(status().isOk());
    }

}
