package com.movetoplay.computer_vision;


import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.ViewModelProvider;

import com.movetoplay.computer_vision.mlkit_utils.ClassificationResult;
import com.movetoplay.computer_vision.mlkit_utils.EMASmoothing;
import com.movetoplay.computer_vision.mlkit_utils.PoseClassifier;
import com.movetoplay.computer_vision.mlkit_utils.PoseSample;
import com.movetoplay.computer_vision.mlkit_utils.RepetitionCounter;
import com.google.android.gms.common.internal.Preconditions;
import com.google.mlkit.vision.pose.Pose;
import com.movetoplay.db.UserEntity;
import com.movetoplay.model.Touch;
import com.movetoplay.network_api.ApiService;
import com.movetoplay.network_api.RetrofitClient;
import com.movetoplay.pref.Pref;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoseClassifierProcessor {


    int int_final = 0;
    private String getPose = Pref.INSTANCE.getPose();

    private static final String TAG = "PoseClassifierProcessor";
    private ApiService apiService = new RetrofitClient().getApi();
    private final String POSE_SAMPLES_FILE = "pose/"+getPose;

    //jumps.csv
    // Specify classes for which we want rep counting.
    // These are the labels in the given {@code POSE_SAMPLES_FILE}. You can set your own class labels
    // for your pose samples.

    // Указываем классы, для которых мы хотим подсчет повторений.
    // Это метки в данном {@code POSE_SAMPLES_FILE}. Вы можете установить свои собственные метки класса
    // для ваших образцов позы.
    private static final String[] POSE_CLASSES = {
            "jump", "sit", "push-up"
    };
    // Exercise

    private final boolean isStreamMode;

    private EMASmoothing emaSmoothing;
    private List<RepetitionCounter> repCounters;
    private PoseClassifier poseClassifier;
    private String lastRepResult;

    @WorkerThread
    public PoseClassifierProcessor(Context context, boolean isStreamMode) {
        Log.e(TAG, "HUY");
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        this.isStreamMode = isStreamMode;
        if (isStreamMode) {
            emaSmoothing = new EMASmoothing();
            repCounters = new ArrayList<>();
            lastRepResult = "";
        }
        loadPoseSamples(context);
    }

    private void loadPoseSamples(Context context) {
        List<PoseSample> poseSamples = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
            String csvLine = reader.readLine();
            while (csvLine != null) {
                // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
                PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
                if (poseSample != null) {
                    poseSamples.add(poseSample);
                }
                csvLine = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error when loading pose samples.\n" + e);
        }
        poseClassifier = new PoseClassifier(poseSamples);
        if (isStreamMode) {
            for (String className : POSE_CLASSES) {
                repCounters.add(new RepetitionCounter(className));
            }
        }
    }

    /**
     * Given a new {@link Pose} input, returns a list of formatted {@link String}s with Pose
     * classification results.
     *
     * <p>Currently it returns up to 2 strings as following:
     * 0: PoseClass : X reps
     * 1: PoseClass : [0.0-1.0] confidence
     */
    @WorkerThread
    public List<String> getPoseResult(Pose pose) {
        Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
        List<String> result = new ArrayList<>();
        ClassificationResult classification = poseClassifier.classify(pose);

        // Update {@link RepetitionCounter}s if {@code isStreamMode}.
        if (isStreamMode) {
            // Feed pose to smoothing even if no pose found.
            classification = emaSmoothing.getSmoothedResult(classification);

            // Return early without updating repCounter if no pose found.
            if (pose.getAllPoseLandmarks().isEmpty()) {
                result.add(lastRepResult);
                return result;
            }

            for (RepetitionCounter repCounter : repCounters) {
                int repsBefore = repCounter.getNumRepeats();
                int repsAfter = repCounter.addClassificationResult(classification);
                if (repsAfter > repsBefore) {
                    // Play a fun beep when rep counter updates.
                    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                    lastRepResult = String.format(Locale.US,"%s : %d reps", repCounter.getClassName(), repsAfter);
                    Log.e("Result",lastRepResult);
                    String numberOnly= lastRepResult.replaceAll("[^0-9]", "");

                    int number = Integer.parseInt(numberOnly);
                    Log.e("ResultNumber",numberOnly);
                    //push_ups.csv  sits.csv
                    long unixTime = Instant.now().getEpochSecond();
                    Pref.INSTANCE.setUnixTime(unixTime);

                    switch (Pref.INSTANCE.getPose()){
                        case "jumps.csv":
                            int jump = Pref.INSTANCE.getJumps();
                            int jumpResult = jump + number;
                            Pref.INSTANCE.setJumps(jumpResult);
                            Pref.INSTANCE.setCountTouch(number);
                            Pref.INSTANCE.setStartUnixTimestampTouch(1661597525);
                            break;
                        case "sits.csv":
                            int sits = Pref.INSTANCE.getSits();
                            int sitResult = sits + number;
                            Pref.INSTANCE.setSits(sitResult);
                            Pref.INSTANCE.setCountTouch(number);
                            Pref.INSTANCE.setStartUnixTimestampTouch(1661597525);
                            break;
                        case "push_ups.csv":
                            int push_ups = Pref.INSTANCE.getPush_ups();
                            int push_upsResult = push_ups + number;
                            Pref.INSTANCE.setPush_ups(push_upsResult);
                            Pref.INSTANCE.setCountTouch(number);
                            Pref.INSTANCE.setStartUnixTimestampTouch(1661597525);
                            break;
                    }
                    UserEntity userEntity = new UserEntity();
                    userEntity.setPos(String.valueOf(int_final + 1));
                    break;
                }
            }
            result.add(lastRepResult);
        }

        // Add maxConfidence class of current frame to result if pose is found.
        if (!pose.getAllPoseLandmarks().isEmpty()) {
            String maxConfidenceClass = classification.getMaxConfidenceClass();
            String maxConfidenceClassResult = String.format(
                    Locale.US,
                    "%s : %.2f confidence",
                    maxConfidenceClass,
                    classification.getClassConfidence(maxConfidenceClass)
                            / poseClassifier.confidenceRange());
            result.add(maxConfidenceClassResult);
        }
        return result;
    }
}
