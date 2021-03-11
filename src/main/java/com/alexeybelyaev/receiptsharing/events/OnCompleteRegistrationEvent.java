package com.alexeybelyaev.receiptsharing.events;

import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


public class OnCompleteRegistrationEvent extends ApplicationEvent {


    @Getter
    @Setter
    private ApplicationUser user;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private Locale locale;



    public OnCompleteRegistrationEvent(ApplicationUser user,
                                       Locale locale,
                                       String url) {
        super(user);
        this.url = url;
        this.locale = locale;
        this.user = user;
    }

}
