package pl.neptun.api;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NeptunRestController {

    @GetMapping(path = "/hello", produces=MediaType.APPLICATION_JSON_VALUE)
    public String sayHello()
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
        return "db";
    }
}
