package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * The REST service which accesses user data.
 */
@Path("/couples/")
public class CoupleRestService extends RestService {

    @Path("create/for/event") @POST @Consumes(JSON) @Produces(JSON)
    public List createForEvent(List<Event> wildcardEvents) {
        Event event = wildcardEvents.get(0);
        List<Rating> ratingsSelected = RatingRestService.getForEventSelected(wildcardEvents);
        Map<String, String> coupleMap = new TreeMap<>();
        Map<String, String> selectionMap = new TreeMap<>();
        for (Rating rating : ratingsSelected) {
            // If there is a mutual selection, process the couple
            if (selectionMap.containsKey(rating.getOtherUserEmail())
                    && selectionMap.containsValue(rating.getThisUserEmail())) {
                coupleMap.put(rating.getThisUserEmail(), rating.getOtherUserEmail());
                // Save the couple only if neither of its members participate in other couples
                if (! coupleMap.containsKey(rating.getThisUserEmail())
                        && ! coupleMap.containsValue(rating.getThisUserEmail())
                        && ! coupleMap.containsKey(rating.getOtherUserEmail())
                        && ! coupleMap.containsValue(rating.getOtherUserEmail())) {
                    coupleMap.put(rating.getThisUserEmail(), rating.getOtherUserEmail());
                }
            } else { // otherwise, add selection
                selectionMap.put(rating.getThisUserEmail(), rating.getOtherUserEmail());
            }
        }
        // Make couple objects from the couple map
        List<Couple> couples = new ArrayList<>();
        for (Map.Entry<String, String> coupleEntry : coupleMap.entrySet()) {
            String email1 = coupleEntry.getKey();
            String email2 = coupleEntry.getValue();
            User user1 = (User)UserRestService.get(Arrays.asList(new User(email1))).get(0);
            User user2 = (User)UserRestService.get(Arrays.asList(new User(email2))).get(0);
            couples.add(new Couple(event.getOrganizerEmail(), event.getTime(), email1, email2,
                    user1.getNameAndSurname(), user1.getBirthDate(),
                    user2.getNameAndSurname(), user2.getBirthDate()));
        }
        // Writing couples
        ObjectifyService.ofy().save().entities(couples).now();
        return couples;
    }

    @Path("get/for/event") @POST @Consumes(JSON) @Produces(JSON)
    public List getForEvent(List<Event> wildcardEvents) {
        Set<Couple> couples = new TreeSet<>();
        for (Event wildcardEvent : wildcardEvents) {
            couples.addAll(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime()).list());
        }
        return Arrays.asList(couples.toArray());
    }

    @Path("delete/for/event") @POST @Consumes(JSON) @Produces(JSON)
    public static List deleteForEvent(List<Event> wildcardEvents) {
        for (Event wildcardEvent : wildcardEvents) {
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime()).keys()).now();
        }
        return new ArrayList();
    }

    @Path("get/for/attendance") @POST @Consumes(JSON) @Produces(JSON)
    public List getForAttendance(List<Attendance> wildcardAttendances) {
        Set<Couple> couples = new TreeSet<>();
        for (Attendance wildcardAttendance : wildcardAttendances) {
            couples.addAll(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("eventOrganizerEmail", wildcardAttendance.getEventOrganizerEmail())
                    .filter("eventTime", wildcardAttendance.getEventTime())
                    .filter("userEmail1", wildcardAttendance.getUserEmail()).list());
            couples.addAll(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("eventOrganizerEmail", wildcardAttendance.getEventOrganizerEmail())
                    .filter("eventTime", wildcardAttendance.getEventTime())
                    .filter("userEmail2", wildcardAttendance.getUserEmail()).list());
        }
        return Arrays.asList(couples.toArray());
    }

    @Path("get/all") @POST @Consumes(JSON) @Produces(JSON)
    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Couple.class).list());
    }

    @Path("add") @POST @Consumes(JSON) @Produces(JSON)
    public List add(List<Couple> items) {
        ObjectifyService.ofy().save().entities(items).now();
        return items;
    }

    @Path("debug/create") @GET @Produces(JSON)
    public static List debugCreate() {
        List list = Arrays.asList(
                new Couple("Rei@email.com", "2015-02-28 21:00", "Joe@email.com", "Mih@email.com",
                        "Joe", "1988-12-23", "Mih", "1992-03-18"),
                new Couple("Rei@email.com", "2015-03-28 23:00", "Mih@email.com", "Rei@email.com",
                        "Mih", "1992-03-18", "Rei", "1991-07-11"),
                new Couple("Rei@email.com", "2015-03-28 23:00", "Joe@email.com", "Rei@email.com",
                        "Joe", "1988-12-23", "Rei", "1991-07-11"));
        ObjectifyService.ofy().save().entities(list).now();
        return list;
    }

    @Path("debug/delete/all") @GET @Produces(JSON)
    public static String debugDeleteAll() {
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Couple.class).keys());
        return "Deleted.";
    }


    @Path("debug/reset") @GET @Produces(JSON)
    public static List debugReset() {
        debugDeleteAll();
        return debugCreate();
    }

    @Path("debug/get/all") @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
