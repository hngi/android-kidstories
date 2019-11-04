package com.project.android_kidstories.viewModel;

import androidx.lifecycle.ViewModel;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.model.User;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 10/22/19
 */
public class FragmentsSharedViewModel extends ViewModel {

    public User currentUser;
    public List<Story> currentUsersStories;


   public void setUser(User user){
       currentUser = user;
   }
}
