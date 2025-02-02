package VTTPday27.workshop.bootstrap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import VTTPday27.workshop.repository.CommentsRepository;



//1. go the day27workshoppaf folder that holds the comment.json
//2. run: mongoimport  --uri="mongodb+srv://samuel:samuel@vttpday27.389qz.mongodb.net/?retryWrites=true&w=majority&appName=VTTPday27"  --username=samuel --password=samuel --db=commentDB --collection=comment --type=json --jsonArray --file=./comment.json
//2.5. makes sure that the comment.json has already been loaded under the collection name "comment"
//3. run mvn clean spring-boot:run -Dspring-boot.run.arguments="--load=comment.json"

//drop the collection named 'abc' 
//add new/replace atribute '_id' for every new document, and copy the contents of 'c_id' to '_id'
//insert modified documents into abc collection
//create a text index for c_text field


@Component
public class InitComments implements CommandLineRunner{
    private final Logger logger = Logger.getLogger(InitComments.class.getName());

    @Autowired
    private CommentsRepository commentsRepository;


// mvn spring-boot:run -Dspring-boot.run.arguments="--load=comment.json"

    @Override
    public void run(String... args) throws Exception{

        //arguement doesnt start with --load, override method ends prematurely
        if((args.length <= 0) || (!args[0].startsWith("--load="))){
            return;
        }

        //--load=comment.json
        String[] terms = args[0].split("=");
        //terms[1] = comment.json
        Path p = Paths.get(terms[1]);
        //regex splits by the "." so we only take "comment"
        final String collectionName = p.getFileName().toString().split("\\.")[0];

        //drop collection "comment"
        logger.info("### Dropping collection: %s".formatted(collectionName));
        commentsRepository.dropCollection(collectionName);

        //read comment.json
        try(Reader r = new FileReader(p.toFile())){
            BufferedReader br = new BufferedReader(r);
             
            logger.info("### Reading JSON file: %s".formatted(p.toString()));
            JsonReader jsonReader = Json.createReader(br);
            JsonArray arr = jsonReader.readArray();
            br.close();
            logger.info("### Processing JSON documents: %d".formatted(arr.size()));

        // **********************************************8


            // Prepare batch insertion
            List<Document> batch = new ArrayList<>();
            int batchSize = 10000;  // Define batch size (adjust as necessary)
            AtomicInteger atomicCounter = new AtomicInteger(0);

            // Process each JSON object
            arr.stream()
                .map(j -> Document.parse(j.toString()))
                .map(d -> {
                    d.put("_id", d.getString("c_id")); // Replace default _id with c_id
                    return d;
                })
                .forEach(d -> {
                    batch.add(d);
                    atomicCounter.incrementAndGet();

                    // Insert batch if the batch size is reached
                    if (batch.size() >= batchSize) {
                        commentsRepository.insertCommentsBatch(batch, collectionName);
                        logger.info("Inserted batch of %d documents.".formatted(batch.size()));
                        batch.clear();  // Clear the batch after insertion
                    }
                });

            // Insert any remaining documents in the last batch
            if (!batch.isEmpty()) {
                commentsRepository.insertCommentsBatch(batch, collectionName);
                logger.info("Inserted final batch of %d documents.".formatted(batch.size()));
            }


        // *************************************
            long count = commentsRepository.countCollection(collectionName);  // Count all documents in the collection
            logger.info("### %d documents processed".formatted(count));
        }

        //creating text index on c_text field
        logger.info("### Creating text index");
        commentsRepository.createTextIndex("c_text", collectionName);
        logger.info("### finished tasks");


    }
    


}

//MULTI INSERTION, WORKS!!
// // Prepare batch insertion
// List<Document> batch = new ArrayList<>();
// int batchSize = 10000;  // Define batch size (adjust as necessary)
// AtomicInteger atomicCounter = new AtomicInteger(0);

// // Process each JSON object
// arr.stream()
//     .map(j -> Document.parse(j.toString()))
//     .map(d -> {
//         d.put("_id", d.getString("c_id")); // Replace default _id with c_id
//         return d;
//     })
//     .forEach(d -> {
//         batch.add(d);
//         atomicCounter.incrementAndGet();

//         // Insert batch if the batch size is reached
//         if (batch.size() >= batchSize) {
//             commentsRepository.insertCommentsBatch(batch, collectionName);
//             logger.info("Inserted batch of %d documents.".formatted(batch.size()));
//             batch.clear();  // Clear the batch after insertion
//         }
//     });

// // Insert any remaining documents in the last batch
// if (!batch.isEmpty()) {
//     commentsRepository.insertCommentsBatch(batch, collectionName);
//     logger.info("Inserted final batch of %d documents.".formatted(batch.size()));
// }



//SINNGLE insertion, WORKS!!
// int counter = 0;
// AtomicInteger atomicCounter = new AtomicInteger(counter);


// //process comment.json jsonarray
// //each json object in the array is parsed into a document
// //for each document, insert into collection
// arr.stream()
//     .map(j -> Document.parse(j.toString()))
//     //replace the default ObjectId "_id" values with values from the "c_id" field
//     //returned modified document "d"
//     //d.put is a void return method, hence we use return d
//     .map(d -> {
//         d.put("_id", d.getString("c_id")); 
//         return d;
//     })
//     //insert every document into "comment" collection
//     .forEach( d -> {
//         //Batch insert is more efficient
//         commentsRepository.insertComments(d, collectionName);
//         logger.info("Processed %d".formatted(atomicCounter.incrementAndGet()));
//     });