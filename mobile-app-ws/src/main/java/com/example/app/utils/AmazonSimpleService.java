package com.example.app.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.example.app.dao.UserDao;

public class AmazonSimpleService {

	final String FROM = "yuvina47@gmail.com";

	// The subject line for the email.
	final String SUBJECT = "One last step to complete your registration with PhotoApp";

	final String PASSWORD_RESET_SUBJECT = "Password reset request";

	// The HTML body for the email.
	final String HTMLBODY = "<h1>Please verify your email address</h1>"
			+ "<p>Thank you for registering with our mobile app. To complete registration process and be able to log in,"
			+ " click on the following link: "
			+ "<a href='http://ec2-3-88-139-62.compute-1.amazonaws.com:8080/mobile-app-ws/users/email-verification?token=$tokenValue'>"
			+ "Final step to complete your registration" + "</a><br/><br/>"
			+ "Thank you! And we are waiting for you inside!";

	// The email body for recipients with non-HTML email clients.
	final String TEXTBODY = "Please verify your email address. "
			+ "Thank you for registering with our mobile app. To complete registration process and be able to log in,"
			+ " open then the following URL in your browser window: "
			+ " http://ec2-3-88-139-62.compute-1.amazonaws.com:8080/mobile-app-ws/users/email-verification?token=$tokenValue"
			+ " Thank you! And we are waiting for you inside!";

	final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password</h1>" + "<p>Hi, $firstName!</p> "
			+ "<p>Someone has requested to reset your password with our project. If it were not you, please ignore it."
			+ " otherwise please click on the link below to set a new password: "
			+ "<a href='http://localhost:8080/verification-service/password-reset.html?token=$tokenValue'>"
			+ " Click this link to Reset Password" + "</a><br/><br/>" + "Thank you!";

	// The email body for recipients with non-HTML email clients.
	final String PASSWORD_RESET_TEXTBODY = "A request to reset your password " + "Hi, $firstName! "
			+ "Someone has requested to reset your password with our project. If it were not you, please ignore it."
			+ " otherwise please open the link below in your browser window to set a new password:"
			+ " http://localhost:8080/verification-service/password-reset.html?token=$tokenValue" + " Thank you!";

	public void verify(UserDao dao) {

		AmazonSimpleEmailService emailService = AmazonSimpleEmailServiceClientBuilder.standard()
				.withRegion(Regions.US_EAST_1).build();

		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", dao.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", dao.getEmailVerificationToken());

		SendEmailRequest emailRequest = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(dao.getEmail()))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);

		emailService.sendEmail(emailRequest);

		System.out.println("Email Sent!");
	}

	public boolean sendPasswordResetRequest(String firstName, String email, String token) {

		boolean returnValue = false;

		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.build();

		String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
		htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);

		String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
		textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);

		SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(email))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(PASSWORD_RESET_SUBJECT)))
				.withSource(FROM);

		SendEmailResult result = client.sendEmail(request);

		if (result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty())) {

			returnValue = true;
		}

		return returnValue ;
	}

}
