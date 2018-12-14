package pl.neptun.api;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.neptun.model.Answer;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class NeptunRestController {

    @GetMapping(path = "/hello", produces=MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> sayHello()
    {

        /*
        //Get data from service layer into entityList.
        List<Entity> entityList = new ArrayList<>();

        List<JSONObject> entities = new ArrayList<JSONObject>();


        for (Entity n : entityList) {
            JSONObject entity = new JSONObject();
            try {
                entity.put("aa", "bb");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            entities.add(entity);
        }
        */
        //return new ResponseEntity<>(entities, HttpStatus.OK);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("results", Answer.class);
        return map;
    }
}
