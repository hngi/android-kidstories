package com.project.android_kidstories.data.source.remote.api;

import com.project.android_kidstories.data.model.Category;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.model.User;
import com.project.android_kidstories.data.source.remote.response_models.BaseResponse;
import com.project.android_kidstories.data.source.remote.response_models.BaseResponse2;
import com.project.android_kidstories.data.source.remote.response_models.bookmark.BookmarkResponse;
import com.project.android_kidstories.data.source.remote.response_models.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.data.source.remote.response_models.category.CategoriesAllResponse;
import com.project.android_kidstories.data.source.remote.response_models.category.CategoryStoriesResponse;
import com.project.android_kidstories.data.source.remote.response_models.comment.CommentResponse;
import com.project.android_kidstories.data.source.remote.response_models.loginRegister.DataResponse;
import com.project.android_kidstories.data.source.remote.response_models.loginRegister.LoginResponse;
import com.project.android_kidstories.data.source.remote.response_models.loginRegister.RegistrationDataResponse;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryAllResponse;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryBaseResponse;
import com.project.android_kidstories.data.source.remote.response_models.story.reaction.ReactionResponse;
import com.project.android_kidstories.data.source.remote.response_models.story.reaction.StoryPageResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface Api {

    //Authentication APIs
    @POST("auth/register")
    Call<BaseResponse<RegistrationDataResponse>> registerUser(@Body User user);

    @POST("auth/login")
    @FormUrlEncoded
    Call<LoginResponse> loginUser(@Field("email") String email, @Field("password") String password);

    //TODO: May be an API issues
    @Headers("Content-Type:application/json")
    @POST("auth/logout")
    Call<BaseResponse<DataResponse>> logoutUser(@Header("Authorization") String token);

    @GET("auth/user")
    Call<BaseResponse<DataResponse>> getUser(@Header("Authorization") String token);

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

    @Multipart
    @POST("users/profile/update-image")
    Call<BaseResponse<Void>> updateUserProfilePicture(@Header("Authorization") String token, @Part MultipartBody.Part file);


    @Multipart
    @POST("users/profile/update-image")
    Call<ResponseBody> uploadUserImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file,
            @Part("name") RequestBody requestBody
    );


    //Story APIs

    @GET("stories/{id}")
    Call<StoryBaseResponse> getStory(@Path("id") int id);

    @GET("stories/{id}")
    Call<StoryBaseResponse> getStoryWithAuth(@Header("Authorization") String token, @Path("id") int id);

    @GET("stories")
    Call<StoryAllResponse> getAllStories(@Query("page") String page);

    @GET("stories")
    Call<StoryAllResponse> getAllStoriesWithAuth(@Header("Authorization") String token, @Query("page") String page);

    @GET("stories")
    Call<BaseResponse<StoryPageResponse>> getAllStoriesWithAuth2(@Header("Authorization") String token);


    /***
     *
     * @param token
     * @param title
     * @param body
     * @param categoryId
     * @param photo
     * @param ageInRange: this field should be entered in a range format like 1-5, 5-37, 45-78
     * @param author
     * @param //storyDuration
     * @return
     */
    @Multipart
    @POST("stories")
//    @Headers({
//            "Content-Type: application/json",
//            "Accept: application/json",
//            "Accept: multipart/form-data"
//    })
    Call<BaseResponse<Story>> addStory(
            @Header("Authorization") String token,
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("category_id") RequestBody categoryId,
            @Part MultipartBody.Part photo,
            @Part("age") RequestBody ageInRange,
            @Part("author") RequestBody author
    );

    //    @Multipart
//    @POST("stories")
//    Call<BaseResponse<Story>> addStory(
//            @Header("Authorization") String token,
//            @Part("title") RequestBody title,
//            @Part("body") RequestBody story,
//            @Part("category_id") RequestBody categoryId,
//            @Part("age") RequestBody ageInRange,
//            @Part("author") RequestBody author,
//           // @Part("story_duration") RequestBody storyDuration,
//            @Part("image\"; filename=\"myfile.jpg\" ") RequestBody image
////            @Part MultipartBody.Part image
//    );


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
                                     @Path("storyId") Integer storyId);

    @POST("stories/{storyId}/reactions/dislike")
    Call<ReactionResponse> dislikeStory(@Header("Authorization") String token,
                                        @Path("storyId") Integer storyId);


    //Bookmark APIs
    @POST("bookmarks/stories/{storyId}")
    Call<BookmarkResponse> bookmarkStory(@Header("Authorization") String token,
                                         @Path("storyId") Integer storyId);

    @DELETE("bookmarks/stories/{storyId}")
    Call<Void> deleteBookmarkedStory(@Header("Authorization") String token,
                                     @Path("storyId") Integer storyId);

    @GET("bookmarks/stories/{storyId}/status")
    Call<BookmarkResponse> getStoryBookmarkStatus(@Header("Authorization") String token,
                                                  @Path("storyId") Integer storyId);

    @GET("bookmarks/stories")
    Call<UserBookmarkResponse> getUserBookmarks(@Header("Authorization") String token);


    //Catergory APIs
    @GET("categories")
    Call<CategoriesAllResponse> getAllCategories();
    //Call<BaseResponse<List<Category>>> getAllCategories();

    @GET("categories/{id}")
    Call<BaseResponse<Category>> getCategory(@Path("id") String categoryId);

    @GET("categories/{id}/stories")
    Call<BaseResponse<CategoryStoriesResponse>> getStoriesByCategoryIdandUser(@Path("id") String categoryId);

    @GET("categories/{id}/stories")
    Call<BaseResponse2> getStoriesByCategoryIdandUser2(@Path("id") String categoryId);


    //Comment APIs
    @Multipart
    @POST("comments")
    Call<BaseResponse<CommentResponse>> addComment(@Header("Authorization") String token,
                                                   @Part("body") RequestBody comment,
                                                   @Part("story_id") Integer storyId);

    @PUT("comments/{id}")
    Call<BaseResponse<CommentResponse>> updateComment(@Header("Authorization") String token,
                                                      @Path("id") Integer commentId,
                                                      @Field("body") String comment);

    @DELETE("comments/{id}")
    Call<Void> deleteComment(@Header("Authorization") String token,
                             @Path("id") Integer commentId);


}
