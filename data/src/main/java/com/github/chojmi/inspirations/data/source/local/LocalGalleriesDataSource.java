package com.github.chojmi.inspirations.data.source.local;

import com.github.chojmi.inspirations.domain.entity.PhotoEntity;
import com.github.chojmi.inspirations.domain.repository.GalleriesDataSource;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public final class LocalGalleriesDataSource implements GalleriesDataSource {

    @Inject
    public LocalGalleriesDataSource() {
    }

    @Override
    public Observable<List<PhotoEntity>> loadGallery(String galleryId) {
        return loadGallery(galleryId, 1);
    }

    @Override
    public Observable<List<PhotoEntity>> loadGallery(String galleryId, int page) {
        return Observable.just(Collections.emptyList());
    }
}
