package com.stackpulse.automation.ui.model;

public enum FooterOptionsEnum {
    ALL ("All"),
    COMPLETED ("Completed"),
    ACTIVE ("Active");

    public final String value;

    FooterOptionsEnum(String value) {
        this.value = value;
    }
}
