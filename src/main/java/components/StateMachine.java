package components;

import imgui.ImGui;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateMachine extends Component {

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void refreshTextures() {
        for (AnimationState state : states) {
            state.refreshTextures();
        }
    }

    public void setDefaultState(String animationTitle) {
        for (AnimationState state : states) {
            if (state.title.equals(animationTitle)) {
                defaultStateTitle = animationTitle;
                if (currentState == null) {
                    currentState = state;
                }
                return;
            }
        }

        System.out.println("Unable to find default state '" + animationTitle + "'");
    }

    public void addState(String from, String to, String onTrigger) {
        this.stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void addState(AnimationState state) {
        this.states.add(state);
    }

    public void trigger(String trigger) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.title) && state.trigger.equals(trigger)) {
                if (stateTransfers.get(state) != null) {
                    String newState = stateTransfers.get(state);
                    int newStateIndex = stateIndexOf(newState);
                    if (newStateIndex > -1) {
                        currentState = states.get(newStateIndex);
                    }
                }
                return;
            }
        }
    }

    @Override
    public void start() {
        for (AnimationState state : states) {
            if (state.title.equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (currentState != null) {
            currentState.update(dt);
            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
            if (sprite != null) {
                sprite.setSprite(currentState.getCurrentSprite());
            }
        }
    }

    @Override
    public void editorUpdate(float dt) {
        if (currentState != null) {
            currentState.update(dt);
            SpriteRenderer sprite = gameObject.getComponent(SpriteRenderer.class);
            if (sprite != null) {
                sprite.setSprite(currentState.getCurrentSprite());
            }
        }
    }

    @Override
    public void imgui() {
        for (AnimationState state : states) {
            ImString title = new ImString(state.title);
            ImGui.inputText("State: ", title);
            state.title = title.get();

            int index = 0;
            for (Frame frame : state.animationFrames) {
                float[] tmp = new float[1];
                tmp[0] = frame.frameTime;
                ImGui.dragFloat("Frame(" + index + ") Time: ", tmp, 0.01f);
                frame.frameTime = tmp[0];
                index++;
            }
        }
    }

    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (AnimationState state : states) {
            if (state.title.equals(stateTitle)) {
                return index;
            }
            index++;
        }

        return -1;
    }
}
