package com.isa.cottages.Model;

public enum RegistrationType {
    COTTAGE_ADVERTISER("Cottage Advertiser"),
    BOAT_ADVERTISER("Boat Advertiser"),
    ADVENTURE_ADVERTISER("Adventure Advertiser");

    private final String displayName;

    RegistrationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
