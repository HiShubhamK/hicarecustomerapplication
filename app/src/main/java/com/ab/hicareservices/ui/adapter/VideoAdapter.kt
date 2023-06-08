package com.ab.hicareservices.ui.adapter

import android.media.session.MediaController
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.dashboard.VideoData
import com.ab.hicareservices.databinding.VideoAdapterBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso


class VideoAdapter(private val  fragmentActivity: FragmentActivity?) : RecyclerView.Adapter<VideoAdapter.MainViewHolder>() {

    var socialmedialist = mutableListOf<VideoData>()
    private lateinit var mediaController:MediaController
//    private var mOnServiceRequestClickHandler: OnServiceRequestClickHandler? = null

    fun setvideo(socialmedialist: List<VideoData>?) {
        if (socialmedialist != null) {
            this.socialmedialist = socialmedialist.toMutableList()
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = VideoAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val socialmedialist = socialmedialist[position]
//            Picasso.get().load(socialmedialist.ImageUrl).into( holder.binding.videoViewThumbnail)
//            mediaController = MediaController(fragmentActivity!!)
//            mediaController.setAnchorView(holder.binding.videoView)
//            holder.binding.videoView.setMediaController(mediaController)
//            holder.binding.videoView.keepScreenOn = true
//            holder.binding.videoView.setVideoPath(socialmedialist.VideoUrl)
//            holder.binding.videoView.start() //call this method for auto playing video
//
//
//            holder.binding.videoView.setOnPreparedListener(OnPreparedListener {
//                holder.binding.videoViewThumbnail.setVisibility(View.GONE)
//                //you can Hide progress dialog here when video is start playing;
//            })
//            holder.binding.videoView.setOnCompletionListener(OnCompletionListener {
//                holder.binding.videoView.stopPlayback()
//                holder.binding.videoViewThumbnail.setVisibility(View.VISIBLE)
//            })
            fragmentActivity!!.lifecycle.addObserver(holder.binding.videoView)

            holder.binding.videoView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = socialmedialist.VideoUrl.toString()
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return socialmedialist.size
    }


    class MainViewHolder(val binding: VideoAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
