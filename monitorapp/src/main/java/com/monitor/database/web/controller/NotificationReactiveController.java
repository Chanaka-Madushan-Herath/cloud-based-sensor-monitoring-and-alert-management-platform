package com.monitor.database.web.controller;

import com.monitor.database.model.Notification;
import com.monitor.database.repository.NotificationReactiveRepository;
import com.monitor.Authentication.entity.User;
import com.monitor.Authentication.Repository.UserRepository;
import com.monitor.Authentication.utility.JWTUtility;
import com.monitor.sensor_data.Sensors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notification/mongodb")
public class NotificationReactiveController {

    /*private NotificationService notificationService;*/
    private NotificationReactiveRepository notificationReactiveRepository;
   @Autowired
    private  JWTUtility jwtUtility;
   @Autowired
   private UserRepository userRepository;

    @Autowired
    public NotificationReactiveController(/*NotificationService notificationService, */NotificationReactiveRepository notificationReactiveRepository) {
        //this.notificationService = notificationService;
        this.notificationReactiveRepository = notificationReactiveRepository;
    }

    /*@PostMapping
    public Mono<Notification> post(@RequestBody Notification notification) {
        return notificationService.save(notification);
    }*/
    @PostMapping("/send")
    public Mono<Notification> post(@RequestBody Sensors sensors) {
        String data_value = sensors.getData_value();
        String date = sensors.getDate();
        Notification notification = new Notification(date, data_value);
        System.out.println("listening");
        return this.notificationReactiveRepository.save(notification);
    }
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Notification> getByTailing(@RequestHeader("Authorization") String jwt){
        String token = jwt.substring(7);
        String username = jwtUtility.getDetailsFromToken(token);
        String email = username.substring(0, username.indexOf(','));
        User user=this.userRepository.findByEmail(email);
        String notificationMethod = user.getNotification();
        String name =user.getName();
        String e_mail =user.getEmail();
        String phone = user.getPhone();
        Flux<Notification> stream = notificationReactiveRepository.findByTime("24");
        Disposable subscription = stream.doOnNext(System.out::println).subscribe();
        return stream;
    }

}
