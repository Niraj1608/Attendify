package com.project.gui.dashboard;

import javafx.beans.property.*;

public class User {
    private final StringProperty email;
    private final DoubleProperty percentage;

    public User(String email, double percentage) {
        this.email = new SimpleStringProperty(email);
        this.percentage = new SimpleDoubleProperty(percentage);
    }

    public StringProperty nameProperty() {
        return email;
    }

    public DoubleProperty percentageProperty() {
        return percentage;
    }
}
