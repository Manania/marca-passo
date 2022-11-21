package com.marcacorrida.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.marcacorrida.datasource.CorridaRepository;
import com.marcacorrida.registro.GravarCorridaFragment;
import com.marcacorrida.R;
import com.marcacorrida.historico.HistoricoFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 2;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainBar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this, NUM_PAGES);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? "Relógio" : "Histórico")
        ).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_appbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            //Compartilha o historico em formato JSON
            case R.id.action_share:
                try(CorridaRepository repository = new CorridaRepository(this)) {
                    String export = repository.listar().toString();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, export);
                    sendIntent.setType("text/plain"); //Especifica o formato de arquivo
                    Intent shareIntent = Intent.createChooser(sendIntent, null); //O usuário escolhe o aplicativo consumidor
                    startActivity(shareIntent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            getSupportActionBar().show();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private final int ITEM_COUNT;
        public ScreenSlidePagerAdapter(FragmentActivity fragment, final int itemCount) {
            super(fragment);
            this.ITEM_COUNT = itemCount;
        }

        @Override
        public Fragment createFragment(int position) {
            if(position == 0) {
                return new GravarCorridaFragment();
            } else {
                return new HistoricoFragment();
            }
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

}