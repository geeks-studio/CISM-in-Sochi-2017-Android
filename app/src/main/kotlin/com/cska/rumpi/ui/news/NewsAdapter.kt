package com.cska.rumpi.ui.news

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cska.rumpi.R
import com.cska.rumpi.network.models.NewsModel
import com.cska.rumpi.ui.base.AdapterConstants
import com.cska.rumpi.ui.base.BaseAdapter
import com.cska.rumpi.ui.base.OnViewTypeClickListener
import com.cska.rumpi.ui.base.ViewType
import com.cska.rumpi.ui.base.ViewTypeDelegateAdapter
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.displayImage
import com.cska.rumpi.utils.getLocalHumanReadableAgoDateString
import com.cska.rumpi.utils.inflate
import java.util.Date

/**
 * Created by rumpi on 03.02.2017.
 */

class NewsAdapter : BaseAdapter(){
    init{
        delegateAdapters.put(AdapterConstants.NEWS, NewsDelegateAdapter())
    }

    fun addNews(news: List<NewsModel>){
        items.addAll(news);
        notifyDataSetChanged()
    }

    fun clearAndAddNews(news: List<NewsModel>){
        items.clear()
        items.addAll(news)
        notifyDataSetChanged()
    }

    fun getNews(): List<NewsModel>{
        return items
                .filter { it.getViewType() == AdapterConstants.NEWS }
                .map { it as NewsModel }
    }

    fun getLastNewsId() =
        if(items.size == 0)  0L
        else (items[items.size - 1] as NewsModel).id

}

class NewsDelegateAdapter : ViewTypeDelegateAdapter {
    override fun onCreateViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder{
        return NewsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  item: ViewType,
                                  next: ViewType?,
                                  listener: OnViewTypeClickListener?) {
        holder as NewsViewHolder
        val nextImage = if(next != null) (next as NewsModel).photo else null
        holder.bind(item as NewsModel, nextImage, listener)
    }

    class NewsViewHolder(parent: ViewGroup) :RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_news)){

        private val imageView by bindView<ImageView>(R.id.in_iv_image)
        private val imageTitle by bindView<TextView>(R.id.in_tv_title)
        private val imageTime by bindView<TextView>(R.id.in_tv_time)

        private val textTitle by bindView<TextView>(R.id.in_tv_t_title)
        private val textTime by bindView<TextView>(R.id.in_tv_t_time)
        private val bottomDivider by bindView<View>(R.id.bottom_divider)

        private val containerImage by bindView<View>(R.id.in_image_exist_container)
        private val containerNopeImage by bindView<View>(R.id.in_image_nope_container)
        private val rootContainer by bindView<View>(R.id.in_root_view)

        fun bind(item: NewsModel, nextImage: String?, listener: OnViewTypeClickListener?) = with(itemView){
            val sharedPref = context.applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
            val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
            if(item.photo.isNullOrEmpty()){
                containerImage.visibility = View.GONE
                containerNopeImage.visibility = View.VISIBLE
                textTitle.text = item.title
                textTime.text = Date().getLocalHumanReadableAgoDateString(resources, language, item.updated_at)
                if(nextImage.isNullOrEmpty()){
                    bottomDivider.visibility = View.VISIBLE
                }else{
                    bottomDivider.visibility = View.INVISIBLE
                }
            }else {
                containerImage.visibility = View.VISIBLE
                containerNopeImage.visibility = View.GONE
                imageView.displayImage(item.photo)
                imageTitle.text = item.title
                imageTime.text = Date().getLocalHumanReadableAgoDateString(resources, language, item.updated_at)
            }

            rootContainer.setOnClickListener{ listener?.onItemClick(item.id)}
        }

    }

}