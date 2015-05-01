package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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
        List<User> users = new ArrayList<>();
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
        return users;
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
        List<User> users = new ArrayList<>();
        // listing event-related attendances
        List<Attendance> eventAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance eventAttendance : eventAttendances) {
            // and adding users - from each active of them
            if (eventAttendance.getActive().equals("true")) {
                users.addAll(ObjectifyService.ofy().load().type(User.class)
                        .filter("email", eventAttendance.getUserEmail()).list());
            }
        }
        return users;
    }

    @Path(Api.GET_FOR_EVENT) @POST @Consumes(JSON) @Produces(JSON)
    public static List<User> getForEvent(List<Event> wildcardEvents) {
        List<User> users = new ArrayList<>();
        // listing event-related attendances
        List<Attendance> eventAttendances = AttendanceRestService.getForEvent(wildcardEvents);
        for (Attendance eventAttendance : eventAttendances) {
            // and adding users - from each of them
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("email", eventAttendance.getUserEmail()).list());
        }
        return users;
    }

    @Path(Api.GET) @POST @Consumes(JSON) @Produces(JSON)
    public static List<User> get(List<User> wildcardUsers) {
        Set<User> users = new TreeSet<>();
        for (User wildcardUser : wildcardUsers) {
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("email", wildcardUser.getEmail()).list());
        }
        return new ArrayList<>(users);
    }

    public static List<User> getUnique(List<User> wildcardUsers) {
        Set<User> users = new TreeSet<>();
        for (User wildcardUser : wildcardUsers) {
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("username", wildcardUser.getUsername()).list());
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("email", wildcardUser.getEmail()).list());
        }
        return Arrays.asList(users.toArray(new User[users.size()]));
    }

    @Path(Api.GET_LOGIN) @POST @Consumes(JSON) @Produces(JSON)
    public static List<User> getLogin(List<User> wildcardUsers) {
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
            // Forcing group
            for (User user : items) {
                user.setGroup(User.USER);
            }
            return put(items);
        } else {
            return new ArrayList();
        }
    }

    @Path(Api.ADD_PENDING_ORGANIZER) @POST @Consumes(JSON) @Produces(JSON)
    public List addPendingOrganizer(List<User> items) {
        if (UserRestService.getUnique(items).isEmpty()) {
            // Forcing group and invalidating password
            for (User user : items) {
                user.setGroup(User.PENDING_ORGANIZER);
                String passwordLock = Math.abs(new Random(System.currentTimeMillis()).nextLong()) + "_";
                user.setPassword(passwordLock + user.getPassword());
            }
            return put(items);
        } else {
            return new ArrayList();
        }
    }

    @Path(Api.ACTIVATE_PENDING_ORGANIZER + "/{token}") @GET @Produces(JSON)
    public String activatePendingOrganizer(@PathParam("token") String secretToken) {
        List<User> pendingOrganizers = getPendingOrganizers();
        int activationCount = 0;
        for (User user : pendingOrganizers) {
            // Re-enabling password
            if (user.getPassword().startsWith(secretToken + "_")) {
                user.setPassword(user.getPassword().substring(secretToken.length() + 1));
                user.setGroup(User.ORGANIZER);
                activationCount++;
            }
        }
        put(pendingOrganizers);
        return "Activated " + activationCount + " organizers of " + pendingOrganizers.size();
    }

    public static List<User> getPendingOrganizers() {
        return ObjectifyService.ofy().load().type(User.class)
                .filter("group", User.PENDING_ORGANIZER).list();
    }

    @Path(Api.ADD_BY_ORGANIZER) @POST @Consumes(JSON) @Produces(JSON)
    public List addByOrganizer(List<User> items) {
        return add(items);
    }

    private List put(List<User> items) {
        // saving images separately and replacing them with links
        for (User user : items) {
            user.setPhoto(ImageRestService.put(user.getPhoto()));
            user.setThumbnail(ImageRestService.putThumbnail(user.getThumbnail()));
        }
        ObjectifyService.ofy().save().entities(items).now();
        return items;
    }

    @Path(Api.REPLACE) @POST @Consumes(JSON) @Produces(JSON)
    public List replace(List<User> users) {
        if (users.size() == 2) {
            User oldUser = users.get(0);
            List<User> usersMatchingOldOrNewUser = UserRestService.getUnique(users);
            // replace if the new user matches no one or the old same one
            if (usersMatchingOldOrNewUser.isEmpty()
                || usersMatchingOldOrNewUser.size() == 1
                && usersMatchingOldOrNewUser.get(0).getEmail().equals(oldUser.getEmail())
                && usersMatchingOldOrNewUser.get(0).getUsername().equals(oldUser.getUsername())) {
                return replaceForUser(users);
            }
        }
        return new ArrayList();
    }

    private List replaceForUser(List<User> users) {
        List<User> oldUsers = users.subList(0, 1);
        // deleting old user
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(User.class)
                .filter("email", oldUsers.get(0).getEmail()).keys()).now();
        // putting new user
        List<User> newUsers = users.subList(1, 2);
        put(newUsers);
        // linking related data to the new user
        AttendanceRestService.replaceForUser(users);
        CoupleRestService.replaceForUser(users);
        RatingRestService.replaceForUser(users);
        return newUsers;
    }

    public static List<User> getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(User.class).list());
    }

    @Path(Api.GET_OTHER_FOR_EVENT) @POST @Consumes(JSON) @Produces(JSON)
    public List<User> getOtherForEvent(List<Event> events) {
        Set<User> usersAddedByOrganizers = new TreeSet<>();
        // first, get all users referred by organizers
        usersAddedByOrganizers.addAll(ObjectifyService.ofy().load().type(User.class)
                .filter("password", "").list());
        // then, remove users who already attend this event
        usersAddedByOrganizers.removeAll(UserRestService.getForEvent(events));
        return new ArrayList<>(usersAddedByOrganizers);
    }

    public List getForEventByAttendance(List<Attendance> attendances) {
        List<Event> wildcardEvents = new ArrayList<>();
        for (Attendance attendance : attendances) {
            wildcardEvents.add(new Event(attendance.getEventOrganizerEmail(),
                    attendance.getEventTime(), "", "", "", "", "", ""));
        }
        return getForEvent(wildcardEvents);
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
