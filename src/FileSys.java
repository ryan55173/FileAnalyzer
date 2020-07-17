import java.io.File;
import java.util.ArrayList;

public class FileSys {

    // Class objects
    private File rootDirectory;
    private boolean rootDirectorySet = false;
    private Window window;
    // File objects
    private ArrayList<File> projectDirectories;
    private ArrayList<File> projectFiles;

    // Constructor
    FileSys(Window window) {
        this.window = window;
    }

    // Methods
    private void examineFiles() {
        if (!rootDirectorySet) {
            window.addOutput("Root directory is not set...");
            return;
        }
        projectDirectories = new ArrayList<>();
        projectFiles = new ArrayList<>();
        File[] firstReadFiles = rootDirectory.listFiles();
        ArrayList<File> loopDir = new ArrayList<>();
        for (File readFile : firstReadFiles) {
            if (readFile.isDirectory()) {
                loopDir.add(readFile);
            } else {
                projectFiles.add(readFile);
            }
        }
        while (loopDir.size() != 0) {
            ArrayList<File> nextDir = new ArrayList<>();
            for (File readDir : loopDir) {
                projectDirectories.add(readDir);
                File[] allRead = readDir.listFiles();
                for (File f : allRead) {
                    if (f.isDirectory()) {
                        nextDir.add(f);
                    } else {
                        projectFiles.add(f);
                    }
                }
            }
            loopDir.clear();
            loopDir.addAll(nextDir);
            nextDir.clear();
        }
    }
    public void analyzeFiles(boolean blendFiles, boolean mayaFiles, boolean metaFiles) {
        this.window.addOutput("Analyzing files...");
        examineFiles();
        this.window.addOutput("Searching constraints...");
        if (blendFiles) {
            searchBlend();
        }
        if (mayaFiles) {
            searchMaya();
        }
        if (metaFiles) {
            searchMeta();
        }
    }
    private void searchBlend() {
        ArrayList<File> foundBlend = new ArrayList<>();
        for (File checkFile : this.projectFiles) {
            String fullPath = checkFile.getAbsolutePath();
            String[] portions = fullPath.split("\\\\");
            String specificFileString = portions[(portions.length - 1)];
            if (specificFileString.contains(".blend")) {
                foundBlend.add(checkFile);
            }
        }
        if (foundBlend.size() == 0) {
            this.window.addOutput("No \".blend\" files found");
            return;
        }
        for (File blendFile : foundBlend) {
            String outputString = "Blend File: " + blendFile.getAbsolutePath();
            this.window.addOutput(outputString);
        }
    }
    private void searchMaya() {
        ArrayList<File> foundMaya = new ArrayList<>();
        for (File checkFile : this.projectFiles) {
            String fullPath = checkFile.getAbsolutePath();
            String[] portions = fullPath.split("\\\\");
            String specificFileString = portions[(portions.length - 1)];
            if (specificFileString.contains(".maya")) {
                foundMaya.add(checkFile);
            }
        }
        if (foundMaya.size() == 0) {
            this.window.addOutput("No \".maya\" files found");
            return;
        }
        for (File mayaFile : foundMaya) {
            String outputString = "Maya File: " + mayaFile.getAbsolutePath();
            this.window.addOutput(outputString);
        }
    }
    private void searchMeta() {
        // TODO: Look for lone meta files without associated file
    }

    // Getters and setters
    public boolean isRootDirectorySet() {
        return this.rootDirectorySet;
    }
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        rootDirectorySet = true;
    }

}
