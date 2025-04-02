package com.utility.creation.utilitycreation.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.utility.creation.utilitycreation.exceptions.customexception.EmailSendException;
import com.utility.creation.utilitycreation.utils.Constants;

@Service
public class EmailService {
    
    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${email.from}")
    private String emailFrom;

    public String sendEmail(String to, String subject, Map<String, String> dynamicData) {
        Email from = new Email(emailFrom); // Replace with your verified sender email
        Email toEmail = new Email(to);
        // Set the template ID for your dynamic template in SendGrid
        String templateId = "d-42e885619a384842a690520efe7bd0c7";  // Replace with your actual template ID

        // Create a Mail object and attach dynamic data to the template
        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setTemplateId(templateId);

        // Add dynamic data
        Personalization personalization = new Personalization();
        personalization.addTo(toEmail);

        // Add dynamic template data
        for (Map.Entry<String, String> entry : dynamicData.entrySet()) {
            personalization.addDynamicTemplateData(entry.getKey(), entry.getValue());
        }

        mail.addPersonalization(personalization);

        SendGrid sendGrid = new SendGrid(apiKey);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            return "Status Code: " + response.getStatusCode();
        } catch (IOException ex) {
           throw new EmailSendException(Constants.EMAIL_SEND_ERROR);
        }
    }
}
