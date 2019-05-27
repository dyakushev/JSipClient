package com.dyakushev.model.util;

import com.dyakushev.event.EventManager;
import com.dyakushev.event.EventOperation;
import com.dyakushev.pojo.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class Utils {
    private static final String configurationFile = new String("config.properties");
    private static EventManager events = new EventManager(EventOperation.LOG, EventOperation.STATUS, EventOperation.LISTENING_POINT);
    private static String sipUsername, sipPassword, sipDomain, sipServerIP, sipServerPort, sipTransport, sipFromAddress, sipFromDisplayName, sipFromName, peerHostPort, transport;
    private static ObservableList<Property> list = FXCollections.observableArrayList();

    public static String getSipUsername() {
        return sipUsername;
    }

    public static String getSipPassword() {
        return sipPassword;
    }

    public static String getSipDomain() {
        return sipDomain;
    }

    public static String generateCallId() {
        return "12345";
    }


    public static String getSipServerIP() {
        return sipServerIP;
    }

    public static String getSipServerPort() {
        return sipServerPort;
    }

    public static String getSipTransport() {
        return sipTransport;
    }

    public static String getPeerHostPort() {
        return peerHostPort;
    }

    public static String getTransport() {
        return transport;
    }

    public static String getSipFromName() {
        return sipFromName;
    }

    public static String getSipFromAddress() {
        return sipFromAddress;
    }

    public static String getSipFromDisplayName() {
        return sipFromDisplayName;
    }

    public static EventManager getEvents() {
        return events;
    }

    public static ObservableList<Property> readConfigurationProperties() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configurationFile)) {
            list.clear();
            properties.load(fis);
            sipUsername = properties.getProperty("sip.username");
            sipPassword = properties.getProperty("sip.password");
            sipDomain = properties.getProperty("sip.domain");
            sipServerIP = properties.getProperty("sip.server.ip");
            sipServerPort = properties.getProperty("sip.server.port");
            sipTransport = properties.getProperty("sip.transport");
            sipFromAddress = properties.getProperty("sip.from.address");
            sipFromDisplayName = properties.getProperty("sip.from.displayname");
            sipFromName = properties.getProperty("sip.from.name");
            peerHostPort = sipServerIP + ":" + sipServerPort;

            list.add(new Property("sip.username", sipUsername));
            list.add(new Property("sip.password", sipPassword));
            list.add(new Property("sip.domain", sipDomain));
            list.add(new Property("sip.server.ip", sipServerIP));
            list.add(new Property("sip.server.port", sipServerPort));

            transport = sipTransport;
        }

        return list;
    }
        public static void fakeProperties(){
            sipUsername = "test";
            sipPassword = "test";
            sipDomain = "test.ru";
            sipServerIP = "127.0.0.1";
            sipServerPort = "5082";
            sipTransport = "UDP";
            sipFromAddress = "test";
            sipFromDisplayName = "test";
            sipFromName = "test";
            peerHostPort = sipServerIP + ":" + sipServerPort;
        }




    public static void writeConfigurationProperties(ObservableList<Property> list) throws IOException {
        Properties properties = new Properties();
        try (OutputStream os = new FileOutputStream(configurationFile)) {
            for (Property property : list) {
                String field = property.getField();
                String value = property.getValue();
                properties.put(field, value);
            }
            properties.store(os, "");
        }
    }

    public static void updateLogArea(String message) {
        events.notify(EventOperation.LOG, message);
    }

    public static void updateStatusArea(String message) {
        events.notify(EventOperation.STATUS, message);
    }

    public static void updateListeningPointArea(String message) {
        events.notify(EventOperation.LISTENING_POINT, message);
    }

    public static String getConfigurationFile() {
        return configurationFile;
    }
}
