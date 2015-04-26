package com.oleksiykovtun.iwmy.speeddating;

import com.oleksiykovtun.iwmy.speeddating.data.User;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

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
        if (authorizationRequired(request)) {
            List<User> authorizedUsers = getAuthorizedUsers(request);
            if (authorizedUsers.size() != 1) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }
        return request;
    }

    private boolean authorizationRequired(ContainerRequest request) {
        // The only exception is when a new user is being registered
        return request.getPath() != null && !request.getPath().equals(Api.USERS + Api.ADD)
                && !request.getPath().equals(Api.MAIL + Api.REQUEST_ORGANIZER);
    }

    private List<User> getAuthorizedUsers(ContainerRequest request) {
        User wildcardUserToAuthorize
                = new User(getAuthorizationId(request), getAuthorizationPassword(request),
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
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