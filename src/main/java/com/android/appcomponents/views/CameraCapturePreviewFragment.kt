package com.android.cameracapturecomponent.views

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.android.appcomponents.R
import com.android.cameracapturecomponent.interfaces.OnImageCaptureCallback
import com.android.cameracapturecomponent.interfaces.OnVideoCaptureCallback
import com.android.cameracapturecomponent.util.visible
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.android.synthetic.main.fragment_preview.*


internal class CameraCapturePreviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            Glide.with(this).load(contentUri).into(imgPreview)
        } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            loadVideo(contentUri)
        }

        imgSave.setOnClickListener {
            if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                onImageCaptureCallback?.onImageCaptured(contentUri)
            } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                onVideoCaptureCallback?.onVideoCaptured(contentUri)
            }
            activity?.supportFragmentManager?.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        imgCancel.setOnClickListener {
            contentUri.toFile().delete()
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun loadVideo(contentUri: Uri) {
        vvVideoPreview.visible()
        val player = activity?.let {
            ExoPlayer.Builder(it).build()
        }

        vvVideoPreview.player = player

        val mediaItem: MediaItem = MediaItem.fromUri(contentUri)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()

    }

    companion object {
        lateinit var contentUri: Uri
        private var mediaType: Int = 1
        private var onImageCaptureCallback: OnImageCaptureCallback? = null
        private var onVideoCaptureCallback: OnVideoCaptureCallback? = null

        fun newInstance(
            contentUri: Uri,
            mediaType: Int,
            onImageCaptureCallback: OnImageCaptureCallback?,
            onVideoCaptureCallback: OnVideoCaptureCallback?
        ): CameraCapturePreviewFragment {
            Companion.contentUri = contentUri
            Companion.mediaType = mediaType
            this.onImageCaptureCallback = onImageCaptureCallback
            this.onVideoCaptureCallback = onVideoCaptureCallback
            return CameraCapturePreviewFragment()
        }
    }
}