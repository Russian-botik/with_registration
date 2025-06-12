package com.example.demo;

import com.example.demo.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/rooms")
public class ClientRoomController {
    @Autowired
    private ScheduleApiClient apiClient;

    @GetMapping
    public RoomDto[] getRooms() {
        return apiClient.getRooms();
    }

    @PostMapping
    public RoomDto addRoom(@RequestBody RoomDto room) {
        return apiClient.addRoom(room);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        apiClient.deleteRoom(id);
    }
} 