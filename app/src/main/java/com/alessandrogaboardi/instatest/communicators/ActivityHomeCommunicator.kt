package com.alessandrogaboardi.instatest.communicators

import android.view.View
import com.alessandrogaboardi.instatest.db.models.ModelMedia

/**
 * Created by alessandro on 05/11/2017.
 */
interface ActivityHomeCommunicator {
    fun onMediaDetailRequested(item: ModelMedia, view: View)
}