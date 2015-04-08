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
                    couples.add(new Couple(event.getOrganizerEmail(), event.getTime(),
                            userMale.getEmail(), userFemale.getEmail(),
                            userMale.getNameAndSurname(), userMale.getBirthDate(),
                            userFemale.getNameAndSurname(), userFemale.getBirthDate()));
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
    public List put(List<Couple> couples) {
        // writing couples
        ObjectifyService.ofy().save().entities(couples).now();
        // sending emails
        if (! couples.isEmpty()) {
            List<Email> emails = new ArrayList<>();
            emails.add(getEmailForOrganizer(couples));
            for (Couple couple : couples) {
                emails.add(getEmailForCoupleUser1(couple));
                emails.add(getEmailForCoupleUser2(couple));
            }
            EmailRestService.send(emails);
        }
        return couples;
    }

    private Email getEmailForOrganizer(List<Couple> couples) {
        String message = Api.SUBJECT_COUPLES + ":\n";
        for (Couple couple : couples) {
            message += "\nCouple:\n";
            message += getCoupleMessageForUser(couple.getName1(), couple.getBirthDate1());
            message += getCoupleMessageForUser(couple.getName2(), couple.getBirthDate2());
        }
        return new Email(Api.APP_EMAIL, Api.APP_NAME,
                couples.get(0).getEventOrganizerEmail(), "Organizer",
                Api.SUBJECT_COUPLES + " - " + Api.APP_NAME, message);
    }

    private Email getEmailForCoupleUser1(Couple couple) {
        return new Email(Api.APP_EMAIL, Api.APP_NAME,
                couple.getUserEmail1(), couple.getName1(),
                Api.SUBJECT_COUPLE + " - " + Api.APP_NAME, Api.SUBJECT_COUPLE + ":\n"
                    + getCoupleMessageForUser(couple.getName2(), couple.getBirthDate2()));
    }

    private Email getEmailForCoupleUser2(Couple couple) {
        return new Email(Api.APP_EMAIL, Api.APP_NAME,
                couple.getUserEmail2(), couple.getName2(),
                Api.SUBJECT_COUPLE + " - " + Api.APP_NAME, Api.SUBJECT_COUPLE + ":\n"
                    + getCoupleMessageForUser(couple.getName1(), couple.getBirthDate1()));
    }

    private String getCoupleMessageForUser(String name, String birthDate) {
        String message = "Name: " + name;
        message += "\nAge: " + TimeConverter.getYearsFromDate(birthDate);
        message += "\n";
        return message;
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
