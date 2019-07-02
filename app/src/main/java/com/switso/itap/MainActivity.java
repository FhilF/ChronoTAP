package com.switso.itap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.switso.itap.Approver.ApproverFailureTimeInOutFragment;
import com.switso.itap.Approver.ApproverLeaveOfAbsenceFragment;
import com.switso.itap.Approver.ApproverOffsiteFragment;
import com.switso.itap.Approver.ApproverOvertimeFragment;
import com.switso.itap.Approver.ApproverPendingApprovalsFragment;
import com.switso.itap.Approver.ApproverRestDayFragment;
import com.switso.itap.Approver.ApproverShiftCodeFragment;
import com.switso.itap.Approver.ApproverTOILCreditFormFragment;
import com.switso.itap.Form.FilingFormFragment;
import com.switso.itap.Form.FilingFormHomeFragment;
import com.switso.itap.Form.FormFailureToInOutFragment;
import com.switso.itap.Form.FormLeaveOfAbsenceFragment;
import com.switso.itap.Form.FormOffSiteAssignment;
import com.switso.itap.Form.FormOvertimeFragment;
import com.switso.itap.Form.FormRestdayFragment;
import com.switso.itap.Form.FormShiftCodeFragment;
import com.switso.itap.Form.FormTOILCreditFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        FilingFormHomeFragment.OnFragmentInteractionListener,
        FilingFormFragment.OnFragmentInteractionListener,
        FormLeaveOfAbsenceFragment.OnFragmentInteractionListener,
        FormFailureToInOutFragment.OnFragmentInteractionListener,
        FormOffSiteAssignment.OnFragmentInteractionListener,
        FormTOILCreditFragment.OnFragmentInteractionListener,
        FormOvertimeFragment.OnFragmentInteractionListener,
        FormRestdayFragment.OnFragmentInteractionListener,
        FormShiftCodeFragment.OnFragmentInteractionListener,
        ApproverFailureTimeInOutFragment.OnFragmentInteractionListener,
        ApproverLeaveOfAbsenceFragment.OnFragmentInteractionListener,
        ApproverTOILCreditFormFragment.OnFragmentInteractionListener,
        ApproverOvertimeFragment.OnFragmentInteractionListener,
        ApproverRestDayFragment.OnFragmentInteractionListener,
        ApproverShiftCodeFragment.OnFragmentInteractionListener,
        ApproverOffsiteFragment.OnFragmentInteractionListener,
        ApproverPendingApprovalsFragment.OnFragmentInteractionListener,
        RawLogsFragment.OnFragmentInteractionListener{

    Context context;
    String employee_Fname, employee_Email, employee_Image,employee_Deparment,employee_Role,employee_Id,employee_User_Id;
    GlobalFunction globalFunction;

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalFunction = new GlobalFunction(MainActivity.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        if (getIntent().getExtras() != null) {
            employee_Id = getIntent().getStringExtra("employee_id");
            employee_User_Id = getIntent().getStringExtra("user_id");
            employee_Fname = getIntent().getStringExtra("employee_fname");
            employee_Email = getIntent().getStringExtra("employee_email");
            employee_Image = getIntent().getStringExtra("employee_image");
            employee_Deparment = getIntent().getStringExtra("employee_department");
            employee_Role = getIntent().getStringExtra("employee_role");
        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout relBtnLogout = (RelativeLayout)findViewById(R.id.RelBtnLogout);



        relBtnLogout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                getIntent().removeExtra("employee_id");
                getIntent().removeExtra("employee_fname");
                getIntent().removeExtra("employee__email");
                getIntent().removeExtra("employee_image");
                getIntent().removeExtra("employee_department");
                getIntent().removeExtra("employee_role");
                getIntent().removeExtra("user_id");
                signOut();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        navigationView.setItemIconTintList(null);
        View hView =  navigationView.getHeaderView(0);
        ImageView nav_image = (ImageView)hView.findViewById(R.id.user_image);
        TextView nav_user = (TextView)hView.findViewById(R.id.user_fullname);
        TextView nav_email = (TextView)hView.findViewById(R.id.user_email);

        if(employee_Fname != null){
            Glide.with(this).load(employee_Image).apply(RequestOptions.circleCropTransform()).into(nav_image);
            nav_user.setText(employee_Fname);
            nav_email.setText(employee_Email);

        }

        bundle.putString("title", "Home");
        SetFragmentWithoutBackstack(new HomeFragment(),"Home", bundle);
        navigationView.getMenu().getItem(0).setChecked(true);
        setTitle("Home");
    }

    // Change frame
    public void SetFragment(Fragment fragment, String name, Bundle bundle) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmContainer, fragment, name);
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }


    public void SetFragmentWithoutBackstack(Fragment fragment, String name, Bundle bundle) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frmContainer, fragment, name);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (count == 0){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                super.onBackPressed();
            }
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
        return super.onOptionsItemSelected(item);
    }

    final Bundle bundle = new Bundle();
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            bundle.putString("title", "Home");
            SetFragment(new HomeFragment(),"Home", bundle);
        } else if (id == R.id.nav_filingform) {
            bundle.putString("title", "Approval Form");
            SetFragment(new FilingFormHomeFragment(),"Approval Form", bundle);

        } else if (id == R.id.nav_formapproval) {
            bundle.putString("title", "Approval Form");
            SetFragment(new ApproverPendingApprovalsFragment(), "Approval Form", bundle);

        } else if (id == R.id.nav_raw_logs) {
            bundle.putString("title", "Raw Logs");
            SetFragment(new RawLogsFragment(),"Raw Logs", bundle);
        }

        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }
}
