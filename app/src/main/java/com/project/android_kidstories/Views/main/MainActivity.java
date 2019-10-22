package com.project.android_kidstories.Views.main;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.AddStoryActivity;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.LoginActivity;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import com.project.android_kidstories.NightmodeActivity;
import com.project.android_kidstories.base.BaseActivity;
import com.project.android_kidstories.ui.home.Fragments.CategoriesFragment;
import com.project.android_kidstories.ui.edit.ProfileFragment;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.ui.edit.ProfileFragment;
import com.project.android_kidstories.ui.home.Fragments.CategoriesFragment;
import com.project.android_kidstories.ui.home.HomeFragment;
import com.project.android_kidstories.ui.home.StoryAdapter;
import com.project.android_kidstories.ui.info.AboutFragment;
import com.project.android_kidstories.ui.profile.BookmarksFragment;
import com.project.android_kidstories.ui.support.DonateFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 12/10/19
 */


public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String USER_KEY_INTENT_EXTRA ="com.project.android_kidstories_USER_KEY";

    private static final String TAG = "kidstories";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Repository repository;
    private StoryAdapter storyAdapter;
    private GoogleApiClient mGoogleApiClient;
    private BottomNavigationView bottomNavigationView;
    private SharePref sharePref;
    public static int LastTabPosition = 0;
    private String token;
    private String firstname, lastname, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Stories");
        setSupportActionBar(toolbar);
        sharePref = SharePref.getINSTANCE(getApplicationContext());

//        Get token from SharedPref
        getUserDetails();

        initViews();

        if (savedInstanceState == null) {
            openHomeFragment();
        }

        setupProfile(navigationView);

//        Preparing token to be parsed to fragments
        Bundle data = new Bundle();
        data.putString("token", token);

        // Making the header image clickable
        View headerView = navigationView.getHeaderView(0);

        TextView userName = headerView.findViewById(R.id.nav_header_name);
        name = firstname + " " + lastname;
        userName.setText(name);

        ImageView navImage = headerView.findViewById(R.id.nav_header_imageView);
        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.project.android_kidstories.ui.profile.ProfileFragment profileFragment = new com.project.android_kidstories.ui.profile.ProfileFragment();
//                Add bundle data containing "token" before parsing to profileFragment
                profileFragment.setArguments(data);
                setUpFragment(profileFragment);
                getSupportActionBar().setTitle("Profile");
                drawer.closeDrawer(GravityCompat.START);
                for (int i = 0; i < navigationView.getMenu().size(); i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
            }
        });
    }

    private void initViews() {

        drawer = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        View headerView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //For test
        /*RecyclerView recyclerView=findViewById(R.id.main_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(storyAdapter);*/
        repository = Repository.getInstance(this.getApplication());

        storyAdapter = new StoryAdapter(repository);



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
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(home);
                        openHomeFragment();
                        navigationView.setCheckedItem(R.id.nav_home);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        msg ="Stories";
                        break;
                    case R.id.nav_categories:
                        fragment = new CategoriesFragment();
                        msg="Categories";
                        bottomNavigationView.setVisibility(View.GONE);
                        break;
                    case R.id.nav_donate:
                        fragment = new DonateFragment();
                        msg="Donate";
                        bottomNavigationView.setVisibility(View.GONE);
                        break;
                    case R.id.nav_about:
                        fragment = new AboutFragment();
                        msg="About";
                        bottomNavigationView.setVisibility(View.GONE);
                        break;
                    case R.id.nav_log_out:
                        showToast("Logging Out");
                        signout();
                        bottomNavigationView.setVisibility(View.GONE);
                        break;
                    case R.id.nav_edit_profile:
                        fragment = new ProfileFragment();
                        msg="Profile";
                        bottomNavigationView.setVisibility(View.GONE);
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


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(home);
                        openHomeFragment();
                        msg = "Stories";
                        break;
                    case R.id.addStory:
                        Intent i = new Intent(getApplicationContext(), AddStoryActivity.class);
                        startActivity(i);
                        break;
                    case R.id.bookmark_fragment:
                        fragment = new BookmarksFragment();
                        msg = "Bookmarks";
                        break;
                }
                if (fragment != null) {
                    setUpFragment(fragment);
                }
                toolbar.setTitle(msg);
                return true;
            }
        });
    }




    private void openHomeFragment() {
        HomeFragment holderFragment = new HomeFragment();

        setUpFragment(holderFragment);
        navigationView.setCheckedItem(R.id.nav_home);
        bottomNavigationView.setSelectedItemId(0);
        bottomNavigationView.setVisibility(View.VISIBLE);
        toolbar.setTitle("Stories");
    }

    private void setUpFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment).commit();
    }


    private void signout() {
        // Facebook logout
        if (LoginManager.getInstance() != null) {
            LoginManager.getInstance().logOut();
        }
        // Google logout
        if (mGoogleApiClient != null) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        sharePref.setIsUserLoggedIn(false);
        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(logout);
        finish();
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        super.onStart();
    }



    private void setupProfile(View view) {

//        repository.getUserProfileApi().getUserProfile(token).enqueue(new Callback<BaseResponse<User>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
//                if (response.isSuccessful()){
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
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

    private void getUserDetails(){
        token = new SharePref(this).getMyToken();
        firstname = new SharePref(this).getUserFirstname();
        lastname = new SharePref(this).getUserLastname();
        Toast.makeText(MainActivity.this, token,Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //TODO: Ehma Refactor to BaseActivity


    private void showSnackBar(View view,String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else if (navigationView.getCheckedItem().getItemId()!=R.id.nav_home) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(home);
                navigationView.setCheckedItem(R.id.nav_home);
                bottomNavigationView.setSelectedItemId(0);
                bottomNavigationView.setVisibility(View.VISIBLE);
                toolbar.setTitle("Stories");
                openHomeFragment();
        } else {
            doExit();
        }

    }


    private void hideDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }


    public void openSettings(){
        Intent intent = new Intent(this, NightmodeActivity.class);

        startActivity(intent);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            openSettings();
        }
        return super.onOptionsItemSelected(item);
    }
    private void doExit() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle(R.string.app_name);
        alertDialog.show();
    }

}
