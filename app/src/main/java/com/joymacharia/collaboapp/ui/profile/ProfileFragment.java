package com.joymacharia.collaboapp.ui.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.joymacharia.collaboapp.AuthActivity;
import com.joymacharia.collaboapp.EditUserInfoActivity;
import com.joymacharia.collaboapp.Fragments.AccountFragment;
import com.joymacharia.collaboapp.HomeActivity;
import com.joymacharia.collaboapp.R;
import com.joymacharia.collaboapp.constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    //
    private View view;
    private MaterialToolbar toolbar;
    private CircleImageView imgProfile;
    private TextView txtName, txtPostsCount;
    private Button btnEditAccount;
    private RecyclerView recyclerView;
    //    private ArrayList<Post> arrayList;
    private SharedPreferences preferences;
    //    private AccountPostAdapter adapter;
    private String imgUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_profile);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        init();
        return root;
    }

/*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }
    */

    private void init()
    {
        preferences = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        ///////////////////
        toolbar = view.findViewById(R.id.toolbarAccount);
       ((HomeActivity) requireContext()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        imgProfile = view.findViewById(R.id.imgAccountProfile);
        txtName = view.findViewById(R.id.txtAccountName);
        txtPostsCount = view.findViewById(R.id.txtAccountPostCount);
        recyclerView = view.findViewById(R.id.recyclerAccount);
        btnEditAccount = view.findViewById(R.id.btnEditAccount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(((HomeActivity)getContext()), EditUserInfoActivity.class);
//                i.putExtra("imgUrl", imgUrl);
//                startActivity(i);
                Intent push = new Intent(((HomeActivity) getContext()), EditUserInfoActivity.class);
                startActivity(push);

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Do you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        StringRequest request = new StringRequest(Request.Method.GET, constants.LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {

                try {
                    JSONObject object = new JSONObject(res);
                    if (object.getBoolean("success")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        ProfileFragment.this.startActivity(new Intent(((HomeActivity) ProfileFragment.this.getContext()), AuthActivity.class));
                        ((HomeActivity) ProfileFragment.this.requireContext()).finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
