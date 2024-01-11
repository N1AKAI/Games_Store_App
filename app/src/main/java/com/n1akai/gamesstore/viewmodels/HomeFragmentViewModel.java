package com.n1akai.gamesstore.viewmodels;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.ClassSnapshotParser;
import com.firebase.ui.database.FirebaseArray;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n1akai.gamesstore.frags.HomeFragmentDirections;
import com.n1akai.gamesstore.models.Game;
import com.n1akai.gamesstore.models.Genre;

import java.util.ArrayList;

public class HomeFragmentViewModel extends ViewModel {
    ArrayList<SlideModel> slideImgs;
    public FirebaseArray<Game> games;
    public FirebaseArray<Genre> genres;
    public NavController navController;

    private MutableLiveData<Boolean> finishedLoadingGenres = new MutableLiveData<>();
    private MutableLiveData<Boolean> finishedLoadingNewReleases = new MutableLiveData<>();
    private MutableLiveData<Boolean> finishedLoadingSlider = new MutableLiveData<>();

    public MutableLiveData<Boolean> getFinishedLoadingGenres() {
        return finishedLoadingGenres;
    }

    public MutableLiveData<Boolean> getFinishedLoadingNewReleases() {
        return finishedLoadingNewReleases;
    }

    public MutableLiveData<Boolean> getFinishedLoadingSlider() {
        return finishedLoadingSlider;
    }

    public HomeFragmentViewModel() {
        games = new FirebaseArray<>(FirebaseDatabase.getInstance().getReference("games").orderByChild("releaseDate").limitToLast(6), new ClassSnapshotParser<>(Game.class));
        genres = new FirebaseArray<>(FirebaseDatabase.getInstance().getReference("genres"), new ClassSnapshotParser<>(Genre.class));
        games.addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                new Handler().postDelayed(() -> {
                    finishedLoadingNewReleases.setValue(true);
                }, 1000);
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {

            }
        });
        genres.addChangeEventListener(new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {

            }

            @Override
            public void onDataChanged() {
                new Handler().postDelayed(() -> {
                    finishedLoadingGenres.setValue(true);
                }, 1000);
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void sliderImage(ImageSlider imageSlider) {
        slideImgs = new ArrayList<>();
        ArrayList<Game> theGames = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("slider");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideImgs.clear();
                new Handler().postDelayed(() -> {
                    finishedLoadingSlider.setValue(true);
                }, 1000);
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Game game = dataSnapshot.getValue(Game.class);
                    theGames.add(game);
                    slideImgs.add(new SlideModel(game.getThumbnailUrl(), ScaleTypes.CENTER_CROP));

                }
               imageSlider.setImageList(slideImgs);
               imageSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Game game = theGames.get(i);
                        navController.navigate(HomeFragmentDirections.actionHomeFragmentToGameDetailFragment(game, game.getTitle()));
                    }

                    @Override
                    public void doubleClick(int i) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        games = null;
        genres = null;
        slideImgs = null;
    }

}
