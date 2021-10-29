package com.isa.cottages.Model;

public enum RegistrationType {COTTAGE_ADVERTISER("CottageAdvertiser"),
    BOAT_ADVERTISER("BoatAdvertiser");

    private final String displayName;

    RegistrationType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
