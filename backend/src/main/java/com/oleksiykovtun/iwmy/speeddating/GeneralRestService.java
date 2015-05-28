package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Image;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;
import com.oleksiykovtun.iwmy.speeddating.data.Thumbnail;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The general backend REST service.
 */
@Path(Api.GENERAL)
public class GeneralRestService {

    protected static final String CHARSET_UTF_8 = ";charset=utf-8";
    protected static final String JSON = MediaType.APPLICATION_JSON + CHARSET_UTF_8;
    protected static final String TEXT = MediaType.TEXT_PLAIN + CHARSET_UTF_8;

    static {
        ObjectifyService.register(Attendance.class);
        ObjectifyService.register(Couple.class);
        ObjectifyService.register(Email.class);
        ObjectifyService.register(Event.class);
        ObjectifyService.register(Rating.class);
        ObjectifyService.register(User.class);
        ObjectifyService.register(Thumbnail.class);
        ObjectifyService.register(Image.class);
    }

    private static final String DEFAULT_MESSAGE = "IWMY Speed Dating\n\n"
            + "http://want2meetu.com\n"
            + "https://github.com/oleksiykovtun/IWMY-Speed-Dating\n\n"
            + "Please use the Android app for accessing the service";

    @GET
    @Path(Api.EMPTY) @Produces(JSON)
    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    @GET
    @Path(Api.PING) @Produces(JSON)
    public String getPing() {
        String timestamp = "" + System.currentTimeMillis();
        // testing Datastore
        Email testingEmail = new Email();
        testingEmail.setCreationTime(timestamp);
        testingEmail.setToAddress("");
        ObjectifyService.ofy().save().entity(testingEmail).now();
        Email responseEmail = ObjectifyService.ofy().load().type(Email.class)
                .filter("creationTime", testingEmail.getCreationTime())
                .filter("toAddress", testingEmail.getToAddress())
                .list().get(0);
        if (responseEmail.equals(testingEmail)) {
            ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(User.class)
                    .filter("creationTime", testingEmail.getCreationTime())
                    .filter("toAddress", testingEmail.getToAddress())
                    .keys()).now();
            return Api.PING;
        }
        throw new RuntimeException();
    }

}
