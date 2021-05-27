package components;

import java.util.Objects;

class StateTrigger {
    public String state;
    public String trigger;

    public StateTrigger() {
    }

    public StateTrigger(String state, String trigger) {
        this.state = state;
        this.trigger = trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != StateTrigger.class) return false;
        StateTrigger t2 = (StateTrigger) o;
        return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, trigger);
    }
}
