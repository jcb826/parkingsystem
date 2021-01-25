package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketDAOTest {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() throws Exception {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;

    }

    @Test
    public void getTicketTest() {
        Ticket ticketToSave = new Ticket();
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(5, parkingType, true);
        ticketToSave.setId(1);
        ticketToSave.setParkingSpot(parkingSpot);
        ticketToSave.setVehicleRegNumber("AZERTY");
        ticketToSave.setPrice(0);
        ticketToSave.setInTime(new Date());
        ticketToSave.setOutTime(new Date());
        ticketDAO.saveTicket(ticketToSave);
        Ticket ticket = ticketDAO.getTicket("AZERTY");
        Assertions.assertThat(ticket).isNotNull();
    }

    @Test
    public void updateTicketTest() {
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(5, parkingType, true);
        Ticket ticketToSave = new Ticket();
        ticketToSave.setId(2);
        ticketToSave.setParkingSpot(parkingSpot);
        ticketToSave.setVehicleRegNumber("OSI");
        ticketToSave.setPrice(0);
        ticketToSave.setInTime(new Date());
        ticketToSave.setOutTime(new Date());
        ticketDAO.saveTicket(ticketToSave);
        ticketToSave.setId(2);
        ticketToSave.setPrice(60);
        ticketToSave.setOutTime(new Date());
        assertEquals(ticketDAO.updateTicket(ticketToSave), true);
    }

    @Test
    public void SaveTicketTest() {
        String regNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot = new ParkingSpot(2, parkingType, true);
        Ticket ticketToSave = new Ticket();
        ticketToSave.setParkingSpot(parkingSpot);
        ticketToSave.setVehicleRegNumber(regNumber);
        ticketToSave.setPrice(2);
        ticketToSave.setInTime(new Date());
        ticketToSave.setOutTime(new Date());
        Assertions.assertThat(ticketDAO.saveTicket(ticketToSave)).isNotNull();
        Ticket result = ticketDAO.getTicket(regNumber);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).extracting(r -> r.getVehicleRegNumber()).isEqualTo(regNumber);
    }

    @Test
    public void compareTicketTest() {

        fareCalculatorService = new FareCalculatorService(ticketDAO);
        ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }
}


