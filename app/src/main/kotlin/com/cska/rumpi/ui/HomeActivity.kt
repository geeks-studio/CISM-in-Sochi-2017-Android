package com.cska.rumpi.ui


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.IdRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.cska.rumpi.R
import com.cska.rumpi.network.RestApi
import com.cska.rumpi.ui.base.BaseActivity
import com.cska.rumpi.ui.base.NavHeader
import com.cska.rumpi.ui.news.NewsFragment
import com.cska.rumpi.ui.objects.ObjectsFragment
import com.cska.rumpi.ui.pdf.CismFragment
import com.cska.rumpi.ui.pdf.MedalFragment
import com.cska.rumpi.ui.results.SearchResultsFragment
import com.cska.rumpi.ui.schedule.ScheduleFragment
import com.cska.rumpi.ui.social.SocialFragment
import com.cska.rumpi.ui.web.MatchFragment
import com.cska.rumpi.ui.web.QuizFragment
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.Prefs
import com.cska.rumpi.utils.bindView
import com.cska.rumpi.utils.fragmentTag
import com.cska.rumpi.utils.setElevationCompat
import org.jetbrains.anko.intentFor



enum class HomeScreens(@IdRes val itemId: Int) {
    NEWS(R.id.nav_news),
    SCHEDULE(R.id.nav_schedule),
    RESULTS(R.id.nav_results),
    VIDEO(R.id.nav_video),
    CHAT(R.id.nav_chat),
    CISM(R.id.nav_cism),
    ARMY(R.id.nav_army),
    SOCIAL(R.id.nav_social),
    QUIZ(R.id.nav_quiz);

