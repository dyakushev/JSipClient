package com.dyakushev.model.util;

import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

public class RequestContent {
    private static AddressFactory addressFactory;
    private static SipProvider sipProvider;
    private static MessageFactory messageFactory;
    private static HeaderFactory headerFactory;
    private static SipStack sipStack;
    private ContactHeader contactHeader;
    private ListeningPoint udpListeningPoint;
    private String fromName;
    private String fromSipAddress;
    private String fromDisplayName;
    private String transport;
    private String peerHostPort;
    private String callId;
    private Integer expiredTimer;


    public RequestContent(ContactHeader contactHeader, ListeningPoint udpListeningPoint, String fromName, String fromSipAddress, String fromDisplayName, String transport, String peerHostPort, Integer expiredTimer, String callId) {
        this.contactHeader = contactHeader;
        this.udpListeningPoint = udpListeningPoint;
        this.fromName = fromName;
        this.fromSipAddress = fromSipAddress;
        this.fromDisplayName = fromDisplayName;
        this.transport = transport;
        this.peerHostPort = peerHostPort;
        this.expiredTimer = expiredTimer;
        this.callId = callId;
    }

    public RequestContent() {
    }

    public static void setAddressFactory(AddressFactory addressFactory) {
        RequestContent.addressFactory = addressFactory;
    }

    public static void setSipProvider(SipProvider sipProvider) {
        RequestContent.sipProvider = sipProvider;
    }

    public static void setMessageFactory(MessageFactory messageFactory) {
        RequestContent.messageFactory = messageFactory;
    }

    public static void setHeaderFactory(HeaderFactory headerFactory) {
        RequestContent.headerFactory = headerFactory;
    }

    public static void setSipStack(SipStack sipStack) {
        RequestContent.sipStack = sipStack;
    }

    public static AddressFactory getAddressFactory() {
        return addressFactory;
    }

    public static SipProvider getSipProvider() {
        return sipProvider;
    }

    public static MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public static HeaderFactory getHeaderFactory() {
        return headerFactory;
    }

    public static SipStack getSipStack() {
        return sipStack;
    }

    public ContactHeader getContactHeader() {
        return contactHeader;
    }

    public ListeningPoint getUdpListeningPoint() {
        return udpListeningPoint;
    }

    public String getFromName() {
        return fromName;
    }

    public String getFromSipAddress() {
        return fromSipAddress;
    }

    public String getFromDisplayName() {
        return fromDisplayName;
    }

    public String getTransport() {
        return transport;
    }

    public String getPeerHostPort() {
        return peerHostPort;
    }

    public Integer getExpiredTimer() {
        return expiredTimer;
    }

    public String getCallId() {
        return callId;
    }
}
