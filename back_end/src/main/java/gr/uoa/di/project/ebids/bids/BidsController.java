package gr.uoa.di.project.ebids.bids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

/* * * * * * * * * * * * * * * * *
 * Controller for requests on bids
 * * * * * * * * * * * * * * * * */

@RestController
public class BidsController {

    @Autowired
    BidsService bidsService;

    // Get all bids
    @GetMapping(value = "/bids", produces = "application/json")
    public ResponseEntity<List<BidsWS>> getBids() {
        return new ResponseEntity<>(bidsService.getBids(), HttpStatus.OK);
    }

    // Add new bid
    @PostMapping(value = "/bids", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Boolean> addBid(@RequestBody BidsWS bid) throws ParseException {
        return new ResponseEntity<>(bidsService.addBid(bid), HttpStatus.OK);
    }

    // Get bid by id
    @GetMapping(value = "/bids/{id}", produces = "application/json")
    public ResponseEntity<BidsWS> getBid(@PathVariable Long id) {
        return new ResponseEntity<>(bidsService.getBid(id), HttpStatus.OK);
    }

    // Update bid
    @PutMapping(value = "/bids/{id}", consumes = "application/json", produces = "application/json")
        public ResponseEntity<Boolean> updateBid(@RequestBody BidsWS bid, @PathVariable Long id) throws ParseException {
        return new ResponseEntity<>(bidsService.updateBid(bid, id), HttpStatus.OK);
    }

    // Update bid
    @DeleteMapping(value = "/bids/{id}", produces = "application/json")
    public ResponseEntity<Boolean> deleteBid(@PathVariable Long id) {
        return new ResponseEntity<>(bidsService.deleteBid(id), HttpStatus.OK);
    }
}
