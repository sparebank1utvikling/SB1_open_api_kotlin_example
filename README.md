# sb1_open_api_example
open api example code

# TL;DR
- Create a "client" at the [Developer portal](https://developer.sparebank1.no/#/documentation/gettingstarted) set `Redirect Uri` to http://localhost:8080
- Rename/copy the file `secrets.properties.example` to `secrets.properties`
- Copy client id & client secret into `secrets.properties`
- Start the app
- Go back to the [Developer portal](https://developer.sparebank1.no/#/documentation/gettingstarted), and copy the Http request from step 2, and open it. 
- Log in with Bank ID

The app now has stored both access token, and refresh token. And it will refresh the access token every time a call from the adapter fails with 401. 

# SpareBank 1 open api documentation
The documentation can be found here:

https://developer.sparebank1.no/#/

