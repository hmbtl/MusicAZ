package az.music.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import az.music.android.player.PlayerConstants;

/**
 * Created by anar on 6/28/15.
 */
public class SongListFragment extends Fragment {

    private ListView listView;
    private int pageNumber;

    private SongListAdapter adapter;

//    private MusicDatabase db;

    private MusicPlayerActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt("PAGE_NUMBER");
//        db = new MusicDatabase(getActivity());
        activity = (MusicPlayerActivity) getActivity();
    }

    public static SongListFragment newInstance(int pageNumber) {
        SongListFragment fragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("PAGE_NUMBER", pageNumber);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_list_fragment, container, false);

        listView = (ListView) view.findViewById(R.id.song_list);

        adapter = new SongListAdapter(getActivity(), R.layout.song_item, PlayerConstants.SONG_LIST_GENERAL);

        listView.setAdapter(adapter);
        listView.setFastScrollEnabled(true);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                if (!PlayerConstants.IS_PLAYING_FROM_ALL) {
                    PlayerConstants.SONG_LIST_PLAYING = PlayerConstants.SONG_LIST_GENERAL;
                    PlayerConstants.IS_PLAYING_FROM_ALL = true;
                }

                PlayerConstants.SONG_NUMBER = position;
                activity.playSong();

            }
        });

        return view;
    }


}



