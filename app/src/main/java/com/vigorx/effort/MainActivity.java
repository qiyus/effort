package com.vigorx.effort;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vigorx.effort.database.EffortOperations;
import com.vigorx.effort.entity.EffortInfo;
import com.vigorx.effort.entity.PunchesInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        mListView =  (ListView) findViewById(R.id.list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 数据库操作
        EffortOperations effortOperations = EffortOperations.getInstance(this);
        effortOperations.open();
        List<EffortInfo> data = effortOperations.getVisibleEffort();
        effortOperations.close();

        assert mListView != null;
        mListView.setAdapter(new MainEffortListAdapter(this, data));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                EffortInfo effort = (EffortInfo) parent.getAdapter().getItem(position);
                intent.putExtra(DetailActivity.EFFORT_KEY, effort);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_add:
                Intent addIntent = new Intent(this, AddActivity.class);
                addIntent.putExtra(AddActivity.TYPE_KEY, AddActivity.TYPE_ADD);
                startActivity(addIntent);
                break;
            case R.id.action_punch:
                EffortOperations operations = EffortOperations.getInstance(MainActivity.this);
                operations.open();
                final List<EffortInfo> efforts = operations.getEffortByToday();
                operations.close();

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.customAlertDialog)
                        .setIcon(android.R.drawable.ic_menu_today)
                        .setTitle(R.string.punch_to_day)
                        .setMultiChoiceItems(getTodayTitle(efforts), getTodayComplete(efforts), new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                EffortInfo effortInfo = efforts.get(which);
                                setTodayComplete(effortInfo, isChecked ? 1 : 0);
                                efforts.set(which, effortInfo);
                            }
                        })
                        .setPositiveButton(R.string.delete_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EffortOperations operations = EffortOperations.getInstance(MainActivity.this);
                                operations.open();
                                for (EffortInfo effort:efforts) {
                                    operations.updatePunches(effort.getPunches());
                                }
                                List<EffortInfo> data = operations.getVisibleEffort();
                                MainEffortListAdapter adapter = new MainEffortListAdapter(MainActivity.this, data);
                                mListView.setAdapter(adapter);

                                operations.close();
                            }
                        });

                builder.create().show();

                break;
            case R.id.action_report:
                Intent reportIntent = new Intent(this, ReportActivity.class);
                startActivity(reportIntent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private String[] getTodayTitle(List<EffortInfo> efforts) {
        int count = efforts.size();
        String[] title = new String[count];
        for (int i = 0; i < count; i++) {
            title[i] = efforts.get(i).getTitle();
        }
        return title;
    }

    private boolean[] getTodayComplete(List<EffortInfo> efforts) {
        int count = efforts.size();
        boolean[] complete = new boolean[count];
        for (int i = 0; i < count; i++) {
            complete[i] = getToadyComplete(efforts.get(i));
        }
        return complete;
    }

    private boolean getToadyComplete(EffortInfo effort) {
        PunchesInfo punchesInfo = getPunchesInfoToday(effort);
        return (1 == punchesInfo.getComplete());
    }

    private void setTodayComplete(EffortInfo effort, int complete) {

        PunchesInfo punchesInfo = getPunchesInfoToday(effort);
        punchesInfo.setComplete(complete);
    }

    private PunchesInfo getPunchesInfoToday(EffortInfo effort) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format;
        format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(effort.getStartDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int startDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(new Date());
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        int offset = currentDayOfYear - startDayOfYear;
        return effort.getPunches()[offset];
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_backup) {
            Toast.makeText(this, R.string.msg_work, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_download) {
            Toast.makeText(this, R.string.msg_work, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_copyright) {
            Intent intent = new Intent(MainActivity.this, AbortActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:hsly_song@163.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.mail_title));
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
