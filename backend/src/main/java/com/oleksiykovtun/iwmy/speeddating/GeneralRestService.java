package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.List;
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

    static {
        ObjectifyService.register(Attendance.class);
        ObjectifyService.register(Couple.class);
        ObjectifyService.register(Event.class);
        ObjectifyService.register(Rating.class);
        ObjectifyService.register(User.class);
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

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        List<Object> items = new ArrayList<>();
        items.addAll(AttendanceRestService.debugGetAll());
        items.addAll(CoupleRestService.debugGetAll());
        items.addAll(EventRestService.debugGetAll());
        items.addAll(RatingRestService.debugGetAll());
        items.addAll(UserRestService.debugGetAll());
        return items;
    }

    @Path(Api.DEBUG_RESET) @GET @Produces(JSON)
    public static List debugReset() {
        List<Object> items = new ArrayList<>();
        items.addAll(AttendanceRestService.debugReset());
        items.addAll(CoupleRestService.debugReset());
        items.addAll(EventRestService.debugReset());
        items.addAll(RatingRestService.debugReset());
        items.addAll(UserRestService.debugReset());
        return items;
    }

}
