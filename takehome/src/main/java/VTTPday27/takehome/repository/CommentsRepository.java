package VTTPday27.takehome.repository;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import VTTPday27.takehome.model.IDNotFoundException;

import static VTTPday27.takehome.utils.Constants.*;

@Repository
public class CommentsRepository {
    @Autowired
    private MongoTemplate template;

    // db.comments.insert({
    //     c_id: "uuid",
    //     user: "Samuel",
    //     rating: 9,
    //     c_text: "could be better",
    //     gid:45986,
    //     date:"2025-01-31"
    // })

    public ObjectId insertNewComment(Document doc){
        //check whether gid in doc is valid
        int gid = doc.getInteger("gid");
        Criteria criteria = Criteria.where(F_GID).is(gid);
        Query query = Query.query(criteria);
        List<Document> docList = template.find(query, Document.class, C_COMMENTS);
        if(docList.isEmpty()){
            throw new IDNotFoundException("Game ID: " + gid + " does not exist" );
        }

        Document newDoc = template.insert(doc, C_COMMENTS); //returns the document with automatically added "_id" property
        ObjectId id = (ObjectId) newDoc.get("_id"); //retrieve the objectid from the _id field
        return id;
    }


    // db.comments.updateMany(
    //     { c_id: "uuid" }, // Filter condition: match documents where c_id is "uuid"
    //     {
    //         $push: {
    //             edited: { // Push new object into the "edited" array
    //                 comment: "test",
    //                 rating: 8,
    //                 date: "2025-01-31"
    //             }
    //         }
    //     },
    //     {upsert:true}
    // )
    public void updateOldComment(String commentId, String comment, int rating, Date date){
        //check whether comment id is valid
        Criteria commentCriteria = Criteria.where(F_C_ID).is(commentId);
        Query commentQuery = Query.query(commentCriteria);
        List<Document> docList = template.find(commentQuery, Document.class, C_COMMENTS);
        if(docList.isEmpty()){
            throw new IDNotFoundException("Comment ID: " + commentId + " does not exist" );
        }


        Criteria criteria = Criteria.where(F_C_ID).is(commentId);
        Query query = Query.query(criteria);

        Update updateOps = new Update()
            .push("edited", new Document("comment", comment)
            .append(F_RATING, rating)
            .append("date", date));
        
        System.out.println(commentId + "\n" +  comment + "\n" + rating + "\n" + date);
        template.upsert(query, updateOps, C_COMMENTS);

    }

    // db.comments.find({c_id: "219ab294"})
    public List<Document> retrieveComment(String commentId){
        Criteria criteria = Criteria.where(F_C_ID).is(commentId);
        Query query = Query.query(criteria);
        return template.find(query, Document.class, C_COMMENTS);
    }


}
