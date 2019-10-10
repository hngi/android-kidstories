package com.project.android_kidstories.Api.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.android_kidstories.Model.Category;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 10/10/19
 */
public class CategoryAllResponse {
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("method")
        @Expose
        private String method;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private List<Category> data = null;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Category> getData() {
            return data;
        }

        public void setData(List<Category> data) {
            this.data = data;
        }
}


