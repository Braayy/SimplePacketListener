package braayy.fun.packetlistener;

public class Response {

    public static final Response DEFAULT = new Response(false, 0);

    public static synchronized Response of(boolean cancel, int priority) {
       synchronized (Response.class) {
           return new Response(cancel, priority);
       }
    }

    private boolean cancel;
    private int priority;

    private Response(boolean cancel, int priority) {
        this.cancel = cancel;
        this.priority = priority;
    }

    public boolean isCancel() {
        return cancel;
    }

    public int getPriority() {
        return priority;
    }
}