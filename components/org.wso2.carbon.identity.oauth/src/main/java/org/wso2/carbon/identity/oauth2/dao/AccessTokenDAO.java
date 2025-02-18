/*
 *
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 * /
 */

package org.wso2.carbon.identity.oauth2.dao;

import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.model.AccessTokenDO;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Access token related data access interface.
 */
public interface AccessTokenDAO {

    void insertAccessToken(String accessToken, String consumerKey, AccessTokenDO accessTokenDO,
                           String userStoreDomain) throws IdentityOAuth2Exception;

    boolean insertAccessToken(String accessToken, String consumerKey,
                              AccessTokenDO newAccessTokenDO, AccessTokenDO existingAccessTokenDO,
                              String rawUserStoreDomain) throws IdentityOAuth2Exception;

    AccessTokenDO getLatestAccessToken(String consumerKey, AuthenticatedUser authzUser, String userStoreDomain,
                                       String scope, boolean includeExpiredTokens) throws IdentityOAuth2Exception;

    /**
     * Get latest access token.
     *
     * @param consumerKey consumer key.
     * @param authzUser authorized user.
     * @param userStoreDomain user store domain.
     * @param scope scope.
     * @param tokenBindingReference token binding reference.
     * @param includeExpiredTokens include expired tokens.
     * @return latest access token.
     * @throws IdentityOAuth2Exception in case of failure.
     */
    default AccessTokenDO getLatestAccessToken(String consumerKey, AuthenticatedUser authzUser, String userStoreDomain,
            String scope, String tokenBindingReference, boolean includeExpiredTokens) throws IdentityOAuth2Exception {

        return getLatestAccessToken(consumerKey, authzUser, userStoreDomain, scope, includeExpiredTokens);
    }

    /**
     * Get tokenId by binding reference.
     * @param bindingRef BindingRef.
     * @return TokenId.
     * @throws IdentityOAuth2Exception
     */
    default Set<String> getTokenIdBySessionIdentifier(String bindingRef) throws IdentityOAuth2Exception {

        return null;
    }

    /**
     * Store tokenId to sessioncontext identifier mapping.
     * @param sessionIdentifier SessionIdentifier.
     * @param tokenId TokenId.
     * @param tenantId TenantId.
     * @throws IdentityOAuth2Exception
     */
    default void storeTokenToSessionMapping(String sessionIdentifier, String tokenId, int tenantId)
            throws IdentityOAuth2Exception {

    }

    Set<AccessTokenDO> getAccessTokens(String consumerKey, AuthenticatedUser userName,
                                       String userStoreDomain, boolean includeExpired) throws IdentityOAuth2Exception;

    AccessTokenDO getAccessToken(String accessTokenIdentifier, boolean includeExpired) throws IdentityOAuth2Exception;

    Set<String> getAccessTokensByUser(AuthenticatedUser authenticatedUser) throws IdentityOAuth2Exception;

    default Set<AccessTokenDO> getAccessTokensByUserForOpenidScope(AuthenticatedUser authenticatedUser)
            throws IdentityOAuth2Exception {

        return null;
    }

    Set<String> getActiveTokensByConsumerKey(String consumerKey) throws IdentityOAuth2Exception;

    Set<AccessTokenDO> getActiveAcessTokenDataByConsumerKey(String consumerKey) throws IdentityOAuth2Exception;

    Set<AccessTokenDO> getAccessTokensByTenant(int tenantId) throws IdentityOAuth2Exception;

    Set<AccessTokenDO> getAccessTokensOfUserStore(int tenantId, String userStoreDomain) throws
            IdentityOAuth2Exception;

    /**
     * This method is to revoke specific tokens where tokens should be plain text tokens.
     *
     * @param tokens tokens that needs to be revoked
     * @throws IdentityOAuth2Exception if failed to revoke the access token
     */
    void revokeAccessTokens(String[] tokens) throws IdentityOAuth2Exception;

    void revokeAccessTokensInBatch(String[] tokens) throws IdentityOAuth2Exception;

    void revokeAccessTokensIndividually(String[] tokens) throws IdentityOAuth2Exception;

    /**
     * This method is to revoke specific tokens where tokens can be plain text tokens or hashed tokens. Hashed tokens
     * can be reached here from internal calls such as from any listeners ex: IdentityOathEventListener etc. We need
     * to differentiate this types of internal calls hence these calls retrieved the tokens from the DB and then try
     * to revoke it.
     * When the Token Hashing Feature enabled, the token which is retrieve from the DB will be a hashed token. Hence
     * we don't need to hash it again.
     *
     * @param tokens        Tokens that needs to be revoked.
     * @param isHashedToken Indicate provided token is a hashed token or plain text token.
     * @throws IdentityOAuth2Exception if failed to revoke the access token
     */
    default void revokeAccessTokens(String[] tokens, boolean isHashedToken) throws IdentityOAuth2Exception {
    }

    /**
     * Revoke the access token(s) as a batch.
     *
     * @param tokens        Token that needs to be revoked.
     * @param isHashedToken Given token is hashed token or plain text.
     * @throws IdentityOAuth2Exception
     */
    default void revokeAccessTokensInBatch(String[] tokens, boolean isHashedToken) throws IdentityOAuth2Exception {
    }

