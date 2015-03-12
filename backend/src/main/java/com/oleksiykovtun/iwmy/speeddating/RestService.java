package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Attendance;
import com.oleksiykovtun.iwmy.speeddating.data.Couple;
import com.oleksiykovtun.iwmy.speeddating.data.Event;
import com.oleksiykovtun.iwmy.speeddating.data.Rating;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import javax.ws.rs.core.MediaType;

/**
 * The back end REST service.
 */
public abstract class RestService {

    static {
        ObjectifyService.register(Attendance.class);
        ObjectifyService.register(Couple.class);
        ObjectifyService.register(Event.class);
        ObjectifyService.register(Rating.class);
        ObjectifyService.register(User.class);
    }

    protected static final String CHARSET_UTF_8 = ";charset=utf-8";
    protected static final String JSON = MediaType.APPLICATION_JSON + CHARSET_UTF_8;

}
