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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * The REST service which accesses user data.
 */
@Path(Api.USERS)
public class UserRestService extends GeneralRestService {

    /**
     * Set active=false for event participants and delete associated ratings and couples
     * @param wildcardEvents
     * @return
     */
    @Path(Api.GET_FOR_EVENT_ACTIVE_RESET) @POST @Consumes(JSON) @Produces(JSON)
    public static List getForEventActiveReset(List<Event> wildcardEvents) {
        // current ratings and previous couples cleanup - before the couple selection process
        RatingRestService.deleteForEvent(wildcardEvents);
        CoupleRestService.deleteForEvent(wildcardEvents);
        // "locking" events from users
        EventRestService.lock(wildcardEvents);
        // listing event-related attendances
        Set<User> users = new TreeSet<>();
        List<Attendance> eventAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance eventAttendance : eventAttendances) {
            // reset active state
            eventAttendance.setActive("false");
            // and adding users - from each of them
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("email", eventAttendance.getUserEmail()).list());
        }
        // save attendances and return users
        AttendanceRestService.add(eventAttendances);
        return Arrays.asList(users.toArray());
    }

    /**
     * Removes attendances and returns remaining users for events in given attendances
     * @param wildcardAttendances attendances to delete
     * @return remaining users for events in given attendances
     */
    @Path(Api.REMOVE_ATTENDANCE) @POST @Consumes(JSON) @Produces(JSON)
    public List removeAttendance(List<Attendance> wildcardAttendances) {
        for (Attendance eventAttendance : wildcardAttendances) {
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load()
                    .type(Attendance.class)
                    .filter("eventOrganizerEmail", eventAttendance.getEventOrganizerEmail())
                    .filter("eventTime", eventAttendance.getEventTime())
                    .filter("userEmail", eventAttendance.getUserEmail())
                    .keys()).now();
        }
        return getForEventByAttendance(wildcardAttendances);
    }

    @Path(Api.GET_FOR_EVENT_ACTIVE) @POST @Consumes(JSON) @Produces(JSON)
    public List getForEventActive(List<Event> wildcardEvents) {
        Set<User> users = new TreeSet<>();
        // listing event-related attendances
        List<Attendance> eventAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance eventAttendance : eventAttendances) {
            // and adding users - from each active of them
            if (eventAttendance.getActive().equals("true")) {
                users.addAll(ObjectifyService.ofy().load().type(User.class)
                        .filter("email", eventAttendance.getUserEmail()).list());
            }
        }
        return Arrays.asList(users.toArray());
    }

    @Path(Api.GET_FOR_EVENT) @POST @Consumes(JSON) @Produces(JSON)
    public List getForEvent(List<Event> wildcardEvents) {
        Set<User> users = new TreeSet<>();
        // listing event-related attendances
        List<Attendance> eventAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance eventAttendance : eventAttendances) {
            // and adding users - from each of them
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("email", eventAttendance.getUserEmail()).list());
        }
        return Arrays.asList(users.toArray());
    }

    @Path(Api.GET) @POST @Consumes(JSON) @Produces(JSON)
    public static List get(List<User> wildcardUsers) {
        Set<User> users = new TreeSet<>();
        for (User wildcardUser : wildcardUsers) {
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("email", wildcardUser.getEmail()).list());
        }
        return Arrays.asList(users.toArray());
    }

    @Path(Api.GET_UNIQUE) @POST @Consumes(JSON) @Produces(JSON)
    public static List getUnique(List<User> wildcardUsers) {
        List<User> users = new ArrayList<>();
        if (wildcardUsers.size() == 1) {
            User wildcardUser = wildcardUsers.get(0);
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("username", wildcardUser.getUsername()).list());
            if (users.size() == 0) {
                users.addAll(ObjectifyService.ofy().load().type(User.class)
                        .filter("email", wildcardUser.getEmail()).list());
            }
        }
        return users;
    }

    @Path(Api.GET_LOGIN) @POST @Consumes(JSON) @Produces(JSON)
    public List getLogin(List<User> wildcardUsers) {
        List<User> users = new ArrayList<>();
        if (wildcardUsers.size() == 1) {
            User wildcardUser = wildcardUsers.get(0);
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("username", wildcardUser.getUsername())
                    .filter("password", wildcardUser.getPassword()).list());
            if (users.size() == 0) {
                users.addAll(ObjectifyService.ofy().load().type(User.class)
                        .filter("email", wildcardUser.getEmail())
                        .filter("password", wildcardUser.getPassword()).list());
            }
        }
        return users;
    }

    @Path(Api.ADD) @POST @Consumes(JSON) @Produces(JSON)
    public List add(List<User> items) {
        if (UserRestService.getUnique(items).isEmpty()) {
            ObjectifyService.ofy().save().entities(items).now();
            return items;
        } else {
            return new ArrayList();
        }
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(User.class).list());
    }

    public List getForEventByAttendance(List<Attendance> attendances) {
        List<Event> wildcardEvents = new ArrayList<>();
        for (Attendance attendance : attendances) {
            wildcardEvents.add(new Event(attendance.getEventOrganizerEmail(),
                    attendance.getEventTime(), "", "", "", "", "", ""));
        }
        return getForEvent(wildcardEvents);
    }

    /**
     * Example:
     *      debug/add/organizer/email=org1@mail.com&password=123&username=org1&name=org1&phone=123&organization=org1&website=org1.com
     * @return JSON object for User
     */
    @Path(Api.DEBUG_ADD_ORGANIZER + "/email={email}&password={password}&username={username}" +
            "&name={nameAndSurname}&phone={phone}&organization={organization}&website={website}")
    @GET @Produces(JSON)
    public String debugAddOrganizer(@PathParam("email") String email,
                                    @PathParam("password") String password,
                                    @PathParam("username") String username,
                                    @PathParam("nameAndSurname") String nameAndSurname,
                                    @PathParam("phone") String phone,
                                    @PathParam("organization") String organization,
                                    @PathParam("website") String website) {
        User user = new User(email, password, username, "organizer", nameAndSurname, "", phone,
                "", "", "", "", "", "", "", "", "", "", organization, website);
        return add(Arrays.asList(user)).size() > 0 ? "Organizer added." : "Already exists.";
    }

    @Path(Api.DEBUG_GET + "/email={email}") @GET @Produces(JSON)
    public static List debugGet(@PathParam("email") String email) {
        return get(Arrays.asList(new User(email)));
    }

    @Path(Api.DEBUG_DELETE + "/email={email}") @GET @Produces(JSON)
    public String debugDelete(@PathParam("email") String email) {
        List usersToDelete = get(Arrays.asList(new User(email)));
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(User.class)
                .filter("email", email)
                .keys()).now();
        return "" + usersToDelete.size() + " user(s) deleted";
    }

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
