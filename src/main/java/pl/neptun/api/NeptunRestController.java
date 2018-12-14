package pl.neptun.api;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.neptun.jpa.NeptunDAO;
import pl.neptun.model.Answer;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class NeptunRestController {

    @GetMapping(path = "/hello", produces=MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> sayHello()
    {
        HashMap<String, Object> map = new HashMap<>(); //hashowa mapa zwraca JSON'a
        map = (HashMap<String, Object>) NeptunDAO.findAll(Answer.class);

        return map;
    }
}
