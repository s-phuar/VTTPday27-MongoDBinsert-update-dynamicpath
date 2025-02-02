package VTTPday27.workshop.repository;

import java.util.List;

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
        // db.comment.drop();
    public void dropCollection(String collectionName){
        template.dropCollection(collectionName);
    }

    //count number of documents in collection
        //db.comment.countDocuments();
    public long countCollection(String collectionName){
        MongoCollection<Document> collectionTable = template.getCollection(collectionName);
        return collectionTable.countDocuments();
    }


    //insert comment into collection
        // db.comment.insertOne({ "_id": "12345", "c_text": "This is a comment" });
    public <T> T insertComments(T doc, String collectionName){
        return template.insert(doc, collectionName);
    }

    //create an index on text for text searches
        //db.comment.createIndex({ c_text: "text", title: "text" });
    //searching text
        //db.commentCL.find({ $text: { $search: "amazing love" } })
    public void createTextIndex(String fieldname, String collectionName){
        MongoCollection<Document> collectionTable = template.getCollection(collectionName);

        collectionTable.createIndex(Indexes.text(fieldname));
    }



    // New method for multi batch insert
        // var collection = db.getCollection(collectionName);
        // collection.insertMany(documents);
    public void insertCommentsBatch(List<Document> documents, String collectionName) {
        if (documents != null && !documents.isEmpty()) {
            MongoCollection<Document> collection = template.getCollection(collectionName);
            // Use insertMany to batch insert the documents
            collection.insertMany(documents);
            System.out.println("Inserted " + documents.size() + " documents into collection " + collectionName);
        } else {
            System.out.println("No documents to insert into collection " + collectionName);
        }
    }





}
