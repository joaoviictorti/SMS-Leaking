# SMS-Leaking
<p align="left">
	<a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/made%20with-kotlin-blueviolet"></a>
	<a href="#"><img src="https://img.shields.io/badge/platform-osx%2Flinux%2Fwindows-blueviolet"></a>
</p>

This application is a small demonstration of an SMS intercept made in Kotlin to send to the target's server 

- [Starting](#starting)
- [Features](#features)

# Starting

Running the code and performing SMS interception

![IMG](img/sms.png)

# Features

- The code uses the Kotlin programming language
- A coroutine is created for the http request
- The intercept uses a BroadcastReceiver call and filters actions from android.provider.Telephony.SMS_RECEIVED