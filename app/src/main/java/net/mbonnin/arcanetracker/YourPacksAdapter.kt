package net.mbonnin.arcanetracker

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pack.*
import net.mbonnin.arcanetracker.room.RDatabaseSingleton
import net.mbonnin.arcanetracker.room.RPack
import java.text.SimpleDateFormat
import java.util.*

class YourPacksAdapter(val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val list = mutableListOf<Item>()

    val TYPE_PACKS = 0
    val TYPE_DUST = 2
    val TYPE_DUST_AVERAGE = 3
    val TYPE_PITY_COUNTER = 4
    val TYPE_PACK = 5

    var lastListSize = 0
    val lock = Object()
    val requestQueue = LinkedList<Any>()

    val publishSubject = PublishSubject.create<List<Item>>()

    class Sentinel

    init {
        val thread = Thread{
            worker()
        }

        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                synchronized(lock) {
                    requestQueue.addLast(Sentinel())
                    lock.notifyAll()
                }
                thread.join(3000)
            }
        })

        publishSubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    list.addAll(it)
                    notifyDataSetChanged()
                }

        thread.start()

        requestMoreDataIfNeeded(0)
    }

    private fun requestMoreDataIfNeeded(position: Int) {
        var needed = false
        if (lastListSize == 0) {
            needed = true
        } else if (list.size > lastListSize && list.size - position < 5) {
            needed = true
        }

        if (needed) {
            lastListSize = list.size
            synchronized(lock) {
                requestQueue.addLast(Any())
                lock.notifyAll()
            }
        }
    }

    private fun worker() {
        val cursor = RDatabaseSingleton.instance.packDao().all()

        while (true) {
            synchronized(lock) {
                while (requestQueue.isEmpty()) {
                    lock.wait()
                }

                val request = requestQueue.removeFirst()
                if (request is Sentinel) {
                    cursor.close()
                    return
                }
            }

            val list = mutableListOf<Item>()
            val _cursorIndexOfId = cursor.getColumnIndexOrThrow("id")
            val _cursorIndexOfTimeMillis = cursor.getColumnIndexOrThrow("timeMillis")
            val _cursorIndexOfCardList = cursor.getColumnIndexOrThrow("cardList")

            var i = 0
            while (cursor.moveToNext() && i++ < 20) {
                val _tmpTimeMillis = cursor.getLong(_cursorIndexOfTimeMillis)
                val _tmpCardList = cursor.getString(_cursorIndexOfCardList)
                val _tmpId = cursor.getLong(_cursorIndexOfId)

                val rpack = RPack(_tmpTimeMillis, _tmpCardList)
                rpack.id = _tmpId

                list.add(PackItem(rpack))
            }

            publishSubject.onNext(list)
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val item = list.get(position)

            when (item) {
                is PacksItem -> return 2
                is DustItem -> return 2
                is DustAverageItem -> return 2
                is PityCounterItem -> return 3
                is PackItem -> return 6
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = list.get(position)

        when (item) {
            is PacksItem -> return TYPE_PACKS
            is DustItem -> return TYPE_DUST
            is DustAverageItem -> return TYPE_DUST_AVERAGE
            is PityCounterItem -> return TYPE_PITY_COUNTER
            is PackItem -> return TYPE_PACK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_PACKS,
            TYPE_DUST,
            TYPE_DUST_AVERAGE,
            TYPE_PITY_COUNTER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pack, parent, false)
                return PackViewHolder(view)
            }
            TYPE_PACK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pack, parent, false)
                return PackViewHolder(view)
            }
            else -> {
                throw Exception()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list.get(position)

        requestMoreDataIfNeeded(position)

        when (item) {
            is PacksItem -> Unit
            is DustItem -> Unit
            is DustAverageItem -> Unit
            is PityCounterItem -> Unit
            is PackItem -> (holder as PackViewHolder).bind(item.rpack)
        }
    }
}

class PackViewHolder(override val containerView: View) : LayoutContainer, RecyclerView.ViewHolder(containerView) {
    fun bind(rpack: RPack) {
        val cardList = rpack.cardList.split(",")

        date.setText(SimpleDateFormat.getDateInstance().format(Date(rpack.timeMillis)))

        for (i in 0 until 5) {
            val textView = (containerView as ViewGroup).getChildAt(2 + i) as TextView

            if (i < cardList.size) {
                var cardId = cardList[i]
                var golden = false
                if (cardId.endsWith("*")) {
                    cardId = cardId.substring(0, cardId.length - 1)
                    golden = true
                }
                val card = CardUtil.getCard(cardId)
                textView.setText(card.name + (if (golden) "(golden)" else ""))

                set.setText(card.set)
            } else {
                textView.setText("?")
            }
        }
    }
}
