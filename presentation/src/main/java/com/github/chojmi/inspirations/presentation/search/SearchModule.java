package com.github.chojmi.inspirations.presentation.search;

import com.github.chojmi.inspirations.domain.usecase.photos.GetSearchPhoto;
import com.github.chojmi.inspirations.presentation.gallery.mapper.PhotoDataMapper;
import com.github.chojmi.inspirations.presentation.search.photos.SearchPhotosContract;
import com.github.chojmi.inspirations.presentation.search.photos.SearchPhotosPresenter;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchModule {

    @Provides
    SearchPhotosContract.Presenter provideSearchPhotosPresenter(GetSearchPhoto getSearchPhoto, PhotoDataMapper photoDataMapper) {
        return new SearchPhotosPresenter(getSearchPhoto, photoDataMapper);
    }
}