package com.project.android_kidstories.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.project.android_kidstories.AddStoryActivity;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.CustomSearchAdapter;
import com.project.android_kidstories.data.Repository;
import com.project.android_kidstories.data.SearchData;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.model.User;
import com.project.android_kidstories.data.source.helpers.BedTimeDbHelper;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.data.source.remote.response_models.BaseResponse;
import com.project.android_kidstories.data.source.remote.response_models.loginRegister.DataResponse;
import com.project.android_kidstories.receivers.AlarmReceiver;
import com.project.android_kidstories.ui.base.BaseActivity;
import com.project.android_kidstories.ui.categories.CategoriesFragment;
import com.project.android_kidstories.ui.donate.DonateFragment;
import com.project.android_kidstories.ui.downloads.DownloadsFragment;
import com.project.android_kidstories.ui.home.HomeFragment;
import com.project.android_kidstories.ui.info.AboutFragment;
import com.project.android_kidstories.ui.info.FeedBackFragment;
import com.project.android_kidstories.ui.login.LoginActivity;
import com.project.android_kidstories.ui.profile.BookmarksFragment;
import com.project.android_kidstories.ui.profile.ProfileFragment;
import com.project.android_kidstories.viewModel.FragmentsSharedViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 12/10/19
 */


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.StoriesListListener {
    public static final String USER_KEY_INTENT_EXTRA = "com.project.android_kidstories_USER_KEY";

    private static final String TAG = "kidstories";
    private DrawerLayout drawer;
    private NavigationView sideNav;
    private Repository repository;
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
    private LoggedInUser userDetails;
    private Toolbar toolbar;


    private Fragment currentFragment;

    public static String getCurrentFragment() {
        return CURRENT_FRAGMENT;
    }

    public static void setCurrentFragment(String currentFragment) {
        CURRENT_FRAGMENT = currentFragment;
    }

    public static void start(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.USER_KEY_INTENT_EXTRA, (Parcelable) currentUser);
        context.startActivity(intent);
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

        sharePref = getSharePref();

        viewModel = ViewModelProviders.of(this).get(FragmentsSharedViewModel.class);
        viewModel.currentUser = new User();

        // Get user details from SharedPref
        userDetails = getUserDetails();

        initViews();

        if (savedInstanceState == null) {
            openHomeFragment();
        }


        // Init side nav header
        View headerView = sideNav.getHeaderView(0);

        userName = headerView.findViewById(R.id.nav_header_name);
        userName.setText(userDetails.getFullname());

        navProfilePic = headerView.findViewById(R.id.nav_header_imageView);
        navProfilePic.setOnClickListener(view -> {
            ProfileFragment profileFragment = ProfileFragment.newInstance();

            // Add bundle data containing "token" before navigating to profileFragment
            Bundle data = new Bundle();
            data.putString("token", userDetails.getToken());
            profileFragment.setArguments(data);

            navigateToFragment(profileFragment, MainActivity.this.getString(R.string.title_profile_fragment));

            drawer.closeDrawer(GravityCompat.START);
            sideNav.setCheckedItem(R.id.nav_stub);
            bottomNavigationView.setVisibility(View.GONE);
            for (int i = 0; i < sideNav.getMenu().size(); i++) {
                sideNav.getMenu().getItem(i).setChecked(false);
            }
        });
    }

    private LoggedInUser getUserDetails() {
        String name;
        String email;
        String token;

        token = sharePref.getUserToken();
        name = sharePref.getUserFullname();
        email = sharePref.getUserEmail();

        return new LoggedInUser(token, email, name);
    }

    private void openHomeFragment() {
        HomeFragment homeFragment = new HomeFragment(this);
        navigateToFragment(homeFragment, getString(R.string.title_home_fragment));

        sideNav.getMenu().getItem(0).setChecked(true);

        bottomNavigationView.setSelectedItemId(R.id.bottommenu_home);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initViews() {

        drawer = findViewById(R.id.main_drawer_layout);
        sideNav = findViewById(R.id.main_nav_view);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        updateProfileImage();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        repository = Repository.getInstance(this.getApplication());

        linkUserDetails();

        setupNavigation();
    }

    public void updateProfileImage() {
        View headerView = sideNav.getHeaderView(0);
        byte[] imageBytes = new BedTimeDbHelper(this).getUserImage();

        if (imageBytes == null) return;

        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        CircleImageView civ = headerView.findViewById(R.id.nav_header_imageView);
        civ.setImageBitmap(bmp);
    }


    /*
























     */

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
                    if (viewModel.currentUser.getImage() != null && !viewModel.currentUser.getImage().isEmpty()) {
                        Glide.with(getApplicationContext())
                                .load(viewModel.currentUser.getImage())
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        BitmapDrawable bd = (BitmapDrawable) resource;
                                        Bitmap b = bd.getBitmap();
                                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                                        b.compress(Bitmap.CompressFormat.JPEG, 100, bs);
                                        new BedTimeDbHelper(MainActivity.this).storeUserImage(bs.toByteArray(), MainActivity.this);
                                        return false;
                                    }
                                })
                                .into(navProfilePic);
                        //
                    } else {
                        // Leave default local image if there is none from the api
                    }

                    if (viewModel.currentUser.getFirstName() != null && !viewModel.currentUser.getLastName().isEmpty()
                            && viewModel.currentUser.getLastName() != null && !viewModel.currentUser.getLastName().isEmpty()) {
                        userName.setText(viewModel.currentUser.getFirstName() + " "
                                + viewModel.currentUser.getLastName());
                    } else {
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


    /*@Override
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
*/
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_function).getActionView();
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.search_function) {
            //start search dialog
            onSearchRequested();
            return true;
        }
        return false;
    }*/

    private void setupNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        sideNav.setOverScrollMode(View.OVER_SCROLL_NEVER);
        sideNav.setNavigationItemSelectedListener(menuItem -> {
            MenuItem checkedItem = sideNav.getCheckedItem();
            if (checkedItem != null && checkedItem.getItemId() == menuItem.getItemId()) return false;

            Fragment fragment = null;
            String title = "";

            bottomNavigationView.setVisibility(View.GONE);

            boolean isLogout = false;

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    fragment = new HomeFragment(this);
                    title = getString(R.string.title_home_fragment);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    break;

                case R.id.nav_downloads:
                    fragment = new DownloadsFragment();
                    title = MainActivity.this.getString(R.string.title_downloads_fragment);
                    break;

                case R.id.nav_donate:
                    fragment = new DonateFragment();
                    title = MainActivity.this.getString(R.string.title_donate_fragment);
                    break;

                case R.id.nav_about:
                    fragment = new AboutFragment();
                    title = MainActivity.this.getString(R.string.title_about_fagment);
                    break;

                case R.id.nav_feed_back:
                    fragment = new FeedBackFragment();
                    title = MainActivity.this.getString(R.string.title_feedback_fragment);
                    break;

                case R.id.nav_categories:
                    fragment = CategoriesFragment.newInstance();
                    title = MainActivity.this.getString(R.string.title_categories_fragment);
                    break;

                case R.id.nav_log_out:
                    createLogoutDialog();
                    isLogout = true;
                    break;
            }

            if (isLogout) return true;
            if (fragment == null) {
                fragment = new HomeFragment(this);
                title = MainActivity.this.getString(R.string.title_home_fragment);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

            sideNav.setCheckedItem(menuItem.getItemId());
            drawer.closeDrawer(GravityCompat.START);
            navigateToFragment(fragment, title);

            return true;

        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (bottomNavigationView.getSelectedItemId() == item.getItemId()) return false;

        Fragment fragment = null;
        String title = null;
        int position = 0;

        switch (item.getItemId()) {
            case R.id.bottommenu_home:
                fragment = new HomeFragment(this);
                title = MainActivity.this.getString(R.string.title_home_fragment);
                position = 0;
                break;

            case R.id.bottommenu_bookmark:
                fragment = new BookmarksFragment();
                title = MainActivity.this.getString(R.string.bookmarks);
                position = 1;
                break;

            case R.id.bottommenu_addstory:
                //TODO: Make add new a fragment
                startActivity(new Intent(MainActivity.this, AddStoryActivity.class));
                break;
        }

        if (fragment == null) return false;

        navigateToFragment(fragment, title);
        bottomNavigationView.setSelectedItemId(position);

        return true;
    }

    private void createLogoutDialog(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you really want to leave us?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        signout();
                    }
                })
                .setNegativeButton("No", null).create();

        dialog.show();
    }

    private void signout() {
        /*// Facebook logout
        if (LoginManager.getInstance() != null) {
            LoginManager.getInstance().logOut();
        }
        // Google logout
        if (mGoogleApiClient != null) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    status -> {
                        // ...
                        Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    });
        }*/
        Repository repository = Repository.getInstance(getApplication());
        repository.getStoryApi().logoutUser(getSharePref().getUserToken()).enqueue(new Callback<BaseResponse<DataResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DataResponse>> call, Response<BaseResponse<DataResponse>> response) {

                if (response.isSuccessful()) {
                    sharePref.setIsUserLoggedIn(false);
                    Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                    logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logout);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DataResponse>> call, Throwable t) {
                sharePref.setIsUserLoggedIn(false);
                Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                finish();
            }
        });
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

    public void navigateToFragment(Fragment fragment, String title) {
        currentFragment = fragment;
        if (fragment instanceof HomeFragment || fragment instanceof BookmarksFragment) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else if(fragment instanceof BookmarksFragment) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        else {
            bottomNavigationView.setVisibility(View.GONE);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, currentFragment)
                .commit();
        updateToolbarTitle(title);

    }

    private void hideDrawer() {
        drawer.closeDrawer(GravityCompat.START, false);
    }

    private void updateToolbarTitle(String title) {
        if (getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else if (!(currentFragment instanceof HomeFragment)) {
            // Not in HomeFragment, open home
            openHomeFragment();
        } else {
            doExit();
        }
    }

    private void doExit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this, R.style.AppTheme_Dialog);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> finishAffinity());
        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setTitle(R.string.app_name);
        alertDialog.show();
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
            if (notificationManager == null) {
                showToast("Failed to create notification channel");
                return;
            }
            notificationManager.createNotificationChannel(channel);
        }
    }



    @Override
    public void storiesArrayList(List<Story> storiesList) {
        SearchData.setStories(storiesList);
    }

    // Class to maintain a user object
    private class LoggedInUser {
        private String token;
        private String fullname;
        private String email;

        LoggedInUser(String token, String email, String fullname) {
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
}


