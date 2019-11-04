package com.project.android_kidstories.Views.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.project.android_kidstories.AddStoryActivity;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.loginRegister.DataResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.LoginActivity;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SettingsActivity;
import com.project.android_kidstories.alarm.AlarmReceiver;
import com.project.android_kidstories.base.BaseActivity;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.streak.StreakActivity;
import com.project.android_kidstories.ui.home.Fragments.CategoriesFragment;
import com.project.android_kidstories.ui.home.Fragments.NewStoriesFragment;
import com.project.android_kidstories.ui.home.Fragments.PopularStoriesFragment;
import com.project.android_kidstories.ui.home.HomeFragment;
import com.project.android_kidstories.ui.home.StoryAdapter;
import com.project.android_kidstories.ui.info.AboutFragment;
import com.project.android_kidstories.ui.info.FeedBackFragment;
import com.project.android_kidstories.ui.profile.BookmarksFragment;
import com.project.android_kidstories.ui.profile.ProfileFragment;
import com.project.android_kidstories.ui.support.DonateFragment;
import com.project.android_kidstories.viewModel.FragmentsSharedViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.project.android_kidstories.ui.edit.ProfileFragment;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 12/10/19
 */


public class MainActivity extends BaseActivity implements View.OnClickListener{
    public static final String USER_KEY_INTENT_EXTRA ="com.project.android_kidstories_USER_KEY";

    private static final String TAG = "kidstories";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Repository repository;
    private StoryAdapter storyAdapter;
    private GoogleApiClient mGoogleApiClient;
    private BottomNavigationView bottomNavigationView;
    public static final String FRAGMENT_NEW = "New Stories";
    TextView userName;

    private FragmentsSharedViewModel viewModel;
    CircleImageView navProfilePic;
    private MenuItem searchItem;
    public static final String FRAGMENT_POPULAR = "Popular Stories";
    public static int lastTabPosition = 0;
    private static String CURRENT_FRAGMENT = "";
    private SharePref sharePref;
    private UserDetails userDetails;
    private Toolbar toolbar;


