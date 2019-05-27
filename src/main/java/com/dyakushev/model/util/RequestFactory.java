package com.dyakushev.model.util;

import com.dyakushev.model.util.RequestContent;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import java.text.ParseException;
import java.util.ArrayList;

public class RequestFactory {
 /*   private static volatile JSipRequest jSipRequest;

    public static JSipRequest getInstance() {
        JSipRequest localInstance = jSipRequest;
        if (localInstance == null)
            synchronized (JSipRequest.class) {
                localInstance = jSipRequest;
                if (localInstance == null) {
                    jSipRequest = localInstance = new JSipRequest();
                }
            }
        return localInstance;
    }

    private JSipRequest() {
    }*/


    public static Request getRegisterRequest(RequestContent jSipRequestContent) throws ParseException, InvalidArgumentException {
        String fromName = jSipRequestContent.getFromName();
        String fromSipAddress = jSipRequestContent.getFromSipAddress();
        String fromDisplayName = jSipRequestContent.getFromDisplayName();
        String transport = jSipRequestContent.getTransport();
        String peerHostPort = jSipRequestContent.getPeerHostPort();
        String callId = jSipRequestContent.getCallId();
        Integer expiredTimer = jSipRequestContent.getExpiredTimer();
        Long invco = 1L;


        AddressFactory addressFactory = RequestContent.getAddressFactory();
        HeaderFactory headerFactory = RequestContent.getHeaderFactory();
        SipProvider sipProvider = RequestContent.getSipProvider();
        MessageFactory messageFactory = RequestContent.getMessageFactory();


        ContactHeader contactHeader = jSipRequestContent.getContactHeader();


        SipURI fromAddress = RequestContent.getAddressFactory().createSipURI(fromName,
                fromSipAddress);
        Address fromNameAddress = addressFactory.createAddress(fromAddress);
        fromNameAddress.setDisplayName(fromDisplayName);

        //create TO header
        ToHeader toHeader = headerFactory.createToHeader(fromNameAddress, null);
        //create From header
        FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
                "12345");
        // create Request URI
        SipURI requestURI = addressFactory.createSipURI(fromName, peerHostPort);
        //URI requestURI = addressFactory.createURI(peerHostPort);


