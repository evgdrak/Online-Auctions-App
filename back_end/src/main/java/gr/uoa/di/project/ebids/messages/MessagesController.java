package gr.uoa.di.project.ebids.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.Map;

/* * * * * * * * * * * * * * * * * * * * * *
 * Controller for requests on messages entity
 * * * * * * * * * * * * * * * * * * * * * */

@RestController
@RequestMapping("/messages")
public class MessagesController {

    @Autowired
    MessagesService messagesService;

    // Get all messages
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<MessagesList> getMessages(@RequestParam Integer page, @RequestParam Integer limit) {
        return new ResponseEntity<>(messagesService.getMessages(page, limit), HttpStatus.OK);
    }

    // Add new message
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> addMessage(@RequestBody MessagesWS message) throws ParseException {
        return new ResponseEntity<>(messagesService.addMessage(message), HttpStatus.OK);
    }

    // Get message by id
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<MessagesWS> getMessage(@PathVariable Long id) {
        return new ResponseEntity<>(messagesService.getMessage(id), HttpStatus.OK);
    }

    // Update message
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Boolean> updateMessage(@RequestBody MessagesWS message, @PathVariable Long id) throws ParseException {
        return new ResponseEntity<>(messagesService.updateMessage(message, id), HttpStatus.OK);
    }

    // Get messages of user
    @GetMapping(value = "/user/{username}", produces = "application/json")
    public ResponseEntity<MessagesList> getMessagesOfUser(@RequestParam(required = false) Map<String, String> parameters, @PathVariable String username, @RequestParam Boolean send) {
        return new ResponseEntity<>(messagesService.getMessagesOfUser(parameters, username, send), HttpStatus.OK);
    }

    // Get unread messages of user
    @GetMapping(value = "/user/{username}/unread", produces = "application/json")
    public ResponseEntity<Long> getUnreadMessagesOfUser(@PathVariable String username) {
        return new ResponseEntity<>(messagesService.getUnreadMessagesOfUser(username), HttpStatus.OK);
    }

    // Delete message
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Boolean> deleteMessage(@PathVariable Long id) {
        return new ResponseEntity<>(messagesService.deleteMessage(id), HttpStatus.OK);
    }

}
