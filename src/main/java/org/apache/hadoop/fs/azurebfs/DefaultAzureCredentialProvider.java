package org.apache.hadoop.fs.azurebfs;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.apache.hadoop.fs.azurebfs.extensions.CustomTokenProviderAdaptee;

import java.io.IOException;
import java.util.Date;

public class DefaultAzureCredentialProvider implements CustomTokenProviderAdaptee {
    private static final String SCOPE = "https://storage.azure.com/.default";

    private AccessToken token = null;
    private DefaultAzureCredential credential = null;

    @Override
    public void initialize(org.apache.hadoop.conf.Configuration configuration, String s) throws IOException {
        this.credential = new DefaultAzureCredentialBuilder().build();
    }

    @Override
    public String getAccessToken() throws IOException {
        TokenRequestContext tokenRequestContext = new TokenRequestContext();
        tokenRequestContext.addScopes(SCOPE);
        token = credential.getToken(tokenRequestContext).block();
        return token.getToken();
    }

    @Override
    public Date getExpiryTime() {
        if (token == null) {
            return null;
        }
        return Date.from(token.getExpiresAt().toInstant());
    }
}
