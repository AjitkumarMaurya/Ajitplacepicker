[![](https://jitpack.io/v/AjitkumarMaurya/Ajitplacepicker.svg)](https://jitpack.io/#AjitkumarMaurya/Ajitplacepicker)

If you're here looking for a place picker you have probably read this:

Google Place Picker alternative

## Download

Add Jitpack in your root build.gradle at the end of repositories:

```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

Step 2. Add the dependency

```gradle
    dependencies {
            // Places library
            implementation 'com.google.android.libraries.places:places:2.0.0'
            // PING Place Picker
            implementation ('com.github.AjitkumarMaurya:Ajitplacepicker:v10.0') {
                               exclude group: 'com.google.android.gms'
                               exclude group: 'androidx.appcompat'
            }
          
            	
    }
```

## Setup

1. Add Google Play Services to your project
   - [How to](https://developers.google.com/android/guides/setup)
2. Sign up for API keys - [How to](https://developers.google.com/places/android-sdk/signup)
3. Add the Android API key to your **AndroidManifest** file as in
   the [sample project](https://github.com/rtchagas/pingplacepicker/blob/master/sample/src/main/AndroidManifest.xml#L15)
   .
4. Optional but strongly recommended to enable R8 in
   you *[gradle.properties](https://github.com/rtchagas/pingplacepicker/blob/master/gradle.properties#L12)*
   file

### - Kotlin

```kotlin
    private fun showPlacePicker() {
    val builder = PingPlacePicker.IntentBuilder()
    builder.setAndroidApiKey("YOUR_ANDROID_API_KEY")
        .setMapsApiKey("YOUR_MAPS_API_KEY")

    // If you want to set a initial location rather then the current device location.
    // NOTE: enable_nearby_search MUST be true.
    // builder.setLatLng(LatLng(37.4219999, -122.0862462))

    try {
        val placeIntent = pingBuilder.build(this)
        startActivityForResult(placeIntent, REQUEST_PLACE_PICKER)
    } catch (ex: Exception) {
        toast("Google Play Services is not Available")
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if ((requestCode == REQUEST_PLACE_PICKER) && (resultCode == Activity.RESULT_OK)) {
        val place: Place? = PingPlacePicker.getPlace(data!!)
        toast("You selected: ${place?.name}")
        toast("You selected: ${data.getData().getStringExtra("addressStr")}")
        toast("You selected: ${place?.name}")
    }
}
```

### - Java

```java
    private void showPlacePicker(){
        PingPlacePicker.IntentBuilder builder=new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("YOUR_ANDROID_API_KEY")
        .setMapsApiKey("YOUR_MAPS_API_KEY");

        // If you want to set a initial location rather then the current device location.
        // NOTE: enable_nearby_search MUST be true.
        // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

        try{
        Intent placeIntent=builder.build(getActivity());
        startActivityForResult(placeIntent,REQUEST_PLACE_PICKER);
        }
        catch(Exception ex){
        // Google Play services is not available... 
        }
        }

@Override
public void onActivityResult(int requestCode,int resultCode,Intent data){
        if((requestCode==REQUEST_PLACE_PICKER)&&(resultCode==RESULT_OK)){
        Place place=PingPlacePicker.getPlace(data);
        if(place!=null){
        Toast.makeText(this,"You selected the place: "+place.getName(),Toast.LENGTH_SHORT).show();
        }
        }
        }
```

## Theming

For day/light theme:

- `res/values/colors.xml`

```xml

<!-- Toolbar color, places icons, text on top of primary surfaces -->
<color name="colorPrimary">@color/material_teal500</color><color name="colorPrimaryDark">
@color/material_teal800
</color><color name="colorOnPrimary">@color/material_white</color>

    <!-- Accent color in buttons and actions -->
<color name="colorSecondary">@color/material_deeporange500</color><color name="colorSecondaryDark">
@color/material_deeporange800
</color><color name="colorOnSecondary">@color/material_white</color>

    <!-- Main activity background -->
<color name="colorBackground">@color/material_grey200</color><color name="colorOnBackground">
@color/material_black
</color>

    <!-- Cards and elevated views background -->
<color name="colorSurface">@color/material_white</color><color name="colorOnSurface">
@color/material_black
</color>

    <!-- Text colors -->
<color name="textColorPrimary">@color/material_on_surface_emphasis_high_type</color><color
name="textColorSecondary">@color/material_on_surface_emphasis_medium
</color>

<color name="colorMarker">@color/material_deeporange400</color><color name="colorMarkerInnerIcon">
@color/material_white
</color>

```

For night/dark theme:

- `res/values-night/colors.xml`

```xml

    <color name="colorPrimary">@color/material_teal300</color>
    <!-- Let the primary dark color as the surface color to not colorfy the status bar -->
    <color name="colorPrimaryDark">@color/colorSurface</color>
    <color name="colorOnPrimary">@color/material_black</color>

    <color name="colorSecondary">@color/material_deeporange200</color>
    <color name="colorSecondaryDark">@color/material_deeporange300</color>
    <color name="colorOnSecondary">@color/material_black</color>

    <color name="colorBackground">@color/colorSurface</color>
    <color name="colorOnBackground">@color/colorOnSurface</color>

    <color name="colorSurface">#202125</color>
    <color name="colorOnSurface">@color/material_white</color>

    <color name="textColorPrimary">@color/material_on_surface_emphasis_high_type</color>
    <color name="textColorSecondary">@color/material_on_surface_emphasis_medium</color>

    <color name="colorMarker">@color/material_deeporange200</color>
    <color name="colorMarkerInnerIcon">@color/colorSurface</color>

git tag -a v11 -m "update v11"                                                                                  
git push origin v11
