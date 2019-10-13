package com.project.android_kidstories.Views.main;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;
import com.project.android_kidstories.DataStore.ApiViewmodel;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.R;
import com.project.android_kidstories.fragments.CategoriesFragment;
import com.project.android_kidstories.ui.edit.ProfileFragment;
import com.project.android_kidstories.ui.home.HomeFragment;
import com.project.android_kidstories.ui.home.StoryAdapter;
import com.project.android_kidstories.ui.info.AboutFragment;
import com.project.android_kidstories.ui.support.DonateFragment;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 12/10/19
 */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "kidstories";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Repository repository;
    private StoryAdapter storyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        initViews();

        if (savedInstanceState == null) {
            openHomeFragment();
        }


        setupProfile(navigationView);

    }

    private void initViews() {

        drawer = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_nav_view);
        View headerView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //For test
        RecyclerView recyclerView=findViewById(R.id.main_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(storyAdapter);
        ApiViewmodel apiViewmodel= ViewModelProviders.of(this).get(ApiViewmodel.class);
        repository = apiViewmodel.getRepository();

        storyAdapter = new StoryAdapter(apiViewmodel);



        setupProfile(headerView);
        openHomeFragment();
        //fetchStories();
        navigationClickListeners();


    }

    private void navigationClickListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        msg="Stories";
                        break;
                    case R.id.nav_edit_profile:
                        fragment = new ProfileFragment();
                        msg="Profile";
                        break;
                    case R.id.nav_categories:
                        fragment = new CategoriesFragment();
                        msg="Categories";
                        break;
                    case R.id.nav_donate:
                        fragment = new DonateFragment();
                        msg="Donate";
                        break;
                    case R.id.nav_about:
                        fragment = new AboutFragment();
                        msg="About";
                        showToast("Add New Account Nav Clicked");
                        break;
                    case R.id.nav_log_out:
                        showToast("Log Out");
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                toolbar.setTitle(msg);
                if (fragment != null) {
                    setUpFragment(fragment);
                }
                return true;
            }
        });
    }




    private void openHomeFragment() {
        HomeFragment holderFragment = new HomeFragment();
        setUpFragment(holderFragment);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void setUpFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }


    private void signout() {
        /*auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();*/
    }


    private void setupProfile(View view) {
        /*CircleImageView navHeaderCircleImage = view.findViewById(R.id.nav_header_imageView);
        TextView navHeaderNameTv = view.findViewById(R.id.nav_header_name);
        navHeaderCircleImage.setOnClickListener(this);

        //navHeaderNameTv.setText(currentUser.getFirstName());

        Glide.with(this)
                .load(R.drawable.profile_pic)
                .centerCrop()
                .placeholder(R.drawable.profile_pic)
                .into(navHeaderCircleImage);*/
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nav_header_imageView:
                showToast("Opening profile setup");
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //TODO: Ehma Refactor to BaseActivity
    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void showSnackBar(View view,String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void initPrefAndsaveToken(){
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        Prefs.putString(AddStoryHelper.TOKEN_KEY,"aTokenStringShouldBeHere");
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else if (navigationView.getCheckedItem().getItemId()!=R.id.nav_home) {
            openHomeFragment();
        } else {
            super.onBackPressed();
        }
    }


    private void hideDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }




}
