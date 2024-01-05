package booking.alerts.service;

import booking.alerts.dto.OrderInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEmailService {

    private final JavaMailSender emailSender;


    private void sendSimpleEmail(String toAddress, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        emailSender.send(simpleMailMessage);
    }

    public void sendOrderEmail(OrderInfoDto order) {
        var orderDetails = """
                Order confirmed
                
                Hotel info:
                    Name: %s
                    City: %s
                    Date in: %s
                    Date out: %s
                    
                Guest info:
                    Name: %s
                    
                
                """.formatted(
                        order.getHotel().getName(), order.getHotel().getCity(), order.getDateIn(), order.getDateOut(),
                        order.getUser().getName()
                );

        sendSimpleEmail(order.getUser().getLogin(), "New order", orderDetails);
    }

}
