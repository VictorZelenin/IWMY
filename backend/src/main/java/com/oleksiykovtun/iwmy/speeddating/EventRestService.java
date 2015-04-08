package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Path(Api.GET) @POST @Consumes(JSON) @Produces(JSON)
    public static List<Event> get(List<Event> wildcardEvents) {
        List<Event> events = new ArrayList<>();
        for (Event wildcardEvent : wildcardEvents) {
            events.addAll(ObjectifyService.ofy().load().type(Event.class)
                    .filter("organizerEmail", wildcardEvent.getOrganizerEmail()).list());
        }
        return events;
    }

    @Path(Api.GET_ALL) @POST @Consumes(JSON) @Produces(JSON)
    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Event.class).list());
    }

    @Path(Api.ADD) @POST @Consumes(JSON) @Produces(JSON)
    public List add(List<Event> items) {
        ObjectifyService.ofy().save().entities(items).now();
        return items;
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
        List<Event> events = EventRestService.get(wildcardEvents);
        for (Event event : events) {
            event.setActual("false");
        }
        ObjectifyService.ofy().save().entities(events).now();
        return events;
    }

    @Path(Api.DEBUG_CREATE) @GET @Produces(JSON)
    public static List debugCreate() {
        List list = Arrays.asList(
                new Event("John@email.com", "2015-02-28 20:00", "Tokyo", "Roger st. 23 apt. 2",
                        "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAUDBAQEAwUEBAQFBQUGBwwIBwcHBw8LCwkMEQ8" +
                                "SEhEPERETFhwXExQaFRERGCEYGh0dHx8fExciJCIeJBweHx7/2wBDAQUFBQcGBw" +
                                "4ICA4eFBEUHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eH" +
                                "h4eHh4eHh4eHh7/wAARCAAxADMDASIAAhEBAxEB/8QAHAAAAgMBAQEBAAAAAAAA" +
                                "AAAAAAQCBQYDBwgB/8QANRAAAgEDAwEFBgILAAAAAAAAAQIDAAQRBRIhMQYTIkF" +
                                "RFDRhcYGRB2IIIzIzQ1OSobHR8P/EABoBAAMAAwEAAAAAAAAAAAAAAAIDBAEFBg" +
                                "D/xAApEQACAgAEBQMFAQAAAAAAAAABAgADBBEhMQUSMkFRE4KhIkJhsdHw/9oAD" +
                                "AMBAAIRAxEAPwD5DgiyOlWFhA7OoK9c4Nd9S0zUNEuRaatp1zZ3Lru7q4iMbhT0" +
                                "OG5wfWu+h3kC6paLOoEfeqrZ6Y3cn+5pmIJrBzG0s4ci2uuuhj0Gmv4g6ADcAD8" +
                                "DXF7d7cyb9zc+EkYxX1tqHZn8Pr2/jnk03R47eEKO6trgKHDFyW4bnHh6+RAHAr" +
                                "yz9IXTl9o0m/0K3hu7K10+KymaIDERUttyMZ2kHAJz6E5xnn8Dx317QgrIz8/7v" +
                                "Ogx/C6hUWVtRPE5ou7hDREhiFzg801daPiAzCLIYEhsVGe9urYo8qpCrHgsRTl9" +
                                "rV4bFYHVRgEBhjkf8a3+IsaxQpUjLactRU6OWRgc99ZlJbS4eQstvKQfymirIXN" +
                                "xj+J9qKcWvJ6IsLUB1iXupS9ou3d+dc1fXLa8uWwsk+pajGsmBwAAzAgD0xXXTu" +
                                "z7WOsOZNZ0IARDDJeCQHJ/KDz1rG+GJQY7gMXOJAOAK/Y5HMs8Yk5XhWDnnB9c1" +
                                "ixH5z9XxGVW1qoAT5mxkmvNLsEih13coZyGhnk7phldqr4cg+I+WOKf7NaxcQQQ" +
                                "X811tdmkScXO6SOWEhcxsFGSCPMDIOCOmaxZnRtDgR2K95MwJJLBAMdB1pm3l7n" +
                                "s7FI5MkSTtlCSAwDJn4804YRGTlPcb6fyTtj7RYG/J01/s11/oOha6d1lrslvBA" +
                                "xdYpLF3dV8gSCAT8fP4dKZHZzs/cqSO0c57liDjSyCMDnrJWBnurSZ3Kj2dW2YV" +
                                "S/p8/rWr7Hsg0OcoSyBpMNk806rhi4i3lawkDxy9vbIsTxezC086VKCTsQe/umZ" +
                                "Os28DNFCrSRKxCOSVLLngkYOOPLNFZ9dpUE+lFEuKuAABhNgqCSSv7jKPIBhVH+" +
                                "a6AysCO7TnqSoqVtmToyDj15pmFVSMtIjgZ8xio/TO82iuNjE2SZrdYQiYQsd3r" +
                                "n/AFXXdMdLFmYjnczFyxxyQen0qzCAxgqoPHGelRaL9WC6BW9BzTAziLamk6598" +
                                "5RyI6gb13ADGeanFeSwxmOKWaND1VZWA+1N3wULtywI8hVcYmdsKCxNBzup0mHr" +
                                "QjWQ3xfyz/VRU/Y5/QD60VjOzx8QMl8zpD0Wr5PcT8jRRTqu88/aQuvc/tTCfu1" +
                                "+VFFGN4J6ZU6h+23zFLW/vX0NFFJ++GemO0UUVRJhP//Z",
                        "230", "$200", "Awesome"),
                new Event("Rei@email.com", "2015-02-28 21:00", "Denver", "Space st. 2 apt. 4",
                        "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDABALDA4MChAODQ4SERATGCgaGBYWGDEjJR0oOjM" +
                                "9PDkzODdASFxOQERXRTc4UG1RV19iZ2hnPk1xeXBkeFxlZ2P/2wBDARESEhgVGC" +
                                "8aGi9jQjhCY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY" +
                                "2NjY2NjY2NjY2P/wAARCAAzADQDASIAAhEBAxEB/8QAGgAAAgMBAQAAAAAAAAAA" +
                                "AAAAAAYDBAUCAf/EADMQAAIBAwMCBQIEBQUAAAAAAAECAwAEEQUSITFRBhMiQZF" +
                                "hcRQyQoEWI3KhorHB0fDx/8QAGQEBAAMBAQAAAAAAAAAAAAAAAwABBAIF/8QAIB" +
                                "EAAgICAQUBAAAAAAAAAAAAAAECEQMhMRITIjNhMv/aAAwDAQACEQMRAD8A1tZV4" +
                                "NIu2R+BE2O44qtcWNvcaFpRIIupUgiSUHBTPue+eaz5fEUOqWU1iyNHNIhXDDaR" +
                                "S+ZdU0gmOCdpIgUcA8gFTuGO2D271ixRSuMtG2XVSlHZv6hG0SPDKy3CEB0uIxl" +
                                "WAyOexzkV1agvpluitska3wD7g4613pPiuwuJntpYU0+3aHO0+pS2WLA/fdkVIb" +
                                "aGBFltQRmFJFUk4bKgk/TP+tVmhSJjmm/pT0uW5urqI3UrK8BeF0XG2TK53dOvH" +
                                "/nOetSDWsIt1kyGhZNpHBIwQfp7iokgeS7uYW3QSNMvDDlTtb/iqniK4bKLMylk" +
                                "GJVUYJySBjtwDXNXJJF8IXtUGNRn9JXLbsHqM8/70V5qEnm3sjjocY+2BRW6PCM" +
                                "UuWNV4koutPluIiJShzjvt5FMFhbw3eisJos8seRzkUu3uqWuqXls1jMwdd2VdC" +
                                "Nox8VbstZNnZXCPBJIsW0B0/UX9jWHMm2ehj3jv6Yt3ZMxYzWm4e0kHPyOvxXui" +
                                "3VxbQXscMT3UW3auc+nHtz0HNTjVNqCUKDCjbJOfUD9u1a/hkxfw/qcij8zu3Yk" +
                                "bQcZpG6iFdy2U4tbtbliZITb3Y3vhj+Vic+k+/vwfpVuWzjv7dJp4EcyopMi8FD" +
                                "1+Kw7OWO7ndVV2UW7NtkwWjbOOvUjFRWWvzpZy2tyS0Tw7EKrgqfb7ipOMpPxLg" +
                                "4pbMqa1dJSC3mH3KkfFFaUdo8yCSLY6HoQw4+h7UUvcC7dlbQ2C3Y7kjHwadtEl" +
                                "STSmY43F1BHX9VJuhMBet6dxJ9I+aarSBjYRzCfy4QxMi49gR7/AL0WVpT2NiV4" +
                                "q+iX+IeOSa22Bo3lyVPXOabfC4ddFvoTuUkupU/0ClyL8ZNOJ7S18zDelhHkY9q" +
                                "YfD38i1vBduIpVkJVWbkkpV5a6QoJuRiaQ5n1YNjG9MHsSMCrFvaxiyeTYBIt5G" +
                                "u4j2wDj5ruCNrK4juJHWQRoQQh6cg1fwzJOixkrLIsmTgYZRiuXNJ6FjjlWxXmK" +
                                "gRjZnAI/wAmoq9cwzwOqNDETjPOD1JoplLQMo7OvDKgzTEjkYx/epdRup47DyEl" +
                                "dYmVsqDwf+4oooZ+004vSyHR7iX8J5fmNsU8DtWiigkZ5/eiiiy/tmnF6kXY4k2" +
                                "flHPXNdvwOKKKNBPkhZEc5dVY9yM0UUV1ZKR//9k=",
                        "470", "$15", "Cool"),
                new Event("Annika@email.com", "2015-03-28 22:00", "Amsterdam", "Urban st. 12 apt. 52",
                        "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAcFBQYFBAcGBQYIBwcIChELCgkJChUPEAwRGBU" +
                                "aGRgVGBcbHichGx0lHRcYIi4iJSgpKywrGiAvMy8qMicqKyr/2wBDAQcICAoJCh" +
                                "QLCxQqHBgcKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqK" +
                                "ioqKioqKioqKir/wAARCAAyADQDASIAAhEBAxEB/8QAHAAAAgMBAQEBAAAAAAAA" +
                                "AAAAAAUEBgcDAgEI/8QANRAAAgEDAwICBgkFAQAAAAAAAQIDAAQRBRIhBjETQSI" +
                                "jUWFxkQcVMkJigaHB0RQXsbLS4f/EABoBAQACAwEAAAAAAAAAAAAAAAMEBQABBg" +
                                "L/xAAnEQACAgECBQQDAQAAAAAAAAABAgARAxIhBBMxQYEzUcHwFCIyYf/aAAwDA" +
                                "QACEQMRAD8AWW63eRiFR8X/APKYxyXm4R4j4/ETUyCLdHkKTzXpIylyd4xz51wI" +
                                "zsanZ6AJ9tjdsQN0YJ/CT+9MAb9E9XNFnGceGf5qJealaaRaSXt9MsNtCu6R27A" +
                                "fznAAHJJwKcaf4d3BFPbtG8UqhkkVshlIyCD5g8V6OQ1ZmAb1O8NleuRjW4FB9l" +
                                "uv/Vdby1u7aSFJNQjvICu9ykarhs4A4prYWUbzAsqsFHYjjNStdSCPTFSONFIcE" +
                                "7VxQ5OKDEKFqBq0OFu5jd9Z7ryUkffb/Y0VYpbMGeUbU4kYc/E0VfBNpHL7znHP" +
                                "6BwMnNdSN0kZfgGkt5r+naIJDKCdgJALd/marn9xYtRlS2sbSaWUkMF24Jwc/AC" +
                                "qleEysLA2kv8AIQGppsEFvc2zRXUccscylHidQVZSMEEeeaeWUB2AumD3HNVfpC" +
                                "e61yL+pktvBCl17+iGBIxu+I8qu0ktrZNEhkRJWU53P37e2idCDp9pvmDt3jGzU" +
                                "KMgBfdUHXCGgIA7sKW3HWFppWoLaS29xKzKHEkcTOuD7wDj869X+qLfKCilMykY" +
                                "ZSM4OMj3cd6JkNqSO8BUbmXEChWlm90p86KgS3Si5m4BzIaK6MMAKkcoTPzhPf6" +
                                "pf3TeNPJczOOWHpH51ZOmdA1iKJrqzVmESNcS7Y9xhTaRuOfLkVL0+8aFphhhC8" +
                                "gbKD0vsgcE/D2D4CrXYdRwx28kKQuiujK7FQWZTjIJzz2qQ+dmGkAAff8AIA4bT" +
                                "+1kmWX6M9WaD6L7m6vJgTDfzF3PA+0CT+tU3W9U1nV9auL76xi8BmPgxyRcxp5L" +
                                "SbqPqGSCxls9NklgtpmBMKttQnOd7L2ycfpUPpHU7mbqO1N66y28TiSVG+8o8vn" +
                                "igbCzXmFffaOrrjPLaXxNf15NVmtLWaBY44Y3O6N1wpGAMA+WKdaDedR31+LjWN" +
                                "PkhtVX1U7QsN4OMHcSRipGm9RabB1jqdzNaI8c1vbRqpAOCNxJ/UfKtEitItW6a" +
                                "MLD1UyNHx5DJAx8KrsacxqKAbDfv0jZMpxi795l7zlnLKA6scg5opdsvbCaazm2" +
                                "+NbyGOTd7R5j3EYP50VPONwaqYrIRdzOySJODU2Bj4fc9qKK03SKsSX4Dm6LgMR" +
                                "InfnyNSOkADrbAgY2fuKKKsR6Xj4lS3qefmaHGi/W9wdozhPL3Gtt6T56Vt88+k" +
                                "3+aKKr8fXxF4j+fMx36VfV9cSeH6O6BCdvGTzzRRRUt+sXD6Yn/9k=",
                        "110", "$25", "Uplifting"),
                new Event("Rei@email.com", "2015-03-28 23:00", "Paris", "Eiffel st. 37 apt. 3",
                        "/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAoHBwgHBgoICAgLCgoLDhgQDg0NDh0VFhEYIx8l" +
                                "JCIfIiEmKzcvJik0KSEiMEExNDk7Pj4+JS5ESUM8SDc9Pjv/2wBDAQoLCw4NDhwQ" +
                                "EBw7KCIoOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7" +
                                "Ozs7Ozs7Ozv/wAARCAAyADMDASIAAhEBAxEB/8QAGwAAAgMBAQEAAAAAAAAAAAAA" +
                                "AAYEBQcDAgH/xAAyEAABAwMCAwYEBgMAAAAAAAABAgMEAAUREiExQWEGEyJRcZEV" +
                                "MkKBBxQWobHRI1Lx/8QAGQEAAwEBAQAAAAAAAAAAAAAAAwQFAgAG/8QAKhEAAQMC" +
                                "BAUDBQAAAAAAAAAAAQACEQMEEiExUQUTMkFhM7HBcYGR0eH/2gAMAwEAAhEDEQA/" +
                                "AGPsk+xEt86HqKXl3R1AQonCuG2RVPertGh3ZKYyHWHockl3/INC1DYlI+k+9TOz" +
                                "LzqLRLuIcDjqbk+oYTsVbb48uNLsqJLmy59wfjkFbhcWEDUTniQPIUFznaBP2jKB" +
                                "LjWiPM/CdC8qUxAulvYjMP3J0AJXqWorBKQrII2Cc79cVwvHa96w3V6D3bDqGyNJ" +
                                "dURjPp1pVtd1uT6UwIExjS2klKVMgkDPmfWu82w3CY6ZU55px3TgqKQNh0FadjjL" +
                                "VapG15hxkYc998u23lTJH4iTJjLiGWI2hexASrAOOp612tPai4Xm7MwpjUZyM6cL" +
                                "R3ePTiTSswWAwtTbqVLCgCnuyDwO+/pTL2ItD826JmgaGY5yVY+Y+VABfzA0qs+n" +
                                "ZmzdWYBoYMHXxKfkmHFSGQ6y0EbaAQMfaivD9rt7j61uOlC1HJGsDeinYC8tJWed" +
                                "kbpG+A3CCFkOJlOvNpxjUnj4euB/FWkGQmJIQ053Ljjjeoqd8OlQ3HLic7darLXY" +
                                "hFkSUhlMd1D2A6pCs6FJwrBz/Feodhu0Z6YtmcgKIAaKwcLTv7HoRQQIRCJSOtp1" +
                                "BbKXDpUpZ5jQQT/VfEPSG5CVpkOZSdjrNXtyiJYtmlzQhWVLBbSSdwcjfHrS62ln" +
                                "OFKUAvYZQBkn7001wIzSLmOxgN7q5iBxeVLJW66rieJJO1bRYba3bLQxGQAFaQVe" +
                                "ZJrKLA0HbzCYxsXBken/ACtfkNsgrwgd5y8O/SkqJkl+69PxYcllK2bo0fz9/lcH" +
                                "7Kw+8t1S1gqOSARRXWTY0yZK3vzS06znGM4opqQoUJAk3GXIOtxwFaeCgMH9qqZl" +
                                "wcRGMoOuobxuUk4zjhmpXw9wJHgTpA5oyaiyYUeNZgyXNC1SM6lqPDB5UAAoxISu" +
                                "7JU6PnJChkFxXPB8/UVDa71U5jU6V4VwIq3lwGShOLhHWUcAnIz71CRH0SW1l1Cg" +
                                "D9KgeVc7JpW7YA12TuPdM/Zh0/qGHqGAV8cdK2MLI51iFnfDN4iOZOzo/qts1ZAP" +
                                "nvWbbNpVXjoiu0+PkqBJhTXpC3G5ykJUdk5O1Fc5Ld2VIWY76EtE+EHl+1FNKAlL" +
                                "l96iSwCgEj6qKKAiqvvLLRUkFtB8P+oqjLLSXCQ0gEJJBCRtRRXHpKJR9Vv1HuuA" +
                                "UpM1OlRGFjGDw3rX4Ul9TDeXnD4BxUaKKxb91V4x1M+6sQ4vHzq96KKKaUJf/9k=",
                        "45", "$95", "Refreshing"));
        ObjectifyService.ofy().save().entities(list).now();
        return list;
    }

    @Path(Api.DEBUG_DELETE_ALL) @GET @Produces(JSON)
    public static String debugDeleteAll() {
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Event.class).keys());
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
