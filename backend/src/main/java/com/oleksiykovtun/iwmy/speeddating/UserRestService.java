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
        List<User> users = new ArrayList<>();
        if (wildcardUsers.size() == 1) {
            User wildcardUser = wildcardUsers.get(0);
            users.addAll(ObjectifyService.ofy().load().type(User.class)
                    .filter("email", wildcardUser.getEmail()).list());
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
        ObjectifyService.ofy().save().entities(items).now();
        return items;
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
        ObjectifyService.ofy().save().entity(user).now();
        return "Organizer added.";
    }

    @Path(Api.DEBUG_GET + "/email={email}") @GET @Produces(JSON)
    public static List debugGet(@PathParam("email") String email) {
        return get(Arrays.asList(new User(email)));
    }

    @Path(Api.DEBUG_CREATE) @GET @Produces(JSON)
    public static List debugCreate() {
        List list = Arrays.asList(
                new User("Joe@email.com", "qwerty", "joe3", "user",
                        "Joe Brown", "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAsICAoIBwsKCQoNDAsNERwSEQ" +
                        "8PESIZGhQcKSQrKigkJyctMkA3LTA9MCcnOEw5PUNFSElIKzZPVU5GVEBHSEX/2wBDAQwND" +
                        "REPESESEiFFLicuRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVFRUVF" +
                        "RUVFRUVFRUX/wAARCAAzADQDASIAAhEBAxEB/8QAGgAAAgIDAAAAAAAAAAAAAAAAAAYEBQI" +
                        "DB//EADIQAAIBAwIDBgUCBwAAAAAAAAECAwAEEQUhBhIxEyJBUXGBI0JhodEHFBUyNVJykb" +
                        "H/xAAZAQADAQEBAAAAAAAAAAAAAAADBAUCAAH/xAAnEQACAgAGAAUFAAAAAAAAAAABAgADB" +
                        "BESITFBIjNRgbEkMjRh4f/aAAwDAQACEQMRAD8AbNNgVrSMhAM58PrVRf2hXWpFhg3cglwo" +
                        "yNvDxrbYcS9isMElu0kjDZg2M1Pg1pDeF2RFVsEqdyPeuC2IS2XMEwRnCZ/ad5T6jpISOI3" +
                        "KtIUDDtJE5juc9fDyqLPaLbWluzQSJFFLzjvYDHJI29APem7VNXtGtljUAl2A39c4pYa3iu" +
                        "9Z1F2DItqiCOIN3RzZ3+1LNrYEkyvUEtQU7rvyOR3IZ16OKfMnNKe252MmFHX+UAdaZ9N1S" +
                        "HVnnmtkiiDKEaJ99vMYrnWrw9jcFJT3GB5GHykVZcC6j2OuQwnftG5MYyDmg2YXwau5WxC1" +
                        "kEL0I/mIwBY454wFHTkBx9qKs7hFEvegiJx1JopI1Ed/Mli3MZkfEVNO0WKe2iEpk5o9wVB" +
                        "H4qx/hMVsgCojYGxc1Q2eruqDEmPQVvk1aR/FmNW2V8+ZgV6rNZAku6tBO0WSiGJw45R19a" +
                        "VjKj8TSwXsrwvIq90fMykdPPIJ9jTBd3iaVZm5v2+JjKxeXrXL77Xbq91aK65h+5E4dWPy4" +
                        "IwPStUqXJ9IxZpqAcD+xi1uOBNYtjCGbtJgOU9MeNb+G7fl4sdgAkdrlifPI2H3qmvdWdVk" +
                        "e3hRWeQsuRzFM75BNRNJ1240+4CMecSyDmdm+u+TWrKyyeGMs6q2hts+51+fU+aTPakbdBj" +
                        "80Uly6mhc/EX/AExopEYcwww6AZTG1uYkUZmjX/FS1TI9Wit5RIvNIwHd5gAAfPFLFs/dFS" +
                        "1bbpVN0HcGlf6m7iLUDqNkxy4ZeiKpPNSvw9pDa1rcVs8hiDK7swG4AUk/ir25l7KxuJQ5E" +
                        "ioSoA8aw/TtQ+p3922SIbUrk/3MwA+wNdq0Vkr1FMUoNqJ6yBrmi3Wi/Emcz2smFWQHp9D5" +
                        "dKoWlJzzAbjOfaukcXzIvDdwsm/PyqB9cj8VzWNO1uIo+uSAdq9w7l682gMYpSwIp5jTGxe" +
                        "JGOMlQaKwAlYfDicr0GF2ooctAHLiY27EDGalhiRuaKKK08WYt3gQdwRuK38BqF0/ViuxM0" +
                        "S+3foooFvlNF7R9RV7w4+dhp1que6ZckexpS0b+oocAlQxGRnfFFFaq8mJX/nL7S6/eXD7m" +
                        "Z/Zsf8AKKKK4ASmWbPmf//Z",
                        "(123)4567890", "1991-12-20",
                        "male", "", "", "",
                        "178", "82", "negative", "positive",
                        "London", "", ""),
                new User("Mih@email.com", "12345678", "mih12", "user",
                        "Mih Simenko", "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAK" +
                        "CgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQc" +
                        "HBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKC" +
                        "goKCgoKCgoKCj/wAARCAAyADIDASIAAhEBAxEB/8QAGwAAAwADAQEAAAAAAAAAAAAAAAYHA" +
                        "QUIBAP/xAAzEAABAwMCBAQEBAcAAAAAAAABAgMEAAUREiEGMUFhExRRgQcicbEVIzIzNEJD" +
                        "cpHB4f/EABcBAQEBAQAAAAAAAAAAAAAAAAUEAwL/xAAkEQACAgIBAgcBAAAAAAAAAAAAAQI" +
                        "DBBEhEkEFEyIxMkRxUf/aAAwDAQACEQMRAD8A5cFbTh+zybxPZYjI1BSwFEEfKOZJHPFaoc" +
                        "6qvwpeiQuFr/cHi4JrS2mWl6cpQlQUT/kisbZOMW0U0xU5pMp1g4P4Ps3DQRIjImXVSSpSn" +
                        "E6iNuQHTONsUtW+xQ5TlxfuttYZY/osnJcHsNsV9uGXZCZ484ohS8OAH+bIx9iDTpMgAHx0" +
                        "J3QMOtlJCsUXK2aY3XRCS2QXjXhyGy0ZtsCmgn9xk8gPUelIixirrxa2w7bJOIziPGC0trU" +
                        "CEkj61DpKAhxQBBA9KvxpuUeQzMqjCXpPPRWaKpIdGBzFVjgK3lqzuMKwpE0Auoz+oJ3AA6" +
                        "/9qc2a0TLpI0QmFr0/MpWNgO5qp2G1y45hAPNPL1aWw0dRznOnGdvrU2RNa6U+S7Dr562uB" +
                        "rs6o12mrQ8pxt3fBSrBT0GPpTZ5cWeNCjeYU7rJbC3VbkY6k/WpxaZb0O/O/ibhL5VjxFAD" +
                        "VjbBxtmqI7JmS2mjBaZeCQclYzgdhyo6W0N1KLS0I/xQtzcKxh9MxxK3ndKC4skZIUcf6z0" +
                        "GKgjp3359aqPxivMh1+DAcUEusN63QnkFHbA9vvUsUc0jjJqG2C58k7Wo9jFFYoqkh2diRo" +
                        "aCHYuhttChjCQAMVo1WGdYbnHddy4xoUtOvf8AUCnY+uCDT3w7DTcLrgjLbWFObbdk+9b7j" +
                        "ltmTZURXQcLXkKTzGOeKHoq3W5sevv1bGtL9IvOt0WUwEzNCXc/uHkr6noe/I9q16Ytws0a" +
                        "RKtr7iozQSspU7+WUlQGyvf2508uWyMiK6p1S3Ak5RrO4Hf1pVscv8RZvDFsAbYU4hlRIB1" +
                        "q1fMd+xJriL55NpJ69PuRjjWBIMlU5L65kZ1enxSrUtCzvoXjkeeDyPTqAnnY11M7w7Dhp8" +
                        "SHFS0wCqS5pA/MKRhtGO5+b2rmCbr828XU6HCtWpOMaTncUlj29a1/AnMo8tqW/c89FY3oq" +
                        "kg2d+cEgC3OHAyX1Z78qOMSfFijppV9xRRUH1kKfbYo3f8AgJP9hqW/D1RTbZxBIPnF8qKK" +
                        "j7MT7ofWFqUwwFKJGRsT2NcxcaADiy7gDA8yv70UVTh/JkXiXwX6aOiiikgU/9k=",
                        "(000)7654321", "1988-07-13",
                        "female", "", "", "",
                        "169", "49", "negative", "negative",
                        "Berlin", "", ""),
                new User("Rei@email.com", "123", "rei791", "organizer",
                        "Rei Makako", "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAoHBwgHBgoICAgLCgoLDhgQD" +
                        "g0NDh0VFhEYIx8lJCIfIiEmKzcvJik0KSEiMEExNDk7Pj4+JS5ESUM8SDc9Pjv/2wBDAQoL" +
                        "Cw4NDhwQEBw7KCIoOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs" +
                        "7Ozs7Ozs7Ozv/wAARCAAyADMDASIAAhEBAxEB/8QAHAAAAwADAQEBAAAAAAAAAAAAAAUGAw" +
                        "QHAgEI/8QAMhAAAQMDAwEGBAUFAAAAAAAAAQIDBAAFERIhMUEGExQyUaEiYXGxFYGR4fAlQ" +
                        "1Jy0f/EABkBAAIDAQAAAAAAAAAAAAAAAAQFAQMGAv/EACkRAAEDAwMCBQUAAAAAAAAAAAEA" +
                        "AgMEESEFEjFBUTJhcZHRM6GxwfH/2gAMAwEAAhEDEQA/AJuyWsTZbbaht5jn0HNdADDaWkN" +
                        "YAAGSPsKRdjYyVtyXiMn4W8/XmnnehUkYHnVn3pBqkxdNt7J5RQh7LhZTa0ud2hQ28x/n61" +
                        "iFmZLxUUDG/wBj+1MlTGwXFZ4ASK1V3FCEkhOTg4A6mkwcb4RMbZyLBLW7U1qGEjKRqH6ms" +
                        "rTDQZWVoBSFKbWD8j/w1sqkNMlGpQUSnHPNKZl0bbhyDqAyo9flvVrA52EW6N8nPCWSrIpm" +
                        "S42kApSdjjpRVNCCJkCPIWnKnGkk8+lFMRq23DuQlJoW3S7seP6XJ2371P8APam0GGZEgIz" +
                        "w0NP1zSbsc4BFlo9Ftq+9UsAhiQ2UrCsoPXpqNcVrd1WQeMKykeWwHbysN3sN3Rb1G0GN35" +
                        "VuX0lW3UgcZ+tJbFYu0iZCVXWW28ouaVshtIGjHmCgBg56eldGaXrRXo6U7mmjKeNrNoAt6" +
                        "Z90EK2YG55UleuzjSID80FZUy2VaEcnA6VyV64PuLPiob7LLiy0nUcKCsZ3Sd+tfoNaQ60t" +
                        "JGQpOKie0lrty24rjjSfEF5KEKxvznHtXJbFACdqMpKyaQiNz+va+F4t6HRb2AgFKAgBIPQ" +
                        "UVRRIrYitjTwKKyxDib2V769m44UJ2TcDch9s/wBxrb8jmmce6Ii321Nu4DUlEhhRJ4UlQI" +
                        "Pt70osrbbRU8HClxA2+Q9a3H7euU5He0gpjviQggf5J0qGPen88jDVu8h97JTptXHUufTNu" +
                        "Cy1+2ey6NHOEgA19cktNHDgXg9QgkewpTEecitozlbWPzFNEvMvN/A+BnqDuKKhmDm2HKrf" +
                        "GWuzkeSR3e4RWEBcS4eHdUoJ0kKJPyAPFS8yQbr2ghsJOWYy85zyr9q2e3F2ejd3b4s1Trr" +
                        "6x0GWx6/X0rH2dhhhtuQ4nG2QD6ngUFUvsLjrhaKmiYyEyHm2L/wKt8ZHaGhTgSR0oqGn2y" +
                        "TLnOv/AIpo1nyhvj3oqxtGdo+EmLYurlrRwC6AfUVQ2UlUQajnnn/Y0UUXqXgb6/KjT+X+n" +
                        "7VIgDwg24FTd5+FEkp2PdHcUUUok5CP0z6jlJtAKkRlEAkrO55qvTsYiRsNBOB60UVE3jYn" +
                        "VXx7/hL1eaiiitQFgHclf//Z",
                        "4567890", "1990-03-17",
                        "female", "", "", "",
                        "176", "53", "negative", "negative",
                        "Tokyo", "SuperDating", "dating.biz"),
                new User("John@email.com", "asdfghjk", "john8", "organizer",
                        "John Doe", "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAs" +
                        "LDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQ" +
                        "wLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyM" +
                        "jIyMjIyMjL/wAARCAAzADUDASIAAhEBAxEB/8QAHAAAAQQDAQAAAAAAAAAAAAAAAAMFBgcB" +
                        "AgQI/8QAMBAAAQMDAgQFAwMFAAAAAAAAAQIDEQAEIQUSBhMxUQciQXGBFGGxMkKRI6HB0fH" +
                        "/xAAZAQACAwEAAAAAAAAAAAAAAAAEBQECAwb/xAAlEQACAgIBAwQDAQAAAAAAAAABAgADBB" +
                        "EhEjFRExRx0QUiQWH/2gAMAwEAAhEDEQA/AJey0GkTtknE12NxGa0CowYJODApUCBhNE95c" +
                        "kjmMfGN19HwxdLE+chBI9ATmqafvLUBUEk9OnSr5v7G31Sxes7lBUy4IMYI9qgt54TNuF12" +
                        "yfUtKlQhDioxHcff8VDOqLswihTYdCVwHGlvwgjdGPvTXcz9Q4JMgzU3v/DTU9CIuHH2nkE" +
                        "DypmUkfmoVqSFs6opKwU7hOapXfXYSqnkTe7HsrrDsODFrRxzly24pueu0xNFcjb3LB6wT/" +
                        "FFbwWenSshxIS1iJC5Ee0da3U8kpEHrSQIJE5rKlhpOBk9qoZiu2Goqk4OD80N3BbW0HFrg" +
                        "fpSgCCaRDpPsRSN6yq4sjBAcbIdbUPRScis7FDqRCKD6Tgzn4mumhanmkSMwaoripYc1G2f" +
                        "KCnek4+f+VJ3+Idd1zWkNvsIIDwbcaQ2fKCMfJimHipTN4/ZPMAbQgpWB+1QOR8UJiYltVv" +
                        "U47jvHWRkUWYfpI37A9vr43GRbAS4oR2P8iiuxy0deeVygowkAxRXRJSpUGJGDA6AnoBpQb" +
                        "awZUe9LOjmKSFYhM/amtxdyzbMlW0TOQJnt1+acLRsXVqhy4TvJEZwD2pEMlGyGx+epRv/A" +
                        "CL6PyNVmU2OqkMBvkedfcyyWyoqKgtI6GtyHOWYzOB7UsphIEIAT2AFbIabSf6pWqOmY/FE" +
                        "DvD2PUDzKb4vcuNK4mvmLZuHLtaVMFBghRG3r2IUZ9qjDm1WltOoStK1EhYUvdKvWOw6Yqw" +
                        "PE22Xb63pt5b+VyWwFT+4K7/NV9d267ZSwvDzdw6lYJySFEZ/vTGvXtmbfiTy9iDROt/Eet" +
                        "AuX7Vq4W2WwVrCTuUAcD7+9FIcN2K9WTdFFw01sKSQ4vbMg/6orocK3E9uvWRvUQ5uDmvkM" +
                        "yBiCeNb+5eVsSW0TmRmukfoHvRRXHxsJseg96wrCTHaiiom9fLCQTxLSHeEuYsStCklKvUE" +
                        "if8AFVVqbqndi1kblJQpUACTtGcUUUU3CkDz9w+sAMQPEU4fYbfNzzEzt2RkjvRRRWlbsFA" +
                        "BjTGxqXqDMgJ5/g8z/9k=",
                        "(321)1234567", "1992-02-07",
                        "male", "", "", "",
                        "192", "74", "neutral", "negative",
                        "Paris", "MegaDating", "dating.com"));
        ObjectifyService.ofy().save().entities(list).now();
        return list;
    }

    @Path(Api.DEBUG_DELETE_ALL) @GET @Produces(JSON)
    public static String debugDeleteAll() {
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(User.class).keys());
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
