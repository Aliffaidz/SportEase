package app.web_socket;

import app.dto.BookingDto;
import app.dto.BookingTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SendUpdatedDataBooking {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public SendUpdatedDataBooking(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    public void sendUpdatedDataBooking(List<BookingTime> bookingTimes){
        log.info("SEND UPDATED BOOKING LIST");
        simpMessagingTemplate.convertAndSend(
                "/booking-table/jadwal-lapangan",
                bookingTimes
        );
    }


}
