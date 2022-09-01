package elawaves;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.elastos.carrier.FriendInfo;

import androidx.navigation.Navigation;
import elawaves.Fragments.AddFriendFragment;
import elawaves.Fragments.FriendsFragment;
import elawaves.Carrier.Messages.MessageManager;

public class HomeActivity extends AppCompatActivity implements FriendsFragment.OnFriendSelectedListener, AddFriendFragment.OnAddFriendFragmentListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private FriendInfo current_chat_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView menu = findViewById(R.id.home_navigation);
        menu.setOnNavigationItemSelectedListener(this);

        MessageManager.setup(this);
    }

    @Override
    public void onFriendSelected(FriendInfo info) {
        this.current_chat_friend = info;
    }

    @Override
    public void onAddFriend(FriendInfo info) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId())
        {
            case R.id.friends_item:
                Navigation.findNavController(this,R.id.nav_host_fragment_home).navigate(R.id.friendsFragment);
                break;
            case R.id.myinfo_item:
                Navigation.findNavController(this,R.id.nav_host_fragment_home).navigate(R.id.accountInfoFragment);
                break;
            //case R.id.wallet_item:
            //    Navigation.findNavController(this,R.id.nav_host_fragment_home).navigate(R.id.walletFragment);
            //    break;
        }
        return true;
    }
}
