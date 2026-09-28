package UI;

import exception.RoomNotFoundException;
import model.Room;
import model.RoomStatus;
import service.RoomService;
import util.CodeGenerater;
import util.InputUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleRoom {

    private RoomService roomService;



    Scanner scanner = new Scanner(System.in);

    public ConsoleRoom(RoomService roomService) {
        this.roomService = roomService;
    }

    public void roomDisponible(){
        try {
            List<Room> dispoRooms = roomService.findRoomDispo();
            printRooms(dispoRooms);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void printRooms(List<Room> dispoRooms) {
        for (Room r : dispoRooms){
            System.out.println("=====================================");
            System.out.println("Room Number : " + r.getRoomNumber());
            System.out.println("Room Capacity : " + r.getCapacity());
            System.out.println("Room Price : " + r.getPricePerNight() + " Per night");
            System.out.println("Status : " + r.getStatus());
            System.out.println("====================================");
        }
    }

    public void viewAllRoom() {
        try {
            List<Room> rooms = roomService.showAllRoom();
            printRooms(rooms);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void createRoom() {

        BigDecimal price = InputUtils.lireBigDecimal(scanner,"Enter PRICE");
        int  capacity = InputUtils.lireInt(scanner,"Enter CAPACITY");

        try {
            roomService.createRoom(capacity,price);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateRoom() {
        String roomNumber = InputUtils.lireString(scanner,"Enter the room number to modify");
        BigDecimal newPrice = InputUtils.lireBigDecimal(scanner,"Enter new PRICE");
        int  newCapacity = InputUtils.lireInt(scanner,"Enter new CAPACITY");
        String newStatus = InputUtils.lireString(scanner,"Enter the new status  AVAILABLE/MAINTENANCE");
        try {
            roomService.updateRoom(newPrice,newCapacity,newStatus.toUpperCase(),roomNumber);
        } catch (RoomNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteRoom() {
        String roomNumber = InputUtils.lireString(scanner,"Enter the room number to Delete");
        try {
            roomService.deleteRoom(roomNumber);
        } catch (SQLException | RoomNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
