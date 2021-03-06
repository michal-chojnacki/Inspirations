package com.github.chojmi.inspirations.domain.repository;

import com.github.chojmi.inspirations.domain.entity.GalleryEntity;
import com.github.chojmi.inspirations.domain.entity.photos.PhotoCommentsEntity;
import com.github.chojmi.inspirations.domain.entity.photos.PhotoFavsEntity;
import com.github.chojmi.inspirations.domain.entity.photos.PhotoInfoEntity;
import com.github.chojmi.inspirations.domain.entity.photos.PhotoSizeListEntity;

import io.reactivex.Observable;

public interface PhotosDataSource {
    Observable<PhotoInfoEntity> loadPhotoInfo(String photoId);

    Observable<PhotoFavsEntity> loadPhotoFavs(String photoId, int page);

    Observable<PhotoCommentsEntity> loadPhotoComments(String photoId);

    Observable<PhotoSizeListEntity> loadPhotoSizes(String photoId);

    Observable<GalleryEntity> loadSearchPhoto(String text);

    Observable<Void> addComment(String photoId, String commentText);
}
