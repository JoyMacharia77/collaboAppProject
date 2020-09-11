package com.joymacharia.collaboapp;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class nav_header extends Fragment{


        private View view;
        private MaterialToolbar toolbar;
        private CircleImageView imgProfile;
        private TextView txtName,txtEmail ;
        private Button btnEditAccount;
        private RecyclerView recyclerView;
        private SharedPreferences preferences;

        private String imgUrl = "";

        public nav_header() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.nav_header_main, container, false);
            init();
            return view;
        }

        private void init() {
            preferences = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            toolbar = view.findViewById(R.id.toolbarAccount);
            ((HomeActivity) getContext()).setSupportActionBar(toolbar);
            setHasOptionsMenu(true);
            imgProfile = view.findViewById(R.id.imgAccountProfile);
            txtName = view.findViewById(R.id.txtAccountName);
           // txtEmail = view.findViewById(R.id.myEmail);
//            txtPostsCount = view.findViewById(R.id.txtAccountPostCount);



        }

        private void getData() {

            StringRequest request = new StringRequest(Request.Method.GET, constants.GET_USER_INFO, res -> {

                try {
                    JSONObject object = new JSONObject(res);
                    JSONObject user = object.getJSONObject("user");
                    txtName.setText(user.getString("name") + " " + user.getString("lastname"));
                    txtEmail.setText(user.getString("email"));
                    Picasso.get().load(constants.URL + "storage/profiles/" + user.getString("photo")).into(imgProfile);

                    imgUrl = constants.URL + "storage/profiles/" + user.getString("photo");

                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                error.printStackTrace();
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String token = preferences.getString("token", "");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Bearer " + token);
                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {

            if (!hidden) {
                getData();
            }

            super.onHiddenChanged(hidden);
        }

        @Override
        public void onResume() {
            super.onResume();
            getData();
        }
    }


