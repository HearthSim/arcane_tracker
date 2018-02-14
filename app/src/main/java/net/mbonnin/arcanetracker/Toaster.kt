package net.mbonnin.arcanetracker

import android.widget.Toast
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A stupid class to debounce Toast
 * If we instantiate them too much, they end up not being shown
 */
object Toaster {
    val queue = LinkedList<String>()
    init {
        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (!queue.isEmpty()) {
                        val message = queue.removeFirst()
                        Toast.makeText(ArcaneTrackerApplication.context, message, Toast.LENGTH_LONG).show()
                    }
        }
    }

    fun show(message: String) {
        queue.addLast(message)
    }
}