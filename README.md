# AppServiceComponents
This is a library which consists of Re usable application components for the reducing the boiler plate code while developing the apps.

# End User
Developers of the project, who will be coding the simislar boiler late code which were identified as reusable components.

# The Overview :
 # Device Info:
 - DeviceInfo Module in the this aar gives Fun's such as :
 - 1.getDeviceInfo() : returns DeviceInfo data class instance with all the data of device needed like serial,version,
			device model, device build number, tyep,user, sdk, board, host, finegerprint,display, hardware etc.
 - 2.getInstalledApps(): returns list of installed apps in the device.
 - 3.getRamDetails(): The Ram Details.
 - 4.getBatteryInfo(): The battery status in percentage(charge left).

 # Network Manager:
 - 1.Network Manager Component is used to communicate with server using REST API.
 - 2.Retrofit and Volley third party library are used to implement REST API in application.
 - 3.This component supports both library Retrofit and Volley for api integration. (Retrofit library is recommanded rather than Volley)
 - 4.This component also use ConnectivityManager for check device internet connection. If there is no internet connection found then Snackbar will
   show with ENABLE button at bottom of the device screen.
 - 5.This component supports caching functionality in case of poor internet connectivity. 
  
 # Encryption/Decryption:
 - 1.Implementation AES Encryption algorithm
 - 2.Implementation hMacSha256 Algoritham
 - 3.Implementation Base64Encode
 - 4.Implementation md5Digest
 - 5.Implementation md5Filechecksum
 - 6.Implementation Base64Decode
 
 # Elastic Search:
 - 1.Elasticsearch component allows you to search huge volumes of data quickly.
 - 2.SearchEnginActivity is added to make a network call to search the data and get the filtered data.
 - 3.Dynamic UI is added for search activity.
 - 4.TODO :A search api can be used by passing the required params , the result will be data that is filtered data in search deta from the api.
 
 # Camera:
 - 1.This component is developed to use android camera feature to capture the photo, video or both.
 - 2.This supports both primary (main camera sensor on back) and secondary camera (selfie camera).
 - 3.This allows user to take a photo or video from camera and returns the uri (path of the file from storage) so It can be further used in app as needed. 
 - 4.TODO :Crop the image and send the croped image.
 
 # Compass:
 - This component will have the multiple compass views.
 - The developer can access them as needed.
 
 # Barcode:
 - TODO :
 - The Barcode scanner is an important component, the ML kit of android is used for scanning the barcode.
 - The results are retuned back to the user in different forms
 
 # UI Utiltiy Components:
 - Custom Alert Dialog
 - Custom Toasts messages
 - Custom Progress bars
 - CardViews
