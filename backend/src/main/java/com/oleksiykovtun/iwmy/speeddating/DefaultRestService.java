package com.oleksiykovtun.iwmy.speeddating;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * The REST service which accesses the original content directly.
 */
@Path("/")
public class DefaultRestService extends RestService {

    private static final String DEFAULT_MESSAGE = "IWMY Speed Dating\n\n"
            + "http://want2meetu.com\n"
            + "https://github.com/oleksiykovtun/IWMY-Speed-Dating\n\n"
            + "Please use the Android app for accessing the service";

    @GET @Path("") @Produces(JSON)
    public String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    @Path("debug/get/all") @GET @Produces(JSON)
    public List debugGetAll() {
        List<Object> items = new ArrayList<>();
        items.addAll(AttendanceRestService.debugGetAll());
        items.addAll(CoupleRestService.debugGetAll());
        items.addAll(EventRestService.debugGetAll());
        items.addAll(RatingRestService.debugGetAll());
        items.addAll(UserRestService.debugGetAll());
        return items;
    }

    @Path("debug/reset") @GET @Produces(JSON)
    public List debugReset() {
        List<Object> items = new ArrayList<>();
        items.addAll(AttendanceRestService.debugReset());
        items.addAll(CoupleRestService.debugReset());
        items.addAll(EventRestService.debugReset());
        items.addAll(RatingRestService.debugReset());
        items.addAll(UserRestService.debugReset());
        return items;
    }

}