    companion object {
        val DEFAULT_SCREEN = HomeScreens.SCHEDULE
    }
}


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    companion object {
        public const val EXTRA_SHOW_SCREEN = "show_screen_extra"
        private const val EXTRA_SCREEN_DATA = "screen_data_extra"

        fun getIntent(context: Context, screen: HomeScreens = HomeScreens.DEFAULT_SCREEN, data: Bundle? = null): Intent =
                context.intentFor<HomeActivity>(
                        EXTRA_SHOW_SCREEN to screen.itemId
                ).apply {
                    if (data != null) putExtra(EXTRA_SCREEN_DATA, data)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }

        fun launchNewTask(context: Context, data: Bundle? = null) {
            val intent = getIntent(context).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                if (data != null) putExtras(data)
            }
            context.startActivity(intent)
        }

        fun launch(context: Context, screen: HomeScreens = HomeScreens.DEFAULT_SCREEN, data: Bundle? = null) {
            context.startActivity(getIntent(context, screen, data))
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////

    private val OUT_STATE_SELECTED_NAV_ITEM = "SELECTED_NAV_ITEM_OUT_STATE"

    private val FRAGMENT_TAG_NEWS = fragmentTag<NewsFragment>()
    private val FRAGMENT_TAG_OBJECTS = fragmentTag<ObjectsFragment>()
    private val FRAGMENT_TAG_RESULTS = fragmentTag<SearchResultsFragment>()
    private val FRAGMENT_TAG_SCHEDULE = fragmentTag<ScheduleFragment>()
    private val FRAGMENT_TAG_VIDEO = fragmentTag<MatchFragment>()
    private val FRAGMENT_TAG_QUIZ = fragmentTag<QuizFragment>()
    private val FRAGMENT_TAG_CISM = fragmentTag<CismFragment>()
    private val FRAGMENT_TAG_ARMY = fragmentTag<MedalFragment>()
    private val FRAGMENT_TAG_SOCIAL = fragmentTag<SocialFragment>()

    private val navDrawerLayout by bindView<DrawerLayout>(R.id.nav_drawer)
    private val navView by bindView<NavigationView>(R.id.nav_drawer_view)
    private val navHeader by lazy { NavHeader(navView.getHeaderView(0)) }
    private val appBarLayout by bindView<AppBarLayout>(R.id.container_toolbar)
    private val toolbar by bindView<Toolbar>(R.id.toolbar)

    override val addToBackStack: Boolean = false
    private var selectedNavItem = -1

    private val drawerListener = object : DrawerLayout.SimpleDrawerListener() {
        private var currentOffset = 0F
        private var updateRequested = false

        override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            if (slideOffset > currentOffset) {
                currentOffset = slideOffset
                if (!updateRequested) {
                    updateRequested = true
                    RestApi().getWeather()
                            .doOnNext { weather ->
                                navHeader.setWeather(this@HomeActivity, weather?.weather?.id, weather?.weather?.temperature)
                            }.map { Unit }
                            .onErrorReturn(Throwable::printStackTrace)
                            .subscribe()
                }
            }
        }

        override fun onDrawerClosed(drawerView: View?) {
            currentOffset = 0F
            updateRequested = false
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initNavDrawer()
        initScreen(savedInstanceState)

        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0)
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        navHeader.setLanguage(this, language)
        navHeader.setOnClickListener(this)
        val token = Prefs.readToken()
        val id = Settings.Secure.getString(applicationContext.contentResolver,
                Settings.Secure.ANDROID_ID);
        RestApi().sendToken(id, token, language)
                .map { Unit }
                .onErrorReturn(Throwable::printStackTrace).subscribe()
        RestApi().getWeather()
                .doOnNext { weather ->
                    navHeader.setWeather(this@HomeActivity, weather?.weather?.id, weather?.weather?.temperature)
                }.map { Unit }
                .onErrorReturn(Throwable::printStackTrace)
                .subscribe()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val screenToShow = intent.getIntExtra(EXTRA_SHOW_SCREEN, HomeScreens.DEFAULT_SCREEN.itemId)
        onScreenSelected(screenToShow)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(OUT_STATE_SELECTED_NAV_ITEM, selectedNavItem)
    }

    override fun onDestroy() {
        navDrawerLayout.removeDrawerListener(drawerListener)
        navHeader.destroy()
        super.onDestroy()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Initializations
    ///////////////////////////////////////////////////////////////////////////

    private fun initNavDrawer() {
        setSupportActionBar(toolbar)

        navDrawerLayout.addDrawerListener(drawerListener)

        val toggle = ActionBarDrawerToggle(this, navDrawerLayout, toolbar, R.string.app_name, R.string.app_name)
        navDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //navHeader.setOnClickListener(this)
        navView.setNavigationItemSelectedListener(this)
    }

    private fun initScreen(state: Bundle?) {
        selectedNavItem = state?.getInt(OUT_STATE_SELECTED_NAV_ITEM, -1) ?: -1
        if (selectedNavItem == -1) {
            onScreenSelected(intent.getIntExtra(EXTRA_SHOW_SCREEN, HomeScreens.DEFAULT_SCREEN.itemId))
        }
        else onScreenSelected(selectedNavItem)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Navigation
    ///////////////////////////////////////////////////////////////////////////

    override fun onNavigationItemSelected(item: MenuItem): Boolean = onScreenSelected(item.itemId)

    private fun onScreenSelected(@IdRes itemId: Int): Boolean {
        navView.setCheckedItem(itemId)
        when (itemId) {
            R.id.nav_news -> {
                prepareToolbar(hasElevation = false)
                navigateToScreen(itemId, onGoToNews)
            }
            R.id.nav_objects -> {
                prepareToolbar(hasElevation = false)
                navigateToScreen(itemId, onGoToObjects)
            }
            R.id.nav_schedule -> {
                prepareToolbar(hasElevation = false)
                navigateToScreen(itemId, onGoToSchedule)
            }
            R.id.nav_results -> {
                prepareToolbar(hasElevation = false)
                navigateToScreen(itemId, onGoToResults)
            }
            R.id.nav_video -> {
                showDialogPrepare()
                //prepareToolbar(hasElevation = false)
                //navigateToScreen(itemId, onGoToVideo)
            }
            R.id.nav_chat -> {
                showDialogPrepare()
                //startChat()
            }
            R.id.nav_quiz -> {
                showDialogPrepare()
               // prepareToolbar(hasElevation = false)
               // navigateToScreen(itemId, onGoToQuiz)
            }
            R.id.nav_cism -> {
                prepareToolbar(hasElevation = false)
                navigateToScreen(itemId, onGoToCism)
            }
            R.id.nav_army -> {
                prepareToolbar(hasElevation = false)
                navigateToScreen(itemId, onGoToArmy)
            }
            R.id.nav_social -> {
                prepareToolbar(hasElevation = false)
                navigateToScreen(itemId, onGoToSocial)
            }
        }
        return true
    }

    fun navigateToScreen(@IdRes itemId: Int, navigator: (Bundle?) -> Any?) {
        if (itemId != selectedNavItem) {
            selectedNavItem = itemId
            navigator(intent.getBundleExtra(EXTRA_SCREEN_DATA))
        }
        closeDrawer()
    }

    fun prepareToolbar(hasElevation: Boolean = true, hasTitle: Boolean = true) {
        if (hasElevation)
            appBarLayout.setElevationCompat(R.dimen.elevation_action_bar)
        else appBarLayout.setElevationCompat(0F)

        supportActionBar?.setDisplayShowTitleEnabled(hasTitle)
    }

    fun startChat() {
        var intent: Intent? = packageManager.getLaunchIntentForPackage("ru.cism.chat")
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=ru.cism.chat")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Navigators

    private val onGoToNews = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_NEWS)
    }

    private val onGoToObjects = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_OBJECTS)
    }

    private val onGoToSchedule = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_SCHEDULE)
    }

    private val onGoToResults = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_RESULTS)
    }

    private val onGoToVideo = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_VIDEO)
    }

    private val onGoToArmy = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_ARMY)
    }

    private val onGoToCism = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_CISM)
    }

    private val onGoToSocial = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_SOCIAL)
    }

    private val onGoToQuiz = { data: Bundle? ->
        placeFragment(FRAGMENT_TAG_QUIZ)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Helpers
    ///////////////////////////////////////////////////////////////////////////

    private fun closeDrawer(): Boolean =
            if (navDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                navDrawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            else false

    private fun changeLanguage(language: String){
        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0);
        val languageOld = sharedPref.getString(Consts.PREF_LANGUAGE, "en")
        if(languageOld == language) return
        val editor = sharedPref.edit()
        editor.putString(Consts.PREF_LANGUAGE, language)
        Consts.setLanguage(baseContext, language)
        editor.apply()
        HomeActivity.launchNewTask(this)
    }

    ///////////////////////////////////////////////////////////////////////////
    // On Click
    ///////////////////////////////////////////////////////////////////////////

    override fun onClick(v: View) {
        when (v.id){
            R.id.nhm_tv_en -> changeLanguage("en")
            R.id.nhm_tv_ru -> changeLanguage("ru")
        }
    }

    fun showDialogPrepare(){
        AlertDialog.Builder(this)
            .setTitle("В разработке")
            .setMessage("Этот раздел заработает 23-ого февраля" ).setCancelable(true)
            .show()
    }
}
