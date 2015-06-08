package com.oleksiykovtun.iwmy.speeddating;

import com.oleksiykovtun.iwmy.speeddating.data.User;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by alx on 2015-04-26.
 */
public class RequestAuthorizationFilter implements ContainerRequestFilter {

    @Override
    public ContainerRequest filter(ContainerRequest request) throws WebApplicationException {
        if (authorizationRequired(request) && getAuthorizedUsers(request).size() != 1) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return request;
    }

    private boolean authorizationRequired(ContainerRequest request) {
        String path = "/" + request.getPath();
        // The exceptions are when a new user is being registered or logs in,
        // or for images or counters, or for password resetting
        return !path.equals(Api.USERS + Api.GET_LOGIN)
                && !path.equals(Api.USERS + Api.ADD)
                && !path.equals(Api.MAIL + Api.REQUEST_ORGANIZER)
                && !path.equals(Api.USERS + Api.ADD_PENDING_ORGANIZER)
                && !path.startsWith(Api.USERS + Api.ACTIVATE_PENDING_ORGANIZER)
                && !path.startsWith(Api.IMAGES + Api.GET)
                && !path.equals(Api.USERS + Api.GET_COUNT_ORGANIZERS)
                && !path.equals(Api.USERS + Api.GET_COUNT_USERS)
                && !path.equals(Api.COUPLES + Api.GET_COUNT)
                && !path.equals(Api.EVENTS + Api.GET_COUNT) // deprecated
                && !path.equals(Api.EVENTS + Api.GET_COUNT_ACTUAL)
                && !path.equals(Api.EVENTS + Api.GET_COUNT_ACTUAL_FUTURE)
                && !path.equals(Api.EVENTS + Api.GET_COUNT_UNACTUAL_FUTURE)
                && !path.equals(Api.MAIL + Api.RESET_PASSWORD);
    }

    private boolean isOnlyAdminAllowed(ContainerRequest request) {
        return request.getPath().contains("/debug/");
    }

    private List<User> getAuthorizedUsers(ContainerRequest request) {
        User wildcardUserToAuthorize
                = new User(getAuthorizationId(request), getAuthorizationPassword(request),
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        // forbidding unauthorized admin requests
        if (isOnlyAdminAllowed(request)
                && ! wildcardUserToAuthorize.getEmail().equals(Api.APP_EMAIL)) {
            return new ArrayList<>();
        }
        return UserRestService.getLogin(Arrays.asList(wildcardUserToAuthorize));
    }

    private String getAuthorizationId(ContainerRequest request) {
        try {
            String credentials = getAuthorizationCredentials(request);
            // Getting "<id>" from credentials "<id>:<password>"
            return credentials.substring(0, credentials.indexOf(":"));
        } catch (Throwable e) {
            return "";
        }
    }

    private String getAuthorizationPassword(ContainerRequest request) {
        try {
            String credentials = getAuthorizationCredentials(request);
            // Getting "<password>" from credentials "<id>:<password>"
            return credentials.substring(credentials.indexOf(":") + 1);
        } catch (Throwable e) {
            return "";
        }
    }

    private String getAuthorizationCredentials(ContainerRequest request) {
        String authorizationHeader = request.getHeaderValue("Authorization");
        // Removing "Basic " from header "Basic <base64_credentials>"
        String base64Credentials
                = authorizationHeader.substring(authorizationHeader.indexOf(" ")).trim();
        return Base64Converter.getStringFromBase64String(base64Credentials);
    }

}