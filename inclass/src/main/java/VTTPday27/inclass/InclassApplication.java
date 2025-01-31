package VTTPday27.inclass;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import VTTPday27.inclass.repositories.CommentsRepository;

@SpringBootApplication
public class InclassApplication implements CommandLineRunner {

	@Autowired
	private CommentsRepository commentsRepository;

	public static void main(String[] args) {
		SpringApplication.run(InclassApplication.class, args);
	}



	@Override
	public void run(String... args){

		List<Document> results = commentsRepository.searchComments("love", "amazing"); //search for either love or amazing
		
		for(Document d: results){
			System.out.printf(">>> %s\n\n", d.toJson()); //prints out c_text and weight
		}

		for(Document d: results){
			System.out.printf(">>>> %s\n\n", d); //prints out c_text and weight (different from 1 and 3)
		}

		for(Document d: results){
			System.out.printf(">>>>> %s\n\n", d.toJson().toString()); //prints out c_text and weight (essentially the same as 1.)
		}

	}

}