    /**
     * Revoke the access token(s) individually.
     *
     * @param tokens        Token that needs to be revoked.
     * @param isHashedToken Given token is hashed token or plain text.
     * @throws IdentityOAuth2Exception
     */
    default void revokeAccessTokensIndividually(String[] tokens, boolean isHashedToken) throws IdentityOAuth2Exception {
    }

    void revokeAccessToken(String tokenId, String userId) throws IdentityOAuth2Exception;

    void invalidateAndCreateNewAccessToken(String oldAccessTokenId, String tokenState, String consumerKey,
                                           String tokenStateId, AccessTokenDO accessTokenDO,
                                           String userStoreDomain) throws IdentityOAuth2Exception;

    default void invalidateAndCreateNewAccessToken(String oldAccessTokenId, String tokenState, String consumerKey,
                                                   String tokenStateId, AccessTokenDO accessTokenDO,
                                                   String userStoreDomain, String grantType)
            throws IdentityOAuth2Exception {
        invalidateAndCreateNewAccessToken(oldAccessTokenId, tokenState, consumerKey, tokenStateId, accessTokenDO,
                userStoreDomain);
    }

    void updateUserStoreDomain(int tenantId, String currentUserStoreDomain,
                               String newUserStoreDomain) throws IdentityOAuth2Exception;

    String getTokenIdByAccessToken(String token) throws IdentityOAuth2Exception;

    List<AccessTokenDO> getLatestAccessTokens(String consumerKey, AuthenticatedUser authzUser,
                                              String userStoreDomain, String scope,
                                              boolean includeExpiredTokens, int limit) throws IdentityOAuth2Exception;

    /**
     * Get latest access tokens.
     *
     * @param consumerKey consumer key.
     * @param authzUser authorized user.
     * @param userStoreDomain user store domain.
     * @param scope scope.
     * @param tokenBindingReference token binding reference.
     * @param includeExpiredTokens include expired tokens.
     * @param limit limit.
     * @return list of latest access tokens.
     * @throws IdentityOAuth2Exception in case of failure.
     */
    default List<AccessTokenDO> getLatestAccessTokens(String consumerKey, AuthenticatedUser authzUser,
            String userStoreDomain, String scope, String tokenBindingReference, boolean includeExpiredTokens, int limit)
            throws IdentityOAuth2Exception {

        return getLatestAccessTokens(consumerKey, authzUser, userStoreDomain, scope, includeExpiredTokens, limit);
    }

    /**
     * Update access token to the given state.
     *
     * @param tokenId         ID of the access token to update the state.
     * @param tokenState      state to update.
     * @deprecated to use {{@link #updateAccessTokenState(String, String, String)}}
     * @throws IdentityOAuth2Exception
     */
    void updateAccessTokenState(String tokenId, String tokenState) throws IdentityOAuth2Exception;

    /**
     * Update access token to the given state.
     *
     * @param tokenId         ID of the access token to update the state.
     * @param tokenState      state to update.
     * @param grantType      state to update.
     * @throws IdentityOAuth2Exception
     */
    default void updateAccessTokenState(String tokenId, String tokenState, String grantType)
            throws IdentityOAuth2Exception {
        updateAccessTokenState(tokenId, tokenState);
    }

    default Set<AccessTokenDO> getActiveTokenSetWithTokenIdByConsumerKeyForOpenidScope(String consumerKey)
            throws IdentityOAuth2Exception {

        return Collections.emptySet();
    }

    /**
     * Retrieve the active access tokens of a given user with a given access token binding reference.
     *
     * @param user       authenticated user
     * @param bindingRef access token binding reference
     * @return set of active access objects
     * @throws IdentityOAuth2Exception if the retrieval process fails
     */
    default Set<AccessTokenDO> getAccessTokensByBindingRef(AuthenticatedUser user, String bindingRef) throws
            IdentityOAuth2Exception {

        return null;
    }

    /**
     * Retrieve the active access tokens with a given access token binding reference.
     *
     * @param bindingRef access token binding reference
     * @return set of active access objects
     * @throws IdentityOAuth2Exception if the retrieval process fails
     */
    default Set<AccessTokenDO> getAccessTokensByBindingRef(String bindingRef) throws IdentityOAuth2Exception {

        return Collections.emptySet();
    }

    /**
     * Retrieve the access token for a given token id.
     *
     * @param tokenId token id.
     * @return access token.
     * @throws IdentityOAuth2Exception if the retrieval process fails.
     */
    default String getAccessTokenByTokenId(String tokenId) throws IdentityOAuth2Exception {

        return null;
    }

    /**
     * Updates whether the token is issued for a consent required grant.
     *
     * @param tokenId ID of the token.
     * @param isConsentedGrant Grant type which the corresponding token is issued.
     * @throws IdentityOAuth2Exception If there are any failures in update.
     */
    default void updateTokenIsConsented(String tokenId, boolean isConsentedGrant)
            throws IdentityOAuth2Exception {
    }
}
