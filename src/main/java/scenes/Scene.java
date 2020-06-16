package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import imgui.ImGui;
import jade.Camera;
import jade.GameObject;
import jade.GameObjectDeserializer;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public Camera camera() {
        return this.camera;
    }

    public void sceneImgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxCompId = -1;
            int maxGoId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i=0; i < objs.length; i++) {
                addGameObjectToScene(objs[i]);
                for (Component c : objs[i].getAllComponents()) {
                    if (c.uid() > maxCompId) {
                        maxCompId = c.uid();
                    }
                }
                if (objs[i].uid() > maxGoId) {
                    maxGoId = objs[i].uid();
                }
            }

            maxCompId++;
            maxGoId++;
            Component.init(maxCompId);
            GameObject.init(maxGoId);
            this.levelLoaded = true;
        }
    }
}
