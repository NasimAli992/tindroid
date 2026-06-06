package co.tinode.tindroid.ui;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.tinode.tindroid.R;

/**
 * Main Activity with Modern Bottom Navigation Bar
 * 
 * Integration guide:
 * 1. Replace existing navigation implementation with BottomNavigationController
 * 2. Add bottom_navigation_bar.xml to your main layout
 * 3. Initialize BottomNavigationController in onCreate()
 * 4. Implement tab switching logic in listener callbacks
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationController.BottomNavListener {
    
    private BottomNavigationController navigationController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize bottom navigation bar
        initializeBottomNavigation();
    }
    
    /**
     * Initialize the modern bottom navigation bar
     */
    private void initializeBottomNavigation() {
        // Get navigation item views from the layout
        FrameLayout navNotifications = findViewById(R.id.nav_notifications);
        FrameLayout navChannels = findViewById(R.id.nav_channels);
        FrameLayout navMessages = findViewById(R.id.nav_messages);
        FrameLayout navVoiceClub = findViewById(R.id.nav_voice_club);
        
        // Verify views are not null
        if (navNotifications == null || navChannels == null || 
            navMessages == null || navVoiceClub == null) {
            throw new IllegalStateException("Bottom navigation views not found. " +
                    "Ensure bottom_navigation_bar.xml is included in activity_main.xml");
        }
        
        // Create and initialize controller
        navigationController = new BottomNavigationController();
        navigationController.initialize(navNotifications, navChannels, navMessages, navVoiceClub, this);
        
        // Show initial notification badge for demonstration
        navigationController.showNotificationBadge();
    }
    
    @Override
    public void onTabSelected(int tabIndex, String tabName) {
        // Handle tab selection
        switch (tabIndex) {
            case 0: // Notifications
                showNotificationsFragment();
                Toast.makeText(this, "Notifications selected", Toast.LENGTH_SHORT).show();
                break;
            case 1: // Channels
                showChannelsFragment();
                Toast.makeText(this, "Channels selected", Toast.LENGTH_SHORT).show();
                break;
            case 2: // Messages
                showMessagesFragment();
                Toast.makeText(this, "Messages selected", Toast.LENGTH_SHORT).show();
                break;
            case 3: // Voice Club
                showVoiceClubFragment();
                Toast.makeText(this, "Voice Club selected", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    
    @Override
    public void onTabReselected(int tabIndex, String tabName) {
        // Handle reselection (scroll to top, refresh, etc.)
        Toast.makeText(this, tabName + " reselected", Toast.LENGTH_SHORT).show();
    }
    
    // Fragment display methods - implement according to your app structure
    private void showNotificationsFragment() {
        // TODO: Replace with actual fragment transaction
    }
    
    private void showChannelsFragment() {
        // TODO: Replace with actual fragment transaction
    }
    
    private void showMessagesFragment() {
        // TODO: Replace with actual fragment transaction
    }
    
    private void showVoiceClubFragment() {
        // TODO: Replace with actual fragment transaction
    }
}