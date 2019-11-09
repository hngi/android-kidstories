package com.project.android_kidstories.ui.story_viewing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.like.LikeButton;
import com.project.android_kidstories.CommentActivity;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.Repository;
import com.project.android_kidstories.data.model.ReadStory;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.data.source.local.relational.database.StoryLab;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.response_models.bookmark.BookmarkResponse;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryBaseResponse;
import com.project.android_kidstories.data.source.remote.response_models.story.reaction.ReactionResponse;
import com.project.android_kidstories.ui.base.BaseActivity;
import com.project.android_kidstories.ui.reading_status.ReadingStatusActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Locale;

import static com.project.android_kidstories.utils.CommonUtils.loadBitmap;

public class SingleStoryActivity extends BaseActivity {

    public static final String STORY_ID_KEY = "story_id";
    public static final String STORY_NAME_KEY = "story_name";

    private MediaPlayer backgroundMusicPlayer;
    private ImageView story_pic, like_btn;

    int story_id = 0;
    String story_name = "";

    ImageView playButton;
    ImageView markAsReadBtn;
    ImageButton btn_speak;
    private ProgressBar progressBar;
    private Repository repository;
    private Api storyApi;

    View contentView;
    Story currentStory;
    StoryLab storyLab;
    ImageButton btn_stop;
    TextView speak_text;
    LikeButton likeButton;
    TextToSpeech textToSpeech;
    SharePref sharePref;
    Button comment_btn;
    String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";
    ImageButton stopButton;
    private Toolbar toolbar;
    View error_msg;
    private TextView story_author, story_duration, story_title, story_content;
    private ImageView saveStory, likeIcon, dislikeIcon;
    private TextView likeCount, dislikeCount;

    private FloatingActionButton bookmarkFab;

