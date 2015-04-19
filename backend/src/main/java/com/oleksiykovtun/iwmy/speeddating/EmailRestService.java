package com.oleksiykovtun.iwmy.speeddating;

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
                Message message
                        = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
                message.setFrom(new InternetAddress(email.getFromAddress(), email.getFromName()));
                if (! email.getToAddress().matches(Email.VALIDATION_REGEX)) {
                    throw new Exception("Receiver's email address is invalid");
                }
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(email.getToAddress(), email.getToName()));
                message.setSubject(MimeUtility.encodeText(email.getSubject(), "utf-8", "B"));
                message.setText(email.getMessage());
                Transport.send(message);
            } catch (Throwable t) {
                unsentEmailList.add(email);
            }
        }
        return unsentEmailList;
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

}
