package com.oleksiykovtun.iwmy.speeddating;

import com.googlecode.objectify.ObjectifyService;
import com.oleksiykovtun.iwmy.speeddating.data.Email;
import com.oleksiykovtun.iwmy.speeddating.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
                email.setSentTime(Time.getFullDateTimeNow());
            } catch (Throwable t) {
                email.setSentTime(Time.getFullDateTimeNow() + " FAILED");
                unsentEmailList.add(email);
            }
        }
        put(emailList);
        return unsentEmailList;
    }

    @Path(Api.REQUEST_ORGANIZER) @POST @Consumes(JSON) @Produces(JSON)
    public static List requestOrganizer(List<Email> emailList) {
        if (emailList.size() == 1 || emailList.size() == 2) {
            // Forcing email to admin
            Email email = emailList.get(0);
            email.setToAddress(Api.APP_EMAIL);
            // Finding which pending organizer activation to request
            List<User> pendingOrganizers = UserRestService.getPendingOrganizers();
            for (User user : pendingOrganizers) {
                if (email.getMessage().contains(user.getEmail())) {
                    // Setting activation id to the password-locking secret
                    email.setMessage(email.getMessage().replace(":" + user.getEmail(),
                            user.getPassword().substring(0, user.getPassword().indexOf("_"))));
                }
            }
            // sending email to admin
            send(emailList.subList(0, 1));
            // saving email for confirmation
            if (emailList.size() == 2) {
                put(emailList.subList(1, 2));
            }
        }
        return new ArrayList();
    }

    public static List sendOrganizerActivated(List<User> users) {
        List<Email> emailsToUser = ObjectifyService.ofy().load().type(Email.class)
                .filter("toAddress", users.get(0).getEmail()).list();
        // the first email to this user saved is a confirmation email
        while (emailsToUser.size() > 1) {
            emailsToUser.remove(1);
        }
        return send(emailsToUser);
    }

    @Path(Api.RESET_PASSWORD) @POST @Consumes(JSON) @Produces(JSON)
    public static List resetPassword(List<Email> emailList) {
        if (emailList.size() == 1) {
            Email email = emailList.get(0);
            String usernameOrEmail = email.getToAddress();
            User wildcardLoginUser = new User(usernameOrEmail, "", usernameOrEmail,
                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
            List<User> existingUsers = UserRestService.getUnique(Arrays.asList(wildcardLoginUser));
            if (existingUsers.size() == 1) {
                User realUser = existingUsers.get(0);
                if (realUser.getGroup().equals(User.USER) && realUser.getReferralEmail().length() == 0
                        || realUser.getGroup().equals(User.ORGANIZER)) {
                    email.setToAddress(realUser.getEmail());
                    if (ObjectifyService.ofy().load().type(Email.class)
                            .filter("toAddress", email.getToAddress())
                            .filter("fromAddress", email.getFromAddress()).list().size() <= 2) { // todo make labels
                        email.setMessage(email.getMessage()
                                .replace("PASSWORD", realUser.getPassword()));
                        email.setMessage(email.getMessage()
                                .replace("CONTACTS_SPEED_DATING", Api.APP_EMAIL));
                        send(emailList);
                        return emailList;
                    } else {
                        email.setToAddress("TOO_OFTEN " + email.getToAddress());
                    }
                } else {
                    email.setToAddress("NOT_ALLOWED " + email.getToAddress());
                }
            } else {
                email.setToAddress("WRONG " + email.getToAddress());
            }
        }
        put(emailList);
        return new ArrayList();
    }

    public static List getAll() {
        return new ArrayList<>(ObjectifyService.ofy().load().type(Email.class).list());
    }

    public static List put(List<Email> items) {
        for (Email item : items) {
            item.setCreationTime(Time.getFullDateTimeNow());
        }
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