    public static String getCurrentFragment() {
        return CURRENT_FRAGMENT;
    }
    public static void setCurrentFragment(String currentFragment) {
        CURRENT_FRAGMENT = currentFragment;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(AlarmReceiver.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            try {
                notificationManager.createNotificationChannel(channel);
            } catch (NullPointerException npe) {
                showError(npe.getMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();


        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        updateToolbarTitle("Stories");

        if (lastTabPosition == 1) {
            setCurrentFragment(FRAGMENT_POPULAR);
        } else {
            setCurrentFragment(FRAGMENT_NEW);
        }

        sharePref = SharePref.getINSTANCE(getApplicationContext());
        viewModel = ViewModelProviders.of(this).get(FragmentsSharedViewModel.class);
        viewModel.currentUser = new User();

        // Get user details from SharedPref
        userDetails = getUserDetails();

        initViews();

        if (savedInstanceState == null) {
            openHomeFragment();
        }

        // Preparing token to be parsed to fragments
        Bundle data = new Bundle();
        data.putString("token", userDetails.getToken());

        // Making the header image clickable
        View headerView = navigationView.getHeaderView(0);

        userName = headerView.findViewById(R.id.nav_header_name);
        userName.setText(userDetails.getFullname());

        ImageView navImage = headerView.findViewById(R.id.nav_header_imageView);
        navImage.setOnClickListener(view -> {
            ProfileFragment profileFragment = new ProfileFragment();
            // Add bundle data containing "token" before parsing to profileFragment
            profileFragment.setArguments(data);
            setUpFragment(profileFragment);
            updateToolbarTitle("Profile");
            drawer.closeDrawer(GravityCompat.START, true);
            for (int i = 0; i < navigationView.getMenu().size(); i++) {
                navigationView.getMenu().getItem(i).setChecked(false);
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }

    private void updateToolbarTitle(String title) {
        try {
            getSupportActionBar().setTitle(title);
        } catch (NullPointerException npe) {
            showError(npe.getMessage());
        }
    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


    private UserDetails getUserDetails() {
        String firstname, lastname;
        String email;
        String token;

        token = sharePref.getMyToken();
        firstname = sharePref.getUserFirstname();
        lastname = sharePref.getUserLastname();
        email = sharePref.getUserEmail();

        return new UserDetails(token, email, firstname + " " + lastname);
    }

    private void openHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        setUpFragment(homeFragment);

        navigationView.setCheckedItem(R.id.nav_home);
        bottomNavigationView.setSelectedItemId(0);
        bottomNavigationView.setVisibility(View.VISIBLE);

        updateToolbarTitle("Stories");
    }

    private void initViews() {

        drawer = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        updateProfileImage();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        repository = Repository.getInstance(this.getApplication());
        storyAdapter = new StoryAdapter(repository);

        linkUserDetails();

        navigationClickListeners();
    }

    public void updateProfileImage() {
        View headerView = navigationView.getHeaderView(0);
        byte[] imageBytes = new BedTimeDbHelper(this).getUserImage();

        if (imageBytes == null) return;

        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        CircleImageView civ = headerView.findViewById(R.id.nav_header_imageView);
        civ.setImageBitmap(bmp);
    }


    public void linkUserDetails() {
        repository.getStoryApi().getUser("Bearer " + userDetails.getToken()).enqueue(new Callback<BaseResponse<DataResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DataResponse>> call, Response<BaseResponse<DataResponse>> response) {

                if (response.isSuccessful()) {
                    Log.d("User Details", response.body().getData().toString());
                    Log.d("User Name", response.body().getData().getFirstName());
                    viewModel.currentUser.setFirstName(response.body().getData().getFirstName());
                    viewModel.currentUser.setLastName(response.body().getData().getLastName());
                    viewModel.currentUser.setImage(response.body().getData().getImageUrl());
                    viewModel.currentUser.setEmail(response.body().getData().getEmail());
                    viewModel.currentUser.setId(response.body().getData().getId().longValue());
                    sharePref.setUserId(response.body().getData().getId());
                    if(viewModel.currentUser.getImage() != null && !viewModel.currentUser.getImage().isEmpty()) {
                        Glide.with(getApplicationContext())
                                .load(viewModel.currentUser.getImage())
                                .into(navProfilePic);
                    }
                    else{
                        // Leave default local image if there is none from the api
                    }

                    if(viewModel.currentUser.getFirstName() != null && !viewModel.currentUser.getLastName().isEmpty()
                    && viewModel.currentUser.getLastName() != null && !viewModel.currentUser.getLastName().isEmpty() ){
                        userName.setText(viewModel.currentUser.getFirstName() + " "
                        + viewModel.currentUser.getLastName());
                    }
                    else{
                        userName.setText("Username");
                    }

                } else {
                    Log.d("User Details", "something went wrong");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DataResponse>> call, Throwable t) {

                Log.d("User Details", "Network Failure");
                Log.d("User Details", t.getMessage());
            }
        });
    }

    private void navigationClickListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                String msg = "";
                switch (menuItem.getItemId()) {

                    case R.id.nav_feed_back:
                        fragment = new FeedBackFragment();
                        msg = "Feedback";
                        bottomNavigationView.setVisibility(View.GONE);
                        break;

                    case R.id.nav_home:
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                        openHomeFragment();
                        navigationView.setCheckedItem(R.id.nav_home);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        msg = "Stories";
                        break;

                    case R.id.nav_categories:
                        fragment = new CategoriesFragment();
                        msg="Categories";
                        bottomNavigationView.setVisibility(View.GONE);
                        break;


                    /*case R.id.nav_saved_stories:
                        Intent intent = new Intent(MainActivity.this, SavedStoriesActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;*/

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
                    /*case R.id.nav_edit_profile:
                        fragment = new ProfileFragment();
                        msg = "Profile";
                        bottomNavigationView.setVisibility(View.GONE);
                        break;*/
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
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                        openHomeFragment();
                        msg = "Stories";
                        break;
                    case R.id.addStory:
                        Intent i = new Intent(getApplicationContext(), AddStoryActivity.class);
                        i.putExtra("token", userDetails.getToken());
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
        logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Stories");
        //hideSearchMenu();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.e("TAAAAG1", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("TAAAAG1", newText);
                if (getCurrentFragment().equals(FRAGMENT_NEW))
                    NewStoriesFragment.storySearchListener.onStorySearched(newText);
                else if (getCurrentFragment().equals(FRAGMENT_POPULAR))
                    PopularStoriesFragment.storySearchListener.onStorySearched(newText);
                return true;
            }
        });
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
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);

        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_settings) {
            openSettings();
        }

        if (item.getItemId() == R.id.action_streaks) {
            StreakActivity.start(this);
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

// Class to maintain a user object
class UserDetails {
    private String token;
    private String fullname;
    private String email;

    UserDetails(String token, String email, String fullname) {
        this.token = token;
        this.email = email;
        this.fullname = fullname;
    }

    String getFullname() {
        return fullname;
    }

    String getEmail() {
        return email;
    }

    String getToken() {
        return token;
    }
}
