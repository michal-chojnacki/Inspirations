package com.github.chojmi.inspirations.presentation.gallery.ui.grid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chojmi.inspirations.presentation.R;
import com.github.chojmi.inspirations.presentation.blueprints.BaseFragment;
import com.github.chojmi.inspirations.presentation.gallery.model.GridAdapterUiModel;
import com.github.chojmi.inspirations.presentation.gallery.model.Photo;
import com.github.chojmi.inspirations.presentation.gallery.model.PhotoComments;
import com.github.chojmi.inspirations.presentation.gallery.model.PhotoFavs;
import com.github.chojmi.inspirations.presentation.main.MainActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class GridFragment extends BaseFragment<MainActivity> implements GridPhotoContract.View, GridPhotoAttrsContract.View, GridAdapter.Listener {
    @BindView(R.id.rv_gallery) RecyclerView recyclerView;
    @Inject GridPhotoContract.Presenter photoPresenter;
    @Inject GridPhotoAttrsContract.Presenter photoAttrsPresenter;
    private GridAdapter galleryAdapter;
    private CompositeDisposable disposables;
    public static GridFragment newInstance() {
        return new GridFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInspirationsApp().createGridComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.disposables = new CompositeDisposable();
        initRecyclerView();
        photoPresenter.setView(this);
        photoAttrsPresenter.setView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        photoPresenter.destroyView();
        photoAttrsPresenter.destroyView();
        disposables.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getInspirationsApp().releaseGridComponent();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        galleryAdapter = new GridAdapter(this);
        disposables.add(galleryAdapter.getOnItemClickObservable().subscribe(uiModel -> photoPresenter.photoSelected(uiModel.getPhoto())));
        recyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void showPhotos(List<Photo> photos) {
        galleryAdapter.setData(GridAdapterUiModel.create(photos));
    }

    @Override
    public void openPhotoView(Photo photo) {
        getNavigator().navigateToPhoto(getContext(), photo);
    }

    @Override
    public void showFavs(int position, PhotoFavs photoFavs) {
        galleryAdapter.setFavs(position, photoFavs);
    }

    @Override
    public void showComments(int position, PhotoComments photoComments) {
        galleryAdapter.setComments(position, photoComments);
    }

    @Override
    public void onNewItemBind(int position, Photo photo) {
        photoAttrsPresenter.loadComments(position, photo);
        photoAttrsPresenter.loadFavs(position, photo);
    }
}