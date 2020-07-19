import java.io.File;
import java.util.ArrayList;

public class FileSys {

    // Class objects
    private File rootDirectory;
    private boolean rootDirectorySet = false;
    private Window window;
    // Main search objects
    private ArrayList<File> projectDirectories;
    private ArrayList<File> projectFiles;
    // Specific search objects
    private ArrayList<File> foundBlenderFiles;
    private ArrayList<File> foundMayaFiles;
    private ArrayList<File> metaIssuesFound;

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
        if (firstReadFiles == null) {
            System.out.println("Error: No files in root directory");
            return;
        }
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
                if (allRead == null) {
                    continue;
                }
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
        // Starting analyze output
        this.window.addOutput("Gathering files...");
        examineFiles();
        this.window.addOutput("Examining files...");
        int blendFound = 0;
        int mayaFound = 0;
        int metaIssuesFound = 0;
        if (blendFiles) {
            blendFound = searchBlend();
        }
        if (mayaFiles) {
            mayaFound = searchMaya();
        }
        if (metaFiles) {
            metaIssuesFound = searchMeta();
        }
        // End of full search output statements
        this.window.addOutput("Ending Search...");
        if (blendFiles) {
            this.window.addOutput("Blender Files Found: " + blendFound);
        }
        if (mayaFiles) {
            this.window.addOutput("Maya Files Found: " + mayaFound);
        }
        if (metaFiles) {
            this.window.addOutput("Metadata Issues Detected: " + metaIssuesFound);
        }
    }
    private int searchBlend() {
        // For search blender files check box
        int blendFound = 0;
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
            return 0;
        }
        for (File blendFile : foundBlend) {
            String outputString = "Blend File: " + blendFile.getAbsolutePath();
            this.window.addOutput(outputString);
            blendFound += 1;
        }
        this.foundBlenderFiles = new ArrayList<>();
        this.foundBlenderFiles.addAll(foundBlend);
        return blendFound;
    }
    private int searchMaya() {
        // For search Maya files check box
        int mayaFound = 0;
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
            return 0;
        }
        for (File mayaFile : foundMaya) {
            String outputString = "Maya File: " + mayaFile.getAbsolutePath();
            this.window.addOutput(outputString);
            mayaFound += 1;
        }
        this.foundMayaFiles = new ArrayList<>();
        this.foundMayaFiles.addAll(foundMaya);
        return mayaFound;
    }
    private int searchMeta() {
        // For search meta files check box
        int issuesFound = 0;
        ArrayList<File> foundMeta = new ArrayList<>(); // All meta initial
        for (File checkFile : this.projectFiles) {
            String fullPath = checkFile.getAbsolutePath();
            String[] portions = fullPath.split("\\\\");
            String specificFileString = portions[(portions.length - 1)];
            if (specificFileString.contains(".meta")) {
                foundMeta.add(checkFile);
            }
        }
        if (foundMeta.size() == 0) {
            this.window.addOutput("No \".meta\" files found");
            return 0;
        }
        // Files returned from main search
        ArrayList<File> refinedMeta = new ArrayList<>();
        ArrayList<File> associatedFiles = new ArrayList<>();
        ArrayList<File> warningMeta = new ArrayList<>();
        // Find lone meta
        for (File metaFile : foundMeta) {
            File assocFile = findAssociatedFile(metaFile);
            if (assocFile != null) {
                refinedMeta.add(metaFile);
                associatedFiles.add(assocFile);
            } else {
                warningMeta.add(metaFile);
            }
        }
        // Do something wth found info
        if (warningMeta.size() != 0) {
            for (File warningMetaFile : warningMeta) {
                String outputString = "Meta: " + warningMetaFile.getAbsolutePath();
                this.window.addOutput(outputString);
                issuesFound += 1;
            }
        } else {
            this.window.addOutput("No issues with \".meta\" files detected");
            issuesFound = 0;
        }
        this.metaIssuesFound = new ArrayList<>();
        this.metaIssuesFound.addAll(warningMeta);
        return issuesFound;
    }
    private File findAssociatedFile(File metaFile) {
        // For finding an associated metadata file
        // Get search string
        String metaPathFull = metaFile.getAbsolutePath();
        String[] metaPathParts = metaPathFull.split("\\\\");
        String fileNameAndExtension = metaPathParts[(metaPathParts.length - 1)];
        String removeString = ".meta";
        fileNameAndExtension = fileNameAndExtension.replace(removeString, "");
        // Check for search string
        File associatedFile = null;
        for (File checkFile : this.projectFiles) {
            String fullPath = checkFile.getAbsolutePath();
            String[] portions = fullPath.split("\\\\");
            String specificFileString = portions[(portions.length - 1)];
            if (specificFileString.contains(".meta")) {
                continue;
            }
            if (specificFileString.contains(fileNameAndExtension)) {
                associatedFile = checkFile;
                break;
            }
        }
        return associatedFile;
    }
    public void prepareSearch() {
        examineFiles();
    }

    // Getters and setters
    public boolean isRootDirectorySet() {
        return this.rootDirectorySet;
    }
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        rootDirectorySet = true;
        this.window.clearDisplayOutput();
    }
    public ArrayList<File> getProjectDirectories() {
        return this.projectDirectories;
    }
    public ArrayList<File> getProjectFiles() {
        return this.projectFiles;
    }
    public ArrayList<File> getFoundBlenderFiles() {
        return this.foundBlenderFiles;
    }
    public ArrayList<File> getFoundMayaFiles() {
        return this.foundMayaFiles;
    }
    public ArrayList<File> getMetaIssuesFound() {
        return this.metaIssuesFound;
    }

}
