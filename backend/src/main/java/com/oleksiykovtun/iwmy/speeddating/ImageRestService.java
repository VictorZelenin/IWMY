package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Image;
import com.oleksiykovtun.iwmy.speeddating.data.Thumbnail;

import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * The REST service which accesses images.
 */
@Path(Api.IMAGES)
public class ImageRestService extends GeneralRestService {

    /**
     * Saves the thumbnail and returns its id
     * @param base64data base64 string
     * @return image id
     */
    public static String putThumbnail(String base64data) {
        String path = "";
        if (base64data.length() > 0) {
            path = "_" + Math.abs(new Random(System.currentTimeMillis()).nextLong())
                    + base64data.length();
            byte[] binaryData = Base64Converter.getBytesFromBase64String(base64data);
            Thumbnail thumbnail = new Thumbnail(path, binaryData);
            ObjectifyService.ofy().save().entity(thumbnail).now();
        }
        return path;
    }

    /**
     * Saves the image and returns its id // todo reuse thumbnail code
     * @param base64data base64 string
     * @return image id
     */
    public static String put(String base64data) {
        String path = "";
        if (base64data.length() > 0) {
            path = "_" + Math.abs(new Random(System.currentTimeMillis()).nextLong())
                    + base64data.length();
            byte[] binaryData = Base64Converter.getBytesFromBase64String(base64data);
            Image image = new Image(path, binaryData);
            ObjectifyService.ofy().save().entity(image).now();
        }
        return path;
    }

    @Path(Api.GET_THUMBNAIL + "/{path}") @GET @Produces("image/jpeg")
    public static byte[] getThumbnail(@PathParam("path") String path) {
        List<Thumbnail> thumbnails = ObjectifyService.ofy().load().type(Thumbnail.class)
                .filter("path", path).list();
        if (thumbnails.size() == 1) {
            return thumbnails.get(0).getBinaryData();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @Path(Api.GET + "/{path}") @GET @Produces("image/jpeg") // todo reuse thumbnail code
    public static byte[] get(@PathParam("path") String path) {
        List<Image> images = ObjectifyService.ofy().load().type(Image.class)
                .filter("path", path).list();
        if (images.size() == 1) {
            return images.get(0).getBinaryData();
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @Path(Api.DEBUG_DELETE + "/{path}") @GET @Produces(TEXT)
    public String debugDelete(@PathParam("path") String path) {
        final int deletedImagesCount = ObjectifyService.ofy().load().type(Image.class)
                .filter("path", path).count() + ObjectifyService.ofy().load().type(Thumbnail.class)
                .filter("path", path).count();
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Image.class)
                .filter("path", path).keys()).now();
        ObjectifyService.ofy().delete().keys(ObjectifyService.ofy().load().type(Thumbnail.class)
                .filter("path", path).keys()).now();
        return deletedImagesCount + " image(s) deleted";
    }

}
