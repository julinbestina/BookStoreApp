package com.bl.utils;


import java.io.IOException;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.bl.configuration.MessagingConfig;
import com.bl.dto.OrderDTO;
import com.bl.model.Book;
import com.rabbitmq.client.Channel;





@Component
public class EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;

	
	public void sendEmail(String toEmail) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject("Book store order");
		message.setText(" Your order placed Successfully.");
		mailSender.send(message);
	}
	
	public void sendEmailWithTemplate(String toEmail,OrderDTO orderDTO) {
		List<Book> cart = orderDTO.getCartBook();
		String body =
				 "<table text-align =\"centre\", border=\"1px solid black\" border-collapse= \"collapse\" padding= 10px>\n"
				+ "<tr>\n"
				+ "    <th>Book name</th>\n"
				+ "    <th> Author</th>\n"
				+ "    <th>Total price</th>\n"
				+ "</tr>\n"
				+ "<c:forEach var =\"row\" items = \"${cart}\">\n"
				+ "<tr>\n"
				+ "    <td>'${row.bookName}'</td>\n"
				+ "    <td>row.author</td>\n"
				+ "    <td>row.price</td>\n"
				+ "</tr>\n"
				+ "</c:forEach>\n"
				+ "</table>\n";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(toEmail);
			helper.setSubject("Book store order");
			helper.setText(body,true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    
	@RabbitListener(queues=MessagingConfig.QUEUE)
	public void consumeMessageFromQueue(String email) {
		logger.info("Message received from queue");
		sendEmail(email);
	}
	
//	 private MailProperties properties;
//	  
//	    @RabbitListener(queues = MessagingConfig.QUEUE)
//	    @RabbitHandler
//	    public void process(Channel channel, org.springframework.amqp.core.Message message) throws IOException {
//	        try {
//	            Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//	            String email =  (String) converter.fromMessage(message);
//	            sendEmail(email);
//	            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//	        }
//	    }
	
	   
}
