package com.fresent.fresent.drawer;

import android.view.View;
import android.widget.Toast;

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

        static Drawer.OnDrawerItemClickListener comingSoonListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Toast.makeText(view.getContext(), "Coming soon!", Toast.LENGTH_SHORT).show();
                return false;
            }
        };

        static PrimaryDrawerItem home = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_home)
            .withName("My Classes")
            .withIcon(R.drawable.ic_home_pink_400_24dp);

        static PrimaryDrawerItem students = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_students)
            .withName("Students")
            .withIcon(R.drawable.ic_face_pink_400_24dp);

        static PrimaryDrawerItem archive = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_archive)
            .withName("Archive")
            .withIcon(R.drawable.ic_date_range_pink_400_24dp)
            .withOnDrawerItemClickListener(comingSoonListener);


        static PrimaryDrawerItem settings = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_settings)
            .withName("Settings")
            .withIcon(R.drawable.ic_settings_pink_400_24dp)
            .withOnDrawerItemClickListener(comingSoonListener);

        static PrimaryDrawerItem aboutUs = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_about_us)
            .withName("About Us")
            .withOnDrawerItemClickListener(comingSoonListener);

        static PrimaryDrawerItem privacyPolicy = new PrimaryDrawerItem()
            .withIdentifier(R.id.nav_privacy_policy)
            .withName("Privacy Policy")
            .withOnDrawerItemClickListener(comingSoonListener);

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