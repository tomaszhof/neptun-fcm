package pl.neptun.api;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.neptun.jpa.NeptunDAO;
import pl.neptun.model.Answer;

import javax.transaction.Transactional;
import java.util.HashMap;

@Transactional
@RestController
@RequestMapping("/api")
public class NeptunRestController {

    @GetMapping(path = "/hello", produces=MediaType.APPLICATION_JSON_VALUE)
    public String sayHello()
    {
        HashMap<String, Object> map = new HashMap<>(); //hashowa mapa zwraca JSON'a
        Answer list = NeptunDAO.findByCode(Answer.class, "A1");

        return list.toString();
    }
}
