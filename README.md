# SoftwareMaintenanceGrp5

# Diamond Hunter & MapViewer

This repository contains both projects: Diamond Hunter, which is the main original game, and MapViewer, which is a standalone Map Editor for the main game with the functionality to change the player usable items(Axe and Boat) location.

Both of the application are built into `.jar` files and placed into the [bin/](bin/) folder.

## Diamond Hunter

### Running Diamond Hunter

To run Diamond Hunter, open and start up [Diamond-Hunter.jar](bin/Diamond-Hunter.jar) and the game should be launched.

Windows user might encounter a problem with directly running [Diamond-Hunter.jar](bin/Diamond-Hunter.jar), normally with the game getting stuck at the menu. In that case, open up your command prompt(`cmd`), and make sure your current directory contains the Jar file (default location of the jar files will be in [bin/Diamond-Hunter.jar](bin/Diamond-Hunter.jar)), then type in the command `java -jar Diamond-Hunter.jar` in the command prompt to run it instead.

If you are running Diamond Hunter from the source code, simply run [com.neet.DiamondHunter.Main.Game.main()](src/com/neet/DiamondHunter/Main/Game.java) to start the application.

## MapViewer

### Running MapViewer

To run MapViewer, open and start up [MapViewer.jar](bin/MapViewer.jar) and the game should be launched just like Diamond Hunter.

Windows user might encounter a problem with directly running [MapViewer.jar](bin/MapViewer.jar), with nothing happening after attempting to run it. In that case, open up your command prompt(`cmd`), and make sure your current directory contains the Jar file (default location of the jar files will be in [bin/MapViewer.jar](bin/MapViewer.jar)), then type in the command `java -jar MapViewer.jar` in the command prompt to run it instead.

If you are running MapViewer from the source code however, run [com.neet.DiamondHunter.MapViewer.MapViewer.main()](src/com/neet/DiamondHunter/MapViewer/MapViewer.java) to start the application.

## Features in MapViewer
### 1) Set Boat Position
Admin can set the boat position by clicking the below button.

![BoatButton](Resources/ReadMe/BoatButton.png)
### 2) Set Axe Poaition
Admin can set the axe position by clicking the below button.

![AxeButton](Resources/ReadMe/AxeButton.png)
### 3) Reset the axe and boat position
Admin can change back the position of axe and boat to the beginning by clicking the below button.

![DefaultButton](Resources/ReadMe/DefaultButton.png)
### 4) Undo
Admin can undo the previous steps applied by clicking the below button.

![UndoButton](Resources/ReadMe/UndoButton.png)
### 5) Zoom In and Zoom Out
Admin can zoom in and zoom out the map by clicking the below image respectively. After zooming it, admin can move around by using 'W' (Top), 'A' (Left), 'S'(Down) and 'D' (Right).

![Zoominout](Resources/ReadMe/ZoomInOut.png)

<img src="Resources/ReadMe/zoomin.png" alt="zoomin" width="500" height="500" />
<img src="Resources/ReadMe/zoomin2.png" alt="zoomin2" width="500" height="500" />
<img src="Resources/ReadMe/zoomout.png" alt="zoomout" width="500" height="500" />
### 6) Information bar
All the informations and updates show at the bottom blue bar.

![InformationBar](Resources/ReadMe/bar.png)

### 7) Closing MapViewer - Confirmation Dialog
Confirmation dialog will pop up if admin leaves without saving his or her changes. 

![ConfirmationWindows](Resources/ReadMe/confirmation.png)
### 8) Setting item location - Warning Dialog
Admin can only relocate the axes and the boat at a empty space. Once the admin relocates the items in a space which is already filled, warning windows will pop up.

![WarningWindows](Resources/ReadMe/warning.png)
### 9) Loading and Saving Map

![FileMenu](Resources/ReadMe/File.png)
##### i) Load Map
To edit a map that has been saved in some folder, click the load map menu item to load the map.

![load_map](Resources/ReadMe/load_map.png)

System will display filename of currently opened map at the top bar menu.

![load_map_interface](Resources/ReadMe/load_map_interface.png)
##### ii) Save
Save map updates the existing opened map. If there isn't any saved file, it will automatically goes to the 'Save As' window.
##### iii) Save as
It creates a new map and saves in local directory. To save a newly created item map, click save as.

![save_as](Resources/ReadMe/save_as.png)
##### iv) Save as default
Save as default saves the default map, a confirmation dialog alert will popup. The admin would need to confirm if he/she would like to set it as default.

![default_map](Resources/ReadMe/default_map.png)
### 10) Help
##### i) Instruction
All the functions and details of system are displayed in the instruction windows.

![instruction](Resources/ReadMe/instruction.png)
##### ii) About 
Author 

![about](Resources/ReadMe/about.png)

![HelpMenu](Resources/ReadMe/Help.png)
### 11) Load Map in Game
To use the default map, press 'Start'. To load the customized map, press 'Load Map' and continue pressing 'Start'.

![menu](Resources/ReadMe/menu.png)
