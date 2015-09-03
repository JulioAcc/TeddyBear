package com.priest.teddybear.lobby;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.priest.teddybear.R;
import com.priest.teddybear.helper.ExpandableListAdapter;
import com.priest.teddybear.parse.ParseConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by priest on 8/9/15.
 */
public class LobbyFragment extends Fragment {

    private ParseUser parseUser;

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    private TextView userNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.lobbyactivity_main, container, false);

        parseUser = ParseUser.getCurrentUser();

        userNameTextView = (TextView) rootView.findViewById(R.id.lobby_welcome_tv);
        userNameTextView.setText("Welcome, " + parseUser.getString(ParseConstants.USER_CLASS_ATTRIBUTE_NAME));

        // get the listview
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.lobby_expandable_list);

        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(listDataHeader.get(groupPosition));
                alertDialog.setMessage("Are you sure you wish to join " + listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition) + " ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "YES", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "NO", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });


        // preparing list data
        prepareListData();

        expandableListAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild,
                ExpandableListAdapter.DATA_TYPE_GAMELAYOUT);

        // setting list adapter
        expandableListView.setAdapter(expandableListAdapter);

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getResources().getString(R.string.lobby_explist_public_games));
        listDataHeader.add(getResources().getString(R.string.lobby_explist_friends_games));
        listDataHeader.add(getResources().getString(R.string.lobby_explist_private_games));

        // Adding child data
        List<String> publicGames = new ArrayList<String>();
        publicGames.add("Owner: Julio Castillo - 2/3");

        List<String> friendsGames = new ArrayList<String>();
        friendsGames.add("Owner: Julio Castillo - 1/3");

        List<String> privateGames = new ArrayList<String>();
        privateGames.add("Owner: Julio Castillo - 4/4 (FULL)");

        listDataChild.put(listDataHeader.get(0), publicGames); // Header, Child data
        listDataChild.put(listDataHeader.get(1), friendsGames);
        listDataChild.put(listDataHeader.get(2), privateGames);
    }
}
