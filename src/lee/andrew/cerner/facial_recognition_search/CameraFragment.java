package lee.andrew.cerner.facial_recognition_search;

import java.io.File;

import lee.andrew.cerner.facial_recognition_search.CameraPreview.TakePictureTask;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraFragment extends Fragment {
    private Camera mCamera = null;
    private CameraPreview mPreview;
    private FrameLayout layout;
   
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        inflater.inflate(R.layout.fragment_camera, container, false);
        layout = (FrameLayout) v.findViewById(R.id.camera_preview);
        initializeCameraPreview();
        Button captureButton = (Button) v.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            

            @Override
            public void onClick(View v) {
                TakePictureTask takePictureTask = CameraFragment.this.mPreview.new TakePictureTask();
                takePictureTask.execute(); 
//                mCamera.takePicture(null, null, mPreview.getPictureCallback());
                File photoFile = mPreview.getPhotoFile();

                
            }
        });
        return v;
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            Log.d("Lee", "camera opened");
        } catch (Exception e) {
        }
        return c;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            removeCameraPreview();
        }

    }

    private void removeCameraPreview() {
        mCamera.setPreviewCallback(null);
        mPreview.getHolder().removeCallback(mPreview);
        mCamera.release();
        mCamera = null;
        Log.d("Lee", "Camera Released");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            initializeCameraPreview();
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (mCamera != null) {
            removeCameraPreview();
        }
    }

    private void initializeCameraPreview() {
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(getActivity(), mCamera);
        layout.addView(mPreview);
    }
    
}
