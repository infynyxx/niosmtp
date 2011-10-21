/**
* Licensed to niosmtp developers ('niosmtp') under one or more
* contributor license agreements. See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* niosmtp licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package me.normanmaurer.niosmtp.client.callback;

import me.normanmaurer.niosmtp.SMTPResponse;
import me.normanmaurer.niosmtp.client.impl.SMTPDeliveryFutureImpl;
import me.normanmaurer.niosmtp.transport.SMTPClientSession;

/**
 * {@link AbstractResponseCallback} implementation which should get sub-classed by implementations which may be used within the <code>PIPELINING</code>
 * context. It makes sure the callbacks will not get executed if {@link SMTPDeliveryFutureImpl}
 * @author Maurer
 *
 */
public abstract class AbstractPipelineResponseCallback extends AbstractResponseCallback{

    @Override
    public final void onResponse(SMTPClientSession session, SMTPResponse response) {
        if (session.getAttributes().containsKey(PIPELINING_ACTIVE_KEY)) {
            SMTPDeliveryFutureImpl future = (SMTPDeliveryFutureImpl) session.getAttributes().get(FUTURE_KEY);

            // Check if the future is complete if not execute the callback
            if (!future.isDone()) {
                onResponseInternal(session, response);
            }
        } else {
            onResponseInternal(session, response);
        }
    }

    protected abstract void onResponseInternal(SMTPClientSession session, SMTPResponse response);
}
