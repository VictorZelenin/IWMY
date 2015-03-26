package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;

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

    /**
     * Generates ratings for other active attendances.
     * @param wildcardAttendances attendances (the first one will be used)
     * @return ratings rating list
     */
    @Path(Api.GENERATE_FOR_ATTENDANCE_ACTIVE) @POST @Consumes(JSON) @Produces(JSON)
    public static List generateForAttendanceActive(List<Attendance> wildcardAttendances) {
        Set<Rating> ratingSet = new TreeSet<>();
        if (wildcardAttendances.size() > 0) {
            Attendance wildcardAttendance = wildcardAttendances.get(0);
            List<Event> eventsWildcard = new ArrayList<>();
            eventsWildcard.add(new Event(wildcardAttendance.getEventOrganizerEmail(),
                    wildcardAttendance.getEventTime(), "", "", "", "", "", ""));
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
                            activeAttendance.getUserEmail(), "" + ratingNumber, "", ""));
                    ++ratingNumber;
                }
            }
        }
        return Arrays.asList(ratingSet.toArray());
    }

    @Path(Api.PUT) @POST @Consumes(JSON) @Produces(JSON)
    public List put(List<Rating> items) {
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

    public static List getForEventSelected(List<Event> wildcardEvents) {
        Set<Rating> outputSet = new TreeSet<>();
        // listing event-related attendances
        List<Attendance> userAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance userAttendance : userAttendances) {
            // and adding selected ratings - from each of them
            outputSet.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("eventOrganizerEmail", userAttendance.getEventOrganizerEmail())
                    .filter("eventTime", userAttendance.getEventTime())
                    .filter("selection", "selected").list());
        }
        return Arrays.asList(outputSet.toArray());
    }

    public static List getForAttendance(List<Attendance> attendanceList) {
        Set<Rating> outputSet = new TreeSet<>();
        for (Attendance userAttendance : attendanceList) {
            outputSet.addAll(ObjectifyService.ofy().load().type(Rating.class)
                    .filter("thisUserEmail", userAttendance.getUserEmail())
                    .filter("eventOrganizerEmail", userAttendance.getEventOrganizerEmail())
                    .filter("eventTime", userAttendance.getEventTime()).list());
        }
        return Arrays.asList(outputSet.toArray());
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Rating.class).list());
    }

    @Path(Api.DEBUG_CREATE) @GET @Produces(JSON)
    public static List debugCreate() {
        List list = Arrays.asList(
                new Rating("John@email.com", "2015-02-28 20:00",
                        "Joe@email.com", "Mih@email.com", "1", "", ""),
                new Rating("Annika@email.com", "2015-03-28 22:00",
                        "Rei@email.com", "Joe@email.com", "2", "selected", "quite ok"),
                new Rating("Rei@email.com", "2015-03-28 23:00",
                        "Mih@email.com", "Joe@email.com", "3", "selected", ""),
                new Rating("Rei@email.com", "2015-03-28 23:00",
                        "Joe@email.com", "Rei@email.com", "4", "", "not really"),
                new Rating("Rei@email.com", "2015-02-28 21:00",
                        "Mih@email.com", "Joe@email.com", "5", "selected", "quite ok"),
                new Rating("Rei@email.com", "2015-02-28 21:00",
                        "Joe@email.com", "Mih@email.com", "6", "selected", ""),
                new Rating("Rei@email.com", "2015-02-28 21:00",
                        "Joe@email.com", "Rei@email.com", "7", "", "not really"));
        ObjectifyService.ofy().save().entities(list).now();
        return list;
    }

    @Path(Api.DEBUG_DELETE_ALL) @GET @Produces(JSON)
    public static String debugDeleteAll() {
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Rating.class).keys());
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
