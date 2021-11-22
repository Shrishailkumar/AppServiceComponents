package com.elasticsearch

import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.util.regex.Pattern


class ElasticSearchItemView {
    var value: JSONObject? = null
    var imageLoaded = false

    constructor(value: JSONObject) {
        this.value = value
    }

    fun addView(context: Context): View {
        var view: View? = null
        var relativeLayout = LinearLayout(context)
        var layoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        relativeLayout.orientation = LinearLayout.VERTICAL
        relativeLayout.layoutParams = layoutParam
        val keysItr: Iterator<String> = value?.keys() as Iterator<String>
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            val value: Any = value?.get(key)!!
            if (value is String) {
                if (!isNumeric(value.toString()) && !value.toString().contains("http")
                    && !value.toString().contains("https")
                    && !Patterns.WEB_URL.matcher(value).matches() &&
                    !Pattern.compile("[!@#\$%&*()_.+=|<>?{}\\\\[\\\\]~-]").matcher(value).find()
                ) {
                    var textView = TextView(context)
                    textView.text = value.toString()
                    relativeLayout.addView(textView)
                }
                if (value.toString().contains("http")
                    && value.toString().contains("https")
                ) {
                    if (!imageLoaded){
                        var imageView = ImageView(context)
                        val params = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            240
                        )
                        imageView!!.layoutParams = params
                        // imageView?.setImageResource(R.drawable.ic_launcher_background)
                        Glide.with(context).load("https://source.unsplash.com/user/c_v_r/100x100").into(
                            imageView!!
                        )
                        relativeLayout.addView(imageView)
                        imageLoaded = true
                    }

                }
            }
        }
        return relativeLayout!!
    }

    fun isNumeric(str: String): Boolean {
        return try {
            str.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    /* fun testImage (url :String) : Boolean {
         return try {
             val image: BufferedImage = ImageIO.read(URL(url))
             //BufferedImage image = ImageIO.read(new URL("http://someimage.jpg"));
             image != null
         } catch (e: MalformedURLException) {
             // TODO Auto-generated catch block
             System.err.println("URL error with image")
             e.printStackTrace()
             false
         } catch (e: IOException) {
             System.err.println("IO error with image")
             // TODO Auto-generated catch block
             e.printStackTrace()
             false
         }
         return false
     }*/

}