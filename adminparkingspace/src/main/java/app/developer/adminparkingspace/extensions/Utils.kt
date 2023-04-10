package app.developer.adminparkingspace.extensions

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.developer.adminparkingspace.R

object Utils {
    fun progressDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.layout_progress_dialog)
        dialog.setCancelable(false)
        val lottieAnimation = dialog.findViewById<ImageView>(R.id.gif)
        Glide.with(context).load(R.drawable.load_gif).into(lottieAnimation)
        return dialog
    }
}