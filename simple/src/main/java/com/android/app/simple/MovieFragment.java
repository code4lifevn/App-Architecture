package com.android.app.simple;

import java.util.ArrayList;
import java.util.List;

import com.android.app.ui.fragment.AbstractBaseFragment;
import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * a simple for movie
 * Created by frodo on 2015/4/2.
 */
public class MovieFragment extends AbstractBaseFragment implements MoviePresenter.MovieView {
    List<Movie> movies = new ArrayList<>();
    BaseAdapter movieAdapter;
    private MoviePresenter presenter;
    private GridView gridView;

    @Override
    public void onCreatePresenter() {
        presenter = new MoviePresenter(this);
        presenter.attachMainControllerToModel(getMainController());
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_movie_list;
    }

    @Override
    public void initView() {
        gridView = (GridView) getView().findViewById(R.id.gridview);

        movieAdapter = new BaseAdapter() {

            @Override
            public int getCount() {
                return movies.size();
            }

            @Override
            public Object getItem(int position) {
                return movies.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_movie_item, null);
                    holder = new ViewHolder();
                    holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Movie movie = (Movie) getItem(position);
                Picasso.with(getActivity()).load(movie.imageUrl).centerCrop().resize(200, 280).into(holder.imageView);
                holder.textView.setText(movie.name);

                return convertView;
            }

            class ViewHolder {
                ImageView imageView;
                TextView textView;
            }
        };

        gridView.setAdapter(movieAdapter);
    }

    @Override
    public void registerListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public void initBusiness() {
        presenter.loadMovies();
    }

    @Override
    public MoviePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showMovieList(final List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_SHORT).show();
    }
}
