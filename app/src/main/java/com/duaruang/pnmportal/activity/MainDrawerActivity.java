package com.duaruang.pnmportal.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.News;
import com.duaruang.pnmportal.data.Pegawai;
import com.duaruang.pnmportal.firebase.AppFirebaseMessageService;
import com.duaruang.pnmportal.firebase.AppNotificationManager;
import com.duaruang.pnmportal.fragment.MainFragment;
import com.duaruang.pnmportal.preference.AppPreference;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import com.duaruang.pnmportal.R;

import io.fabric.sdk.android.Fabric;

public class MainDrawerActivity extends BaseActivity implements MainFragment.OnFragmentInteractionListener{

    private Pegawai userSSOModel;
    private static final int PROFILE_SETTING = 100000;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    String username, email = null;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
//      setContentView(R.layout.activity_main_drawer);
        Fabric.with(this, new Crashlytics());
        //Fetching data from shared preferences
        userSSOModel    = AppPreference.getInstance().getUserSSOLoggedIn();
        username        = userSSOModel.getUsername();
        email           = userSSOModel.getEmail();

        //Remove line to test RTL support
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName(username).withEmail(email).withIcon(R.drawable.profile).withIdentifier(100);
//        final IProfile profile2 = new ProfileDrawerItem().withName("Demo User").withEmail("demo@github.com").withIcon("https://avatars2.githubusercontent.com/u/3597376?v=3&s=460").withIdentifier(101);
//        final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2).withIdentifier(102);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.md_blue_800, getApplicationContext().getTheme())))
                .withSelectionListEnabledForSingleProfile(false)
                .withCompactStyle(true)
                .addProfiles(profile)
//                .addProfiles(
//                        profile,
////                        profile2,
//
//                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
//                        new ProfileSettingDrawerItem().withName("Change Photo").withDescription("Change your photo").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_photo_size_select_small).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING)
////                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
//                )
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intentProfile = new Intent(getApplicationContext(), ProfileScreenActivity.class);
                        startActivity(intentProfile);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        Intent intentProfile = new Intent(getApplicationContext(), ProfileScreenActivity.class);
                        startActivity(intentProfile);
                        return false;
                    }
                })
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
//                        //sample usage of the onProfileChanged listener
//                        //if the clicked item has the identifier 1 add a new profile ;)
//                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
//                            int count = 100 + headerResult.getProfiles().size() + 1;
//                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Agus" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile).withIdentifier(count);
//                            if (headerResult.getProfiles() != null) {
//                                //we know that there are 2 setting elements. set the new profile above them ;)
//                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
//                            } else {
//                                headerResult.addProfiles(newProfile);
//                            }
//                        }
//
//                        //false if you have not consumed the event and it should close the drawer
//                        return false;
//                    }
//                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
//                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header

                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_news).withIcon(R.drawable.iconnewsdetail1).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_event).withIcon(R.drawable.iconevendetail1).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_faq).withIcon(R.drawable.icon_faq).withIdentifier(4).withSelectable(false)
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_stop).withIdentifier(30).withSelectable(false)
//                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_stop).withIdentifier(30).withSelectable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;

                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(MainDrawerActivity.this, NewsActivity.class);
                            } else
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(MainDrawerActivity.this, EventActivity.class);
                            } else
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainDrawerActivity.this, SettingsActivity.class);
                            } else
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(MainDrawerActivity.this, FaqActivity.class);
                            } else
                            if (drawerItem.getIdentifier() == 30) {
                                logout();
                            }
                            if (intent != null) {
                                MainDrawerActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })

                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withSelectedItem(-1)
//              .withShowDrawerUntilDraggedOpened(true)
                .build();

        result.addStickyFooterItem(new PrimaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(R.drawable.iconlogout).withIdentifier(30).withSelectable(false));

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(-1, false);

            //set the active profile
            headerResult.setActiveProfile(profile);


        }
        if (result.isDrawerOpen())
            result.closeDrawer();

        // update badge
//        result.updateBadge(2, new StringHolder(10 + ""));
        //Set default home
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.main_fragment_container, new MainFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    private void showLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String tag = bundle.getString(AppNotificationManager.EXTRA_TAG);
            if (tag!=null && tag.equals(AppFirebaseMessageService.TYPE_FORCE_LOGOUT)) {
                logout();
                showLogin();
                return;
            }
        }

        if (bundle != null) {
            String tag = bundle.getString(AppNotificationManager.EXTRA_TAG);
            News dataModel = bundle.getParcelable(AppNotificationManager.EXTRA_DATA);
            if (dataModel != null) {
                if (tag!=null && tag.equals(AppFirebaseMessageService.TYPE_NEWS_GENERAL)) {
                    showNewsDetail(dataModel);
                } else if (tag!=null && tag.equals(AppFirebaseMessageService.TYPE_NEWS_PRIVATE)) {
                    showNewsDetail(dataModel);
                }
                //} else if (tag!=null && tag.equals(AppFirebaseMessageService.TYPE_EVENT)) {
                //   showPengajuan(dataModel);
                //}
            }
        }
    }

    private void showNewsDetail(News model) {
        Intent intent = new Intent(this, NewsGeneralDetailActivity.class);
        intent.putExtra("title", model.getTitle());
        intent.putExtra("description", model.getDescription());
        intent.putExtra("picture", model.getPicture());
        intent.putExtra("url_download", model.getUrlDownload());
        intent.putExtra("created_date", model.getCreated_date());
        startActivity(intent);
    }

    //Logout function
    public void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        AppPreference.getInstance().clearData();
                        //Starting login activity
                        Intent intent = new Intent(MainDrawerActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    /*
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };
    */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onBackPressed() {
//        //handle the back press :D close the drawer first and if the drawer is closed close the activity
//        if (result != null && result.isDrawerOpen()) {
//            result.closeDrawer();
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onResume() {
        //onResume happens after onStart and onActivityCreate
        super.onResume();

        username = userSSOModel.getUsername();
        email = userSSOModel.getEmail();

        if (TextUtils.equals("Username", username)){
            //Starting login activity
            Intent intent = new Intent(MainDrawerActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Session expired. Mohon login ulang", Toast.LENGTH_LONG).show();
            finish();
        }

        if (result.isDrawerOpen()) {
            result.closeDrawer();
            result.setSelection(-1);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //replaces the default 'Back' button action
        if(keyCode== KeyEvent.KEYCODE_BACK)   {
            //handle the back press :D close the drawer first and if the drawer is closed close the activity
            if (result != null && result.isDrawerOpen()) {
                result.closeDrawer();
                result.setSelection(-1);
            } else {
                super.onBackPressed();
//                finish();
            }
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(String str) {

    }
}
