package pl.neptun.api;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NeptunRestController {
    @GetMapping(path = "/hello", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sayHello()
    {
        //Get data from service layer into entityList.

        List<JSONObject> entities = new ArrayList<JSONObject>();
        Entity[] entityList = new Entity[5];

        for (Entity n : entityList) {
            JSONObject entity = new JSONObject();
            try {
                entity.put("aa", "bb");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            entities.add(entity);
        }
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }
}
