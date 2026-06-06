package co.tinode.tindroid.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * Controller for Modern Bottom Navigation Bar
 * Handles smooth animations, tab switching, and state management
 * Features: Glassmorphism design, premium animations, responsive UI
 * 
 * Inspired by: Telegram, Discord, WhatsApp, Instagram
 * API Level: 21+
 */
public class BottomNavigationController {
    
    // Navigation Item Views
    private FrameLayout navNotifications;
    private FrameLayout navChannels;
    private FrameLayout navMessages;
    private FrameLayout navVoiceClub;
    
    // Current active tab index
    private int activeTabIndex = 2; // Messages is default active
    private BottomNavListener listener;
    
    // Animation duration in milliseconds
    private static final long ANIMATION_DURATION = 300;
    private static final long SCALE_DURATION = 250;
    
    /**
     * Interface for tab selection callbacks
     */
    public interface BottomNavListener {
        void onTabSelected(int tabIndex, String tabName);
        void onTabReselected(int tabIndex, String tabName);
    }
    
    /**
     * Initialize the bottom navigation bar with views
     * @param notificationsView Notifications tab container
     * @param channelsView Channels tab container
     * @param messagesView Messages tab container (primary)
     * @param voiceClubView Voice Club tab container
     * @param listener Callback listener for tab events
     */
    public void initialize(@NonNull FrameLayout notificationsView,
                          @NonNull FrameLayout channelsView,
                          @NonNull FrameLayout messagesView,
                          @NonNull FrameLayout voiceClubView,
                          @NonNull BottomNavListener listener) {
        this.navNotifications = notificationsView;
        this.navChannels = channelsView;
        this.navMessages = messagesView;
        this.navVoiceClub = voiceClubView;
        this.listener = listener;
        
        // Set up click listeners for each tab
        setupTabClickListeners();
        
        // Set initial state (Messages tab active)
        setActiveTab(2);
    }
    
    /**
     * Setup click listeners for all navigation items
     */
    private void setupTabClickListeners() {
        navNotifications.setOnClickListener(v -> selectTab(0, "notifications"));
        navChannels.setOnClickListener(v -> selectTab(1, "channels"));
        navMessages.setOnClickListener(v -> selectTab(2, "messages"));
        navVoiceClub.setOnClickListener(v -> selectTab(3, "voice_club"));
    }
    
    /**
     * Select a tab with smooth animations
     * @param tabIndex Index of the tab (0-3)
     * @param tabName Name of the tab
     */
    private void selectTab(int tabIndex, String tabName) {
        // If the same tab is selected, trigger reselection callback
        if (activeTabIndex == tabIndex) {
            if (listener != null) {
                listener.onTabReselected(tabIndex, tabName);
            }
            return;
        }
        
        // Animate to new tab
        setActiveTab(tabIndex);
        
        // Notify listener
        if (listener != null) {
            listener.onTabSelected(tabIndex, tabName);
        }
    }
    
    /**
     * Set the active tab with animations
     * @param tabIndex Index of the tab to activate (0-3)
     */
    private void setActiveTab(int tabIndex) {
        // Deactivate previous tab
        deactivateTab(activeTabIndex);
        
        // Activate new tab
        activateTab(tabIndex);
        
        // Update active index
        activeTabIndex = tabIndex;
    }
    
    /**
     * Activate a tab with scale and color animation
     * @param tabIndex Index of the tab to activate
     */
    private void activateTab(int tabIndex) {
        FrameLayout tabView = getTabView(tabIndex);
        if (tabView == null) return;
        
        // Get icon and label views
        ImageView icon = tabView.findViewById(getIconId(tabIndex));
        View glowView = tabView.findViewById(co.tinode.tindroid.R.id.glow_background);
        
        // Create animation set for simultaneous animations
        AnimatorSet animatorSet = new AnimatorSet();
        
        // Scale animation for icon
        if (icon != null) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(icon, View.SCALE_X, 1f, 1.15f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1f, 1.15f);
            scaleX.setDuration(SCALE_DURATION);
            scaleY.setDuration(SCALE_DURATION);
            scaleX.setInterpolator(new OvershootInterpolator(1.2f));
            scaleY.setInterpolator(new OvershootInterpolator(1.2f));
            
            animatorSet.playTogether(scaleX, scaleY);
        }
        
