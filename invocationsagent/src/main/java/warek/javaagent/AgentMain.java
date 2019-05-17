package warek.javaagent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Executable;

public class AgentMain {

    private static final Logger logger = LogManager.getLogger(AgentMain.class);

    public static void premain(String agentOps, Instrumentation inst) {
        instrument(inst);
    }

    public static void agentmain(String agentOps, Instrumentation inst) {
        instrument(inst);
    }

    private static void instrument(Instrumentation inst) {
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform(new LogMethodsInvocations())
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .installOn(inst);
    }

    private static class LogMethodsInvocations implements Transformer {
        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                                                ClassLoader classLoader, JavaModule module) {
            log("load: " + typeDescription.getCanonicalName());

            final AsmVisitorWrapper methodsVisitor =
                    Advice.to(EnterMethodAdvice.class, ExitMethodAdvice.class)
                            .on(ElementMatchers.isMethod());

            final AsmVisitorWrapper constructorsVisitor =
                    Advice.to(EnterMethodAdvice.class, ExitMethodAdvice.class)
                            .on(ElementMatchers.isConstructor());

            return builder.visit(methodsVisitor).visit(constructorsVisitor);
        }
    }

    private static class EnterMethodAdvice {
        @Advice.OnMethodEnter
        static void onEnter(@Advice.Origin final Executable executable) {
            log("enter: " + executable.getName());
        }
    }

    private static class ExitMethodAdvice {
        @Advice.OnMethodExit
        static void onExit(@Advice.Origin final Executable executable) {
            log("exit: " + executable.getName());
        }
    }

    /**
     * This method is called by instructed application.
     * So it's need to be public and static!
     */
    public static void log(String msg) {
        logger.info(msg);
    }

}

