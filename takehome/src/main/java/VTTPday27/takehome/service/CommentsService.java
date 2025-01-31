package VTTPday27.takehome.service;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VTTPday27.takehome.repository.CommentsRepository;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

import static VTTPday27.takehome.utils.Constants.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    //builds the document in service
    public ObjectId insertNewComment(String name, int rating, String comments, int gid){

        String uuid = UUID.randomUUID().toString().substring(0, 8);

        Document toInsert = new Document();
        toInsert.append(F_C_ID, uuid) //comments id
                .append(F_USER, name)   //username
                .append(F_RATING, rating) //user's rating
                .append(F_C_TEXT, comments) //user's comments
                .append(F_GID, gid)    //game's id
                .append("date", Instant.now().toString()); //add in a new field 'timestamp' to the database

        return commentsRepository.insertNewComment(toInsert);
    }


    public void updateOldComment(String commentId, String comment, int rating, Date date){
        commentsRepository.updateOldComment(commentId, comment, rating, date);
    }

    public JsonObject retrieveComment(String commentId){
        List<Document> docList = commentsRepository.retrieveComment(commentId);

        Document doc = docList.get(0);

        try{
        List<Document> editedList = doc.getList("edited", Document.class);
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            
        for(Document editedObj: editedList){
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            JsonObject temp = jsonObjectBuilder
                .add("comment", editedObj.getString("comment"))
                .add("rating", editedObj.getInteger("rating"))
                .add("date", editedObj.getDate("date").toString())
                .build();

                //Date date = doc.getDate("date"); if actual ISOdate
            jsonArrayBuilder.add(temp);
        }
        JsonArray jArray = jsonArrayBuilder.build();
        JsonObject jsonObj = Json.createObjectBuilder()
            .add("user", doc.getString("user"))
            .add("rating", doc.getInteger("rating"))
            .add("comment", doc.getString("c_text"))
            .add("ID", doc.getInteger("gid"))
            .add("date", doc.getString("date"))
            .add("edited", jArray)
            .add("timestamp", Instant.now().toString())
            .build();

            return jsonObj;

    }catch(NullPointerException e){
        boolean jArray = false;

        JsonObject jsonObj = Json.createObjectBuilder()
            .add("user", doc.getString("user"))
            .add("rating", doc.getInteger("rating"))
            .add("comment", doc.getString("c_text"))
            .add("ID", doc.getInteger("gid"))
            .add("date", false)
            .add("edited", jArray)
            .add("timestamp", Instant.now().toString())
            .build();

            return jsonObj;
    }




    }

}
