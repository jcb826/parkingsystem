package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingSpotDAOTest {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;

    @BeforeAll

    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;


    }

    @Test
    public void getNextAvailableSlotTest() {

        ParkingType parkingType = ParkingType.CAR;
        Assertions.assertNotNull(parkingSpotDAO.getNextAvailableSlot(parkingType));
    }

    @Test
    public void updateParkingTest() {
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(3, parkingType, false);
        assertEquals(parkingSpotDAO.updateParking(parkingSpot), true);

    }

    @Test
    public void checkAvailabilityTest() {
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(1, parkingType, true);
        parkingSpotDAO.updateParking(parkingSpot);
        assertEquals(parkingSpotDAO.checkAvailability(1), true);
    }


}
