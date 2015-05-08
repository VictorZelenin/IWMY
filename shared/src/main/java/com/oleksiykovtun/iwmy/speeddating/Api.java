package com.oleksiykovtun.iwmy.speeddating;

public class Api {

    public static final String APP_EMAIL = "iwmy.speed.dating@gmail.com";

    public static final String USERS = "/users/";
    public static final String ADD = "add";
    public static final String ADD_PENDING_ORGANIZER = "add/pending/organizer";
    public static final String ACTIVATE_PENDING_ORGANIZER = "activate/pending/organizer";
    public static final String ADD_BY_ORGANIZER = "add/by/organizer";
    public static final String REPLACE = "replace";
    public static final String GET = "get";
    public static final String GET_OTHER_FOR_EVENT = "get/other/for/event";
    public static final String DEBUG_GET = "debug/get";
    public static final String DEBUG_DELETE = "debug/delete";
    public static final String GET_LOGIN = "get/login";
    public static final String GET_FOR_EVENT = "get/for/event";
    public static final String GET_FOR_EVENT_LOCK = "get/for/event/lock";
    public static final String GET_FOR_EVENT_ACTIVE = "get/for/event/active";
    public static final String GET_FOR_EVENT_ACTIVE_RESET = "get/for/event/active/reset";
    public static final String REMOVE_ATTENDANCE = "remove/attendance";
    public static final String GET_COUNT_ORGANIZERS = "get/count/organizers";
    public static final String GET_COUNT_USERS = "get/count/users";

    public static final String EVENTS = "/events/";
    public static final String GET_ALL_FOR_USER = "get/all/for/user";
    @Deprecated public static final String GET_ALL = "get/all";
    public static final String GET_FOR_USER = "get/for/user";
    public static final String GET_FOR_TIME = "get/for/time";
    public static final String SET_UNACTUAL = "set/unactual";
    public static final String SET_USER_RATINGS_ALLOW = "set/user/ratings/allow";
    public static final String DELETE = "delete";

    public static final String ATTENDANCES = "/attendances/";
    public static final String TOGGLE_ACTIVE = "toggle/active";
    public static final String GET_FOR_EVENT_ACTIVE_CHECK_VOTED = "get/for/event/active/check/voted";

    public static final String RATINGS = "/ratings/";
    public static final String PUT = "put";
    public static final String PUT_ACTUAL = "put/actual";
    public static final String GET_FOR_ATTENDANCE_ACTIVE = "get/for/attendance/active";

    public static final String COUPLES = "/couples/";
    public static final String GENERATE_FOR_EVENT = "generate/for/event";
    public static final String GET_FOR_ATTENDANCE = "get/for/attendance";

    public static final String IMAGES = "/images/";
    public static final String GET_THUMBNAIL = "get/thumbnail";

    public static final String MAIL = "/mail/";
    public static final String SEND = "send";
    public static final String REQUEST_ORGANIZER = "request/organizer";
    public static final String RESET_PASSWORD = "reset/password";
    public static final String DEBUG_SEND = "debug/send";

    public static final String GENERAL = "/";
    public static final String EMPTY = "";
    public static final String PING = "ping";
    public static final String DEBUG_GET_ALL = "debug/get/all";

}