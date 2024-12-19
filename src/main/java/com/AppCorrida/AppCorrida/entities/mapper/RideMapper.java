package com.AppCorrida.AppCorrida.entities.mapper;

import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.User;
import com.AppCorrida.AppCorrida.entities.dto.DestinationDTO;
import com.AppCorrida.AppCorrida.entities.dto.OriginDTO;
import com.AppCorrida.AppCorrida.entities.dto.RideDTO;
import com.AppCorrida.AppCorrida.entities.dto.UserDTO;
import com.AppCorrida.AppCorrida.entities.enums.RideStatus;
import org.springframework.stereotype.Component;

@Component
public class RideMapper {

    public RideDTO convertToRideDTO(Ride ride) {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setId(ride.getId());
        rideDTO.setStatus(ride.getStatus().name());

        OriginDTO originDTO = new OriginDTO();
        originDTO.setLatitude(ride.getOrigin().getLatitude());
        originDTO.setLongitude(ride.getOrigin().getLongitude());
        rideDTO.setOrigin(originDTO);

        DestinationDTO destinationDTO = new DestinationDTO();
        destinationDTO.setLatitude(ride.getDestination().getLatitude());
        destinationDTO.setLongitude(ride.getDestination().getLongitude());
        rideDTO.setDestination(destinationDTO);

        if (ride.getStatus() == RideStatus.IN_PROGRESS || ride.getStatus() == RideStatus.FINISHED) {
            User driver = ride.getDriver();
            if (driver != null) {
                rideDTO.setDriver(new UserDTO(driver.getId(), driver.getName(), driver.getEmail(), driver.getCarPlate()));
            }
        }
        return rideDTO;
    }
}
