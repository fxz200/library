package com.example.myapplication;

import android.util.Log;
import android.widget.Toast;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.*;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.ux.BaseArFragment;

import java.util.Collections;
import java.util.Set;

public class CleanArFragment extends BaseArFragment {
    private static final String TAG = "XXX";

    @Override
    public boolean isArRequired() {
        return true;
    }

    @Override
    public String[] getAdditionalPermissions() {
        return new String[0];
    }

    @Override
    protected void handleSessionException(UnavailableException sessionException) {

        String message;
        if (sessionException instanceof UnavailableArcoreNotInstalledException) {
            message = "Please install ARCore";
        } else if (sessionException instanceof UnavailableApkTooOldException) {
            message = "Please update ARCore";
        } else if (sessionException instanceof UnavailableSdkTooOldException) {
            message = "Please update this app";
        } else if (sessionException instanceof UnavailableDeviceNotCompatibleException) {
            message = "This device does not support AR";
        } else {
            message = "Failed to create AR session";
        }
        Log.e(TAG, "Error: " + message, sessionException);
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Config getSessionConfiguration(Session session) {

        Config config = new Config(session);
        config.setCloudAnchorMode(Config.CloudAnchorMode.ENABLED);
        return config;
    }

    @Override
    protected Set<Session.Feature> getSessionFeatures() {
        return Collections.emptySet();
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);
        getPlaneDiscoveryController().hide();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPlaneDiscoveryController().hide();
    }
}