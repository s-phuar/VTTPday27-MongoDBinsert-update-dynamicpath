package VTTPday27.workshop.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;

@Repository
public class CommentsRepository {
    
    @Autowired
    private MongoTemplate template;

    //drop collection
    public void dropCollection(String collectionName){
        template.dropCollection(collectionName);
    }

    //insert comment into collection
    public <T> T insertComments(T doc, String collectionName){
        return template.insert(doc, collectionName);
    }

    //create an index on text for text searches
    public void createTextIndex(String fieldname, String collectionName){
        MongoCollection<Document> collectionTable = template.getCollection(collectionName);

        collectionTable.createIndex(Indexes.text(fieldname));
    }








}
