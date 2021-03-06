package com.github.chojmi.inspirations.presentation.search;

import android.text.TextUtils;

import com.github.chojmi.inspirations.domain.common.UseCase;
import com.github.chojmi.inspirations.domain.entity.GalleryEntity;
import com.github.chojmi.inspirations.domain.utils.Preconditions;
import com.github.chojmi.inspirations.presentation.common.mapper.PhotoDataMapper;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static com.github.chojmi.inspirations.domain.utils.Preconditions.checkNotNull;

public class SearchPhotosPresenter implements SearchPhotosContract.Presenter {
    private final UseCase<String, GalleryEntity> getGalleryEntity;
    private final PhotoDataMapper photoDataMapper;
    private final String initialSearchQuery;
    private SearchPhotosContract.View view;
    private CompositeDisposable disposables;

    public SearchPhotosPresenter(@NonNull UseCase<String, GalleryEntity> getGalleryEntity,
                                 @NonNull PhotoDataMapper photoDataMapper, @NonNull String initialSearchQuery) {
        this.getGalleryEntity = Preconditions.checkNotNull(getGalleryEntity);
        this.photoDataMapper = Preconditions.checkNotNull(photoDataMapper);
        this.initialSearchQuery = Preconditions.checkNotNull(initialSearchQuery);
    }

    @Override
    public void setView(@NonNull SearchPhotosContract.View view) {
        this.disposables = new CompositeDisposable();
        this.view = checkNotNull(view);
        view.getSearchObservable().startWith(initialSearchQuery)
                .filter(charSequence -> !TextUtils.isEmpty(charSequence))
                .throttleLast(200, TimeUnit.MILLISECONDS)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> search(charSequence.toString()));
    }

    @Override
    public void destroyView() {
        this.disposables.clear();
        this.view = null;
    }

    @Override
    public void search(String text) {
        disposables.add(getGalleryEntity.process(text)
                .doOnComplete(() -> view.toggleProgressBar(false))
                .subscribe(submitUiModel -> {
            view.toggleProgressBar(submitUiModel.isInProgress());
            if (submitUiModel.isInProgress()) {
                return;
            }
            if (submitUiModel.isSucceed()) {
                view.renderView(photoDataMapper.transform(submitUiModel.getResult()));
            }
        }, Timber::e));
    }
}
