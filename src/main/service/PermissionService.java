package main.service;

import main.domain.Permissions;
import main.domain.Ship;

public class PermissionService {
    public boolean canCarryHazardous(Ship ship) {
        int permissions = ship.getPermissions();
        if (permissions == Permissions.CARRY_HAZARDOUS) return true;
        if (permissions == Permissions.VIEW_MANIFEST + Permissions.CARRY_HAZARDOUS) return true;
        if (permissions == Permissions.EDIT_MANIFEST + Permissions.CARRY_HAZARDOUS) return true;
        if (permissions == Permissions.VIEW_MANIFEST + Permissions.EDIT_MANIFEST + Permissions.CARRY_HAZARDOUS) return true;
        if (permissions == Permissions.CARRY_HAZARDOUS + Permissions.CROSS_RESTRICTED_SECTOR) return true;
        if (permissions == Permissions.VIEW_MANIFEST + Permissions.CARRY_HAZARDOUS + Permissions.CROSS_RESTRICTED_SECTOR) return true;
        if (permissions == Permissions.EDIT_MANIFEST + Permissions.CARRY_HAZARDOUS + Permissions.CROSS_RESTRICTED_SECTOR) return true;
        if (permissions == 15) return true;
        return false;
    }

    public boolean cannotCrossRestrictedSector(Ship ship) {
        return (ship.getPermissions() & Permissions.CROSS_RESTRICTED_SECTOR) == 0;
    }

    public void grantHazardousPermission(Ship ship) {
        ship.setPermissions(ship.getPermissions() | Permissions.CARRY_HAZARDOUS);
    }

    public void removeEditPermission(Ship ship) {
        ship.setPermissions(ship.getPermissions() & ~Permissions.EDIT_MANIFEST);
    }
}
