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
@Path(Api.COUPLES)
public class CoupleRestService extends GeneralRestService {

    @Path(Api.GENERATE_FOR_EVENT) @POST @Consumes(JSON) @Produces(JSON)
    public List generateForEvent(List<Event> wildcardEvents) {
        // cleanup
        deleteForEvent(wildcardEvents);
        // making all possible couples
        Event event = wildcardEvents.get(0);
        List<Rating> ratingsSelected = RatingRestService.getForEventSelected(wildcardEvents);
        Map<String, String> coupleMap = new TreeMap<>();
        for (Rating rating : ratingsSelected) {
            if (! coupleMap.containsKey(rating.getThisUserEmail())
                    && ! coupleMap.containsKey(rating.getOtherUserEmail())
                    && ! coupleMap.containsValue(rating.getThisUserEmail())
                    && ! coupleMap.containsValue(rating.getOtherUserEmail()))
            coupleMap.put(rating.getThisUserEmail(), rating.getOtherUserEmail());
        }
        // making couple objects from the couple map
        List<Couple> couples = new ArrayList<>();
        for (Map.Entry<String, String> coupleEntry : coupleMap.entrySet()) {
            String email1 = coupleEntry.getKey();
            String email2 = coupleEntry.getValue();
            User user1 = (User)UserRestService.get(Arrays.asList(new User(email1))).get(0);
            User user2 = (User)UserRestService.get(Arrays.asList(new User(email2))).get(0);
            if (! user1.getGender().equals(user2.getGender())) {
                couples.add(new Couple(event.getOrganizerEmail(), event.getTime(), email1, email2,
                        user1.getNameAndSurname(), user1.getBirthDate(),
                        user2.getNameAndSurname(), user2.getBirthDate()));
            }
        }
        return couples;
    }

    @Path(Api.PUT) @POST @Consumes(JSON) @Produces(JSON)
    public List put(List<Couple> items) {
        ObjectifyService.ofy().save().entities(items).now();
        return items;
    }

    @Path(Api.GET_FOR_ATTENDANCE) @POST @Consumes(JSON) @Produces(JSON)
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

    public static List deleteForEvent(List<Event> wildcardEvents) {
        for (Event wildcardEvent : wildcardEvents) {
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime()).keys()).now();
        }
        return new ArrayList();
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Couple.class).list());
    }

    @Path(Api.DEBUG_CREATE) @GET @Produces(JSON)
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

    @Path(Api.DEBUG_DELETE_ALL) @GET @Produces(JSON)
    public static String debugDeleteAll() {
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Couple.class).keys());
        return "Deleted.";
    }

    @Path(Api.DEBUG_RESET) @GET @Produces(JSON)
    public static List debugReset() {
        debugDeleteAll();
        return debugCreate();
    }

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
