package main;

public class Image {

    private boolean trimmed;
    private boolean[] adjusted;

    public Image(){
        trimmed = false;
        adjusted = new boolean[]{false, false};
    }

    public void trim(){
        trimmed = true;
    }

    public void firstAdjustment(){
        adjusted[0] = true;
    }

    public void finalAdjustment(){
        adjusted[1] = true;
    }

    public boolean isTrimmed(){
        return trimmed;
    }

    public boolean getAdjusted() {
        for (boolean b : adjusted) {
            if (!b) {
                return false;
            }
        }
        return true;
    }
}
