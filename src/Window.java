import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Window {

    // Testing stuff
    private static final String TEST_CHAR_WIDTH = "1_3_5_7_9_12_15_18_21_24_27_30_33_36_39_42_45_48_51_54_57_60_63_" +
            "66_69_72_75_78_81_84_87_90_93_96_99_103_107_111_115_119_123_127_131_135_139_143_147_151_155_159_163_167";
    private static final String TEST_CHAR_HEIGHT = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n" +
            "19\n20\n21\n22\n23\n23\n24\n25\n26\n27\n28\n29\n30\n31\n32\n33\n34\n35\n36\n37\n38\n39\n40\n41\n42\n" +
            "43\n44\n45\n46\n47\n48\n49\n50\n51\n52\n53\n54\n55\n56\n57\n58\n59\n60\n61\n62\n63\n64\n65\n66\n67\n68";

    // Static
    private static final String TITLE = "File Analyzer";
    private static final int WIDTH = 600, HEIGHT = 800;
    private static final String ICON_PATH = "data\\angry_cat.ico";
    private static final int BUTTON_FONT_SIZE = 24;
    private static final int WINDOW_FONT_SIZE = 16;
    private static final int VIEW_WIDTH_CHAR = 60;  // For font-size 16
    // Obj cont...
    private JFrame frame;
    private JTextPane outputTextPane;
    FileSys fileSys;
    private ArrayList<String> outputLines;
    private AnalyzerConfig config;
    private JTextPane rootDirPane;
    private JDialog constraintSelection;
    private ArrayList<JCheckBox> checkBoxes;

    // Constructor and initialization
    public Window() {
        config = new AnalyzerConfig();
        frame = new JFrame();
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(new ImageIcon(ICON_PATH).getImage());
        Dimension windowDim = new Dimension(WIDTH, HEIGHT);
        frame.setMinimumSize(windowDim);
        frame.setMaximumSize(windowDim);
        frame.setPreferredSize(windowDim);
        frame.setResizable(false);
        createFrameComponents();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        outputLines = new ArrayList<>();
        this.fileSys = new FileSys(this);
        initAnalyzer();
    }
    private void createFrameComponents() {
        // Set up frame layout, fonts and colors, etc...
        this.frame.setLayout(new BorderLayout());
        Font windowFont = new Font("Arial", Font.PLAIN, WINDOW_FONT_SIZE);
        // Frame main components
        // Main panel
        JPanel mainPanel = new JPanel(); // Start one
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(new BorderLayout());
        // Button panel
        JPanel buttonPanel = new JPanel(); // Start two
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 32, 8));
        buttonPanel.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.GRAY);
        Border topBorder = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                new EmptyBorder(8, 8, 8, 8));
        topPanel.setBorder(topBorder);
        Font topFont = new Font("Arial", Font.BOLD, 16);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        // Top panel components
        JPanel rootDirPanel = new JPanel();
        rootDirPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        rootDirPanel.setBackground(Color.GRAY);
        rootDirPane = new JTextPane();
        rootDirPane.setBackground(Color.GRAY);
        rootDirPane.setForeground(Color.BLACK);
        rootDirPane.setFont(topFont);
        rootDirPane.setEditable(false);
        rootDirPane.setBorder(null);
        rootDirPane.setEnabled(true);
        JLabel setupLabel = new JLabel();
        setupLabel.setBackground(Color.GRAY);
        setupLabel.setForeground(Color.BLUE);
        setupLabel.setFont(topFont);
        setupLabel.setText("Root Directory: ");
        setupLabel.setLabelFor(rootDirPane);
        rootDirPanel.add(setupLabel);
        rootDirPanel.add(rootDirPane);
        // Add components to top panel
        topPanel.add(rootDirPanel);
        topPanel.setEnabled(true);
        // Sub-components start
        // Output text pane
        this.outputTextPane = new JTextPane();
        this.outputTextPane.setBackground(Color.BLACK);
        this.outputTextPane.setForeground(Color.GREEN);
        this.outputTextPane.setCaretColor(Color.GREEN);
        this.outputTextPane.setFont(windowFont);
        this.outputTextPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        this.outputTextPane.setEditable(false);
        this.outputTextPane.setEnabled(true);
        // Scrollable
        JScrollPane outputScroll = new JScrollPane(this.outputTextPane);
        outputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        outputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outputScroll.setBackground(Color.BLACK);
        outputScroll.setBorder(null);
        outputScroll.setWheelScrollingEnabled(true);
        outputScroll.setVisible(true);
        outputScroll.setEnabled(true);
        // Buttons start, button stuff init
        Font buttonFont = new Font("Arial", Font.BOLD, BUTTON_FONT_SIZE);
        Border buttonBorder = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(8, 8, 8, 8));
        // Set root path button
        JButton rootPathButton = new JButton();
        rootPathButton.setBackground(Color.GREEN);
        rootPathButton.setForeground(Color.BLACK);
        rootPathButton.setFont(buttonFont);
        rootPathButton.setText("Set Root Folder");
        rootPathButton.setBorder(buttonBorder);
        rootPathButton.addActionListener(e -> setRootPathAction());
        rootPathButton.setEnabled(true);
        // Analyze button
        JButton analyzeButton = new JButton();
        analyzeButton.setBackground(Color.GREEN);
        analyzeButton.setForeground(Color.BLACK);
        analyzeButton.setFont(buttonFont);
        analyzeButton.setText("Analyze");
        analyzeButton.setBorder(buttonBorder);
        analyzeButton.addActionListener(e -> analyzeButtonAction());
        analyzeButton.setEnabled(true);
        // Close button
        JButton closeButton = new JButton();
        closeButton.setBackground(Color.GREEN);
        closeButton.setForeground(Color.BLACK);
        closeButton.setFont(buttonFont);
        closeButton.setText("Close");
        closeButton.setBorder(buttonBorder);
        closeButton.addActionListener(e -> closeButtonAction());
        closeButton.setEnabled(true);
        // Components add
        buttonPanel.add(rootPathButton);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(closeButton);
        mainPanel.add(outputScroll, BorderLayout.CENTER);
        //mainPanel.add(scrollBar, BorderLayout.EAST);
        // Frame add statements
        this.frame.add(mainPanel, BorderLayout.CENTER);
        this.frame.add(buttonPanel, BorderLayout.SOUTH);
        this.frame.add(topPanel, BorderLayout.NORTH);
    }
    private void initAnalyzer() {
        String rootPath = config.getConfigProperty("rootPath");
        if (!rootPath.equals("none")) {
            this.fileSys.setRootDirectory(new File(rootPath));
            this.rootDirPane.setText(rootPath);
        } else {
            this.rootDirPane.setText("No root selected...");
        }
    }

    // Button actions
    private void closeButtonAction() {
        this.frame.setVisible(false);
        System.exit(0);
    }
    private void setRootPathAction() {
        JPopupMenu popup = new JPopupMenu();
        JFileChooser fileSelection = new JFileChooser();
        fileSelection.setDialogTitle("Select Root Folder");
        fileSelection.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileSelection.setMultiSelectionEnabled(false);
        if (fileSys.isRootDirectorySet()) {
            fileSelection.setCurrentDirectory(new File(config.getConfigProperty("rootPath")));
        }
        fileSelection.setVisible(true);
        int result = fileSelection.showOpenDialog(popup);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fileSelection.getSelectedFile();
            fileSys.setRootDirectory(selectedDir);
            config.setConfigProperty("rootPath", selectedDir.getAbsolutePath());
            this.rootDirPane.setText(selectedDir.getAbsolutePath());
        }
    }
    private void analyzeButtonAction() {
        this.constraintSelection = new JDialog();
        this.constraintSelection.setLayout(new BorderLayout());
        this.constraintSelection.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.constraintSelection.setResizable(false);
        Dimension constraintSize = new Dimension(200, 200);
        this.constraintSelection.setMinimumSize(constraintSize);
        this.constraintSelection.setMaximumSize(constraintSize);
        this.constraintSelection.setPreferredSize(constraintSize);
        this.constraintSelection.setTitle("File Analyzing Constraints");
        // Create components
        this.checkBoxes = new ArrayList<>();
        JPanel constraintBox = new JPanel();
        constraintBox.setLayout(new FlowLayout(FlowLayout.LEFT));
        JCheckBox blendFiles = new JCheckBox();
        blendFiles.setText("Search for \".blend\"");
        JCheckBox mayaFiles = new JCheckBox();
        mayaFiles.setText("Search for \".maya\"");
        JCheckBox metaFiles = new JCheckBox();
        metaFiles.setText("Analyze meta");
        // Add check boxes to stuff
        this.checkBoxes.add(blendFiles);
        this.checkBoxes.add(mayaFiles);
        this.checkBoxes.add(metaFiles);
        constraintBox.add(blendFiles);
        constraintBox.add(mayaFiles);
        constraintBox.add(metaFiles);
        // Finish components
        JButton analyzeButton = new JButton();
        analyzeButton.setText("Analyze");
        analyzeButton.addActionListener(e -> analyzeFileAction());

        // Add to popup
        this.constraintSelection.add(constraintBox, BorderLayout.CENTER);
        this.constraintSelection.add(analyzeButton, BorderLayout.SOUTH);
        this.constraintSelection.setLocationRelativeTo(null);
        this.constraintSelection.setVisible(true);
    }
    private void analyzeFileAction() {
        boolean blendSelected = this.checkBoxes.get(0).isSelected();
        boolean mayaSelected = this.checkBoxes.get(1).isSelected();
        boolean metaSelected = this.checkBoxes.get(2).isSelected();
        this.fileSys.analyzeFiles(blendSelected, mayaSelected, metaSelected);
        this.constraintSelection.dispose();
    }

    // Methods
    public void addOutput(String outputString) {
        if (outputString.length() <= VIEW_WIDTH_CHAR) {
            outputLines.add(outputString);
        } else {
            ArrayList<String> addLines = new ArrayList<>();
            String indent = "    ";
            int addNum = (outputString.length() - VIEW_WIDTH_CHAR) / (VIEW_WIDTH_CHAR - 4);
            addNum += 1;
            char[] outputStringChars = outputString.toCharArray();
            for (int i = 0; i <= addNum; i++) {
                StringBuilder addLineBuilder = new StringBuilder();
                if (i == 0) {
                    int charIndex = 0;
                    while (charIndex < VIEW_WIDTH_CHAR) {
                        char addChar = outputStringChars[charIndex];
                        addLineBuilder.append(addChar);
                        charIndex += 1;
                    }
                }
                else if (i == addNum) {
                    int charIndex = VIEW_WIDTH_CHAR + ((i - 1) * (VIEW_WIDTH_CHAR - 4));
                    addLineBuilder.append(indent);
                    while (charIndex < outputString.length()) {
                        char addChar = outputStringChars[charIndex];
                        addLineBuilder.append(addChar);
                        charIndex += 1;
                    }
                } else {
                    int charIndex = VIEW_WIDTH_CHAR + ((i - 1) * (VIEW_WIDTH_CHAR - 4));
                    int endChar = charIndex + (VIEW_WIDTH_CHAR - 4);
                    addLineBuilder.append(indent);
                    while (charIndex < endChar) {
                        char addChar = outputStringChars[charIndex];
                        addLineBuilder.append(addChar);
                        charIndex += 1;
                    }
                }
                String addLine = addLineBuilder.toString();
                addLines.add(addLine);
            }
            outputLines.addAll(addLines);
        }
        displayOutput();
    }
    private void displayOutput() {
        StringBuilder fullPrintBuilder = new StringBuilder();
        if (this.outputLines.size() == 0) {
            this.outputLines.add("No outputs in console...");
        }
        for (String line : this.outputLines) {
            fullPrintBuilder.append(line);
            fullPrintBuilder.append("\n");
        }
        this.outputTextPane.setText(fullPrintBuilder.toString());
    }

    // Main method
    public static void main(String[] args) {
        new Window();
    }

}
