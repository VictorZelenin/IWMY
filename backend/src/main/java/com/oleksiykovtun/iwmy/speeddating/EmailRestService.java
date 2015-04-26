package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * The REST service which processes mail.
 */
@Path(Api.MAIL)
public class EmailRestService extends GeneralRestService {

    /**
     * Sends emails and returns unsent emails
     * @param emailList emails to send
     * @return unsent emails
     */
    @Path(Api.SEND) @POST @Consumes(JSON) @Produces(JSON)
    public static List send(List<Email> emailList) {
        List<Email> unsentEmailList = new ArrayList<>();
        for (Email email : emailList) {
            try {
                MimeMessage message
                        = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
                message.setFrom(new InternetAddress(email.getFromAddress(), email.getFromName()));
                if (! email.getToAddress().matches(Email.VALIDATION_REGEX)) {
                    throw new Exception("Receiver's email address is invalid");
                }
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(email.getToAddress(), email.getToName()));
                message.setSubject(email.getSubject(), "utf-8");
                message.setText(email.getMessage(), "utf-8");
                Transport.send(message);
            } catch (Throwable t) {
                unsentEmailList.add(email);
            }
        }
        put(emailList);
        return unsentEmailList;
    }

    @Path(Api.REQUEST_ORGANIZER) @POST @Consumes(JSON) @Produces(JSON)
    public static List requestOrganizer(List<Email> emailList) {
        return send(emailList);
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Email.class).list());
    }

    public static List put(List<Email> items) {
        ObjectifyService.ofy().save().entities(items).now();
        return items;
    }

    /**
     * Example:
     *      mail/debug/send/fromEmail=iwmy.speed.dating@gmail.com&fromName=IWMY-Speed-Dating&toEmail=iwmy.speed.dating@gmail.com&toName=IWMY-Speed-Dating&subject=testSubject&message=TestMessage
     * @return confirmation message
     */
    @Path(Api.DEBUG_SEND + "/fromEmail={fromEmail}&fromName={fromName}&toEmail={toEmail}" +
            "&toName={toName}&subject={subject}&message={message}") @GET @Produces(JSON)
    public String debugSend(@PathParam("fromEmail") String fromEmail,
                                @PathParam("fromName") String fromName,
                                @PathParam("toEmail") String toEmail,
                                @PathParam("toName") String toName,
                                @PathParam("subject") String subject,
                                @PathParam("message") String message) {
        List unsentEmails = send(Arrays.asList(
                new Email(fromEmail, fromName, toEmail, toName, subject, message)));
        if (! unsentEmails.isEmpty()) {
            return "Email was not sent";
        }
        return "Email sent.";
    }

    @Path(Api.DEBUG_GET_ALL) @GET @Produces(JSON)
    public static List debugGetAll() {
        return getAll();
    }

}
