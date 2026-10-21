package com.unabia;

import com.unabia.dao.DatabaseConnection;
import com.unabia.ui.ScenarioMenu;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        new ScenarioMenu().start();
    }
}
