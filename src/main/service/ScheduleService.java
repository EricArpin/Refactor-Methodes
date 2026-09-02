package main.service;

public class ScheduleService {
    private final int[] departuresByDay = {4, 7, 3, 8, 12, 6, 2};

    public int createMask(boolean monday, boolean tuesday, boolean wednesday,
                          boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        int mask = 0;
        if (monday) mask += 1;
        if (tuesday) mask += 2;
        if (wednesday) mask += 4;
        if (thursday) mask += 8;
        if (friday) mask += 16;
        if (saturday) mask += 32;
        if (sunday) mask += 64;
        return mask;
    }

    public boolean runsOnFriday(int mask) {
        return (mask & 16) != 0;
    }

    public int getDepartures(int day) {
        try {
            return departuresByDay[day];
        } catch (ArrayIndexOutOfBoundsException exception) {
            return 0;
        }
    }
}
