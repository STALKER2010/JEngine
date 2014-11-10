package andrews.jengine;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * Animation class, to make creating animations easier
 * 
 * @author STALKER_2010
 */
public class Animation {
	public final Map<Integer, Step> steps = new HashMap<>();
	public boolean isPlaying = false;
    public boolean isLooped = false;
	public long countFrames = -1;
	public int currentStep = 0;
    public String name = "";

    @JsonIgnore
	public Runnable runAfterEnd = null;

    @JsonIgnore
	public Animation(String name) {
		this.name = name;
	}

    public Animation() {

    }

	/**
	 * Update current step number
	 */
    @JsonIgnore
	public void update() {
		if (isPlaying) {
			countFrames++;
			{
				int curID = 0;
				currentStep = 0;
				for (int i = 0; i < steps.size(); i++) {
					final Step st = steps.get(i);
					if ((curID + st.nextSpeed) <= countFrames) {
						curID += st.nextSpeed;
						currentStep++;
					}
				}
			}
			if (currentStep == steps.size()) {
                if (runAfterEnd != null) {
                    runAfterEnd.run();
                }
                if (!isLooped) {
                    isPlaying = false;
                    currentStep = steps.size() - 1;
                } else {
                    currentStep = 0;
                    countFrames = -1;
                }
			}
            if (isPlaying) {
				try {
					final Runnable r = steps.get(currentStep).onChangeTo;
					if (r != null) {
						r.run();
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Reset current step number
	 */
    @JsonIgnore
	public void reset() {
		currentStep = 0;
		countFrames = -1;
	}

	/**
	 * Get current step to draw.
	 * 
	 * @return Animation.Step container for one picture
	 */
    @JsonIgnore
	public Step getStep() {
		if (steps.containsKey(currentStep)) {
			Step s = steps.get(currentStep);
			final Runnable r = s.onGet;
			if (r != null) {
				r.run();
			}
			return s;
		} else {
            System.out.println("Step No. " + currentStep + " not found!");
            return null;
        }
	}

	/**
	 * Add step in the end of queue
	 * 
	 * @param sprite
	 *            Sprite to add
	 */
    @JsonIgnore
	public Step addStep(Sprite sprite) {
		Step s = new Step();
		s.id = steps.size();
		s.sprite = sprite.name;
		steps.put(s.id, s);
		return s;
	}

	/**
	 * One frame in animation
	 * 
	 * @author STALKER_2010
	 */
	public static class Step {
		public String sprite = "";
		public int id = -1;
		public long nextSpeed = (long) (0.5 * Game.FPS);
        @JsonIgnore
		public Runnable onChangeTo = null;
        @JsonIgnore
		public Runnable onGet = null;

        public Step() {

        }
	}
}
