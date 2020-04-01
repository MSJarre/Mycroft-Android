/*
 *  Copyright (c) 2017. Mycroft AI, Inc.
 *
 *  This file is part of Mycroft-Android a client for Mycroft Core.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package mycroft.ai

import android.util.Log
import mycroft.ai.utils.User

import org.json.JSONException
import org.json.JSONObject

/**
 * Specialised Runnable that parses the [JSONObject] in [.message]
 * when run. If it contains a [Utterance] object, the callback
 * defined in [the constructor][.MessageParser] will
 * be [called][SafeCallback.call] with that object as a parameter.
 *
 *
 * TODO: Add error-aware callback for cases where the message is malformed.
 *
 *
 * @author Philip Cohn-Cort
 */
internal class MessageParser(private val message: String,
                             private val callback: SafeCallback<Utterance>) : Runnable {
    private val logTag = "MessageParser"
    private var response = false

    override fun run() {
        // new format
        // {"data": {"utterance": "There are only two hard problems in Computer Science: cache invalidation, naming things and off-by-one-errors."}, "type": "speak", "context": null}
        try {
            val obj = JSONObject(message)
            if (obj.getJSONObject("context").has("userid") && obj.getJSONObject("context").getString("userid") == User.UserName) {
                if (obj.getJSONObject("data").has("expect_response") &&  obj.getJSONObject("data").getString("expect_response") == "true") {
                    response = true
                }
                if (obj.optString("type") == "speak") {
                    val ret = Utterance(obj.getJSONObject("data").getString("utterance"), UtteranceFrom.MYCROFT, response)
                    callback.call(ret)
                }
            }
        } catch (e: JSONException) {
            Log.e(logTag, "The response received did not conform to our expected JSON format.", e)
        }
    }
}
