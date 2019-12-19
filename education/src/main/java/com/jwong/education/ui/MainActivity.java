package com.jwong.education.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jwong.education.R;

public class MainActivity extends AppCompatActivity {

//    private HashMap<String, Stack<Fragment>> mStacks;
//    public static final String TAB_CLOCK = "tab_clock";
//    public static final String TAB_STUDENT = "tab_student";
//    public static final String TAB_REPORT = "tab_report";
//    public static final String TAB_SETTING = "tab_setting";
//    private String mCurrentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_clock, R.id.navigation_student,
                R.id.navigation_report, R.id.navigation_setting).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
//        navView.setOnNavigationItemSelectedListener(this);
//        mStacks = new HashMap<>();
//        mStacks.put(TAB_CLOCK, new Stack<>());
//        mStacks.put(TAB_STUDENT, new Stack<>());
//        mStacks.put(TAB_REPORT, new Stack<>());
//        mStacks.put(TAB_SETTING, new Stack<>());

    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        ActionBar actionBar = getSupportActionBar();
//        switch (menuItem.getItemId()) {
//            case R.id.navigation_clock:
//                actionBar.setTitle(R.string.title_clock);
//                actionBar.setIcon(R.drawable.ic_clock_black_24dp);
//                actionBar.setLogo(R.drawable.ic_clock_black_24dp);
//                Log.d(getClass().getSimpleName(), "selected clock");
//                selectedTab(TAB_CLOCK);
//                break;
//            case R.id.navigation_student:
//                actionBar.setTitle(R.string.title_student);
//                actionBar.setIcon(R.drawable.ic_student_black_24dp);
//                actionBar.setLogo(R.drawable.ic_student_black_24dp);
//                Log.d(getClass().getSimpleName(), "selected student");
//                selectedTab(TAB_STUDENT);
//                break;
//            case R.id.navigation_report:
//                actionBar.setTitle(R.string.title_report);
//                actionBar.setIcon(R.drawable.ic_report_black_24dp);
//                actionBar.setLogo(R.drawable.ic_report_black_24dp);
//                Log.d(getClass().getSimpleName(), "selected report");
//                selectedTab(TAB_REPORT);
//                break;
//            case R.id.navigation_setting:
//                actionBar.setTitle(R.string.title_setting);
//                actionBar.setIcon(R.drawable.ic_setting_black_24dp);
//                actionBar.setLogo(R.drawable.ic_setting_black_24dp);
//                Log.d(getClass().getSimpleName(), "selected setting");
//                selectedTab(TAB_SETTING);
//                break;
//        }
//        return true;
//    }
//
//    private void selectedTab(String tabId) {
//        mCurrentTab = tabId;
//        if (mStacks.get(tabId).size() == 0) {
//            if (tabId.equals(TAB_CLOCK)) {
//                pushFragments(tabId, new ClockFragment(), true);
//            } else if (tabId.equals(TAB_STUDENT)) {
//                pushFragments(tabId, new StudentFragment(), true);
//            } else if (tabId.equals(TAB_REPORT)) {
//                pushFragments(tabId, new ReportFragment(), true);
//            } else if (tabId.equals(TAB_SETTING)) {
//                pushFragments(tabId, new SettingFragment(), true);
//            }
//        } else {
//            pushFragments(tabId, mStacks.get(tabId).lastElement(), false);
//        }
//    }
//
//    public void pushFragments(String tag, Fragment fragment, boolean shouldAdd) {
//        if (shouldAdd)
//            mStacks.get(tag).push(fragment);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.replace(R.id.nav_host_fragment, fragment);
//        ft.commit();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.top_nav_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        return NavHostFragment.findNavController(getSupportFragmentManager()
//                .findFragmentById(R.id.nav_host_fragment)).navigateUp();
//    }
}
