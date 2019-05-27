package com.dyakushev.model;

import com.dyakushev.model.auth.AccountManagerImpl;
import com.dyakushev.model.util.Utils;
import com.dyakushev.model.util.RequestContent;
import com.dyakushev.model.util.RequestFactory;
import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
import gov.nist.javax.sip.header.AuthenticationHeader;
import junit.framework.TestCase;

import javax.sip.*;
import javax.sip.address.AddressFactory;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import java.util.TooManyListenersException;

public class SipListenerImpl implements SipListener {
    private static SipProvider sipProvider;
    private static AddressFactory addressFactory;
    private static MessageFactory messageFactory;
    private static HeaderFactory headerFactory;
    private static SipStack sipStack;
    private ContactHeader contactHeader;
    private ListeningPoint udpListeningPoint;
    private ClientTransaction clientTransaction;
    private Dialog dialog;
    private String authenticationHeader;


    long invco = 1;

    @Override
    public void processRequest(RequestEvent requestReceivedEvent) {
        Request request = requestReceivedEvent.getRequest();
        ServerTransaction serverTransactionId = requestReceivedEvent
                .getServerTransaction();
        if (request.getMethod().equals(Request.BYE))
            processBye(request, serverTransactionId);
    }

    public void processBye(Request request,
                           ServerTransaction serverTransactionId) {
        try {
            if (serverTransactionId == null) {
                return;
            }
            Dialog dialog = serverTransactionId.getDialog();
            Response response = messageFactory.createResponse(200, request);
            serverTransactionId.sendResponse(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            TestCase.fail("Exit JVM");

        }
    }

    public void processResponse(ResponseEvent responseReceivedEvent) {
        Response response = (Response) responseReceivedEvent.getResponse();
        Utils.updateLogArea(response.toString());
        ClientTransaction tid = responseReceivedEvent.getClientTransaction();
        CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);

        if (tid == null) {
            Utils.updateLogArea("Stray response -- dropping ");
            return;
        }

        try {
            if (response.getStatusCode() == Response.OK) {
                if (cseq.getMethod().equals(Request.INVITE)) {
                    Dialog dialog = clientTransaction.getDialog();
                    Request ackRequest = dialog.createAck(cseq.getSeqNumber());
                    dialog.sendAck(ackRequest);
                } else if (cseq.getMethod().equals(Request.CANCEL)) {
                    if (dialog.getState() == DialogState.CONFIRMED) {
                        Request byeRequest = dialog.createRequest(Request.BYE);
                        ClientTransaction ct = sipProvider
                                .getNewClientTransaction(byeRequest);
                        dialog.sendRequest(ct);
                    }
                } else if (cseq.getMethod().equals(Request.REGISTER)) {
                    Utils.updateStatusArea("registered");
                }
            } else if (response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED
                    || response.getStatusCode() == Response.UNAUTHORIZED) {
                AuthenticationHelper authenticationHelper =
                        ((SipStackExt) sipStack).getAuthenticationHelper(new AccountManagerImpl(), headerFactory);

                clientTransaction = authenticationHelper.handleChallenge(response, tid, sipProvider, 5);
                authenticationHeader = clientTransaction.getRequest().getHeader(AuthenticationHeader.AUTHORIZATION).toString();
                clientTransaction.sendRequest();

                invco++;
                Utils.updateLogArea(clientTransaction.getRequest().toString());

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // TestCase.fail("Exit JVM");
        }


    }


    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
        Utils.updateLogArea("Transaction Time out");
    }

    public void sendCancel() {
        try {
            Utils.updateStatusArea("Sending cancel");
            Request cancelRequest = clientTransaction.createCancel();
            ClientTransaction cancelTid = sipProvider
                    .getNewClientTransaction(cancelRequest);
            cancelTid.sendRequest();
            Utils.updateLogArea(cancelRequest.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void processIOException(IOExceptionEvent exceptionEvent) {
        Utils.updateLogArea("IOException happened for "
                + exceptionEvent.getHost() + " port = "
                + exceptionEvent.getPort());

    }

    public void processTransactionTerminated(
            TransactionTerminatedEvent transactionTerminatedEvent) {
        Utils.updateLogArea("Transaction terminated event recieved");
    }

    public void processDialogTerminated(
            DialogTerminatedEvent dialogTerminatedEvent) {
        Utils.updateLogArea("dialogTerminatedEvent");

    }

    public void init() throws IOException, InvalidArgumentException, TooManyListenersException, SipException {
        //read the configuration
       Utils.updateStatusArea("initialization");

        if (Utils.readConfigurationProperties().isEmpty()) {
            System.exit(0);
        }


        SipFactory sipFactory = null;
        sipStack = null;
        sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");

        Properties properties = new Properties();
    /*    properties.setProperty("javax.sip.OUTBOUND_PROXY", Utils.getPeerHostPort() + "/"
                + Utils.getTransport());*/
        properties.setProperty("javax.sip.STACK_NAME", "JSIP");


        sipStack = sipFactory.createSipStack(properties);


        headerFactory = sipFactory.createHeaderFactory();
        addressFactory = sipFactory.createAddressFactory();
        messageFactory = sipFactory.createMessageFactory();
        udpListeningPoint = sipStack.createListeningPoint("127.0.0.1",
                5060, "udp");
        sipProvider = sipStack.createSipProvider(udpListeningPoint);


        SipListenerImpl listener = this;
        sipProvider.addSipListener(listener);
        sipStack.start();
        fillRequestStaticContent();

        Utils.updateStatusArea("Ready");
        Utils.updateListeningPointArea("127.0.0.1:5060 UDP");


    }


    public void createRegister() throws ParseException, InvalidArgumentException, SipException {
        Utils.updateStatusArea("Registering");
        RequestContent requestContent = new RequestContent(
                this.contactHeader,
                this.udpListeningPoint,
                Utils.getSipFromName(),
                Utils.getSipFromAddress(),
                Utils.getSipFromDisplayName(),
                Utils.getTransport(),
                Utils.getPeerHostPort(),
                60,
                "12356"
        );
        Request registerRequest = RequestFactory.getRegisterRequest(requestContent);
        clientTransaction = sipProvider.getNewClientTransaction(registerRequest);
        clientTransaction.sendRequest();
        Utils.updateLogArea(registerRequest.toString());
    }

    public void createRegisterCancel() throws ParseException, InvalidArgumentException, SipException {
        Utils.updateStatusArea("Registration cancelling");
        RequestContent requestContent = new RequestContent(
                this.contactHeader,
                this.udpListeningPoint,
                Utils.getSipFromName(),
                Utils.getSipFromAddress(),
                Utils.getSipFromDisplayName(),
                Utils.getTransport(),
                Utils.getPeerHostPort(),
                0,
                "12356"
        );
        Request registerCancelRequest = RequestFactory.getRegisterCancelRequest(requestContent, authenticationHeader);
        clientTransaction = sipProvider.getNewClientTransaction(registerCancelRequest);
        clientTransaction.sendRequest();
        Utils.updateLogArea(registerCancelRequest.toString());
        Utils.updateStatusArea("Registration cancelled");


    }

    public void stop() {
        if (sipStack != null)
            sipStack.stop();
    }

    /*
     *
     * */
    private void fillRequestStaticContent() {

        RequestContent.setAddressFactory(addressFactory);
        RequestContent.setHeaderFactory(headerFactory);
        RequestContent.setMessageFactory(messageFactory);
        RequestContent.setSipProvider(sipProvider);
        RequestContent.setSipStack(sipStack);


    }
}
