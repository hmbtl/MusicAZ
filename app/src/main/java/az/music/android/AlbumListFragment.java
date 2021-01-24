package az.music.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import az.music.android.player.PlayerConstants;

/**
 * Created by anar on 6/28/15.
 */
public class AlbumListFragment extends Fragment {

    private GridView gridView;
    private int pageNumber;

    private AlbumAdapter adapter;


    private MusicPlayerActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt("PAGE_NUMBER");
        activity = (MusicPlayerActivity) getActivity();
    }

    public static AlbumListFragment newInstance(int pageNumber) {
        AlbumListFragment fragment = new AlbumListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("PAGE_NUMBER", pageNumber);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_list_layout, container, false);

        gridView = (GridView) view.findViewById(R.id.album_grid_view);

        adapter = new AlbumAdapter(getActivity(),PlayerConstants.ALBUM_LIST);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(),AlbumDetailActivity.class).putExtra(
                        "albumPosition",i
                ));
            }
        });
        return view;
    }


}



