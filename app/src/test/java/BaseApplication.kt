import android.app.Application
import android.content.Context
import com.hc.hicareservices.R
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        // configuring the font for calligraphy
        try {
            ViewPump.init(
                ViewPump.builder()
                    .addInterceptor(
                        CalligraphyInterceptor(
                            CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/font.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()
                        )
                    )
                    .build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        var instance: BaseApplication? = null
        val context: Context?
            get() = instance
    }
}
