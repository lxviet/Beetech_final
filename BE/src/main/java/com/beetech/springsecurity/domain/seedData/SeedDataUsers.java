package com.beetech.springsecurity.domain.seedData;

import com.beetech.springsecurity.SpringSecurityApplication;
import com.beetech.springsecurity.domain.entity.MstUser;
import com.beetech.springsecurity.domain.enums.DeleteFlag;
import com.beetech.springsecurity.domain.enums.Role;
import com.beetech.springsecurity.domain.enums.UserStatus;
import com.beetech.springsecurity.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Component
public class SeedDataUsers {
    private static final Logger log = LoggerFactory.getLogger(SpringSecurityApplication.class);

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository) {
        return (args) -> {

//            userRepository.save(new MstUser(1, "buingochuy124@gmail.com",
//                    "$2a$10$HtePZQtptEYtBModxURzgeAO.U763fm2W3ScUa.k05CTjYKpkoFY.",
//                    "Bui Ngoc Huy", Role.USER
//
//                    , LocalDate.of(2000, Month.DECEMBER, 24)
//                    , UserStatus.NORMAL, DeleteFlag.NORMAL));
//
//            userRepository.save(new MstUser(2, "vietdaithanh2123@gmail.com",
//                    "$2a$10$HtePZQtptEYtBModxURzgeAO.U763fm2W3ScUa.k05CTjYKpkoFY.",
//                    "Le Xuan Viet", Role.ADMIN,

//                    LocalDate.of(2000, Month.DECEMBER, 24)
//                    , UserStatus.NORMAL, DeleteFlag.NORMAL));

            log.info("Users found with findAll():");
            log.info("-------------------------------");
            for (MstUser user : userRepository.findAll()) {
                log.info(user.toString());
            }
            log.info("");


        };
    }

}
