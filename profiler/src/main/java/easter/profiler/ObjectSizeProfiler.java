package easter.profiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class ObjectSizeProfiler {

    static final Logger LOGGER = LoggerFactory.getLogger(ObjectSizeProfiler.class);
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        if(instrumentation != null) {
            return instrumentation.getObjectSize(o); //unfortunately only returns 'shallow' object size
        } else {
            LOGGER.info("Instrumentation was not initialized properly.");
            return 0L;
        }
    }
}