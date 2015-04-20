package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.text.ParseException;
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
        // making all possible couples
        Event event = wildcardEvents.get(0);
        List<Rating> ratingsSelected = RatingRestService.getForEventSelected(wildcardEvents);
        List<Couple> couples = new ArrayList<>();
        // detecting couples (mutual choice)
        for (int i = 0; i < ratingsSelected.size(); ++i) {
            for (int j = 0; j < i; ++j) {
                Rating rating1 = ratingsSelected.get(i);
                Rating rating2 = ratingsSelected.get(j);
                if (rating2.getThisUserEmail().equals(rating1.getOtherUserEmail())
                        && rating2.getOtherUserEmail().equals(rating1.getThisUserEmail())) {
                    // making a couple object from the mutual ratings
                    User user1 = (User) UserRestService.get(Arrays
                            .asList(new User(rating1.getThisUserEmail()))).get(0);
                    User user2 = (User) UserRestService.get(Arrays
                            .asList(new User(rating2.getThisUserEmail()))).get(0);
                    User userMale = user1.getGender().equals(User.MALE) ? user1 : user2;
                    User userFemale = user2.getGender().equals(User.FEMALE) ? user2 : user1;
                    couples.add(new Couple(event, userMale, userFemale));
                }
            }
        }
        return couples;
    }

    /**
     * Adds couples to database and sends emails to them and to organizer
     * @param couples couples to add to database
     * @return couples added
     */
    @Path(Api.PUT) @POST @Consumes(JSON) @Produces(JSON)
    public static List put(List<Couple> couples) {
        ObjectifyService.ofy().save().entities(couples).now();
        return couples;
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

    @Path(Api.GET_FOR_EVENT) @POST @Consumes(JSON) @Produces(JSON)
    public static List getForEvent(List<Event> wildcardEvents) {
        Set<Couple> couples = new TreeSet<>();
        for (Event wildcardEvent : wildcardEvents) {
            couples.addAll(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime()).list());
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

    private static List<Couple> getForUser(List<User> wildcardUsers) {
        Set<Couple> couples = new TreeSet<>();
        for (User wildcardUser : wildcardUsers) {
            couples.addAll(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("userEmail1", wildcardUser.getEmail()).list());
            couples.addAll(ObjectifyService.ofy().load().type(Couple.class)
                    .filter("userEmail2", wildcardUser.getEmail()).list());
        }
        return Arrays.asList(couples.toArray(new Couple[couples.size()]));
    }

    public static List replaceForUser(List<User> users) {
        List<User> oldUsers = users.subList(0, 1);
        List<Couple> userRelatedItems = getForUser(oldUsers);
        // deleting for old user
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Couple.class)
                .filter("userEmail1", oldUsers.get(0).getEmail()).keys()).now();
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Couple.class)
                .filter("userEmail2", oldUsers.get(0).getEmail()).keys()).now();
        // replacing user email
        List<User> newUsers = users.subList(1, 2);
        for (Couple relatedItem : userRelatedItems) {
            if (relatedItem.getUserEmail1().equals(oldUsers.get(0).getEmail())) {
                relatedItem.setUser1(newUsers.get(0));
            }
            if (relatedItem.getUserEmail2().equals(oldUsers.get(0).getEmail())) {
                relatedItem.setUser2(newUsers.get(0));
            }
        }
        // adding for new users
        return put(userRelatedItems);
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Couple.class).list());
    }

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
