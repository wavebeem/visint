/**
 * @author Brian Mock
 */
public class WaveFunc
extends Func {
    protected float scale  = 16f;
    protected float period = 20f;
    protected float freq;

    protected void calcFreq() {
        freq = TWO_PI/period;
    }

    public WaveFunc() {
        calcFreq();
    }

    public float valueAt(float x, float z) {
        return (float) (scale * Math.sin(freq * x));
    }

    public float xPartial(float x) {
        return (float) (freq * scale * Math.cos(freq * x));
    }
    public float zPartial(float z) {
        return 0;
    }
}
