package com.dyakushev.model.auth;


import com.dyakushev.model.util.Utils;
import gov.nist.javax.sip.clientauthutils.AccountManager;
import gov.nist.javax.sip.clientauthutils.UserCredentials;

import javax.sip.ClientTransaction;

public class AccountManagerImpl implements AccountManager {
    public UserCredentials getCredentials(ClientTransaction challengedTransaction, String realm) {
        return new UserCredentialsImpl(Utils.getSipUsername(), Utils.getSipDomain(), Utils.getSipPassword());
    }
}