    private ImageButton ZoomIn, ZoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alt_activity_single_story);

        storyLab = StoryLab.get(this);
        sharePref = getSharePref();
        repository = Repository.getInstance(this.getApplication());
        storyApi = repository.getStoryApi();

        story_id = getIntent().getIntExtra(STORY_ID_KEY, 0);
        story_name = getIntent().getStringExtra(STORY_NAME_KEY);

        bookmarkFab = findViewById(R.id.fab_bookmark);
        contentView = findViewById(R.id.nestedscroll_single_story);
        progressBar = findViewById(R.id.story_content_bar);
        playButton = findViewById(R.id.play_story);
        story_author = findViewById(R.id.author);
        story_title = findViewById(R.id.txt_story_title);
        story_content = findViewById(R.id.story_content);
        story_duration = findViewById(R.id.duration);
        story_pic = findViewById(R.id.story_pic);
        error_msg = findViewById(R.id.error_msg);
        saveStory = findViewById(R.id.save_story);

        likeIcon = findViewById(R.id.likeIcon);
        dislikeIcon = findViewById(R.id.dislikeIcon);
        likeCount = findViewById(R.id.likesCount);
        dislikeCount = findViewById(R.id.dislikesCount);

        ZoomIn = findViewById(R.id.Zoom_In);
        ZoomOut = findViewById(R.id.Zoom_Out);
        markAsReadBtn = findViewById(R.id.btn_markasread);

        markAsReadBtn.setOnClickListener(view -> {
            if (markAsReadBtn.isSelected()) return;
            int storiesRead = sharePref.getInt(ReadingStatusActivity.STORIES_READ_KEY);
            storiesRead += 1;
            sharePref.setInt(ReadingStatusActivity.STORIES_READ_KEY, storiesRead);

            ReadingStatusActivity.displayUserReadingStatus(this, storiesRead);

            repository.insertReadStoryId(new ReadStory(String.valueOf(story_id)));

            markAsReadBtn.setSelected(true);

        });

        saveStory.setOnClickListener(view -> {
            if (currentStory == null) return; // In case the content is ever shown when there's no story

            if (storyInDownloads()) {
                // Remove story
                removeSavedStory(currentStory);
            } else {
                // Save story offline
                storyLab.addStory(currentStory);
                // Save the story's image locally too
                BitmapDrawable bitmapDrawable = (BitmapDrawable) story_pic.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                saveImageFile(bitmap
                        , currentStory.getTitle() + ".png");
                showToast("Story is now available offline");
                saveStory.setSelected(true);
            }
        });

        likeIcon.setOnClickListener(v -> {
            // Do nothing if it is liked already
            if (likeIcon.isSelected()) return;

            likeIcon.setSelected(true);
            dislikeIcon.setSelected(false);

            String token = "Bearer " + getSharePref().getUserToken();
            final Context context = this;

            storyApi.likeStory(token, currentStory.getId())
                    .enqueue(new Callback<ReactionResponse>() {
                        @Override
                        public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                            if (response.isSuccessful()) {
                                // Update like count
                                ReactionResponse rr = response.body();
                                if (rr == null) {
                                    likeIcon.setSelected(false);
                                    Toast.makeText(context, "Could not like story", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                likeCount.setText(String.valueOf(rr.getLikesCount()));
                                dislikeCount.setText(String.valueOf(rr.getDislikesCount()));
                                Toast.makeText(context, "Story liked", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReactionResponse> call, Throwable t) {
                            likeIcon.setSelected(false);
                            Toast.makeText(context, "Could not like story, check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        dislikeIcon.setOnClickListener(v -> {
            if (dislikeIcon.isSelected()) return;

            dislikeIcon.setSelected(true);
            likeIcon.setSelected(false);

            String token = "Bearer " + getSharePref().getUserToken();
            final Context context = this;

            storyApi.dislikeStory(token, currentStory.getId())
                    .enqueue(new Callback<ReactionResponse>() {
                        @Override
                        public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                            if (response.isSuccessful()) {
                                // Update like count
                                ReactionResponse rr = response.body();
                                if (rr == null) {
                                    dislikeIcon.setSelected(false);
                                    Toast.makeText(context, "Could not dislike story", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                likeCount.setText(String.valueOf(rr.getLikesCount()));
                                dislikeCount.setText(String.valueOf(rr.getDislikesCount()));
                                Toast.makeText(context, "Story disliked", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReactionResponse> call, Throwable t) {
                            dislikeIcon.setSelected(false);
                            Toast.makeText(context, "Could not dislike story, check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        bookmarkFab.setOnClickListener(view -> {
            if (bookmarkFab.isSelected()) {
                deleteBookmarkedStory(currentStory.getId());
            } else {
                bookmarkStory(currentStory.getId());
            }
        });


        //todo : check authorization for premium stories
        getStoryWithId(story_id);

    }

    private void deleteBookmarkedStory(int id) {
        bookmarkFab.setSelected(false);
        storyApi.deleteBookmarkedStory("Bearer " + getSharePref().getUserToken(), id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SingleStoryActivity.this, "Story deleted from bookmarks", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SingleStoryActivity.this, "Could not delete story from bookmarks", Toast.LENGTH_SHORT).show();
                            bookmarkFab.setSelected(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SingleStoryActivity.this, "Could not delete story from bookmarks", Toast.LENGTH_SHORT).show();
                        bookmarkFab.setSelected(true);
                    }
                });
    }

    private void bookmarkStory(int id) {
        bookmarkFab.setSelected(true);
        storyApi.bookmarkStory("Bearer " + getSharePref().getUserToken(), id)
                .enqueue(new Callback<BookmarkResponse>() {
                    @Override
                    public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SingleStoryActivity.this, "Story added to bookmarks", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SingleStoryActivity.this, "Could not add story to bookmarks", Toast.LENGTH_SHORT).show();
                            bookmarkFab.setSelected(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<BookmarkResponse> call, Throwable t) {
                        Toast.makeText(SingleStoryActivity.this, "Could not add story to bookmarks", Toast.LENGTH_SHORT).show();
                        bookmarkFab.setSelected(false);
                    }
                });

    }

    /**
     * Returns true if story has been downloaded already
     */
    private boolean storyInDownloads() {
        Story storyDownloaded = storyLab.getStory(story_name);
        return storyDownloaded != null;
    }

    private void updateIcons() {
        // Check if story is in the downloads
        if (storyInDownloads()) {
            saveStory.setSelected(true);
        } else {
            saveStory.setSelected(false);
        }

        // Check if story has been read already
        repository.getStoryForId(String.valueOf(story_id)).observe(this, readStory -> {
            if (readStory != null) {
                // It has been read
                markAsReadBtn.setSelected(true);
            } else {
                markAsReadBtn.setSelected(false);
            }
        });

        //check user's previous reaction to story
        Toast.makeText(this, String.valueOf(currentStory.isLiked()), Toast.LENGTH_SHORT).show();
        likeIcon.setSelected(currentStory.isLiked());
        dislikeIcon.setSelected(currentStory.isDisliked());

        likeCount.setText(String.valueOf(currentStory.getLikesCount()));
        dislikeCount.setText(String.valueOf(currentStory.getDislikesCount()));

        // bookmark
        if (currentStory.isBookmark()) {
            bookmarkFab.setSelected(true);
        } else {
            bookmarkFab.setSelected(false);
        }

    }

    private void saveImageFile(Bitmap b, String picName) {
        FileOutputStream fos;
        try {
            fos = openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            showToast("Can't save story image");
            Log.d("TAG", "file not found");
            e.printStackTrace();
        }
    }

    private void showViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    private void hideViews(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    private void updateViews() {
        if (currentStory == null) {
            // Show an error
            showViews(error_msg);
            hideViews(progressBar, contentView);
            return;
        }

        // currentStory is not null, display views
        showViews(contentView);
        hideViews(progressBar, error_msg);
        // Populate views
        story_title.setText(currentStory.getTitle());
        story_author.setText(String.format("Written by %s", currentStory.getAuthor()));
        story_content.setText(currentStory.getBody());
        story_duration.setText(currentStory.getStoryDuration());

        Glide.with(this)
                .load(currentStory.getImageUrl())
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Image load failed, try to get it offline
                        Bitmap bitmap = loadBitmap(SingleStoryActivity.this, currentStory.getTitle() + ".png");
                        if (bitmap != null) {
                            story_pic.setImageBitmap(bitmap);
                        }
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(story_pic);

        updateIcons();
    }

    private Story getStoryOffline() {
        StoryLab storyLab = StoryLab.get(this);
        return storyLab.getStory(story_name);
    }

    public void getStoryWithId(int id) {
        storyApi.getStoryWithAuth("Bearer " + getSharePref().getUserToken(), id).enqueue(new Callback<StoryBaseResponse>() {
            @Override
            public void onResponse(Call<StoryBaseResponse> call, Response<StoryBaseResponse> response) {
                StoryBaseResponse storyBaseResponse = response.body();

                if (response.isSuccessful() && storyBaseResponse != null) {
                    currentStory = storyBaseResponse.getData();
                } else {
                    // Try to get story offline
                    currentStory = getStoryOffline();
                }

                updateViews();
            }

            @Override
            public void onFailure(Call<StoryBaseResponse> call, Throwable t) {
                // Try to get story offline
                currentStory = getStoryOffline();
                updateViews();
            }
        });


        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.ENGLISH);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(SingleStoryActivity.this, "This Language is not Supported", Toast.LENGTH_SHORT).show();
                } else {
                    //btn_speak.setEnabled(true);
                    //textToSpeech.setPitch(0.6f);
                    textToSpeech.setSpeechRate(0.85f);
                }
            }

        }, picoPackage);

        //background Music and tts
        backgroundMusicPlayer = MediaPlayer.create(this, R.raw.kidsong2);
        playButton.setOnClickListener(v -> {
            if (backgroundMusicPlayer.isPlaying()) {
                playButton.setSelected(false);
                textToSpeech.stop();
                backgroundMusicPlayer.pause();
            } else {
                playButton.setSelected(true);
                play();
                speak();
            }
        });

    }

    private void play() {
        backgroundMusicPlayer.seekTo(0);
        backgroundMusicPlayer.start();
        backgroundMusicPlayer.setLooping(true);
    }

    public void sendCommentList(View view) {
        Intent intent = new Intent(SingleStoryActivity.this, CommentActivity.class);
        intent.putExtra("storyId", story_id);
        startActivity(intent);
    }

    private void speak() {
        String text = story_content.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        textToSpeech.stop();
        backgroundMusicPlayer.pause();
        playButton.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
        }

        super.onDestroy();
    }

    public void removeSavedStory(Story story) {
        new AlertDialog.Builder(SingleStoryActivity.this, R.style.AppTheme_Dialog)
                .setMessage(SingleStoryActivity.this.getString(R.string.remove_from_downloads))
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    storyLab.deleteStory(story);
                    saveStory.setSelected(false);
                    showToast("Story removed from downloads");
                })
                .setNegativeButton("No", null).show();
    }

    public void back(View view) {
        finish();
    }
}
