package org.geogebra.common.sound;

import org.geogebra.common.kernel.geos.GeoAudio;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.util.AsyncOperation;

/**
 * Sound manager.
 */
public interface SoundManager {

	/**
	 * Pause or resume sound.
	 * @param resume whether to resume
	 */
	void pauseResumeSound(boolean resume);

	/**
	 * Plays a single note using the midi sequencer.
	 *
	 * @param note note
	 * @param duration duration
	 * @param instrument instrument number
	 * @param velocity velocity
	 */
	void playSequenceNote(int note, double duration,
			int instrument, int velocity);

	/**
	 * Plays a sequence of notes generated by the string noteString using the
	 * midi sequencer.
	 *
	 * @param noteString sequence of notes
	 * @param instrument instrument number
	 */
	void playSequenceFromString(String noteString, int instrument);

	/**
	 * Plays a tone generated by the time-valued input function f(t) for t = min
	 * to t = max seconds.
	 *
	 * @param geoFunction function
	 * @param min x-min
	 * @param max x-max
	 */
	void playFunction(GeoFunction geoFunction, double min, double max);

	/**
	 * @param geoAudible
	 *            audio geo element
	 * @param url
	 *            file to play. Desktop currently just supports .mid, Wed
	 *            supports .mp3
	 */
	void playFile(GeoElement geoAudible, String url);

	/**
	 * Plays a tone generated by the time-valued input function f(t) for t = min
	 * to t = max seconds. Also allows adjustment of the sampling rate and bit
	 * depth.
	 *
	 * @param geoFunction function
	 * @param min min
	 * @param max max
	 * @param sampleRate sample rate
	 * @param bitDepth the bit depth
	 */
	void playFunction(GeoFunction geoFunction, double min, double max,
			int sampleRate, int bitDepth);

	/**
	 * Loads audio resource represented by GeoAudio object for further operations.
	 * 
	 * @param geo
	 *            to load.
	 */
	void loadGeoAudio(GeoAudio geo);

	/**
	 * Gets the length of the audio given by its URL.
	 * 
	 * @param geoAudio
	 *           Audio geo element.
	 * @return the duration of the audio resource.
	 */
	int getDuration(GeoAudio geoAudio);

	/**
	 * Gets the current time elapsed of the audio given by its URL.
	 * 
	 * @param geoAudio
	 *            Audio geo element.
	 * @return the duration of the audio resource.
	 */
	int getCurrentTime(GeoAudio geoAudio);

	/**
	 * Sets the current time position of the audio given by its URL.
	 * 
	 * @param geoAudio
	 *            Audio geo element.
	 * @param pos
	 *            to set.
	 */
	void setCurrentTime(GeoAudio geoAudio, int pos);

	/**
	 * 
	 * @param url
	 *            to check if it is a valid audio file.
	 * @param callback
	 *            to process the result.
	 */
	void checkURL(String url, AsyncOperation<Boolean> callback);

	/**
	 * Plays/resumes GeoAudio object.
	 * 
	 * @param geo
	 *            to play.
	 */
	void play(GeoAudio geo);

	/**
	 * Pauses GeoAudio object.
	 * 
	 * @param geo
	 *            to pause.
	 */
	void pause(GeoAudio geo);

	/**
	 * 
	 * @param geo
	 *            audio object to check.
	 * @return if GeoAudio object is playing now.
	 */
	boolean isPlaying(GeoAudio geo);
}
