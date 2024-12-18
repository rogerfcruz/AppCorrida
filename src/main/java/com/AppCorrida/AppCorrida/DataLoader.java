package com.AppCorrida.AppCorrida;

import com.AppCorrida.AppCorrida.security.SecurityConfig;
import com.AppCorrida.AppCorrida.entities.Destination;
import com.AppCorrida.AppCorrida.entities.Origin;
import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.User;
import com.AppCorrida.AppCorrida.entities.enums.RideStatus;
import com.AppCorrida.AppCorrida.entities.enums.UserType;
import com.AppCorrida.AppCorrida.repositories.RideRepository;
import com.AppCorrida.AppCorrida.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
public class DataLoader {

    @Autowired
    private SecurityConfig securityConfig;

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, RideRepository rideRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            User user1 = new User(null, "Jose", "jose@gmail.com", passwordEncoder.encode("jose123"), UserType.PASSENGER, null, null, null);
            User user2 = new User(null, "Carlos", "carlos@gmail.com", passwordEncoder.encode("carlos123"), UserType.PASSENGER, null, null, null);
            User user3 = new User(null, "Ana", "ana@gmail.com", passwordEncoder.encode("ana123"), UserType.DRIVER, "ABC9876", null, null);
            User user4 = new User(null, "João", "joao@gmail.com", passwordEncoder.encode("joao123"), UserType.PASSENGER, null, null, null);
            User user5 = new User(null, "Francisco", "francisco@gmail.com", passwordEncoder.encode("francisco123"), UserType.PASSENGER, null, null, null);
            User user6 = new User(null, "Maria", "maria@gmail.com", passwordEncoder.encode("maria123"), UserType.DRIVER, "ABC1234", null, null);
            User user7 = new User(null, "Lucas", "lucas@gmail.com", passwordEncoder.encode("lucas123"), UserType.PASSENGER, null, null, null);
            User user8 = new User(null, "Felipe", "felipe@gmail.com", passwordEncoder.encode("felipe123"), UserType.PASSENGER, null, null, null);
            User user9 = new User(null, "Pedro", "pedro@gmail.com", passwordEncoder.encode("pedro123"), UserType.DRIVER, "ABC9876", null, null);
            User user10 = new User(null, "Daniela", "daniela@gmail.com", passwordEncoder.encode("daniela123"), UserType.PASSENGER, null, null, null);

            userRepository.saveAll(Arrays.asList(user1,user2,user3,user4,user5,user6,user7,user8,user9,user10));

            Ride ride1 = new Ride(null, user1, null, new Origin(-19.863256, -44.019666), new Destination(-19.892472, -44.025197), RideStatus.WAITING, null, null, null);
            Ride ride2 = new Ride(null, user2, null, new Origin(-19.863256, -44.019666), new Destination(-22.879831, -42.036608), RideStatus.WAITING, null, null, null);

            rideRepository.saveAll(Arrays.asList(ride1,ride2));
        };
    }
}
