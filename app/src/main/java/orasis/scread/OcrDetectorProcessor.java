package orasis.scread;

/**
 * Created by ikiaras on 31/3/2017.
 */

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;


import orasis.scread.Camera.GraphicOverlay;

import static android.content.ContentValues.TAG;

/**
 * Created by ikiaras on 30/3/2017.
 */

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;
    public static String TextBlockObject;

    public OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;
    }

    @Override
    public void release() {
        mGraphicOverlay.clear();
        Intro.mTts.stop();
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            if (item != null && item.getValue() != null) {
                Log.d("Processor", "Text detected" + item.getValue());
            }
            Intro.mTts.speak(item.getValue(),  TextToSpeech.QUEUE_FLUSH, null);
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }
    }


    private TextBlock getText(float rawX, float rawY) {
        OcrGraphic graphic = mGraphicOverlay.getGraphicAtLocation(rawX, rawY);
        TextBlock text = null;
        if (graphic != null) {
            text = graphic.getTextBlock();
            if (text != null && text.getValue() != null) {
                Intent data = new Intent();
                data.putExtra(TextBlockObject, text.getValue());
                //setResult(CommonStatusCodes.SUCCESS, data);
                // finish();
            }
            else {
                Log.d(TAG, "text data is null");
            }
        }
        else {
            Log.d(TAG,"no text detected");
        }
        return text;
    }

}

