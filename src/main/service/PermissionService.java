package main.service;

import main.domain.Permissions;
import main.domain.Ship;

public class PermissionService {
    public boolean canCarryHazardous(Ship ship) {
        return (ship.getPermissions() & Permissions.CARRY_HAZARDOUS) != 0;
    }
}
