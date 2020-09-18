package hr.stipanic.tomislav.thrifty.utils


import android.text.format.DateFormat
import android.view.View
import com.google.type.Date
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.utils.Constants.DAY
import hr.stipanic.tomislav.thrifty.utils.Constants.FORTY_DAYS
import hr.stipanic.tomislav.thrifty.utils.Constants.HOUR
import hr.stipanic.tomislav.thrifty.utils.Constants.MINUTE
import hr.stipanic.tomislav.thrifty.utils.Constants.TWO_DAYS
import hr.stipanic.tomislav.thrifty.utils.Constants.TWO_HOURS
import hr.stipanic.tomislav.thrifty.utils.Constants.TWO_MINUTES
import java.util.*

object StringUtils {

    fun showLocation(city: String, country: String): String? = "$city, $country"

    fun showCurrency(price: Float): String? = "$price $"

    fun hideCurrency(price: Float): String? = "$price"

    fun showUsername(username: String): String? = "by $username"

    fun getID(userId: String, time: Long) = "$userId$time"

    fun getTimePassedString(dateOfPosting: Long?, view: View): String {
        if (dateOfPosting != null) {
            val currentTime: Long = Date().time

            return when (val timePassed = (currentTime - dateOfPosting) / 1000) {
                in 0..TWO_MINUTES -> view.context.getString(R.string.comment_date_now)
                in TWO_MINUTES..HOUR -> String.format(
                    view.context.getString(R.string.x_minutes_ago),
                    (timePassed / MINUTE).toInt()
                )
                in HOUR..TWO_HOURS -> view.context.getString(R.string.one_hour_ago)
                in TWO_HOURS..DAY -> String.format(
                    view.context.getString(R.string.x_hours_ago),
                    (timePassed / HOUR).toInt()
                )
                in DAY..TWO_DAYS -> view.context.getString(R.string.one_day_ago)
                in TWO_DAYS..FORTY_DAYS -> String.format(
                    view.context.getString(R.string.x_days_ago),
                    (timePassed / DAY).toInt()
                )
                else -> DateFormat.format("dd.MM.yyyy", dateOfPosting).toString()
            }
        } else {
            return "???"
        }
    }

}
