package org.finalframework.devops.java;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.finalframework.util.Asserts;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.*;

/**
 * @author likly
 * @version 1.0
 * @date 2020/11/21 23:25:07
 * @since 1.0
 */
public final class Decompiler {
    private Decompiler() {
    }

    public static String decompile(Class<?> clazz, String methodName) {
        Instrumentation instrumentation = Instrumentations.get();

        HashSet<Class<?>> classes = new HashSet<>(Arrays.asList(clazz));
        ClassDumpTransformer transformer = new ClassDumpTransformer(classes, new File(ClassLoader.getSystemClassLoader().getResource(".").getPath()));
        Instrumentations.retransformClasses(instrumentation, transformer, classes);
        Map<Class<?>, File> dumpResult = transformer.getDumpResult();
        File file = dumpResult.get(clazz);
        String absolutePath = file.getAbsolutePath();
        String decompile = decompile(absolutePath, methodName);
        file.delete();
        return decompile;
    }


    public static String decompile(String classFilePath, String methodName) {
        return decompile(classFilePath, methodName, false);
    }

    public static String decompile(String classFilePath, String methodName, boolean hideUnicode) {
        final StringBuilder result = new StringBuilder(8192);

        OutputSinkFactory mySink = new OutputSinkFactory() {
            @Override
            public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
                return Arrays.asList(SinkClass.STRING, SinkClass.DECOMPILED, SinkClass.DECOMPILED_MULTIVER,
                        SinkClass.EXCEPTION_MESSAGE);
            }

            @Override
            public <T> Sink<T> getSink(final SinkType sinkType, SinkClass sinkClass) {
                return sinkable -> {
                    // skip message like: Analysing type demo.MathGame
                    if (sinkType == SinkType.PROGRESS) {
                        return;
                    }
                    result.append(sinkable);
                };
            }
        };

        HashMap<String, String> options = new HashMap<String, String>();
        /**
         * @see org.benf.cfr.reader.util.MiscConstants.Version.getVersion() Currently,
         *      the cfr version is wrong. so disable show cfr version.
         */
        options.put("showversion", "false");
        options.put("hideutf", String.valueOf(hideUnicode));
        if (Asserts.nonBlank(methodName)) {
            options.put("methodname", methodName);
        }

        CfrDriver driver = new CfrDriver.Builder().withOptions(options).withOutputSink(mySink).build();
        List<String> toAnalyse = new ArrayList<String>();
        toAnalyse.add(classFilePath);
        driver.analyse(toAnalyse);

        return result.toString();
    }
}