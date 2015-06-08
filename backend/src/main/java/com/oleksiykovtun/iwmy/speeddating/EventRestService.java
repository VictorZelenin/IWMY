package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * The REST service which accesses user data.
 */
@Path(Api.EVENTS)
public class EventRestService extends GeneralRestService {

    /**
     * Gets events filtered only by organizer
     * @param wildcardEvents events to get organizer
     * @return list of fully specified events
     */
    @Path(Api.GET) @POST @Consumes(JSON) @Produces(JSON)
    public static List<Event> get(List<Event> wildcardEvents) {
        List<Event> events = new ArrayList<>();
        for (Event wildcardEvent : wildcardEvents) {
            events.addAll(ObjectifyService.ofy().load().type(Event.class)
                    .filter("organizerEmail", wildcardEvent.getOrganizerEmail()).list());
        }
        return events;
    }

    /**
     * Gets strict events filtered by both organizer and time
     * @param wildcardEvents events to get organizer and time
     * @return list of fully specified events
     */
    @Path(Api.GET_FOR_TIME) @POST @Consumes(JSON) @Produces(JSON)
    public static List<Event> getForTime(List<Event> wildcardEvents) {
        List<Event> events = new ArrayList<>();
        for (Event wildcardEvent : wildcardEvents) {
            events.addAll(ObjectifyService.ofy().load().type(Event.class)
                    .filter("organizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("time", wildcardEvent.getTime()).list());
        }
        return events;
    }

    @Path(Api.GET_FOR_ATTENDANCE_ACTIVE) @POST @Consumes(JSON) @Produces(JSON)
    public static List getForAttendanceActive(List<Attendance> wildcardAttendances) {
        // getting active user attendance
        List<Attendance> activeAttendances = AttendanceRestService.getActive(wildcardAttendances);
        // then getting events for active attendances
        List<Event> wildcardUserEvents = new ArrayList<>();
        for (Attendance activeAttendance : activeAttendances) {
            wildcardUserEvents.add(new Event(activeAttendance.getEventOrganizerEmail(),
                    activeAttendance.getEventTime(), "", "", "", "", "", "", "", "", "", "", ""));
        }
        List<Event> activeEvents = EventRestService.getForTime(wildcardUserEvents);
        // finally, getting only started events
        List<Event> startedActiveEvents = new ArrayList<>();
        for (Event event : activeEvents) {
            if (! event.getMaxRatingsPerUser().equals("0")) {
                startedActiveEvents.add(event);
            }
        }
        return startedActiveEvents;
    }

    @Path(Api.GET_FOR_USER) @POST @Consumes(JSON) @Produces(JSON)
    public static List getForUser(List<User> wildcardUsers) {
        List<Attendance> userAttendances = AttendanceRestService.getForUser(wildcardUsers);
        List<Event> wildcardUserEvents = new ArrayList<>();
        for (Attendance userAttendance : userAttendances) {
            wildcardUserEvents.add(new Event(userAttendance.getEventOrganizerEmail(),
                    userAttendance.getEventTime(), "", "", "", "", "", "", "", "", "", "", ""));
        }
        return EventRestService.getForTime(wildcardUserEvents);
    }

    @Path(Api.GET_ALL_FOR_USER) @POST @Consumes(JSON) @Produces(JSON)
    public static List getAllForUser(List<User> users) {
        List<Event> userEvents = new ArrayList<>();
        if (users.size() == 1) {
            int userAge = Integer.parseInt(Time.getYearsFromDate(users.get(0).getBirthDate()));
            List<Event> allEvents = getAll();
            for (Event event : allEvents) {
                int minEventAllowedAge = (event.getMinAllowedAge().length() == 0)
                        ? Integer.MIN_VALUE : Integer.parseInt(event.getMinAllowedAge().trim());
                int maxEventAllowedAge = (event.getMaxAllowedAge().length() == 0)
                        ? Integer.MAX_VALUE : Integer.parseInt(event.getMaxAllowedAge().trim());
                if (minEventAllowedAge <= userAge && userAge <= maxEventAllowedAge
                        && ! event.getActual().equals("false")) {
                    userEvents.add(event);
                }
            }
        }
        return userEvents;
    }

    @Path(Api.GET_ALL) @POST @Consumes(JSON) @Produces(JSON)
    public static List<Event> getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Event.class).list());
    }

    public static List<Event> getUniqueForOrganizer(List<Event> wildcardEvents) {
        Set<Event> users = new TreeSet<>();
        for (Event wildcardEvent : wildcardEvents) {
            users.addAll(ObjectifyService.ofy().load().type(Event.class)
                    .filter("organizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("actual", "true")
                    .filter("place", wildcardEvent.getPlace()).list());
            users.addAll(ObjectifyService.ofy().load().type(Event.class)
                    .filter("organizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("time", wildcardEvent.getTime()).list());
        }
        return new ArrayList<>(users);
    }

    @Path(Api.ADD) @POST @Consumes(JSON) @Produces(JSON)
    public List add(List<Event> items) {
        if (EventRestService.getUniqueForOrganizer(items).isEmpty()) {
            return put(savePhotos(items));
        } else {
            return new ArrayList();
        }
    }

    @Path(Api.PUT) @POST @Consumes(JSON) @Produces(JSON)
    public static List put(List<Event> items) {
        ObjectifyService.ofy().save().entities(items).now();
        return items;
    }

    /**
     * Saving images separately and replacing them with links if they are not already links
     * @param events events with base64 image data
     * @return events with image links
     */
    private static List<Event> savePhotos(List<Event> events) {
        for (Event event : events) {
            if (! event.getPhoto().startsWith("_")) {
                event.setPhoto(ImageRestService.put(event.getPhoto()));
            }
            if (! event.getThumbnail().startsWith("_")) {
                event.setThumbnail(ImageRestService.putThumbnail(event.getThumbnail()));
            }
        }
        return events;
    }

    /**
     * Sets max ratings per user 0 for corresponding full events
     * @param wildcardEvents wildcard events for getting full events
     * @return resulting full events
     */
    public static List lock(List<Event> wildcardEvents) {
        List<Event> fullUnlockedEvents = EventRestService.getForTime(wildcardEvents);
        for (Event fullUnlockedEvent : fullUnlockedEvents) {
            fullUnlockedEvent.setMaxRatingsPerUser("0");
            fullUnlockedEvent.setAllowSendingRatings("false");
        }
        return EventRestService.put(fullUnlockedEvents);
    }

    /**
     * Deletes events that match wildcard events by organizerEmail and time
     * @param wildcardEvents events with organizerEmail and time specified to delete by
     * @return events which remain and match wildcard events by organizerEmail
     */
    @Path(Api.DELETE) @POST @Consumes(JSON) @Produces(JSON)
    public List delete(List<Event> wildcardEvents) {
        // todo delete orphan attendances
        for (Event wildcardEvent : wildcardEvents) {
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Event.class)
                    .filter("organizerEmail", wildcardEvent.getOrganizerEmail())
                    .filter("time", wildcardEvent.getTime())
                    .keys()).now();
        }
        return get(wildcardEvents);
    }

    @Path(Api.SET_UNACTUAL) @POST @Consumes(JSON) @Produces(JSON)
    public List setUnactual(List<Event> wildcardEvents) {
        List<Event> events = EventRestService.getForTime(wildcardEvents);
        for (Event event : events) {
            event.setActual("false");
        }
        put(events);
        return events;
    }

    @Path(Api.SET_USER_RATINGS_ALLOW) @POST @Consumes(JSON) @Produces(JSON)
    public List setUserRatingsAllow(List<Event> wildcardEvents) {
        List<Event> events = EventRestService.getForTime(wildcardEvents);
        for (Event event : events) {
            event.setAllowSendingRatings("true");
        }
        put(events);
        return events;
    }

    @Path(Api.REPLACE) @POST @Consumes(JSON) @Produces(JSON)
    public List replace(List<Event> events) {
        if (events.size() == 2) {
            Event oldEvent = events.get(0);
            List<Event> eventsMatchingOldOrNewEvent = EventRestService.getUniqueForOrganizer(events);
            // replace if the new event matches no other event by this org. or the old same one
            if (eventsMatchingOldOrNewEvent.isEmpty()
                    || eventsMatchingOldOrNewEvent.size() == 1
                    && eventsMatchingOldOrNewEvent.get(0).getOrganizerEmail().equals(oldEvent.getOrganizerEmail())
                    && eventsMatchingOldOrNewEvent.get(0).getPlace().equals(oldEvent.getPlace())
                    || eventsMatchingOldOrNewEvent.size() == 1
                    && eventsMatchingOldOrNewEvent.get(0).getOrganizerEmail().equals(oldEvent.getOrganizerEmail())
                    && eventsMatchingOldOrNewEvent.get(0).getTime().equals(oldEvent.getTime())) {
                return replaceForEvent(events);
            }
        }
        return new ArrayList();
    }

    private List replaceForEvent(List<Event> events) {
        List<Event> oldEvents = events.subList(0, 1);
        // deleting old event
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Event.class)
                .filter("organizerEmail", oldEvents.get(0).getOrganizerEmail())
                .filter("time", oldEvents.get(0).getTime()).keys()).now();
        // putting new event
        List<Event> newEvents = events.subList(1, 2);
        put(savePhotos(newEvents));
        // linking related data to the new event
        AttendanceRestService.replaceForEvent(events);
        CoupleRestService.replaceForEvent(events);
        RatingRestService.replaceForEvent(events);
        return newEvents;
    }

    @Deprecated @Path(Api.GET_COUNT) @GET @Produces(TEXT)
    public String getCount() {
        return "" + ObjectifyService.ofy().load().type(Event.class).list().size();
    }

    @Deprecated @Path(Api.GET_COUNT_ACTUAL) @GET @Produces(TEXT)
    public String getCountActual() {
        return "" + ObjectifyService.ofy().load().type(Event.class)
                .filter("actual", "true").list().size();
    }

    /**
     * Gives the number of future actual events as plain text
     * Event time is treated as UTC
     * @return number of future actual events as plain text
     */
    @Path(Api.GET_COUNT_ACTUAL_FUTURE) @GET @Produces(TEXT)
    public String getCountActualFuture() {
        List<Event> actualEvents = ObjectifyService.ofy().load().type(Event.class)
                .filter("actual", "true").list();
        int futureEventCount = 0;
        for (Event event : actualEvents) {
            if (Time.getMillisFromDateTime(event.getTime()) < 0) {
                ++futureEventCount;
            }
        }
        return "" + futureEventCount;
    }

    /**
     * Gives the number of future unactual events as plain text
     * Event time is treated as UTC
     * @return number of future unactual events as plain text
     */
    @Path(Api.GET_COUNT_UNACTUAL_FUTURE) @GET @Produces(TEXT)
    public String getCountUnctualFuture() {
        List<Event> unactualEvents = ObjectifyService.ofy().load().type(Event.class)
                .filter("actual", "false").list();
        int futureEventCount = 0;
        for (Event event : unactualEvents) {
            if (Time.getMillisFromDateTime(event.getTime()) < 0) {
                ++futureEventCount;
            }
        }
        return "" + futureEventCount;
    }

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
