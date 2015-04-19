package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
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
@Path(Api.ATTENDANCES)
public class AttendanceRestService extends GeneralRestService {

    @Path(Api.TOGGLE) @POST @Consumes(JSON) @Produces(JSON)
    public List toggle(List<Attendance> wildcardAttendances) {
        Set<Attendance> attendanceSet = new TreeSet<>();
        for (Attendance wildcardAttendance : wildcardAttendances) {
            attendanceSet.addAll(ObjectifyService.ofy().load().type(Attendance.class)
                    .filter("eventOrganizerEmail", wildcardAttendance.getEventOrganizerEmail())
                    .filter("eventTime", wildcardAttendance.getEventTime())
                    .filter("userEmail", wildcardAttendance.getUserEmail()).list());
        }
        for (Attendance attendance : attendanceSet) {
            if (attendance.getActive().equals("true")) {
                attendance.setActive("false");
            } else {
                attendance.setActive("true");
            }
        }
        ObjectifyService.ofy().save().entities(attendanceSet).now();
        return new ArrayList<>();
    }

    @Path(Api.GET) @POST @Consumes(JSON) @Produces(JSON)
    public List get(List<Attendance> wildcardAttendances) {
        List<Attendance> attendances = new ArrayList<>();
        if (wildcardAttendances.size() == 1) {
            Attendance wildcardAttendance = wildcardAttendances.get(0);
            attendances.addAll(ObjectifyService.ofy().load().type(Attendance.class)
                    .filter("eventOrganizerEmail", wildcardAttendance.getEventOrganizerEmail())
                    .filter("eventTime", wildcardAttendance.getEventTime())
                    .filter("userEmail", wildcardAttendance.getUserEmail()).list());
        }
        return attendances;
    }

    @Path(Api.GET_FOR_EVENT_ACTIVE_CHECK_VOTED) @POST @Consumes(JSON) @Produces(JSON)
    public static List<Attendance> getForEventActiveVoted(List<Event> wildcardEvents) {
        Set<Attendance> attendances = new TreeSet<>();
        for (Event wildcardEvent : wildcardEvents) {
            attendances.addAll(ObjectifyService.ofy().load().type(Attendance.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime())
                    .filter("active", "true").list());
        }
        // making "fake" attendances of not yet voted users false
        for (Attendance attendance : attendances) {
            if (RatingRestService.getForAttendance(Arrays.asList(attendance)).isEmpty()) {
                attendance.setActive("false");
            }
        }
        return new ArrayList<>(attendances);
    }

    /**
     * Checking until all active attendants put ratings.
     * @param wildcardEvents events for active attendants
     * @return ratings created by all active attendants or empty if not by all of them
     */
    @Path(Api.CHECK_FOR_EVENT_ACTIVE_ALL) @POST @Consumes(JSON) @Produces(JSON)
    public static List checkForEventActiveAll(List<Event> wildcardEvents) {
        List<Attendance> attendances = getForEventActive(wildcardEvents);
        boolean allActive = true;
        for (Attendance attendance : attendances) {
            if (RatingRestService.getForAttendance(Arrays.asList(attendance)).isEmpty()) {
                allActive = false;
            }
        }
        return allActive ? attendances : new ArrayList();
    }

    @Path(Api.ADD) @POST @Consumes(JSON) @Produces(JSON)
    public static List add(List<Attendance> items) {
        ObjectifyService.ofy().save().entities(items).now();
        return items;
    }

    @Path(Api.DELETE) @POST @Consumes(JSON) @Produces(JSON)
    public List delete(List<Attendance> wildcardAttendances) {
        for (Attendance wildcardAttendance : wildcardAttendances) {
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Attendance.class)
                    .filter("eventOrganizerEmail", wildcardAttendance.getEventOrganizerEmail())
                    .filter("eventTime", wildcardAttendance.getEventTime())
                    .keys()).now();
        }
        return get(wildcardAttendances);
    }

    public static List<Attendance> getForEventActive(List<Event> wildcardEvents) {
        Set<Attendance> attendances = new TreeSet<>();
        for (Event wildcardEvent : wildcardEvents) {
            attendances.addAll(ObjectifyService.ofy().load().type(Attendance.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime())
                    .filter("active", "true").list());
        }
        return new ArrayList<>(attendances);
    }

    public static List<Attendance> getForEvent(List<Event> wildcardEvents) {
        Set<Attendance> attendances = new TreeSet<>();
        for (Event wildcardEvent : wildcardEvents) {
            attendances.addAll(ObjectifyService.ofy().load().type(Attendance.class)
                    .filter("eventOrganizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("eventTime", wildcardEvent.getTime()).list());
        }
        return new ArrayList<>(attendances);
    }

    public static List<Attendance> getForUser(List<User> wildcardUsers) {
        List<Attendance> attendances = new ArrayList<>();
        for (User wildcardUser : wildcardUsers) {
            attendances.addAll(ObjectifyService.ofy().load().type(Attendance.class)
                    .filter("userEmail", wildcardUser.getEmail()).list());
        }
        return attendances;
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Attendance.class).list());
    }

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
