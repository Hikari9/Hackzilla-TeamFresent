package com.fresent.fresent.drawer;

import com.fresent.fresent.R;
import com.fresent.fresent.base.BaseActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class DrawerFactory {

    private static class NavItems {

        static PrimaryDrawerItem home = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_home)
            .withName(R.string.drawer_home)
            .withIcon(R.drawable.ic_home_pink_400_24dp);

        /*
        static PrimaryDrawerItem gallery = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_gallery)
            .withName(R.string.drawer_gallery)
            .withIcon(R.drawable.ic_photo_library_pink_400_24dp);
        */

        static PrimaryDrawerItem students = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_students)
            .withName("Drawer Students")
            .withIcon(R.drawable.ic_face_pink_400_24dp);

        static PrimaryDrawerItem archive = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_archive)
            .withName("Archive")
            .withIcon(R.drawable.ic_date_range_pink_400_24dp);

        static PrimaryDrawerItem settings = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_settings)
            .withName("Settings")
            .withIcon(R.drawable.ic_settings_pink_400_24dp);

        static PrimaryDrawerItem aboutUs = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_about_us)
            .withName("About Us");

        static PrimaryDrawerItem privacyPolicy = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_privacy_policy)
            .withName("Privacy Policy");

        static ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem()
            .withName("Professor Charles Xavier")
            .withEmail("charlesxavier@example.com")
            .withIcon(R.drawable.professor_x);

        static IDrawerItem[] drawerItems = {
            home,
            // gallery,
            students,
            archive,
            settings,
            new DividerDrawerItem(),
            aboutUs,
            privacyPolicy
        };

    }

    public static Drawer createDrawerFor(BaseActivity activity) {

        // add header
        AccountHeader header = new AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.color.primary_dark)
            .addProfiles(NavItems.profileDrawerItem)
            .build();

        return new DrawerBuilder()
            .withActivity(activity)
            .withToolbar(activity.getToolbar())
            .withAccountHeader(header)
            .addDrawerItems(NavItems.drawerItems)
            .build();

    }

}