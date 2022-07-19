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
 ### Snackbar:
Initializing `UISnackBar` by passing activity context `this`. If it's a fragment use `context as Activity` as a constructor parameter.

```
val snackBar = UISnackBar(this).apply {
            backgroundColorRes(R.color.purple_500)
            cornerRadius(7F)
            padding(10)
            textTypeface(Typeface.DEFAULT_BOLD)
            duration(Snackbar.LENGTH_SHORT)
            textColorRes(R.color.white)
            message("Test UISnackBar")
        }
```
To show the Snackbar just call the below line
```
snackBar.show()
```

### Progressbar:
To show custom progressbar call the below method. Pass activity context `this`, if it's a fragment use `context as Activity` as a constructor parameter.

```
UIProgressBar.showProgressBar(
            this,
            ContextCompat.getColor(context as Activity, R.color.purple_200))
 ```
 To hide progress dialog
 
 ```
 UIProgressBar.hideProgressBar()
 ```
 **NOTE:** To avoid memory leak, it is highly recommendable to call `hideProgressBar()` inside `onDestroy()` if it's activity and `onDestroyView()` is it's fragment.
 
### Alert Dialog:
To create a custom alert dialog use `UIAlertDialog` class and call the builder method followed by required title, body, icon and button text. If it's a fragment use `context as Activity` as a constructor parameter. `onPositive` & `onNegative` are anonymous callback methods.

```
 UIAlertDialog.build(this)
            .title("Alert Dialog Title")
            .body("Alert Dialog Body")
            .position(UIAlertDialog.POSITIONS.CENTER)
            .icon(R.drawable.custom_icon_tick)
            .onPositive("OK") {
                Log.d("TAG", "Positive Clicked")
            }.onNegative(
                "Cancel",
                buttonBackgroundColor = R.drawable.custom_rounded_white,
                textColor = ContextCompat.getColor(context as Activity, android.R.color.black)
            )
```

### RecyclerView:
This custom recyclerview will accept any kind of layout and data that we want to show in the list. With this there is not need to implement the adapter or the viewholder class. All we need is enable databinding in app gradle file, pass item layout and pass list of data.

```
val data: List<RecyclerItem> = listOfData.map {
            it.toRecyclerItem()
        }
setRecyclerViewItems(recyclerView, data)
```

```
fun YourDataClass.toRecyclerItem() = RecyclerItem(
        data = this,
        layoutId = R.layout.yout_item_layout,
        variableId = BR.sampleData)
```
### Toast:
With this `UIToast`, you can create an attractive toast with icon and different background as well. Pass activity context `this` as a constructor parameter, else use `context as Activity` if it's a fragment. 

```
 UIToast.showToast(
            context as Activity, "Test Toast",
            Toast.LENGTH_LONG, R.drawable.your_icon, R.color.white, R.color.black
        ).show()
```

### Date Picker/Calendar:
With `UICalendarView`, we can create an interactive calendar with different background color, current day color and selected day color.

```
private fun showCalendar() {
        val dialog = Dialog(activity as Activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.your_calendar_layout)

        val currentCalendar = Calendar.getInstance(Locale.getDefault())
        val calendarView = dialog.findViewById<UICalendarView>(R.id.your_calendarview)

        calendarView.apply {
            //Show monday as first date of week
            initDayOfWeek = Calendar.MONDAY

            //Show/hide overflow days of a month
            isOverflowDateVisible = false

            calendarBackgroundColor =
                (ContextCompat.getColor(activity as Activity, R.color.white))

            calendarHeaderBackgroundColor =
                (ContextCompat.getColor(activity as Activity, android.R.color.holo_purple))
            calendarTitleTextColor = (ContextCompat.getColor(activity as Activity, R.color.black))
            weekLayoutBackgroundColor =
                (ContextCompat.getColor(activity as Activity, R.color.white))
            dayOfWeekTextColor = (ContextCompat.getColor(activity as Activity, R.color.black))
            dayOfMonthTextColor = (ContextCompat.getColor(activity as Activity, R.color.black))
            disabledDayBackgroundColor =
                (ContextCompat.getColor(activity as Activity, R.color.disabled_grey_light))
            disabledDayTextColor =
                (ContextCompat.getColor(activity as Activity, R.color.day_disabled_text_color))
            selectedDayBackground =
                (ContextCompat.getColor(activity as Activity, android.R.color.holo_green_dark))
            selectedDayTextColor = (ContextCompat.getColor(activity as Activity, R.color.white))
            currentDayOfMonth =
                (ContextCompat.getColor(activity as Activity, R.color.sky_blue))

            //call refreshCalendar to update calendar the view
            refreshCalendar(currentCalendar)
        }.setUICalendarListener(object : UICalendarListener {

            override fun onDateSelected(date: Date) {
                val df = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                Toast.makeText(activity as Activity, df.format(date), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onLongClick(date: Date) {
                val df = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                Toast.makeText(activity as Activity, df.format(date), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onMonthChanged(date: Date) {
                val df = SimpleDateFormat("MM-yyyy", Locale.ENGLISH)
                Toast.makeText(activity as Activity, df.format(date), Toast.LENGTH_SHORT)
                    .show()
            }
        })
        dialog.show()
 ```

### Time Picker:
To create a customized time picker, use `UITimePicker`. End developer can send their own style/theme, time and title.

**Note:** For activity pass `supportFragmentManager` and for fragment pass `parentFragmentManager` as paremeter in show method.

```
private fun showTime() {
        val materialTimePicker = UITimePicker().materialTimeBuilder(
            R.style.Theme_DemoAppsForSharedComponent_TimePicker
        ).build()

        materialTimePicker.show(parentFragmentManager, "UIComponentFragment")
        
        materialTimePicker.addOnPositiveButtonClickListener {
            val pickedHour: Int = materialTimePicker.hour
            val pickedMinute: Int = materialTimePicker.minute

            UIToast.showToast(
                context as Activity, "Selected $pickedHour:$pickedMinute",
                Toast.LENGTH_LONG, R.drawable.custom_icon_tick, R.color.sky_blue, R.color.white
            ).show()
        }
    }
```
