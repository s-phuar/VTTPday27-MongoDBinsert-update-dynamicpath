package VTTPday27.inclass.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

@Repository
public class CommentsRepository {

    @Autowired
    private MongoTemplate template;

    //text search the mongo collection
    public List<Document> searchComments(String... terms){ //pass any number of String parameters into the method

        TextCriteria textCriteria = TextCriteria.forDefaultLanguage() //english is default language
            .matchingAny(terms); //consider matchingPhrase for full sentences/whole phrasesc

        TextQuery query = (TextQuery) TextQuery.queryText(textCriteria)
            .sortByScore() //automatically calculated by mongo, sort according to a relevance score with the search term
            .includeScore("weight") //includes the relevance score in our search
            .limit(10);

        query.fields()
            .include("c_text", "weight"); //each document returned should only have c_text and weight returned

        return template.find(query, Document.class, "commentCL");
        
    }

}
