/*
 * Copyright (c) 2023 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIf;
import vavi.util.properties.annotation.Property;
import vavi.util.properties.annotation.PropsEntity;
import vavix.util.Checksum;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Test1.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2023-01-19 nsano initial version <br>
 */
@EnabledIf("localPropertiesExists")
@PropsEntity(url = "file:local.properties")
public class Test1 {

    static boolean localPropertiesExists() {
        return Files.exists(Paths.get("local.properties"));
    }

    @Property(name = "sen.home")
    String sen_home;

    @Property(name = "fn.voice")
    String fn_voice;

    @BeforeEach
    void setup() throws Exception {
        if (localPropertiesExists()) {
            PropsEntity.Util.bind(this);
        }
    }

    @Test
    void test0() throws Exception {
        // Gyutan's essence is text to label conversion
        Gyutan.main(new String[] {
                "-x", // sen
                sen_home,
                "-m", // voice
                fn_voice,
                "-of", // HTS-style full-context labels w/ time
                "tmp/test.full.lab",
                "src/test/resources/test.txt"
        });
        assertEquals(Checksum.getChecksum(Paths.get("src/test/resources/test.full.lab")),
                Checksum.getChecksum(Paths.get("tmp/test.full.lab")));
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "GITHUB_WORKFLOW", matches = ".*")
    void test1() throws Exception {
        Gyutan.main(new String[] {
                "-x", // sen
                sen_home,
                "-m", // voice
                fn_voice,
                "-ow",
                "tmp/test.wav",
                "-ol", // HTS-style full-context labels
                "tmp/test.lab",
                "-of", // HTS-style full-context labels w/ time
                "tmp/test.full.lab",
                "-g", // volume [dB]
                "-40",
                "src/test/resources/test.txt"
        });
        speak(Files.newInputStream(Paths.get("tmp/test.wav")));
    }

    /** */
    void speak(InputStream is) throws Exception {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
        DataLine.Info line = new DataLine.Info(Clip.class, ais.getFormat());
        Clip clip = (Clip) AudioSystem.getLine(line);
        CountDownLatch cdl = new CountDownLatch(1);
        clip.addLineListener(e -> { if (e.getType() == LineEvent.Type.STOP) cdl.countDown(); });
        clip.open(ais);
        clip.start();
        cdl.await();
        clip.stop();
        clip.close();
    }
}