        // Glow animation - fade in and scale
        if (glowView != null) {
            ObjectAnimator glowAlpha = ObjectAnimator.ofFloat(glowView, View.ALPHA, 0.1f, 0.4f);
            ObjectAnimator glowScale = ObjectAnimator.ofFloat(glowView, View.SCALE_X, 0.8f, 1.2f);
            ObjectAnimator glowScaleY = ObjectAnimator.ofFloat(glowView, View.SCALE_Y, 0.8f, 1.2f);
            
            glowAlpha.setDuration(ANIMATION_DURATION);
            glowScale.setDuration(ANIMATION_DURATION);
            glowScaleY.setDuration(ANIMATION_DURATION);
            
            animatorSet.playTogether(glowAlpha, glowScale, glowScaleY);
        }
        
        animatorSet.start();
    }
    
    /**
     * Deactivate a tab with reverse animations
     * @param tabIndex Index of the tab to deactivate
     */
    private void deactivateTab(int tabIndex) {
        FrameLayout tabView = getTabView(tabIndex);
        if (tabView == null) return;
        
        // Get icon and label views
        ImageView icon = tabView.findViewById(getIconId(tabIndex));
        View glowView = tabView.findViewById(co.tinode.tindroid.R.id.glow_background);
        
        // Create animation set
        AnimatorSet animatorSet = new AnimatorSet();
        
        // Scale back to normal
        if (icon != null) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(icon, View.SCALE_X, 1.15f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(icon, View.SCALE_Y, 1.15f, 1f);
            scaleX.setDuration(SCALE_DURATION);
            scaleY.setDuration(SCALE_DURATION);
            
            animatorSet.playTogether(scaleX, scaleY);
        }
        
        // Glow fade out
        if (glowView != null) {
            ObjectAnimator glowAlpha = ObjectAnimator.ofFloat(glowView, View.ALPHA, 0.4f, 0.1f);
            ObjectAnimator glowScale = ObjectAnimator.ofFloat(glowView, View.SCALE_X, 1.2f, 0.8f);
            ObjectAnimator glowScaleY = ObjectAnimator.ofFloat(glowView, View.SCALE_Y, 1.2f, 0.8f);
            
            glowAlpha.setDuration(ANIMATION_DURATION);
            glowScale.setDuration(ANIMATION_DURATION);
            glowScaleY.setDuration(ANIMATION_DURATION);
            
            animatorSet.playTogether(glowAlpha, glowScale, glowScaleY);
        }
        
        animatorSet.start();
    }
    
    /**
     * Show notification badge on Notifications tab
     */
    public void showNotificationBadge() {
        View badge = navNotifications.findViewById(co.tinode.tindroid.R.id.badge_notifications);
        if (badge != null) {
            badge.setVisibility(View.VISIBLE);
            addPulsingAnimation(badge);
        }
    }
    
    /**
     * Hide notification badge
     */
    public void hideNotificationBadge() {
        View badge = navNotifications.findViewById(co.tinode.tindroid.R.id.badge_notifications);
        if (badge != null) {
            badge.setVisibility(View.GONE);
        }
    }
    
    /**
     * Get the current active tab index
     */
    public int getActiveTabIndex() {
        return activeTabIndex;
    }
    
    /**
     * Get tab view by index
     */
    private FrameLayout getTabView(int tabIndex) {
        switch (tabIndex) {
            case 0: return navNotifications;
            case 1: return navChannels;
            case 2: return navMessages;
            case 3: return navVoiceClub;
            default: return null;
        }
    }
    
    /**
     * Get icon view ID by tab index
     */
    private int getIconId(int tabIndex) {
        switch (tabIndex) {
            case 0: return co.tinode.tindroid.R.id.icon_notifications;
            case 1: return co.tinode.tindroid.R.id.icon_channels;
            case 2: return co.tinode.tindroid.R.id.icon_messages;
            case 3: return co.tinode.tindroid.R.id.icon_voice_club;
            default: return -1;
        }
    }
    
    /**
     * Add pulsing animation to notification badge
     */
    private void addPulsingAnimation(View view) {
        ObjectAnimator pulse1 = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 1.3f);
        ObjectAnimator pulse2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 1.3f);
        
        pulse1.setDuration(600);
        pulse2.setDuration(600);
        pulse1.setRepeatCount(ObjectAnimator.INFINITE);
        pulse2.setRepeatCount(ObjectAnimator.INFINITE);
        pulse1.setRepeatMode(ObjectAnimator.REVERSE);
        pulse2.setRepeatMode(ObjectAnimator.REVERSE);
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(pulse1, pulse2);
        animatorSet.start();
    }
}