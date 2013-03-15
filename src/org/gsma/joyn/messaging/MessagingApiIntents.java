/*
 * Copyright 2013, France Telecom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gsma.joyn.messaging;

import java.lang.String;

/**
 * Interface MessagingApiIntents.
 */
public interface MessagingApiIntents {
    /**
     * Constant FILE_TRANSFER_INVITATION.
     */
    public static final String FILE_TRANSFER_INVITATION = "org.gsma.joyn.messaging.FILE_TRANSFER_INVITATION";

    /**
     * Constant CHAT_INVITATION.
     */
    public static final String CHAT_INVITATION = "org.gsma.joyn.messaging.CHAT_INVITATION";

    /**
     * Constant CHAT_SESSION_REPLACED.
     */
    public static final String CHAT_SESSION_REPLACED = "org.gsma.joyn.messaging.CHAT_SESSION_REPLACED";

} // end MessagingApiIntents