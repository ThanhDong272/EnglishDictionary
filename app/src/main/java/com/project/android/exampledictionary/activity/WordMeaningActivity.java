package com.project.android.exampledictionary.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.android.exampledictionary.R;
import com.project.android.exampledictionary.database.DatabaseHelper;
import com.project.android.exampledictionary.definition.DefinitionFragment;
import com.project.android.exampledictionary.definition.WordTypeFragment;
import com.project.android.exampledictionary.navigation_drawer.DictionaryFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordMeaningActivity extends AppCompatActivity {

    private ViewPager viewPager;

    String enWord;
    DatabaseHelper myDbHelper;
    Cursor c = null;

    public String enDefinition;
    public String enWordType;

    TextToSpeech tts;

    private boolean isAdded;
    MenuItem mItemFavoriteAdded;
    MenuItem mItemFavorite;

    boolean startedFromShare = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        //received values
        Bundle bundle = getIntent().getExtras();
        enWord = bundle.getString("en_word");

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                startedFromShare = true;

                if (sharedText != null) {
                    Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                    Matcher m = p.matcher(sharedText);

                    if (m.matches()) {
                        enWord = sharedText;
                    } else {
                        enWord = "Not Available";
                    }
                }
            }
        }


        myDbHelper = new DatabaseHelper(this);

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        c = myDbHelper.getMeaning(enWord);

        if (c.moveToFirst()) {
            enDefinition = c.getString(c.getColumnIndex("en_definition"));
            enWordType = c.getString(c.getColumnIndex("en_wordtype"));

            myDbHelper.insertHistory(enWord);
        } else {
            enWord = "Not Available";
        }


        //Button speak
        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts = new TextToSpeech(WordMeaningActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        //TODO Auto-generated method stub
                        if (status == TextToSpeech.SUCCESS) {
                            int result = tts.setLanguage(Locale.getDefault());
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("error", "This Language is not supported");
                            } else {
                                tts.speak(enWord, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        } else {
                            Log.e("error", "Initialization Failed");
                        }
                    }
                });
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(enWord);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        viewPager = (ViewPager) findViewById(R.id.tab_viewPager);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        //Tablayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new DefinitionFragment(), "Definition");
        adapter.addFrag(new WordTypeFragment(), "Word Type");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (startedFromShare) {
                Intent intent = new Intent(this, DictionaryFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                onBackPressed();
            }
        }

        switch (id) {
            case R.id.action_favorite:
                isAdded = true;
                this.invalidateOptionsMenu();
                Toast.makeText(WordMeaningActivity.this, "Favorite Word Added", Toast.LENGTH_SHORT).show();
                myDbHelper.insertFavorite(enWord);
                return true;
            case R.id.action_favorite_added:
                isAdded = false;
                this.invalidateOptionsMenu();
                Toast.makeText(WordMeaningActivity.this, "Favorite Word Removed", Toast.LENGTH_SHORT).show();
                myDbHelper.deleteFavorite(enWord);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_definition, menu);

        mItemFavoriteAdded = menu.findItem(R.id.action_favorite_added);
        mItemFavorite = menu.findItem(R.id.action_favorite);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (isAdded) {
            mItemFavoriteAdded.setVisible(true); // hide play button
            mItemFavorite.setVisible(false); // show the pause button
        } else if (!isAdded) {
            mItemFavoriteAdded.setVisible(false); // show play button
            mItemFavorite.setVisible(true); // hide the pause button
        }

        return true;
    }


}
