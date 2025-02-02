package VTTPday27.takehome.controller;

import java.text.ParseException;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import VTTPday27.takehome.service.CommentsService;
import VTTPday27.takehome.utils.DateMethods;

@Controller
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/review/{review_id}")
    public String getReviewPage(@PathVariable("review_id") String review_id, Model model) {
        model.addAttribute("review_id", review_id);
        return "update-comment";
    }
    

    @PostMapping("/review")
    public String insertNewComment(@RequestParam MultiValueMap<String, String> form, Model model){

        String username = form.getFirst("name");
        int rating = Integer.parseInt(form.getFirst("rating"));
        String comments = form.getFirst("comments");
        int gid = Integer.parseInt(form.getFirst("gid"));

        ObjectId id = commentsService.insertNewComment(username, rating, comments, gid);
        model.addAttribute("id", id);

        return "comment-insert";
    }

    //gave up and used postmapping
    //http://localhost:8080/review/uuid
    @PostMapping("/review/{review_id}")
    public String updateOldComment(@RequestParam MultiValueMap<String, String> form, @PathVariable(name="review_id") String c_id, Model model) throws ParseException{

        String comment = form.getFirst("comment");
        int rating = Integer.parseInt(form.getFirst("rating"));
        Date date = DateMethods.strToDate(form.getFirst("date"));

        commentsService.updateOldComment(c_id, comment, rating, date);

        return String.format("redirect:/review/%s", c_id);      
    }

    //part c. http://localhost:8080/review/api/091910bf
    //part d. http://localhost:8080/review/api/219ab294

    @GetMapping("/review/api/{review_id}")
    @ResponseBody
    public ResponseEntity<String> retrieveLatestComments(@PathVariable(name="review_id") String c_id){

        return ResponseEntity.ok().body(commentsService.retrieveComment(c_id).toString());

    }



}
