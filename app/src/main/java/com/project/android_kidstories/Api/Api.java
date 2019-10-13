package com.project.android_kidstories.Api;

import android.net.Uri;

import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.Api.Responses.Category.CategoryStoriesResponse;
import com.project.android_kidstories.Api.Responses.comment.CommentResponse;
import com.project.android_kidstories.Api.Responses.loginRegister.DataResponse;
import com.project.android_kidstories.Api.Responses.loginRegister.LoginResponse;
import com.project.android_kidstories.Api.Responses.story.StoryBaseResponse;
import com.project.android_kidstories.Api.Responses.story.Reaction.ReactionResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Model.Category;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.Model.User;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Api {

    //Authentication APIs
    @POST("auth/register")
    Call<BaseResponse<DataResponse>> registerUser(@Body User user);

    @POST("auth/login")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password);

    //TODO: May be an API issues
    @Headers("Content-Type:application/json")
    @POST("auth/logout")
    Call<BaseResponse<DataResponse>> logoutUser(@Header("Authorization") String token);

    @GET("auth/user")
    Call<DataResponse> getUser(@Header("Authorization")String token);

    @GET("auth/change-password")
    Call<BaseResponse<DataResponse>> changeUserPassword(@Header("Authorization") String token,
                                                        @Field("old_password") String oldPassword,
                                                        @Field("new_password") String newPassword,
                                                        @Field("confirm_password") String confirmPassword);

    //DbUserClass APIs
    @GET("users")
    Call<List<DataResponse>> getAllUsers();

    @GET("users/profile")
    Call<BaseResponse<User>> getUserProfile(@Header("Authorization") String token);

    @PUT("users/profile")
    Call<BaseResponse<User>> updateUserProfile(@Header("Authorization") String token, @Body User user);

    @PUT("users/profile/update-image")
    Call<BaseResponse<DataResponse>> updateUserProfilePicture(@Header("Authorization") String token,
                                                              @Field("Authorization") String confirmToken,
                                                              @Field("photo")Uri photoUri);

    //Story APIs

    @GET("stories/{id}")
    Call<StoryBaseResponse> getStory(@Path("id") int id);

    @GET("stories")
    Call<StoryAllResponse> getAllStories();

    /***
     *
     * @param token
     * @param title
     * @param story
     * @param categoryId
     * @param ageInRange: this field should be entered in a range format like 1-5, 5-37, 45-78
     * @param author
     * @param storyDuration
     * @param image
     * @return
     */
    @Multipart
    @POST("stories")
    Call<BaseResponse<Story>> addStory(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("body") RequestBody story,
            @Part("category_id") RequestBody categoryId,
            @Part("age") RequestBody ageInRange,
            @Part("author") RequestBody author,
            @Part("story_duration") RequestBody storyDuration,
            @Part MultipartBody.Part image
    );


    /***
     *
     * @param token
     * @param title
     * @param story
     * @param categoryId
     * @param ageInRange: this field should be entered in a range format like 1-5, 5-37, 45-78
     * @param author
     * @param storyDuration
     * @param image
     * @return
     */
    @Multipart
    @POST("stories/{id}")
    Call<BaseResponse<Story>> updateStory(
            @Header("Authorization") String token,
            @Part("id") Integer storyId,
            @Part("title") RequestBody title,
            @Part("body") RequestBody story,
            @Part("category_id") RequestBody categoryId,
            @Part("age") RequestBody ageInRange,
            @Part("author") RequestBody author,
            @Part("story_duration") RequestBody storyDuration,
            @Part MultipartBody.Part image
    );

    @POST("stories/{storyId}/reactions/like")
    Call<ReactionResponse> likeStory(@Header("Authorization") String token,
                                     @Part("storyId") Integer storyId);

    @POST("stories/{storyId}/reactions/dislike")
    Call<ReactionResponse> dislikeStory(@Header("Authorization") String token,
                                      @Part("storyId") Integer storyId);



    //Bookmark APIs
    @POST("bookmarks/stories/{storyId}")
    Call<BookmarkResponse> bookmarkStory(@Header("Authorization") String token,
                                         @Part("storyId") Integer storyId);

    @DELETE("bookmarks/stories/{storyId}")
    Call<Void> deleteBookmarkedStory(@Header("Authorization") String token,
                                     @Part("storyId") Integer storyId);

    @GET("bookmarks/stories/{storyId}/status")
    Call<BookmarkResponse> getStoryBookmarkStatus(@Header("Authorization") String token,
                                                  @Part("storyId") Integer storyId);



    //Catergory APIs
    @GET("categories")
    Call<BaseResponse<List<Category>>> getAllCategories();

    @GET("categories/{id}")
    Call<BaseResponse<Category>> getCategory(@Part("id") Integer categoryId);

    @GET("catergories/{id}/stories")
    Call<BaseResponse<CategoryStoriesResponse>> getStoriesByCategoryIdandUser(@Part("id") Integer categoryId);



    //Comment APIs
    @Multipart
    @POST("comments")
    Call<BaseResponse<CommentResponse>> addComment(@Header("Authorization") String token,
                                                   @Part("story_id") RequestBody storyId,
                                                   @Part("body") RequestBody comment);

    @PUT("comments/{id}")
    Call<BaseResponse<CommentResponse>> updateComment(@Header("Authorization") String token,
                                                      @Part("id") Integer commentId,
                                                      @Field("body") String comment);

    @DELETE("comments/{id}")
    Call<Void> deleteComment(@Header("Authorization") String token,
                             @Part("id") Integer commentId);


}
