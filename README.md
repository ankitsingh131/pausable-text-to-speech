# Pausable Text-To-Speech

Written in **Kotlin**. As the name suggest **Pausable Text-To-Speech**, Pause and resume your text utterance with this.

[![](https://jitpack.io/v/ankitsingh131/pausable-text-to-speech.svg)](https://jitpack.io/#ankitsingh131/pausable-text-to-speech)

# Implementation

**1.** Add JitPack in Project Level grade file

```
maven { url "https://jitpack.io" }
```

**2.** Add this dependency in app level gradle file
```
```
implementation 'com.github.ankitsingh131:pausable-text-to-speech:v1.0.1'
```
```

**3.** To Initialize Pausable Text-To-Speech in Java

```
public class MainActivity2 extends AppCompatActivity implements PausableTextToSpeech.OnInitListener, IUtteranceHelper {

    private PausableTextToSpeech pausableTextToSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pausableTextToSpeech = new PausableTextToSpeech(this, this);
    }

    @Override
    public void onInit() {
        pausableTextToSpeech.setOnUtteranceProgressListener(this);
    }

    @Override
    public void onStart(@Nullable String utteranceId) {
        // Called when utterance started.
    }

    @Override
    public void onResume(@Nullable String utteranceId) {
        // Called when utterance resumed.
    }

    @Override
    public void onPause(@Nullable String utteranceId) {
        // Called when utterance paused.
    }

    @Override
    public void onStop(@Nullable String utteranceId, boolean interrupted) {
        // Called when utterance interrupted.
    }

    @Override
    public void onDone(@Nullable String utteranceId) {
        // Called when utterance successfully completed.
    }

    @Override
    public void onError(@Nullable String utteranceId) {
        // Called when Text-To-Speech unable speak utterance.
    }
}
```

**To start an utterance:**
```
pausableTextToSpeech.start("Hey this is sample.", PausableTextToSpeech.QUEUE_FLUSH, null, "dummyUtterance", true);
```
**To pause an utterance:**
```
pausableTextToSpeech.pause(false);
```
> **Tip:** Parameter passed in pause() **if true can be act as stop()**. It is used for clearing the utterance.

**To resume an utterance:**
```
pausableTextToSpeech.resume();
```
**To stop an utterance:** There are two ways to stop utterance.

**1st method**
```
pausableTextToSpeech.stop();
```
**2nd method**
```
pausableTextToSpeech.pause(true);
```
