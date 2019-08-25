package pl.sda.carrent.infrastructure;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainViewController {

    @GetMapping("/main")
    ModelAndView mainView(@RequestParam String city) {
        ModelAndView modelAndView = new ModelAndView("index.html");
        modelAndView.addObject("city", city);
        return modelAndView;
    }

}
