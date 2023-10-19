# SMS-Leaking
<p align="left">
	<a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/made%20with-kotlin-blueviolet"></a>
	<a href="#"><img src="https://img.shields.io/badge/platform-android-blueviolet"></a>
</p>

This application is a small demonstration of an SMS intercept made in Kotlin to send to the target's server 

- [Starting](#starting)
- [Important To Know](#important-to-know)

# Starting

Running the code and performing SMS interception

![IMG](img/sms.png)

# Important To Know

- The code uses the ```Kotlin``` programming language
- A coroutine is created for the http request
- The intercept uses a BroadcastReceiver call and filters actions from ```android.provider.Telephony.SMS_RECEIVED```
- If you use it, remember to change the IP in the kotlin file in the ```Request function```
