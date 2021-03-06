package com.github.chojmi.inspirations.presentation.gallery.ui.comments;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chojmi.inspirations.domain.entity.photos.CommentEntity;
import com.github.chojmi.inspirations.presentation.R;
import com.github.chojmi.inspirations.presentation.utils.ImageViewUtils;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static com.github.chojmi.inspirations.presentation.utils.ImageViewUtils.clearImageCache;

public class CommentsItemView extends ConstraintLayout {
    @BindView(R.id.user_icon) ImageView userIconView;
    @BindView(R.id.user_name) TextView userNameView;
    @BindView(R.id.comment) TextView commentView;

    public CommentsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.gallery_comments_item_view, this);
        ButterKnife.bind(this);

    }

    public void renderView(CommentEntity commentEntity) {
        ImageViewUtils.loadImage(userIconView, commentEntity.getAuthorIcon());
        commentView.setMovementMethod(LinkMovementMethod.getInstance());
        commentView.setText(Html.fromHtml(commentEntity.getContent()));
        userNameView.setText(commentEntity.getAuthorname());
    }

    public Observable<View> getUserClicksObservable() {
        return Observable.merge(RxView.clicks(userNameView), RxView.clicks(userIconView)).map(o -> this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearImageCache(userIconView);
    }
}
