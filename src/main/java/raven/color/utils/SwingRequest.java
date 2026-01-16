package raven.color.utils;

import javax.swing.*;

public class SwingRequest {

    private Timer timer;
    private Runnable request;

    public SwingRequest() {
    }

    public void setRequest(Runnable request, int millis) {
        Timer run = createTimerOrCancel(millis);
        this.request = request;
        run.restart();
    }

    private Timer createTimerOrCancel(int millis) {
        if (timer == null) {
            timer = new Timer(millis, e -> request.run());
            timer.setRepeats(false);
        }
        timer.stop();
        timer.setInitialDelay(millis);
        return timer;
    }

    public void dispose() {
        if (timer != null) {
            timer.stop();
            timer = null;
            request = null;
        }
    }
}
