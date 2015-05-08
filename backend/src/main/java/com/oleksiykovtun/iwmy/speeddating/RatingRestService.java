package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.Arrays;
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
@Path(Api.RATINGS)
public class RatingRestService extends GeneralRestService {

    @Path(Api.GET_FOR_ATTENDANCE_ACTIVE) @POST @Consumes(JSON) @Produces(JSON)
    public static List getForAttendanceActive(List<Attendance> attendanceList) {
        Set<Rating> outputSet = new TreeSet<>();
        // first, filling ratings with existing ones
        for (Attendance userAttendance : attendanceList) {
            outputSet.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("thisUserEmail", userAttendance.getUserEmail())
                    .filter("eventOrganizerEmail", userAttendance.getEventOrganizerEmail())
                    .filter("eventTime", userAttendance.getEventTime()).list());
        }
        // then generating the rest of ratings (the Set will not allow overwriting)
        outputSet.addAll(generateForAttendanceActive(attendanceList));
        return new ArrayList<>(outputSet);
    }

    /**
     * Generates ratings for other active attendances.
     * @param wildcardAttendances attendances (the first one will be used)
     * @return ratings rating list
     */
    public static List<Rating> generateForAttendanceActive(List<Attendance> wildcardAttendances) {
        Set<Rating> ratingSet = new TreeSet<>();
        if (wildcardAttendances.size() > 0) {
            Attendance wildcardAttendance = wildcardAttendances.get(0);
            List<Event> eventsWildcard = new ArrayList<>();
            eventsWildcard.add(new Event(wildcardAttendance.getEventOrganizerEmail(),
                    wildcardAttendance.getEventTime(), "", "", "", "", "", "", "", "", "", "", ""));
            // listing active event-related attendances
            List<Attendance> activeAttendances
                    = AttendanceRestService.getForEventActive(eventsWildcard);
            // and adding ratings from them
            int ratingNumber = 1;
            for (Attendance activeAttendance : activeAttendances) {
                if (! wildcardAttendance.getUserEmail().equals(activeAttendance.getUserEmail())
                        && ! wildcardAttendance.getUserGender().equals(activeAttendance
                        .getUserGender())) {
                    ratingSet.add(new Rating(wildcardAttendance.getEventOrganizerEmail(),
                            wildcardAttendance.getEventTime(), wildcardAttendance.getUserEmail(),
                            activeAttendance.getUserEmail(), "" + ratingNumber,
                            activeAttendance.getUsername(), "", ""));
                    ++ratingNumber;
                }
            }
        }
        return new ArrayList<>(ratingSet);
    }

    @Path(Api.PUT_ACTUAL) @POST @Consumes(JSON) @Produces(JSON)
    public List putActual(List<Rating> items) {
        for (Rating rating : items) {
            rating.setActual("true");
        }
        put(items);
        return items;
    }

    @Path(Api.PUT) @POST @Consumes(JSON) @Produces(JSON)
    public static List put(List<Rating> items) {
        ObjectifyService.ofy().save().entities(items).now();
        return items;
    }

    /**
     * Delete ratings and couples for event
     * @param wildcardEvents
     * @return
     */
    public static List deleteForEvent(List<Event> wildcardEvents) {
        // listing event-related attendances
        List<Attendance> userAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance userAttendance : userAttendances) {
            // and deleting ratings - from each of them
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("eventOrganizerEmail", userAttendance.getEventOrganizerEmail())
                    .filter("eventTime", userAttendance.getEventTime()).keys()).now();
        }
        return new ArrayList();
    }

    public static List<Rating> getForEvent(List<Event> wildcardEvents) {
        Set<Rating> ratings = new TreeSet<>();
        for (Event wildcardEvent : wildcardEvents) {
            ratings.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime()).list());
        }
        return new ArrayList<>(ratings);
    }

    public static List<Rating> getForEventSelected(List<Event> wildcardEvents) {
        Set<Rating> outputSet = new TreeSet<>();
        // listing event-related attendances
        List<Attendance> userAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance userAttendance : userAttendances) {
            // and adding selected ratings - from each of them
            outputSet.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("eventOrganizerEmail", userAttendance.getEventOrganizerEmail())
                    .filter("eventTime", userAttendance.getEventTime())
                    .filter("selection", Rating.SELECTED).list());
        }
        return new ArrayList<>(outputSet);
    }

    public static List<Rating> getForAttendanceActual(List<Attendance> attendanceList) {
        Set<Rating> outputSet = new TreeSet<>();
        for (Attendance userAttendance : attendanceList) {
            outputSet.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("thisUserEmail", userAttendance.getUserEmail())
                    .filter("eventOrganizerEmail", userAttendance.getEventOrganizerEmail())
                    .filter("eventTime", userAttendance.getEventTime())
                    .filter("actual", "true").list());
        }
        return new ArrayList<>(outputSet);
    }

    private static List<Rating> getForUser(List<User> wildcardUsers) {
        Set<Rating> ratings = new TreeSet<>();
        for (User wildcardUser : wildcardUsers) {
            ratings.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("thisUserEmail", wildcardUser.getEmail()).list());
            ratings.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("otherUserEmail", wildcardUser.getEmail()).list());
        }
        return new ArrayList<>(ratings);
    }

    public static List replaceForUser(List<User> users) {
        List<User> oldUsers = users.subList(0, 1);
        List<Rating> userRelatedItems = getForUser(oldUsers);
        // deleting for old user
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Rating.class)
                .filter("thisUserEmail", oldUsers.get(0).getEmail()).keys()).now();
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Rating.class)
                .filter("otherUserEmail", oldUsers.get(0).getEmail()).keys()).now();
        // replacing user email
        List<User> newUsers = users.subList(1, 2);
        for (Rating relatedItem : userRelatedItems) {
            if (relatedItem.getThisUserEmail().equals(oldUsers.get(0).getEmail())) {
                relatedItem.setThisUserEmail(newUsers.get(0).getEmail());
            }
            if (relatedItem.getOtherUserEmail().equals(oldUsers.get(0).getEmail())) {
                relatedItem.setOtherUserEmail(newUsers.get(0).getEmail());
                relatedItem.setUsername(newUsers.get(0).getUsername());
            }
        }
        // adding for new users
        return put(userRelatedItems);
    }

    public static List replaceForEvent(List<Event> events) {
        List<Event> oldEvents = events.subList(0, 1);
        List<Rating> userRelatedItems = getForEvent(oldEvents);
        // deleting for old event
        deleteForEvent(oldEvents);
        // replacing organizer email and time
        List<Event> newEvents = events.subList(1, 2);
        for (Rating relatedItem : userRelatedItems) {
            relatedItem.setEventOrganizerEmail(newEvents.get(0).getOrganizerEmail());
            relatedItem.setEventTime(newEvents.get(0).getTime());
        }
        // adding for new users
        return put(userRelatedItems);
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Rating.class).list());
    }

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