        // Create ViaHeaders
        ArrayList<ViaHeader> viaHeaders = new ArrayList<>();
        ViaHeader viaHeader = headerFactory.createViaHeader("127.0.0.1",
                sipProvider.getListeningPoint(transport).getPort(), transport,
                null);
        // add via headers
        viaHeaders.add(viaHeader);
        CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invco,
                Request.REGISTER);

        // Create a new CallId header
        CallIdHeader callIdHeader;
        callIdHeader = sipProvider.getNewCallId();
        if (callId.trim().length() > 0)
            callIdHeader.setCallId(callId);

        // Create a new MaxForwardsHeader
        MaxForwardsHeader maxForwards = headerFactory
                .createMaxForwardsHeader(70);

        Request request = messageFactory.createRequest(requestURI, Request.REGISTER, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
// Create the contact name address.
        SipURI contactURI = addressFactory.createSipURI(fromName, sipProvider.getListeningPoint(transport).getIPAddress());
        contactURI.setPort(sipProvider.getListeningPoint(transport).getPort());

        Address contactAddress = addressFactory.createAddress(contactURI);

        // Add the contact address.
        contactAddress.setDisplayName(fromName);

        contactHeader = headerFactory.createContactHeader(contactAddress);
        request.addHeader(contactHeader);

        //Add the expires header
        ExpiresHeader expiresHeader = headerFactory.createExpiresHeader(expiredTimer);
        request.addHeader(expiresHeader);
        //   jSipClientHelper.updateLogArea(request.toString());

        // Request request = messageFactory.
        return request;
    }

    public static Request getRegisterCancelRequest(RequestContent requestContent, String authorizationHeader) throws ParseException, InvalidArgumentException {
        Request request = getRegisterRequest(requestContent);
        HeaderFactory headerFactory = RequestContent.getHeaderFactory();
        request.addHeader(headerFactory.createAuthorizationHeader(authorizationHeader));
        return request;
    }

    public static Request getInviteRequest(RequestContent requestContent) throws ParseException,
            InvalidArgumentException {
        String fromName = requestContent.getFromName();
        String fromSipAddress = requestContent.getFromSipAddress();
        String fromDisplayName = requestContent.getFromDisplayName();
        String transport = requestContent.getTransport();
        String peerHostPort = requestContent.getPeerHostPort();
        String callId = requestContent.getCallId();
        Integer expiredTimer = requestContent.getExpiredTimer();
        Long invco = 1L;

        AddressFactory addressFactory = RequestContent.getAddressFactory();
        HeaderFactory headerFactory = RequestContent.getHeaderFactory();
        SipProvider sipProvider = RequestContent.getSipProvider();
        MessageFactory messageFactory = RequestContent.getMessageFactory();
        ListeningPoint udpListeningPoint = requestContent.getUdpListeningPoint();

        ContactHeader contactHeader = requestContent.getContactHeader();


        String toSipAddress = "there.com";
        String toUser = "LittleGuy";
        String toDisplayName = "The Little Blister";

        // create >From Header
        SipURI fromAddress = addressFactory.createSipURI(fromName,
                fromSipAddress);

        Address fromNameAddress = addressFactory.createAddress(fromAddress);
        fromNameAddress.setDisplayName(fromDisplayName);
        FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
                "12345");

        // create To Header
        SipURI toAddress = addressFactory.createSipURI(toUser, toSipAddress);
        Address toNameAddress = addressFactory.createAddress(toAddress);
        toNameAddress.setDisplayName(toDisplayName);
        ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

        // create Request URI
        SipURI requestURI = addressFactory.createSipURI("sip:", peerHostPort);

        // Create ViaHeaders
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        ViaHeader viaHeader = headerFactory.createViaHeader("127.0.0.1",
                sipProvider.getListeningPoint(transport).getPort(), transport,
                null);
        // add via headers
        viaHeaders.add(viaHeader);

        // Create ContentTypeHeader
        ContentTypeHeader contentTypeHeader = headerFactory
                .createContentTypeHeader("application", "sdp");

        // Create a new CallId header
        CallIdHeader callIdHeader;
        callIdHeader = sipProvider.getNewCallId();
        if (callId.trim().length() > 0)
            callIdHeader.setCallId(callId);

        // Create a new Cseq header
        CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(invco,
                Request.INVITE);

        // Create a new MaxForwardsHeader
        MaxForwardsHeader maxForwards = headerFactory
                .createMaxForwardsHeader(70);

        // Create the request.
        Request request = messageFactory.createRequest(requestURI,
                Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader,
                viaHeaders, maxForwards);
        // Create contact headers
        String host = "127.0.0.1";

        SipURI contactUrl = addressFactory.createSipURI(fromName, host);
        contactUrl.setPort(udpListeningPoint.getPort());

        // Create the contact name address.
        SipURI contactURI = addressFactory.createSipURI(fromName, host);
        contactURI.setPort(sipProvider.getListeningPoint(transport).getPort());

        Address contactAddress = addressFactory.createAddress(contactURI);

        // Add the contact address.
        contactAddress.setDisplayName(fromName);

        contactHeader = headerFactory.createContactHeader(contactAddress);
        request.addHeader(contactHeader);

        String sdpData = "v=0\r\n"
                + "o=4855 13760799956958020 13760799956958020"
                + " IN IP4  129.6.55.78\r\n" + "s=mysession session\r\n"
                + "p=+46 8 52018010\r\n" + "c=IN IP4  129.6.55.78\r\n"
                + "t=0 0\r\n" + "m=audio 6022 RTP/AVP 0 4 18\r\n"
                + "a=rtpmap:0 PCMU/8000\r\n" + "a=rtpmap:4 G723/8000\r\n"
                + "a=rtpmap:18 G729A/8000\r\n" + "a=ptime:20\r\n";
        byte[] contents = sdpData.getBytes();

        request.setContent(contents, contentTypeHeader);

        Header callInfoHeader = headerFactory.createHeader("Call-Info",
                "<http://www.antd.nist.gov>");
        request.addHeader(callInfoHeader);

        return request;
    }


}
