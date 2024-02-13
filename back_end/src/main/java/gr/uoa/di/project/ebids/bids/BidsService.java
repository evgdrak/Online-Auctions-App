package gr.uoa.di.project.ebids.bids;

import gr.uoa.di.project.ebids.item.Item;
import gr.uoa.di.project.ebids.item.ItemDAO;
import gr.uoa.di.project.ebids.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/* * * * * * * * * * * * * * * * *
 * Functionalities for bids entity
 * * * * * * * * * * * * * * * * */

@Service
public class BidsService {
    @Autowired
    ItemDAO itemDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    BidsDAO bidsDAO;

    @Autowired
    private BidsRepository bidsRepository;

    public List<BidsWS> getBids(){
        List<BidsWS> bidsWS = new ArrayList<>();
        List<Bids> bids = bidsDAO.getBids();
        for(Bids bid: bids){
            bidsWS.add(new BidsWS(bid));
        }
        return bidsWS;
    }

    public Boolean addBid(BidsWS bid) throws ParseException{
        Item item = itemDAO.getItem(bid.getItem());
        Date time = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(bid.getTime());
        if(item.getStarted().after(time) || item.getEnds().before(time) || item.getCurrently() >= bid.getAmount()){
            return false;
        }
        try {
            // change item with new data
            item.setNumber_of_bids(item.getNumber_of_bids() + 1);
            item.setCurrently(bid.getAmount());
            itemDAO.updateItem(item);

            // add bid
            Bids newBid = new Bids(bid);
            newBid.setItem(item);
            newBid.setUser(userDAO.findByUsername(bid.getUserID()));

            bidsDAO.addBid(newBid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public BidsWS getBid(Long id) {
        return new BidsWS(bidsDAO.getBid(id));
    }

    public Boolean updateBid(BidsWS bidsWS, Long id) throws ParseException {
        Bids bid = bidsDAO.getBid(id);
        if(bid!=null){
            if(bid.getId() != bidsWS.getId()){
                bid.setId(bidsWS.getId());
            }
            Date time = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(bidsWS.getTime());
            if(bid.getTime() != time){
                bid.setTime(time);
            }
            if(bid.getAmount() != bidsWS.getAmount()){
                bid.setAmount(bidsWS.getAmount());
            }
            if(bid.getRating() != bidsWS.getRating()){
                bid.setAmount(bidsWS.getAmount());
            }
            if(bid.getItem().getId() != bidsWS.getItem()){
                bid.setItem(itemDAO.getItem(bidsWS.getItem()));
            }

            bidsDAO.updateBid(bid);
            return true;
        }

        return false;
    }

    public Boolean deleteBid(Long id) {
        if(bidsDAO.getBid(id) != null){
            return bidsDAO.deleteBid(id);
        }
        return false;
    }
}
