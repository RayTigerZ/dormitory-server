package com.ray.dormitory.z;

import com.alibaba.excel.EasyExcel;
import com.ray.dormitory.bean.po.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.24 15:33
 */
public class RoomGenerate {
    public static void main(String[] args) {
        String path = "D:\\graduation-project\\宿舍列表.xlsx";
        List<Room> rooms = new ArrayList<>();
        for (int buildingId = 1; buildingId <= 23; buildingId++) {
            int maxFloor = 0;
            int maxRoom = 0;
            int size = 0;
            if (buildingId >= 1 && buildingId <= 7) {
                maxFloor = 6;
                maxRoom = 28;
                size = 6;
            } else if (buildingId >= 8 && buildingId <= 18) {
                maxFloor = 10;
                maxRoom = 24;
                size = 4;
            } else if (buildingId >= 19 && buildingId <= 23) {
                maxFloor = 12;
                maxRoom = 24;
                size = 4;
            }
            for (int floor = 1; floor <= maxFloor; floor++) {
                for (int roomNum = 1; roomNum <= maxRoom; roomNum++) {
                    Room room = new Room();
                    room.setBuildingId(buildingId);
                    room.setSize(size);
                    StringBuffer number = new StringBuffer();

                    if (buildingId < 10) {
                        number.append(0);
                    }
                    number.append(buildingId);
                    if (floor < 10) {
                        number.append(0);
                    }
                    number.append(floor);
                    if (roomNum < 10) {
                        number.append(0);
                    }
                    number.append(roomNum);
                    room.setNumber(number.toString());
                    rooms.add(room);
                }
            }
        }

        EasyExcel.write(path, Room.class).sheet().doWrite(rooms);

    }
}
