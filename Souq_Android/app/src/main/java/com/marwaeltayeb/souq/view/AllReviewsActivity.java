package com.marwaeltayeb.souq.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.marwaeltayeb.souq.R;
import com.marwaeltayeb.souq.ViewModel.ReviewViewModel;
import com.marwaeltayeb.souq.adapter.ReviewAdapter;
import com.marwaeltayeb.souq.databinding.ActivityAllReviewsBinding;
import com.marwaeltayeb.souq.model.Review;

import java.util.List;

import static com.marwaeltayeb.souq.storage.LanguageUtils.loadLocale;
import static com.marwaeltayeb.souq.utils.Constant.PRODUCT_ID;

public class AllReviewsActivity extends AppCompatActivity {

    private ActivityAllReviewsBinding binding;
    private ReviewViewModel reviewViewModel;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_reviews);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.reviews));

        reviewViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);

        Intent intent = getIntent();
        productId = intent.getIntExtra(PRODUCT_ID, 0);

        setUpRecycleView();

        getReviewsOfProduct();
    }

    private void setUpRecycleView() {
        binding.allReviewsList.setHasFixedSize(true);
        binding.allReviewsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void getReviewsOfProduct() {
        reviewViewModel.getReviews(productId).observe(this, reviewApiResponse -> {
            if (reviewApiResponse != null) {
                binding.rateProduct.setRating(reviewApiResponse.getAverageReview());
                binding.rateNumber.setText(reviewApiResponse.getAverageReview() + getString(R.string.highestNumber));
                reviewList = reviewApiResponse.getReviewList();
                reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewList);
                binding.allReviewsList.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();
            }
        });
    }
}
