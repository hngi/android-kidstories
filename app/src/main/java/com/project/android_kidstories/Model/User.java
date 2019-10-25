package com.project.android_kidstories.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
@Entity(tableName = "user")
public class User implements Parcelable, Serializable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @NonNull
    @PrimaryKey
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("admin")
    @Expose
    private Boolean admin;

    @SerializedName("premium")
    @Expose
    private Boolean premium;
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("bookmark_count")
    @Expose
    private Integer bookmarkCount;

    @SerializedName("liked")
    @Expose
    private Integer liked;

    @SerializedName("image_url")
    @Expose
    private String image;

    @SerializedName("phone")
    @Expose
    private String phoneNumber;

    private String designation;
    private String password;

    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("bookmark")
    @Expose
    @Ignore
    private List<Object> bookmark;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User() {
    }

    public User(Parcel in) {
        token = in.readString();
        id = in.readLong();
        name = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        byte tmpAdmin = in.readByte();
        admin = tmpAdmin == 0 ? null : tmpAdmin == 1;
        byte tmpPremium = in.readByte();
        premium = tmpPremium == 0 ? null : tmpPremium == 1;
        if (in.readByte() == 0) {
            bookmarkCount = null;
        } else {
            bookmarkCount = in.readInt();
        }
        if (in.readByte() == 0) {
            liked = null;
        } else {
            liked = in.readInt();
        }
        image = in.readString();
        phoneNumber = in.readString();
        designation = in.readString();
        password = in.readString();
        photo = in.readString();
        title = in.readString();
        role = in.readString();
        bookmark = null;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeByte((byte) (admin == null ? 0 : admin ? 1 : 2));
        dest.writeByte((byte) (premium == null ? 0 : premium ? 1 : 2));
        if (bookmarkCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bookmarkCount);
        }
        if (liked == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(liked);
        }
        dest.writeString(image);
        dest.writeString(phoneNumber);
        dest.writeString(designation);
        dest.writeString(password);
        dest.writeString(photo);
        dest.writeString(title);
        dest.writeString(role);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public List<Object> getBookmark() {
        return bookmark;
    }

    public void setBookmark(List<Object> bookmark) {
        this.bookmark = bookmark;
    }

    public Integer getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(Integer bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
