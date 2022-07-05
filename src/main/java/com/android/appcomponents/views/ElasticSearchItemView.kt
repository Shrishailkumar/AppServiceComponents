package com.elasticsearch

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
    var count = 0

    constructor(value: JSONObject) {
        this.value = value
    }

    /*fun addView(context: Context): View {
        var view: View? = null
        var relativeLayout = LinearLayout(context)
        var layoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParam.setMargins(8,8,8,8)

        relativeLayout.orientation = LinearLayout.VERTICAL
        relativeLayout.layoutParams = layoutParam
        val keysItr = value?.keys() as Iterator<*>
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            val value: Any = value?.get(key.toString())!!
            if (value is String) {
                if (!isNumeric(value.toString()) && !value.toString().contains("http")
                    && !value.toString().contains("https")
                    && !Patterns.WEB_URL.matcher(value).matches() &&
                    !Pattern.compile("[!@#\$%&*()_.+=|<>?{}\\\\[\\\\]~-]").matcher(value).find()
                ) {
                    count++;
                    var textView = TextView(context)
                    if(count == 1){
                        textView.typeface = Typeface.DEFAULT_BOLD
                        textView.textSize = 16F
                        textView.setTextColor(Color.BLACK)
                    }else if(count == 2){
                        textView.typeface = Typeface.SANS_SERIF
                        textView.textSize = 14F
                        textView.setTextColor(Color.BLACK)
                    }else{
                        textView.typeface = Typeface.SANS_SERIF
                        textView.textSize = 12F
                        textView.setTextColor(Color.GRAY)
                    }
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
                        Glide.with(context)
                            .load("https://source.unsplash.com/user/c_v_r/400x200")
                            .into(imageView!!)
                        relativeLayout.addView(imageView)
                        imageLoaded = true
                    }

                }
            }
        }
        return relativeLayout!!
    }
*/
    fun addView(context: Context): View {
        var view: View? = null
// var cardView = CardView(context)
        var relativeLayout = LinearLayout(context)
        var layoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layoutParam.setMargins(5,5,5,5)
//Card Cutome View Design
// cardView.layoutParams = layoutParam
// cardView.radius = 5.0F
// cardView.setPadding(0,0,0,5)
// cardView.setCardBackgroundColor(Color.WHITE)
// cardView.maxCardElevation = 6F

        relativeLayout.orientation = LinearLayout.VERTICAL
        relativeLayout.layoutParams = layoutParam
        val keysItr = value?.keys() as Iterator<*>
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            val value: Any = value?.get(key.toString())!!
            if (value is String) {
                if (!isNumeric(value.toString()) && !value.toString().contains("http")
                    && !value.toString().contains("https")
                    && !Patterns.WEB_URL.matcher(value).matches() &&
                    !Pattern.compile("[!@#\$%&*()_.+=|<>?{}\\\\[\\\\]~-]").matcher(value).find()
                ) {
                    count++;
                    var textView = TextView(context)
                    if(count == 1){
                        textView.typeface = Typeface.DEFAULT_BOLD
                        textView.textSize = 14F
                        textView.setTextColor(Color.BLACK)
                    }else if(count == 2){
                        textView.typeface = Typeface.SANS_SERIF
                        textView.textSize = 14F
                        textView.setTextColor(Color.BLACK)
                    }else{
                        textView.typeface = Typeface.SANS_SERIF
                        textView.textSize = 12F
                        textView.setTextColor(Color.GRAY)
                    }
                    textView.text = value.toString()
                    relativeLayout.addView(textView)

                }
                if (value.toString().contains("http")
                    && value.toString().contains("https")
                ) {
                    if (!imageLoaded){
                        var imageView = ImageView(context)
//                        imageView.scaleType = ImageView.ScaleType.FIT_XY
                        val params = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            240
                        )
                        imageView!!.layoutParams = params
// imageView?.setImageResource(R.drawable.ic_launcher_background)
                        Glide.with(context)
                            .load("https://source.unsplash.com/user/c_v_r/400x200")
                            .into(imageView!!)
                            relativeLayout.addView(imageView)
// cardView.addView(relativeLayout)
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

}