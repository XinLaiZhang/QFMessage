package top.fluffcotton.annotation;

import org.junit.Test;
import top.fluffcotton.handle.Handle;

import java.util.Set;


public class AnnotationScannerTest {

    @Test
    public void getAnnotationClasses() {
        try {
            Set<Class<?>> annotationClasses = AnnotationScanner.getAnnotationClasses("top.fluffcotton.test", BotHandle.class);
            System.out.println(annotationClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getInterfaceClasses() {
        try {
            Set<Class<Handle>> interfaceClasses = AnnotationScanner.getInterfaceClasses("top.fluffcotton.test", Handle.class);
            System.out.println(interfaceClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}